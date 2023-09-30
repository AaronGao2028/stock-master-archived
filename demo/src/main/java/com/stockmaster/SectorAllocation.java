package com.stockmaster;

public class SectorAllocation {
    private String sectorName;
    private double allocationPercentage;

    public SectorAllocation(String sectorName, double allocationPercentage) {
        this.sectorName = sectorName;
        this.allocationPercentage = allocationPercentage;
    }

    public String toString() {
        return "Sector: " + sectorName + "\n" + "Percent: " + allocationPercentage;
    }
    // Getters and setters

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public double getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(double allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }
}
