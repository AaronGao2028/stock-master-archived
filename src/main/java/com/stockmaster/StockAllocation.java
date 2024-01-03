package com.stockmaster;

import java.util.Comparator;

public class StockAllocation implements Comparator<StockAllocation> {
    private Stock stock;
    private double allocation;

    public StockAllocation() {
    }

    public StockAllocation(Stock stock, double allocation) {
        this.stock = stock;
        this.allocation = allocation;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public double getAllocation() {
        return allocation;
    }

    public void setAllocation(double allocation) {
        this.allocation = allocation;
    }

    @Override
    public int compare(StockAllocation allocation1, StockAllocation allocation2) {
        // Compare allocations in descending order
        // Return a negative value if allocation1 > allocation2
        // Return a positive value if allocation1 < allocation2
        // Return 0 if allocation1 == allocation2
        return Double.compare(allocation2.getAllocation(), allocation1.getAllocation());
    }

    public String toString() {
        return stock + "\nAllocation: " + allocation;
    }
}
