package com.stockmaster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FormController {
    @PostMapping(value = "/submit-sectors")
    @ResponseBody
    public Map<String, Object> submitSectors(
            @RequestParam(value = "sector", required = false) List<String> sectors,
            @RequestParam(value = "marketCap", required = false) List<String> marketCaps,
            @RequestParam(value = "dividend", required = false) List<String> dividends,
            @RequestParam(value = "numStocks", required = false) String numStocks) {
        // Process the selected sectors in your Java program
        System.out.println(numStocks);
        System.out.println(marketCaps);
        System.out.println(dividends);
        System.out.println(sectors);
        double totalPoints = 0;
        List<Stock> list = retrieveStocks();
        String text = "";
        List<Stock> goodStocks = new ArrayList();
        List<Double> percent = new ArrayList();
        List<Double> pointTally = new ArrayList();
        List<StockAllocation> stockAllocationList = new ArrayList();

        for (Stock stock : list) {
            double points = 0;
            boolean sectorMatch = false;

            if (sectors != null) {
                String sec = stock.getSector();
                for (String sector : sectors) {
                    if (sector.equals(sec)) {
                        points += 1.8;

                        sectorMatch = true;
                    }
                }
            }

            boolean marketCapMatch = false;

            if (marketCaps != null) {
                double cap = stock.getMarketCap();
                for (String marketCap : marketCaps) {
                    if (marketCap.equals("nano cap")) {
                        if (cap < 0.05) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    } else if (marketCap.equals("micro cap")) {
                        if (cap > 0.05 && cap < 0.3) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    } else if (marketCap.equals("small cap")) {
                        if (cap > 0.3 && cap < 2) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    } else if (marketCap.equals("mid cap")) {
                        if (cap > 2 && cap < 10) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    } else if (marketCap.equals("large cap")) {
                        if (cap > 10 && cap < 200) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    } else if (marketCap.equals("mega cap")) {
                        if (cap > 200) {
                            points += 1.5;

                            marketCapMatch = true;
                        }
                    }
                }
            }

            boolean dividendMatch = false;

            if (dividends != null) {
                double div = stock.getDividend();
                for (String dividend : dividends) {

                    if (dividend.equals("no dividend")) {
                        if (div == 0) {
                            points++;

                            dividendMatch = true;
                        }
                    } else if (dividend.equals("low dividend")) {
                        if (div > 0.5 && div < 2) {
                            points++;

                            dividendMatch = true;
                        }
                    } else if (dividend.equals("moderate dividend")) {
                        if (div > 2 && div < 4) {
                            points++;

                            dividendMatch = true;
                        }
                    } else if (dividend.equals("high dividend")) {
                        if (div > 4 && div < 6) {
                            points++;

                            dividendMatch = true;
                        }
                    } else if (dividend.equals("ultra high dividend")) {
                        if (div > 6) {
                            points++;

                            dividendMatch = true;
                        }
                    }
                }
            }

            if (sectorMatch && marketCapMatch && dividendMatch) {
                points += 4;
            } else if (sectorMatch && marketCapMatch) {
                points += 2.5;
            } else if (sectorMatch && dividendMatch) {
                points += 1.5;
            } else if (marketCapMatch && dividendMatch) {
                points += 1;
            }

            double randomFactor = Math.random() * 3 + 7;

            points = points * points * randomFactor;

            totalPoints += points;

            if (points > 0) {
                goodStocks.add(stock);
                pointTally.add(1.0 * points);
            }
        }

        for (int i = 0; i < pointTally.size(); i++) {
            percent.add(100.00 * pointTally.get(i) / totalPoints);
        }

        for (int i = 0; i < goodStocks.size(); i++) {
            stockAllocationList.add(new StockAllocation(goodStocks.get(i), percent.get(i)));
        }

        Collections.sort(stockAllocationList, new StockAllocation());

        double totalAllocation = 0;

        for (int i = 0; i < Double.parseDouble(numStocks) && i < stockAllocationList.size(); i++) {
            totalAllocation += stockAllocationList.get(i).getAllocation();
        }

        List<StockAllocation> goodStockAllocationList = new ArrayList();
        List<SectorAllocation> sectorAllocationList = new ArrayList();
        List<MarketCapAllocation> marketCapAllocationList = new ArrayList();
        List<DividendAllocation> dividendAllocationList = new ArrayList();

        Map<String, Double> sectorAllocationMap = new HashMap<>();
        Map<String, Double> marketCapAllocationMap = new HashMap<>();
        Map<String, Double> dividendAllocationMap = new HashMap<>();

        for (int i = 0; i < Double.parseDouble(numStocks) && i < stockAllocationList.size(); i++) {
            double original = 100 * stockAllocationList.get(i).getAllocation() / totalAllocation;
            goodStockAllocationList.add(new StockAllocation(stockAllocationList.get(i).getStock(),
                    ((int) (1000 * (100 * stockAllocationList.get(i).getAllocation() / totalAllocation))) / 1000.000));

            double marketCap = stockAllocationList.get(i).getStock().getMarketCap();
            String marketCapName = "";

            if (marketCap < 0.05) {
                marketCapName = "Nano Cap";
            } else if (marketCap < 0.3) {
                marketCapName = "Micro Cap";
            } else if (marketCap < 2) {
                marketCapName = "Small Cap";
            } else if (marketCap < 10) {
                marketCapName = "Mid Cap";
            } else if (marketCap < 200) {
                marketCapName = "Large Cap";
            } else {
                marketCapName = "Mega Cap";
            }

            double dividend = stockAllocationList.get(i).getStock().getDividend();
            String dividendName = "";

            if (dividend < 0.5) {
                dividendName = "No Dividend";
            } else if (dividend < 2) {
                dividendName = "Low Dividend Yield";
            } else if (dividend < 4) {
                dividendName = "Moderate Dividend Yield";
            } else if (dividend < 6) {
                dividendName = "High Dividend Yield";
            } else {
                dividendName = "Ultra High Dividend Yield";
            }

            String sector = stockAllocationList.get(i).getStock().getSector();
            double allocationPercentage = original;
            if (!sectorAllocationMap.containsKey(sector)) {
                sectorAllocationMap.put(sector, allocationPercentage);
            } else {
                sectorAllocationMap.put(sector, sectorAllocationMap.get(sector) + allocationPercentage);
            }

            if (!marketCapAllocationMap.containsKey(marketCapName)) {
                marketCapAllocationMap.put(marketCapName, allocationPercentage);
            } else {
                marketCapAllocationMap.put(marketCapName,
                        marketCapAllocationMap.get(marketCapName) + allocationPercentage);
            }

            if (!dividendAllocationMap.containsKey(dividendName)) {
                dividendAllocationMap.put(dividendName, allocationPercentage);
            } else {
                dividendAllocationMap.put(dividendName, dividendAllocationMap.get(dividendName) + allocationPercentage);
            }
        }

        for (String key : sectorAllocationMap.keySet()) {
            sectorAllocationList.add(new SectorAllocation(key, (int) (100 * sectorAllocationMap.get(key)) / 100.00));
        }

        for (String key : marketCapAllocationMap.keySet()) {
            marketCapAllocationList
                    .add(new MarketCapAllocation(key, ((int) (100 * marketCapAllocationMap.get(key))) / 100.00));
        }

        for (String key : dividendAllocationMap.keySet()) {
            dividendAllocationList
                    .add(new DividendAllocation(key, ((int) (100 * dividendAllocationMap.get(key))) / 100.00));
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("goodStockAllocationList", goodStockAllocationList);
        responseMap.put("sectorAllocationList", sectorAllocationList);
        responseMap.put("marketCapAllocationList", marketCapAllocationList);
        responseMap.put("dividendAllocationList", dividendAllocationList);

        // Return a response to the client
        return responseMap;
    }

    public List<Stock> retrieveStocks() {
        String dbUrl = "jdbc:postgresql://localhost:5432/stock";
        String dbUsername = "postgres";
        String dbPassword = "password";
        List<Stock> list = new ArrayList();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM stock";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                String ticker = resultSet.getString("ticker");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                double marketCap = resultSet.getDouble("market_cap");
                double volume = resultSet.getDouble("volume");
                double dividend = resultSet.getDouble("dividend");
                String sector = resultSet.getString("sector");

                Stock stock = new Stock(ticker, name, price, marketCap, dividend, volume, sector);

                list.add(stock);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
