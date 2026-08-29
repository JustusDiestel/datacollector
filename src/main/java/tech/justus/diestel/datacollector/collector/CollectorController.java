package tech.justus.diestel.datacollector.collector;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collectors")
public class CollectorController {

    private final CollectorService collectorService;
    private final HttpJsonCollector httpJsonCollector;

    public CollectorController(
            CollectorService collectorService,
            HttpJsonCollector httpJsonCollector
    ) {
        this.collectorService = collectorService;
        this.httpJsonCollector = httpJsonCollector;
    }

    @GetMapping
    public List<Collector> getAllCollectors() {
        return collectorService.getAllCollectors();
    }

    @PostMapping
    public Collector createCollector(
            @RequestBody CreateCollectorRequest request
    ) {
        return collectorService.createCollector(request);
    }

    @PostMapping("/{id}/collect")
    public List<DataRecord> collect(
            @PathVariable Long id
    ) throws Exception {

        return collectorService.collectOnce(id);
    }

    @GetMapping("/{id}/records")
    public List<DataRecord> getRecords(
            @PathVariable Long id
    ) {
        return collectorService.getRecords(id);
    }

    @GetMapping(
            value = "/{id}/export",
            produces = "text/csv"
    )
    public ResponseEntity<String> exportCsv(
            @PathVariable Long id
    ) {

        String csv = collectorService.exportCsv(id);

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"collector-" + id + ".csv\""
                )
                .body(csv);
    }

    @PostMapping("/{id}/start")
    public Collector startCollector(
            @PathVariable Long id
    ) {
        return collectorService.setActive(id, true);
    }

    @PostMapping("/{id}/stop")
    public Collector stopCollector(
            @PathVariable Long id
    ) {
        return collectorService.setActive(id, false);
    }
}