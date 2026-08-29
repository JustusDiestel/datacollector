package tech.justus.diestel.datacollector.collector;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {

    List<DataRecord> findByCollectorIdOrderByCollectedAtDesc(Long collectorId);
}