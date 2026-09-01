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

    private String requestMethod = "GET";

    @Column(length = 10000)
    private String requestBody;

    private Instant createdAt;

    private Instant lastRunAt;

    private boolean active = true;

    @Column(length = 2000)
    private String lastError;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected Collector() {
    }

    public Collector(
            String name,
            String url,
            int intervalSeconds,
            String recordsPath
    ) {
        this(name, url, intervalSeconds, recordsPath, "GET", null);
    }

    public Collector(
            String name,
            String url,
            int intervalSeconds,
            String recordsPath,
            String requestMethod,
            String requestBody
    ) {
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        this.recordsPath = recordsPath == null ? "" : recordsPath.trim();
        this.requestMethod = requestMethod == null || requestMethod.isBlank()
                ? "GET"
                : requestMethod.trim().toUpperCase();
        this.requestBody = requestBody == null || requestBody.isBlank()
                ? null
                : requestBody.trim();
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

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestBody() {
        return requestBody;
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

    public Instant getLastRunAt() {
        return lastRunAt;
    }

    public void setLastRunAt(Instant lastRunAt) {
        this.lastRunAt = lastRunAt;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    @Transient
    public String getStatus() {
        if (!active) {
            return "PAUSED";
        }
        if (lastError != null) {
            return "ERROR";
        }
        return "ACTIVE";
    }
}
