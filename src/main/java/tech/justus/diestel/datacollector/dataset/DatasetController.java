package tech.justus.diestel.datacollector.dataset;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping
    public List<Dataset> getAllDatasets() {
        return datasetService.getAllDatasets();
    }

    @GetMapping("/{id}")
    public Dataset getDataset(@PathVariable Long id) {
        return datasetService.getDatasetById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDataset(@PathVariable Long id) {
        datasetService.deleteDataset(id);
    }
}
