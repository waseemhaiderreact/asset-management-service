package com.sharklabs.ams.request;

public class AddSpendRequest {

    private String walletUUID;
    private String userUUId;
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

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }


}
