/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.Item;
import models.Sales;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public Map<String, Double> getDailyProfit() {
        List<Sales> salesList = salesRepo.getAll();
        Map<String, Double> dailyProfitMap = new TreeMap<>();
       
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        LocalDate lastOfMonth = currentYearMonth.atEndOfMonth();

        for (Sales sale : salesList) {
            try {
                LocalDate saleDate = LocalDate.parse(sale.getDate(), dateFormatter);
                
                // Filter sales to current month
                if (!saleDate.isBefore(firstOfMonth) && !saleDate.isAfter(lastOfMonth)) {
                    Item item = itemRepo.find(sale.getItemId());
                    if (item != null) {
                        double profit = (item.getSellPrice() - item.getPrice()) * sale.getQuantity();
                        dailyProfitMap.merge(sale.getDate(), profit, Double::sum);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing date: " + sale.getDate());
            }
        }

        return dailyProfitMap;
    }

    public JFreeChart createDailyProfitChart(Map<String, Double> dailyProfitMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : dailyProfitMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Profit", entry.getKey());
        }

        String monthName = YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        String title = "Daily Profit - " + monthName;

        return ChartFactory.createLineChart(
            title,
            "Date",
            "Profit",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            true, true, false);
    }
    
     public Map<String, Double> getStockQuantities() {
        List<Item> items = itemRepo.getAll();
        Map<String, Double> stockMap = new TreeMap<>();
        
        for (Item item : items) {
            stockMap.put(item.getName(), (double) item.getStockQuantity());
        }
        
        return stockMap;
    }

    public Map<String, Double> getItemPrices() {
        List<Item> items = itemRepo.getAll();
        Map<String, Double> priceMap = new TreeMap<>();
        
        for (Item item : items) {
            priceMap.put(item.getName(), item.getPrice());
        }
        
        return priceMap;
    }

    public JFreeChart createStockReportChart(
            Map<String, Double> quantityMap, 
            Map<String, Double> priceMap) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : quantityMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Stock Quantity", entry.getKey());
        }

        for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Price (RM)", entry.getKey());
        }

        return ChartFactory.createBarChart(
            "Stock Report",
            "Item Name",
            "Value",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
    }
}
