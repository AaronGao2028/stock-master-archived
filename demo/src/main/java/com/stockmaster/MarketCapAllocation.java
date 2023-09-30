package com.stockmaster;

public class MarketCapAllocation {
    private String marketCapName;
    private double allocationPercentage;

    public MarketCapAllocation(String marketCapName, double allocationPercentage) {
        this.marketCapName = marketCapName;
        this.allocationPercentage = allocationPercentage;
    }

    public String getMarketCapName() {
        return marketCapName;
    }

    public void setMarketCapName(String marketCapName) {
        this.marketCapName = marketCapName;
    }

    public double getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(double allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public String toString() {
        return "Market Cap: " + marketCapName + "\nAllocation Percentage: " + allocationPercentage;
    }
}
