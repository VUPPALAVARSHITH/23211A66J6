package vehicle_scheduler_be.dto;

import lombok.Data;
import vehicle_scheduler_be.model.Depot;

import java.util.List;

@Data
public class DepotResponse {
    private List<Depot> depots;
}