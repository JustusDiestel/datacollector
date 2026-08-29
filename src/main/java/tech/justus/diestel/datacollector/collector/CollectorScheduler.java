package tech.justus.diestel.datacollector.collector;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class CollectorScheduler {

    private final CollectorService collectorService;

    public CollectorScheduler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Scheduled(fixedRate = 10000)
    public void checkCollectors() {

        List<Collector> collectors =
                collectorService.getAllCollectors();

        for (Collector collector : collectors) {

            if (!collector.isActive()) {
                continue;
            }

            if (isDue(collector)) {
                try {
                    collectorService.collectOnce(
                            collector.getId()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isDue(Collector collector) {

        if (collector.getLastRunAt() == null) {
            return true;
        }

        Instant nextRun =
                collector.getLastRunAt()
                        .plusSeconds(
                                collector.getIntervalSeconds()
                        );

        return Instant.now().isAfter(nextRun);
    }
}