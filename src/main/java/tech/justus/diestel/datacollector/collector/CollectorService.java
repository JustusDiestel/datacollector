package tech.justus.diestel.datacollector.collector;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectorService {

    private final CollectorRepository collectorRepository;

    public CollectorService(CollectorRepository collectorRepository) {
        this.collectorRepository = collectorRepository;
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
}