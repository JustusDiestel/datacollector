package tech.justus.diestel.datacollector.collector;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Collector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "collector",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FieldMapping> fieldMappings = new ArrayList<>();

    private String name;

    private String url;

    private int intervalSeconds;

    private String recordsPath;

    private Instant createdAt;

    protected Collector() {
    }

    public Collector(
            String name,
            String url,
            int intervalSeconds,
            String recordsPath
    ) {
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        this.recordsPath = recordsPath;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public String getRecordsPath() {
        return recordsPath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<FieldMapping> getFieldMappings() {
        return fieldMappings;
    }

    public void addFieldMapping(FieldMapping fieldMapping) {
        fieldMappings.add(fieldMapping);
    }
}