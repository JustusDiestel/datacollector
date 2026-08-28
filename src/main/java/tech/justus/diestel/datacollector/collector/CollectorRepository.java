package tech.justus.diestel.datacollector.collector;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectorRepository extends JpaRepository<Collector, Long> {
}