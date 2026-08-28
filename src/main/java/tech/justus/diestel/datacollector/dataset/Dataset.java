package tech.justus.diestel.datacollector.dataset;

import jakarta.persistence.*;


import java.time.Instant;

@Entity
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private DatasetStatus status;

    private Instant createdAt;
    private long recordCount;

    protected Dataset() {
    }

    public Dataset(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = DatasetStatus.RUNNING;
        this.createdAt = Instant.now();
        this.recordCount = 0;
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

    public void incrementRecordCount(){
        recordCount++;
    }

    public Long getId() {
        return id;
    }
}
