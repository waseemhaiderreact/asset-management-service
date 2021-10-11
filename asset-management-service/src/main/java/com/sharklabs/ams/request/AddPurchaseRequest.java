package com.sharklabs.ams.request;

public class AddPurchaseRequest {

    private String walletUUID;
    private String userUUId;
    private Double rate;
    private String rateBasisUnit; //Unit
    private String rateCurrency; //currency
    private Double total;   //price
    private String quantityUnit;
    private Double quantity;
    private String TransactionType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getWalletUUID() {
        return walletUUID;
    }
    public void setWalletUUID(String walletUUID) {
        this.walletUUID = walletUUID;
    }
    public String getUserUUId() {
        return userUUId;
    }
    public void setUserUUId(String userUUId) {
        this.userUUId = userUUId;
    }
    public String getTransactionType() {
        return TransactionType;
    }
    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }
    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public String getRateBasisUnit() {
        return rateBasisUnit;
    }
    public void setRateBasisUnit(String rateBasisUnit) {
        this.rateBasisUnit = rateBasisUnit;
    }
    public String getRateCurrency() {
        return rateCurrency;
    }
    public void setRateCurrency(String rateCurrency) {
        this.rateCurrency = rateCurrency;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public String getQuantityUnit() {
        return quantityUnit;
    }
    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
