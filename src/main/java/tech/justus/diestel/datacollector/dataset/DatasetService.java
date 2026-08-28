package tech.justus.diestel.datacollector.dataset;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DatasetService {
    private final DatasetRepository datasetRepository;

    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public Dataset createDataset(String name, String description){
        Dataset dataset = new Dataset(name, description);
        return datasetRepository.save(dataset);
    }

    public List<Dataset> getAllDatasets(){
        return datasetRepository.findAll();
    }

    public Dataset getDatasetById(Long id){
        return datasetRepository.findById(id).orElseThrow(() -> new RuntimeException("Dataset not found"));
    }
}
