package com.smartfinance.service;

import com.smartfinance.Models.Model;
import com.smartfinance.Models.RiskProfile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvestmentService {

    public static class StockSuggestion {
        public String companyName;
        public String symbol;
        public double price;
        public double percentChange;

        public StockSuggestion(String companyName, String symbol, double price, double percentChange) {
            this.companyName = companyName;
            this.symbol = symbol;
            this.price = price;
            this.percentChange = percentChange;
        }
    }
    private final APIClient apiClient;

    public InvestmentService() {
        this.apiClient = new APIClient();
    }

    public List<StockSuggestion> getStockSuggestions(RiskProfile risk) {
        List<StockSuggestion> suggestions = new ArrayList<>();
        try {
            JSONArray data = new JSONArray();
            if (risk == RiskProfile.AGGRESSIVE) {
                // For aggressive, use most active stocks (higher volume, potentially more volatile)
                String responseActive = apiClient.getNSEMostActive();
                System.out.println("NSE Most Active Response: " + responseActive);
                data = new JSONArray(responseActive);
            } else {
                // For conservative and moderate, use trending stocks
                String response = apiClient.getTrendingStocks();
                System.out.println("Trending Stocks Response: " + response);
                JSONObject json = new JSONObject(response);

                if (json.has("trending_stocks")) {
                    JSONObject trendingStocks = json.getJSONObject("trending_stocks");
                    JSONArray gainers = trendingStocks.has("top_gainers") ? trendingStocks.getJSONArray("top_gainers") : new JSONArray();
                    JSONArray losers = trendingStocks.has("top_losers") ? trendingStocks.getJSONArray("top_losers") : new JSONArray();

                    if (risk == RiskProfile.CONSERVATIVE) {
                        // For conservative, use top losers (potentially undervalued/stable), and add from gainers if needed
                        data = losers;
                        // If less than 5, add from gainers
                        for (int i = 0; i < gainers.length() && data.length() < 5; i++) {
                            data.put(gainers.getJSONObject(i));
                        }
                    } else if (risk == RiskProfile.MODERATE) {
                        // For moderate, use both top gainers and top losers to provide more options
                        // Combine both arrays
                        for (int i = 0; i < gainers.length(); i++) {
                            data.put(gainers.getJSONObject(i));
                        }
                        for (int i = 0; i < losers.length(); i++) {
                            data.put(losers.getJSONObject(i));
                        }
                    }
                }
            }

            for (int i = 0; i < data.length(); i++) {
                JSONObject stock = data.getJSONObject(i);
                String companyName;
                String symbol;
                if (risk == RiskProfile.AGGRESSIVE) {
                    companyName = stock.getString("company");
                    symbol = stock.getString("ticker");
                } else {
                    companyName = stock.getString("company_name");
                    symbol = stock.getString("ric");
                }
                double price = stock.getDouble("price");
                double percentChange = stock.getDouble("percent_change");
                suggestions.add(new StockSuggestion(companyName, symbol, price, percentChange));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    public List<Double> getHistoricalPrices(String symbol, String period) {
        List<Double> prices = new ArrayList<>();
        try {
            String response = apiClient.getHistoricalData(symbol, period); // Use period parameter
            System.out.println("Historical Data Response: " + response);
            JSONObject json = new JSONObject(response);

            if (json.has("datasets")) {
                JSONArray datasets = json.getJSONArray("datasets");
                if (datasets.length() > 0) {
                    JSONObject priceDataset = datasets.getJSONObject(0); // Assuming first is price
                    JSONArray values = priceDataset.getJSONArray("values");
                    for (int i = 0; i < values.length(); i++) {
                        JSONArray point = values.getJSONArray(i);
                        prices.add(Double.parseDouble(point.getString(1)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prices;
    }
}