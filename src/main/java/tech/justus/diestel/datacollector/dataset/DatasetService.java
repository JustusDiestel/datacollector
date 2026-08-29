package tech.justus.diestel.datacollector.dataset;

import org.springframework.stereotype.Service;
import tech.justus.diestel.datacollector.collector.Collector;
import tech.justus.diestel.datacollector.collector.CollectorService;
import tech.justus.diestel.datacollector.collector.DataRecord;

import java.util.List;

@Service
public class DatasetService {

    private final CollectorService collectorService;

    public DatasetService(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    public List<Dataset> getAllDatasets() {
        return collectorService.getAllCollectors()
                .stream()
                .map(this::toDataset)
                .toList();
    }

    public Dataset getDatasetById(Long id) {
        Collector collector = collectorService.getCollectorById(id);
        return toDataset(collector);
    }

    /**
     * Deletes only the collected data.
     * The Collector configuration remains available and can collect again.
     */
    public void deleteDataset(Long id) {
        collectorService.deleteRecords(id);
    }

    private Dataset toDataset(Collector collector) {
        List<DataRecord> records = collectorService.getRecords(collector.getId());

        DatasetStatus status;

        if (collector.getLastError() != null) {
            status = DatasetStatus.ERROR;
        } else if (collector.isActive()) {
            status = DatasetStatus.RUNNING;
        } else {
            status = DatasetStatus.STOPPED;
        }

        return new Dataset(
                collector.getId(),
                collector.getName(),
                "Gesammelte Daten von " + collector.getUrl(),
                status,
                collector.getCreatedAt(),
                records.size(),
                collector.getFieldMappings(),
                records
        );
    }
}
