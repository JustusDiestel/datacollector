package tech.justus.diestel.datacollector.collector;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
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

        String response = restClient
                .get()
                .uri(collector.getUrl())
                .retrieve()
                .body(String.class);

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

            Object convertedValue = switch (mapping.getDataType()) {
                case "DOUBLE" -> value.asDouble();
                case "INTEGER" -> value.asInt();
                case "BOOLEAN" -> value.asBoolean();
                case "STRING" -> value.asText();
                default -> value.asText();
            };

            result.put(mapping.getTargetName(), convertedValue);
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

            result.add(mapRecord(records, collector));

        } else {

            throw new IllegalArgumentException(
                    "recordsPath must point to a JSON object or array"
            );
        }

        return result;
    }
}