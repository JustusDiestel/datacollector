package tech.justus.diestel.datacollector.collector;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
public class DataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id", nullable = false)
    private Collector collector;

    private Instant collectedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    protected DataRecord() {
    }

    public DataRecord(
            Collector collector,
            Map<String, Object> payload
    ) {
        this.collector = collector;
        this.payload = payload;
        this.collectedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Instant getCollectedAt() {
        return collectedAt;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}