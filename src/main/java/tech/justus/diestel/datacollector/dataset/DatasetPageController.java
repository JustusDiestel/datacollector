package tech.justus.diestel.datacollector.dataset;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DatasetPageController {

    private final DatasetService datasetService;

    public DatasetPageController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping("/datasets")
    public String showDatasets(Model model) {
        model.addAttribute("datasets", datasetService.getAllDatasets());
        return "datasets";
    }

    @GetMapping("/datasets/{id}")
    public String showDataset(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("dataset", datasetService.getDatasetById(id));
        return "dataset-detail";
    }

    @PostMapping("/datasets/{id}/delete")
    public String deleteDataset(@PathVariable Long id) {
        datasetService.deleteDataset(id);
        return "redirect:/datasets";
    }
}
