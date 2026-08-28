package tech.justus.diestel.datacollector.dataset;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/datasets")
public class DatasetController {
    private final DatasetService datasetService;


    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping
    public List<Dataset> getAllDatasets() {
        return datasetService.getAllDatasets();
    }

    @PostMapping
    public Dataset createDataset(@RequestBody CreateDatasetRequest request){
        return datasetService.createDataset(
            request.name(),
            request.description()
        );
    }
}
