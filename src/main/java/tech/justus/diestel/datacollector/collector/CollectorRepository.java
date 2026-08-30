package tech.justus.diestel.datacollector.collector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectorRepository extends JpaRepository<Collector, Long> {

    @Query("""
        select distinct c
        from Collector c
        left join fetch c.fieldMappings
        where c.id = :id
        """)
    Optional<Collector> findByIdWithFieldMappings(@Param("id") Long id);

    @Query("""
    select distinct c
    from Collector c
    left join fetch c.fieldMappings
    """)
    List<Collector> findAllWithFieldMappings();
}