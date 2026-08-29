package tech.justus.diestel.datacollector.collector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CollectorService {

    private final CollectorRepository collectorRepository;
    private final DataRecordRepository dataRecordRepository;
    private final HttpJsonCollector httpJsonCollector;

    public CollectorService(
            CollectorRepository collectorRepository,
            DataRecordRepository dataRecordRepository,
            HttpJsonCollector httpJsonCollector
    ) {
        this.collectorRepository = collectorRepository;
        this.dataRecordRepository = dataRecordRepository;
        this.httpJsonCollector = httpJsonCollector;
    }

    public Collector createCollector(CreateCollectorRequest request) {

        Collector collector = new Collector(
                request.name(),
                request.url(),
                request.intervalSeconds(),
                request.recordsPath()
        );

        if (request.fields() != null) {
            for (FieldMappingRequest field : request.fields()) {

                FieldMapping mapping = new FieldMapping(
                        field.sourcePath(),
                        field.targetName(),
                        field.dataType(),
                        field.unit(),
                        collector
                );

                collector.addFieldMapping(mapping);
            }
        }

        return collectorRepository.save(collector);
    }

    public List<Collector> getAllCollectors() {
        return collectorRepository.findAll();
    }

    public Collector getCollectorById(Long id){
        return collectorRepository.findById(id).orElseThrow(() -> new RuntimeException("Collector not found"));
    }


    @Transactional
    public List<DataRecord> collectOnce(Long collectorId) throws Exception {

        Collector collector = getCollectorById(collectorId);

        try {
            List<Map<String, Object>> collectedData =
                    httpJsonCollector.collect(collector);

            List<DataRecord> records = new ArrayList<>();

            for (Map<String, Object> payload : collectedData) {

                DataRecord record = new DataRecord(
                        collector,
                        payload
                );

                records.add(
                        dataRecordRepository.save(record)
                );
            }

            collector.setLastRunAt(Instant.now());
            collector.setLastError(null);
            collectorRepository.save(collector);

            return records;

        } catch (Exception e) {

            collector.setLastRunAt(Instant.now());
            collector.setLastError(e.getMessage());
            collectorRepository.save(collector);

            throw e;
        }
    }

    public List<DataRecord> getRecords(Long collectorId) {
        return dataRecordRepository
                .findByCollectorIdOrderByCollectedAtDesc(collectorId);
    }

    public String exportCsv(Long collectorId) {
        List<DataRecord> records = getRecords(collectorId);
        if (records.isEmpty()) {
            return "";
        }
        StringBuilder csv = new StringBuilder();
        // Header
        csv.append("collectedAt");
        for (String field : records.getFirst().getPayload().keySet()) {
            csv.append(",").append(field);
        }
        csv.append("\n");
        // Daten
        for (DataRecord record : records) {
            csv.append(record.getCollectedAt());
            for (String field : records.getFirst().getPayload().keySet()) {
                Object value = record.getPayload().get(field);
                csv.append(",")
                        .append(value != null ? value : "");
            }
            csv.append("\n");
        }
        return csv.toString();
    }

    public Collector setActive(Long id, boolean active) {

        Collector collector = getCollectorById(id);

        collector.setActive(active);

        return collectorRepository.save(collector);
    }
}