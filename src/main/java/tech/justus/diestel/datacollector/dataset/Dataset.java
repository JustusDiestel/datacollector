package tech.justus.diestel.datacollector.dataset;

import tech.justus.diestel.datacollector.collector.DataRecord;
import tech.justus.diestel.datacollector.collector.FieldMapping;

import java.time.Instant;
import java.util.List;

/**
 * Dataset is the public/read-only view of the data collected by one Collector.
 * It is intentionally not a JPA entity: the source of truth is the Collector
 * plus its DataRecords.
 */
public class Dataset {

    private final Long id;
    private final String name;
    private final String description;
    private final DatasetStatus status;
    private final Instant createdAt;
    private final long recordCount;
    private final List<FieldMapping> fields;
    private final List<DataRecord> records;

    public Dataset(
            Long id,
            String name,
            String description,
            DatasetStatus status,
            Instant createdAt,
            long recordCount,
            List<FieldMapping> fields,
            List<DataRecord> records
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.recordCount = recordCount;
        this.fields = fields;
        this.records = records;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DatasetStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public List<FieldMapping> getFields() {
        return fields;
    }

    public List<DataRecord> getRecords() {
        return records;
    }
}
