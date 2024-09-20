package com.example.security.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AdvertisementSimulator {
    public static void simulateVisits(Long adId, String packageType, int numberOfRequests) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:8443/api/advertisement/visiting/";

        for (int i = 0; i < numberOfRequests; i++) {
            String url = baseUrl + adId + "/" + packageType;
            ResponseEntity<String> response = restTemplate.getForEntity(url, null, String.class);
            System.out.println("Response for request " + i + ": " + response.getStatusCodeValue() + " - " + response.getBody());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
