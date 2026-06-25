package vehicle_scheduler_be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vehicle_scheduler_be.dto.ScheduleResponse;
import vehicle_scheduler_be.service.LoggingService;
import vehicle_scheduler_be.service.VehicleSchedulerService;

@RestController
public class VehicleSchedulerController {

    private final VehicleSchedulerService vehicleSchedulerService;
    private final LoggingService loggingService;

    public VehicleSchedulerController(
            VehicleSchedulerService vehicleSchedulerService,
            LoggingService loggingService
    ) {
        this.vehicleSchedulerService = vehicleSchedulerService;
        this.loggingService = loggingService;
    }

    @GetMapping("/schedule")
    public ScheduleResponse getSchedule() {
        loggingService.log("info", "controller", "Schedule API called");
        return vehicleSchedulerService.generateSchedule();
    }
}