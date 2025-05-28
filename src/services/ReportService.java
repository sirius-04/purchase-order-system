/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.Item;
import models.Sales;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import repository.ItemRepository;
import repository.SalesRepository;
/**
 *
 * @author ngoh
 */
public class ReportService {
    private final SalesRepository salesRepo = new SalesRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    public Map<String, Double> getDailyProfit() {
        List<Sales> salesList = salesRepo.getAll();
        Map<String, Double> dailyProfitMap = new TreeMap<>();

        for (Sales sale : salesList) {
            Item item = itemRepo.find(sale.getItemId());
            if (item != null) {
                double profit = (item.getSellPrice()- item.getPrice()) * sale.getQuantity();
                dailyProfitMap.merge(sale.getDate(), profit, Double::sum);
            }
        }

        return dailyProfitMap;
    }

    public JFreeChart createDailyProfitChart(Map<String, Double> dailyProfitMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : dailyProfitMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Profit", entry.getKey());
        }

        return ChartFactory.createLineChart(
            "Daily Profit",
            "Date",
            "Profit",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            true, true, false);
    }
}
