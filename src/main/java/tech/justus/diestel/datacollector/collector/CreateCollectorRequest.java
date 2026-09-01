package tech.justus.diestel.datacollector.collector;

import java.util.List;

public record CreateCollectorRequest(
        String name,
        String url,
        int intervalSeconds,
        String recordsPath,
        List<FieldMappingRequest> fields
) {

    public CreateCollectorRequest {
        recordsPath = recordsPath == null ? "" : recordsPath.trim();
    }
}