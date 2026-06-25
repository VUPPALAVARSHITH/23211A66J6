package vehicle_scheduler_be.dto;

import lombok.Data;
import vehicle_scheduler_be.model.Vehicle;

import java.util.List;

@Data
public class VehicleResponse {
    private List<Vehicle> vehicles;
}