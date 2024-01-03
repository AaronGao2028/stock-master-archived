package com.stockmaster;

public class DividendAllocation {
    private String dividendName;
    private double allocationPercentage;

    public DividendAllocation(String dividendName, double allocationPercentage) {
        this.dividendName = dividendName;
        this.allocationPercentage = allocationPercentage;
    }

    public String getDividendName() {
        return dividendName;
    }

    public void setDividendName(String dividendName) {
        this.dividendName = dividendName;
    }

    public double getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(double allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public String toString() {
        return "Dividend: " + dividendName + "\nAllocation Percent: " + allocationPercentage;
    }
}
