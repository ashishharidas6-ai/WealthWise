package com.smartfinance.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class APIClient {
    private static final String BASE_URL = "https://stock.indianapi.in";
    private static final String API_KEY = "sk-live-FZdfRv9Zk8MVkY8VvjvQssKHupt9mJZkAf8EUh6w"; // User's API key

    private final HttpClient httpClient;

    public APIClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private HttpRequest buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json")
                .build();
    }

    public String getTrendingStocks() throws IOException, InterruptedException {
        HttpRequest request = buildRequest("/trending");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getNSEMostActive() throws IOException, InterruptedException {
        HttpRequest request = buildRequest("/NSE_most_active");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getStockDetails(String symbol) throws IOException, InterruptedException {
        HttpRequest request = buildRequest("/stock?symbol=" + symbol);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getHistoricalData(String symbol, String period) throws IOException, InterruptedException {
        // Assuming period like "1M", "3M", etc.
        HttpRequest request = buildRequest("/historical_data?symbol=" + symbol + "&period=" + period);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}