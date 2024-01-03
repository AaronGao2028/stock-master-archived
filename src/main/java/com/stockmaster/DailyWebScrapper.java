package com.stockmaster;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

class Helper extends TimerTask {
    public void run() {
        try {
            String fileName = "C:\\Users\\aaron\\eclipse-workspace\\WebDemo\\data.txt";

            BufferedReader in = new BufferedReader(new FileReader(fileName));

            String ticker = in.readLine();

            do {
                String urlHomePage = "https://finance.yahoo.com/quote/" + ticker;
                String urlProfilePage = "https://finance.yahoo.com/quote/" + ticker + "/profile?p=" + ticker;

                try {
                    Document documentHomePage = Jsoup.connect(urlHomePage).get();
                    Document documentProfilePage = Jsoup.connect(urlProfilePage).get();

                    Element priceElement = documentHomePage.select("td[data-test='OPEN-value']").first();
                    double price = Double.parseDouble(priceElement.text());

                    Element volumeElement = documentHomePage.select("td[data-test='TD_VOLUME-value']").first();
                    double volume = Double.parseDouble(volumeElement.text().replaceAll(",", ""));

                    Element companyNameElement = documentHomePage.select("td[data-test='FIFTY_TWO_WK_RANGE-value']")
                            .first();
                    String name = documentHomePage.title();
                    name = name.replace(" Stock Price, News, Quote & History - Yahoo Finance", "");

                    if (name.length() > 40) {
                        name = name.substring(0, 40) + ".";
                    }

                    Element dividendElement = documentHomePage.select("td[data-test='DIVIDEND_AND_YIELD-value']")
                            .first();
                    String dividendString = dividendElement.text();

                    double dividend = 0;

                    if (dividendString.equals("N/A (N/A)")) {
                        dividend = 0;
                    } else {
                        String temp = "";

                        for (int i = 0; i < dividendString.length(); i++) {
                            if (dividendString.charAt(i) != ' ') {
                                temp += dividendString.charAt(i);
                            } else {
                                i = dividendString.length();
                            }
                        }

                        dividend = ((int) (100 * (100 * Double.parseDouble(temp) / price))) / 100.00;
                    }

                    Element marketCapElement = documentHomePage.select("td[data-test='MARKET_CAP-value']").first();
                    String marketCapString = marketCapElement.text();
                    double marketCap = 0;
                    char letter = marketCapString.charAt(marketCapString.length() - 1);

                    if (letter == 'B') {
                        marketCap = Double.parseDouble(marketCapString.substring(0, marketCapString.length() - 1));
                    } else if (letter == 'T') {
                        marketCap = Double.parseDouble(marketCapString.substring(0, marketCapString.length() - 1))
                                * 1000;
                    } else if (letter == 'M') {
                        marketCap = Double.parseDouble(marketCapString.substring(0, marketCapString.length() - 1))
                                / 1000;
                    }

                    marketCap = ((int) (1000 * marketCap)) / 1000.000;

                    Element sectorElement = documentProfilePage.select("span[class=Fw(600)]").first();
                    String sector = sectorElement.text();

                    System.out.println("Ticker: " + ticker);
                    System.out.println("Name: " + name);
                    System.out.println("Price: " + price);
                    System.out.println("Market Cap: " + marketCap);
                    System.out.println("Volume: " + volume);
                    System.out.println("Dividend: " + dividend);
                    System.out.println("Sectors: " + sector);

                    String jdbcUrl = "jdbc:postgresql://localhost:5432/stock";
                    String username = "postgres";
                    String password = "password";

                    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                        String selectQuery = "SELECT COUNT(*) FROM stock WHERE ticker = ?";
                        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                            selectStatement.setString(1, ticker); // Ticker to check

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                resultSet.next();
                                int rowCount = resultSet.getInt(1);

                                if (rowCount > 0) {
                                    // Stock already exists, update it
                                    String updateQuery = "UPDATE stock SET name = ?, price = ?, volume = ?, market_cap = ?, dividend = ?, sector = ? WHERE ticker = ?";
                                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                        updateStatement.setString(1, name);
                                        updateStatement.setDouble(2, price);
                                        updateStatement.setDouble(3, volume);
                                        updateStatement.setDouble(4, marketCap);
                                        updateStatement.setDouble(5, dividend);
                                        updateStatement.setString(6, sector);
                                        updateStatement.setString(7, ticker);

                                        Stock stock = new Stock(ticker, name, price, marketCap, dividend, volume,
                                                sector);

                                        System.out.println("UPDATE");
                                        System.out.println(stock);
                                        System.out.println();

                                        int rowsAffected = updateStatement.executeUpdate();
                                        System.out.println(rowsAffected + " row(s) updated successfully.");
                                    }
                                } else {
                                    // Stock doesn't exist, insert it
                                    String insertQuery = "INSERT INTO stock (ticker, name, price, volume, market_cap, dividend, sector) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                        insertStatement.setString(1, ticker);
                                        insertStatement.setString(2, name);
                                        insertStatement.setDouble(3, price);
                                        insertStatement.setDouble(4, volume);
                                        insertStatement.setDouble(5, marketCap);
                                        insertStatement.setDouble(6, dividend);
                                        insertStatement.setString(7, sector);

                                        Stock stock = new Stock(ticker, name, price, marketCap, dividend, volume,
                                                sector);

                                        System.out.println("INSERT");
                                        System.out.println(stock);
                                        System.out.println();

                                        int rowsAffected = insertStatement.executeUpdate();
                                        System.out.println(rowsAffected + " row(s) inserted successfully.");
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ticker = in.readLine();
            } while (ticker != null);
        } catch (IOException iox) {
            System.out.println("Error! An IOException occured.");
        }
    }
}

public class DailyWebScrapper {
    public static void main(String[] args) throws IOException {

        Timer timer = new Timer();
        TimerTask task = new Helper();

        timer.schedule(task, 0, 86400000);

    }
}