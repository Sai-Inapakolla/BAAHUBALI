package com.bank.loan;

public enum LoanType {
    EDUCATIONAL("Educational Loan", 8.5),
    FARMING("Farming Loan", 6.0),
    GOLD("Gold Loan", 12.0),
    PERSONAL("Personal Loan", 15.0);
    
    private final String displayName;
    private final double defaultInterestRate;
    
    LoanType(String displayName, double defaultInterestRate) {
        this.displayName = displayName;
        this.defaultInterestRate = defaultInterestRate;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public double getDefaultInterestRate() {
        return defaultInterestRate;
    }
}
