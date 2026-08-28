package tech.justus.diestel.datacollector.collector;

import jakarta.persistence.*;

@Entity
public class FieldMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id", nullable = false)
    private Collector collector;

    private String sourcePath;
    private String targetName;
    private String dataType;
    private String unit;

    protected FieldMapping() {
    }

    public FieldMapping(
            String sourcePath,
            String targetName,
            String dataType,
            String unit,
            Collector collector
    ) {
        this.sourcePath = sourcePath;
        this.targetName = targetName;
        this.dataType = dataType;
        this.unit = unit;
        this.collector = collector;
    }

    public Long getId() {
        return id;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getUnit() {
        return unit;
    }

    public Collector getCollector() {
        return collector;
    }
}