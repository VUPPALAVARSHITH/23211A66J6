package vehicle_scheduler_be.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoggingService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String LOG_URL =
            "http://4.224.186.213/evaluation-service/logs";

    private static final String ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiIyMzIxMWE2Nmo2QGJ2cml0LmFjLmluIiwiZXhwIjoxNzgyMzc5NjU2LCJpYXQiOjE3ODIzNzg3NTYsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI2ZTg0ZmZhMy04YmJmLTQzZDMtYTUwYS0xNTRmNjQwYzczZmYiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwic3ViIjoiNzVkODgzOWYtNjgzMC00ZTE3LTg4MWUtYTE1ZGUyNWMzYWE5In0sImVtYWlsIjoiMjMyMTFhNjZqNkBidnJpdC5hYy5pbiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwicm9sbE5vIjoiMjMyMTFhNjZqNiIsImFjY2Vzc0NvZGUiOiJhaFhqdnAiLCJjbGllbnRJRCI6Ijc1ZDg4MzlmLTY4MzAtNGUxNy04ODFlLWExNWRlMjVjM2FhOSIsImNsaWVudFNlY3JldCI6IkVoV1BhUFhaVFdQa2RqVksifQ.vDyf1UZjLCxvBPRrc2DBcnpFPwjYKeaQUnIV0ptuU1Q";

    public void log(String level, String packageName, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(ACCESS_TOKEN);

            Map<String, String> body = new HashMap<>();
            body.put("stack", "backend");
            body.put("level", level);
            body.put("package", packageName);
            body.put("message", message);

            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(body, headers);

            restTemplate.postForEntity(LOG_URL, request, String.class);

        } catch (Exception e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}