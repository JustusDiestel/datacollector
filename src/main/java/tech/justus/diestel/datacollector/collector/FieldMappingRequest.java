package tech.justus.diestel.datacollector.collector;

public record FieldMappingRequest(
        String sourcePath,
        String targetName,
        String dataType,
        String unit
) {
}