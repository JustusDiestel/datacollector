package tech.justus.diestel.datacollector.collector;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<Long, List<DataRecord>> recordsByCollector =
                new HashMap<>();

        for (Collector collector : collectors) {
            recordsByCollector.put(
                    collector.getId(),
                    collectorService.getRecords(collector.getId())
            );
        }

        model.addAttribute("collectors", collectors);
        model.addAttribute("recordsByCollector", recordsByCollector);

        return "collector-dashboard";
    }

    @PostMapping("/collector-dashboard/{id}/start")
    public String startCollector(@PathVariable Long id) {

        collectorService.setActive(id, true);

        return "redirect:/collector-dashboard";
    }

    @PostMapping("/collector-dashboard/{id}/stop")
    public String stopCollector(@PathVariable Long id) {

        collectorService.setActive(id, false);

        return "redirect:/collector-dashboard";
    }

    @PostMapping("/collector-dashboard/{id}/collect")
    public String collectNow(@PathVariable Long id) throws Exception {

        collectorService.collectOnce(id);

        return "redirect:/collector-dashboard";
    }
}