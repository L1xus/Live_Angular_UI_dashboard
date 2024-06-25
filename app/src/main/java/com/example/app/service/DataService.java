
package com.example.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.DataModel;
import com.example.app.repository.DataModelRepository;

@Service
public class DataService {

    @Autowired
    private DataModelRepository repository;

    public String getLast30DaysCarSalesData() {
        JSONObject jsonResponse = new JSONObject();
        try {
            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(30);
            String formattedDate = from.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            List<DataModel> sales = repository.findSalesGreaterThanDate(formattedDate);

            if (!sales.isEmpty()) {
                jsonResponse.put("totalSales", calculateTotalSales(sales));
                jsonResponse.put("carModelSales", getSalesByModel(sales));
                jsonResponse.put("dealerSales", getSalesByDealership(sales));
                jsonResponse.put("stateSales", getSalesByState(sales));
            } else {
                jsonResponse.put("message", "No sales data available for the last 30 days.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse.toString();
    }

    private String calculateTotalSales(List<DataModel> sales) {
        double totalAmount = sales.stream().mapToDouble(DataModel::getPriceDouble).sum();
        return "$" + String.format("%.2fM", totalAmount / 1000000.0);
    }

    private JSONArray getSalesByModel(List<DataModel> sales) {
        JSONArray carModelSales = new JSONArray();
        Map<String, Long> salesByModel = sales.stream()
                .collect(Collectors.groupingBy(DataModel::getModel, Collectors.counting()));
        salesByModel.forEach((model, count) -> {
            JSONObject sale = new JSONObject();
            sale.put("label", model);
            sale.put("data", new JSONArray().put(count));
            carModelSales.put(sale);
        });
        return carModelSales;
    }

    private JSONObject getSalesByDealership(List<DataModel> sales) {
        JSONObject dealerSales = new JSONObject();
        JSONArray dealerNames = new JSONArray();
        JSONArray dealerData = new JSONArray();
        JSONArray dealerDataSet = new JSONArray();
        Map<String, Long> salesByDealer = sales.stream()
                .collect(Collectors.groupingBy(DataModel::getDealerName, Collectors.counting()));
        salesByDealer.forEach((dealer, count) -> {
            dealerNames.put(dealer);
            dealerData.put(count);
        });
        dealerDataSet.put(new JSONObject().put("data", dealerData));
        dealerSales.put("labels", dealerNames);
        dealerSales.put("datasets", dealerDataSet);
        return dealerSales;
    }

    private JSONObject getSalesByState(List<DataModel> sales) {
        JSONObject stateSales = new JSONObject();
        JSONArray stateNames = new JSONArray();
        JSONArray stateData = new JSONArray();
        JSONArray stateDataSet = new JSONArray();
        Map<String, Long> salesByState = sales.stream()
                .collect(Collectors.groupingBy(DataModel::getState, Collectors.counting()));
        salesByState.forEach((state, count) -> {
            stateNames.put(state);
            stateData.put(count);
        });
        stateDataSet.put(new JSONObject().put("data", stateData));
        stateSales.put("labels", stateNames);
        stateSales.put("datasets", stateDataSet);
        return stateSales;
    }
}
