package tech.justus.diestel.datacollector.collector;

import java.util.List;

public record CreateCollectorRequest(
        String name,
        String url,
        int intervalSeconds,
        String recordsPath,
        String requestMethod,
        String requestBody,
        List<FieldMappingRequest> fields
) {

    public CreateCollectorRequest {
        recordsPath = recordsPath == null ? "" : recordsPath.trim();
        requestMethod = requestMethod == null || requestMethod.isBlank()
                ? "GET"
                : requestMethod.trim().toUpperCase();
        requestBody = requestBody == null || requestBody.isBlank()
                ? null
                : requestBody.trim();
    }

    public CreateCollectorRequest(
            String name,
            String url,
            int intervalSeconds,
            String recordsPath,
            List<FieldMappingRequest> fields
    ) {
        this(name, url, intervalSeconds, recordsPath, "GET", null, fields);
    }
}
