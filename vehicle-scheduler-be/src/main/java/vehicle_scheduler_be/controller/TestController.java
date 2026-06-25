package vehicle_scheduler_be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vehicle_scheduler_be.service.LoggingService;

@RestController
public class TestController {

    private final LoggingService loggingService;

    public TestController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @GetMapping("/test")
    public String test() {
        loggingService.log("info", "controller", "Test API called");
        return "Vehicle Scheduler Backend is running";
    }
}
