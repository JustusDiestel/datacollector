package tech.justus.diestel.datacollector.collector;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class CollectorPageController {

    private final CollectorService collectorService;

    public CollectorPageController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @GetMapping("/collector-dashboard")
    public String dashboard(Model model) {

        List<Collector> collectors =
                collectorService.getAllCollectors();

        model.addAttribute("collectors", collectors);

        return "collector-dashboard";
    }

    @PostMapping("/collector-dashboard/{id}/start")
    public String startCollector(@PathVariable Long id) {

        collectorService.setActive(id, true);

        return "redirect:/collector-dashboard/" + id;
    }

    @PostMapping("/collector-dashboard/{id}/stop")
    public String stopCollector(@PathVariable Long id) {

        collectorService.setActive(id, false);

        return "redirect:/collector-dashboard/" + id;
    }

    @PostMapping("/collector-dashboard/{id}/collect")
    public String collectNow(@PathVariable Long id) throws Exception {

        collectorService.collectOnce(id);

        return "redirect:/collector-dashboard/" + id;
    }

    @GetMapping("/collector-dashboard/new")
    public String newCollectorForm() {
        return "collector-create";
    }

    @PostMapping("/collector-dashboard/new")
    public String createCollector(
            @RequestParam String name,
            @RequestParam String url,
            @RequestParam int intervalSeconds,
            @RequestParam String recordsPath,
            @RequestParam List<String> sourcePath,
            @RequestParam List<String> targetName,
            @RequestParam List<String> dataType,
            @RequestParam List<String> unit
    ) {

        List<FieldMappingRequest> fields = new java.util.ArrayList<>();

        for (int i = 0; i < sourcePath.size(); i++) {

            FieldMappingRequest field = new FieldMappingRequest(
                    sourcePath.get(i),
                    targetName.get(i),
                    dataType.get(i),
                    unit.get(i)
            );

            fields.add(field);
        }

        CreateCollectorRequest request = new CreateCollectorRequest(
                name,
                url,
                intervalSeconds,
                recordsPath,
                fields
        );

        collectorService.createCollector(request);

        return "redirect:/collector-dashboard";
    }

    @PostMapping("/collector-dashboard/{id}/delete")
    public String deleteCollector(@PathVariable Long id) {

        collectorService.deleteCollector(id);

        return "redirect:/collector-dashboard";
    }
    @GetMapping("/collector-dashboard/{id}")
    public String collectorDetail(
            @PathVariable Long id,
            Model model
    ) {
        Collector collector = collectorService.getCollectorById(id);
        List<DataRecord> records = collectorService.getRecords(id);

        model.addAttribute("collector", collector);
        model.addAttribute("records", records);

        return "collector-detail";
    }
}