package com.stockmaster;

import jakarta.persistence.*;

@Entity
@Table
public class Stock {
    @Id
    private String ticker;

    private String name;
    private double price;
    private double marketCap;
    private double dividend;
    private double volume;
    private String sector;

    public Stock() {

    }

    public Stock(String ticker, String name, double price, double marketCap, double dividend, double volume,
            String sector) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.marketCap = marketCap;
        this.dividend = dividend;
        this.volume = volume;
        this.sector = sector;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return "Ticker: " + ticker +
                "\nName: " + name +
                "\nPrice: $" + price +
                "\nMarket Cap ($B): " + marketCap +
                "\nVolume: " + volume +
                "\nDividend (%): " + dividend +
                "\nSector: " + sector;
    }

}
