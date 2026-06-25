package vehicle_scheduler_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import vehicle_scheduler_be.model.Vehicle;

import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleResponse {
    private int depotId;
    private int mechanicHours;
    private int usedHours;
    private int totalImpact;
    private List<Vehicle> selectedTasks;
}