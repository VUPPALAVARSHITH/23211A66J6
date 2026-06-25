package vehicle_scheduler_be.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vehicle_scheduler_be.dto.DepotResponse;
import vehicle_scheduler_be.dto.ScheduleResponse;
import vehicle_scheduler_be.dto.VehicleResponse;
import vehicle_scheduler_be.model.Depot;
import vehicle_scheduler_be.model.Vehicle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleSchedulerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final LoggingService loggingService;
    private static final String ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiIyMzIxMWE2Nmo2QGJ2cml0LmFjLmluIiwiZXhwIjoxNzgyMzc5NjU2LCJpYXQiOjE3ODIzNzg3NTYsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI2ZTg0ZmZhMy04YmJmLTQzZDMtYTUwYS0xNTRmNjQwYzczZmYiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwic3ViIjoiNzVkODgzOWYtNjgzMC00ZTE3LTg4MWUtYTE1ZGUyNWMzYWE5In0sImVtYWlsIjoiMjMyMTFhNjZqNkBidnJpdC5hYy5pbiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwicm9sbE5vIjoiMjMyMTFhNjZqNiIsImFjY2Vzc0NvZGUiOiJhaFhqdnAiLCJjbGllbnRJRCI6Ijc1ZDg4MzlmLTY4MzAtNGUxNy04ODFlLWExNWRlMjVjM2FhOSIsImNsaWVudFNlY3JldCI6IkVoV1BhUFhaVFdQa2RqVksifQ.vDyf1UZjLCxvBPRrc2DBcnpFPwjYKeaQUnIV0ptuU1Q";
    private static final String DEPOTS_API =
            "http://4.224.186.213/evaluation-service/depots";

    private static final String VEHICLES_API =
            "http://4.224.186.213/evaluation-service/vehicles";

    public VehicleSchedulerService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public ScheduleResponse generateSchedule() {
        loggingService.log("info", "service", "Vehicle scheduling started");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<DepotResponse> depotEntity =
                restTemplate.exchange(
                        DEPOTS_API,
                        HttpMethod.GET,
                        request,
                        DepotResponse.class
                );

        ResponseEntity<VehicleResponse> vehicleEntity =
                restTemplate.exchange(
                        VEHICLES_API,
                        HttpMethod.GET,
                        request,
                        VehicleResponse.class
                );

        DepotResponse depotResponse = depotEntity.getBody();
        VehicleResponse vehicleResponse = vehicleEntity.getBody();

        List<Depot> depots = depotResponse.getDepots();
        List<Vehicle> vehicles = vehicleResponse.getVehicles();

        Depot depot = depots.get(0);
        int capacity = depot.getMechanicHours();

        loggingService.log("info", "service", "Depot and vehicle data fetched");

        return solveKnapsack(depot, vehicles, capacity);
    }

    private ScheduleResponse solveKnapsack(Depot depot, List<Vehicle> vehicles, int capacity) {
        loggingService.log("info", "service", "Knapsack algorithm started");

        int n = vehicles.size();
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            Vehicle v = vehicles.get(i - 1);
            int duration = v.getDuration();
            int impact = v.getImpact();

            for (int h = 0; h <= capacity; h++) {
                dp[i][h] = dp[i - 1][h];

                if (duration <= h) {
                    dp[i][h] = Math.max(
                            dp[i][h],
                            impact + dp[i - 1][h - duration]
                    );
                }
            }
        }

        List<Vehicle> selected = new ArrayList<>();
        int h = capacity;
        int usedHours = 0;

        for (int i = n; i > 0; i--) {
            if (dp[i][h] != dp[i - 1][h]) {
                Vehicle v = vehicles.get(i - 1);
                selected.add(v);
                usedHours += v.getDuration();
                h -= v.getDuration();
            }
        }

        loggingService.log("info", "service", "Vehicle schedule generated successfully");

        return new ScheduleResponse(
                depot.getID(),
                depot.getMechanicHours(),
                usedHours,
                dp[n][capacity],
                selected
        );
    }
}