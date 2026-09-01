package tech.justus.diestel.datacollector.collector;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@Component
public class HttpJsonCollector {

    private final RestClient restClient;
    private final JsonMapper jsonMapper;

    public HttpJsonCollector(JsonMapper jsonMapper) {
        this.restClient = RestClient.create();
        this.jsonMapper = jsonMapper;
    }

    public JsonNode fetch(Collector collector) throws Exception {

        String response;
        String method = collector.getRequestMethod() == null
                ? "GET"
                : collector.getRequestMethod().toUpperCase();

        if ("GET".equals(method)) {
            response = restClient
                    .get()
                    .uri(collector.getUrl())
                    .retrieve()
                    .body(String.class);
        } else if ("POST".equals(method)) {
            String body = collector.getRequestBody() == null
                    ? "{}"
                    : collector.getRequestBody();

            response = restClient
                    .post()
                    .uri(collector.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported HTTP method: " + method
            );
        }

        JsonNode root = jsonMapper.readTree(response);

        return findPath(root, collector.getRecordsPath());
    }

    private JsonNode findPath(JsonNode root, String path) {

        if (path == null || path.isBlank()) {
            return root;
        }

        JsonNode current = root;

        for (String part : path.split("\\.")) {
            current = current.get(part);

            if (current == null) {
                throw new IllegalArgumentException(
                        "JSON path not found: " + path
                );
            }
        }

        return current;
    }

    public Map<String, Object> mapRecord(
            JsonNode record,
            Collector collector
    ) {
        Map<String, Object> result = new HashMap<>();

        for (FieldMapping mapping : collector.getFieldMappings()) {

            JsonNode value = findPath(record, mapping.getSourcePath());

            result.put(
                    mapping.getTargetName(),
                    convertValue(value, mapping.getDataType())
            );
        }

        return result;
    }

    private Object convertValue(JsonNode value, String dataType) {
        return switch (dataType) {
            case "DOUBLE" -> value.asDouble();
            case "INTEGER" -> value.asInt();
            case "BOOLEAN" -> value.asBoolean();
            case "STRING" -> value.asText();
            default -> value.asText();
        };
    }

    private boolean containsMappedArrays(
            JsonNode record,
            Collector collector
    ) {
        for (FieldMapping mapping : collector.getFieldMappings()) {
            if (findPath(record, mapping.getSourcePath()).isArray()) {
                return true;
            }
        }
        return false;
    }

    private List<Map<String, Object>> mapParallelArrays(
            JsonNode record,
            Collector collector
    ) {
        int arrayLength = -1;

        for (FieldMapping mapping : collector.getFieldMappings()) {
            JsonNode value = findPath(record, mapping.getSourcePath());

            if (value.isArray()) {
                if (arrayLength == -1) {
                    arrayLength = value.size();
                } else if (value.size() != arrayLength) {
                    throw new IllegalArgumentException(
                            "Mapped JSON arrays have different lengths"
                    );
                }
            }
        }

        if (arrayLength < 0) {
            return List.of(mapRecord(record, collector));
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < arrayLength; i++) {
            Map<String, Object> mappedRecord = new HashMap<>();

            for (FieldMapping mapping : collector.getFieldMappings()) {
                JsonNode value = findPath(record, mapping.getSourcePath());
                JsonNode item = value.isArray() ? value.get(i) : value;

                mappedRecord.put(
                        mapping.getTargetName(),
                        convertValue(item, mapping.getDataType())
                );
            }

            result.add(mappedRecord);
        }

        return result;
    }

    public List<Map<String, Object>> collect(Collector collector) throws Exception {

        JsonNode records = fetch(collector);

        List<Map<String, Object>> result = new ArrayList<>();

        if (records.isArray()) {

            for (JsonNode record : records) {
                result.add(mapRecord(record, collector));
            }

        } else if (records.isObject()) {

            if (containsMappedArrays(records, collector)) {
                result.addAll(mapParallelArrays(records, collector));
            } else {
                result.add(mapRecord(records, collector));
            }

        } else {

            throw new IllegalArgumentException(
                    "recordsPath must point to a JSON object or array"
            );
        }

        return result;
    }
}
