package com.example.security.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class ClickSimulationTest {

    private static final String URL_TEMPLATE = "https://localhost:8443/api/notAuthenticated/visiting/{id}";
    private final RestTemplate restTemplate = createRestTemplate();

    public static void main(String[] args) {
        ClickSimulationTest script = new ClickSimulationTest();
        script.runTests();
    }
    private static RestTemplate createRestTemplate() {
        // Kreiranje RestTemplate-a sa podrškom za nevalidne SSL sertifikate
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HostnameVerifier allHostsValid = (hostname, session) -> true;

            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            return new RestTemplate();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create RestTemplate", ex);
        }
    }
    public void runTests() {
        Map<String, Integer> testCases = new HashMap<>();
        testCases.put("BASIC_120_NotOK_2", 12);
        testCases.put("STANDARD_1200_NotOK_3", 110);
        testCases.put("GOLD_12000_NotOK_1", 11000);

        for (Map.Entry<String, Integer> testCase : testCases.entrySet()) {
            String[] parts = testCase.getKey().split("_");
            String packageType = parts[0];
            int requestsPerMinute = testCase.getValue();
            //boolean expectedOk = parts[1].equals("OK");
            long advId = Long.parseLong(parts[3]);


            System.out.println("Testing " + packageType + " package with " + requestsPerMinute + " requests per minute...");

            testPackage(packageType, requestsPerMinute, advId);

            System.out.println();
        }
    }

    private void testPackage(String packageType, int requestsPerMinute, long advId) {
        for (int i = 0; i < requestsPerMinute; i++) {
            if(!sendRequest(packageType, i+1, advId)){
                System.out.println(packageType + " package test " + "failed.");
                return;
            }
        }

        System.out.println(packageType + " package test " + "passed.");
    }



    private boolean sendRequest(String packageType, int iterId, long advId) {
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(URL_TEMPLATE, Void.class, advId);
            if (response.getStatusCodeValue() == 200) {
                //System.out.println(iterId + ". " + "Expected response status " + 200 + " received for " + packageType + " package.");
                return true;
            } else {
                System.err.println(iterId + ". " + "Unexpected response status " + response.getStatusCodeValue() + " for " + packageType + " package.");
                return false;
            }
        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println(iterId + ". " + "Expected response status 429 received for " + packageType + " package.");
            return true;
        } catch (Exception e) {
            System.err.println(iterId + ". " + "Unexpected error: " + e.getMessage());
            return false;
        }
    }
}
