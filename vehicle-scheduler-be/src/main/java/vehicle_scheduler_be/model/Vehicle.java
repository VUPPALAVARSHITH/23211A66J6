package vehicle_scheduler_be.model;

import lombok.Data;

@Data
public class Vehicle {
    private String TaskID;
    private int Duration;
    private int Impact;
}