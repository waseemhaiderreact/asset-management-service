package com.sharklabs.ams.request;

public class AddRequestInWallet {

    private String receiverUUID;
    private String senderUUID;
    private Double quantity;
    private String requestType;
    private String description;
    private String walletType;
    public String getWalletType() {
        return walletType;
    }
    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getReceiverUUID() {
        return receiverUUID;
    }
    public void setReceiverUUID(String receiverUUID) {
        this.receiverUUID = receiverUUID;
    }
    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    public String getSenderUUID() {
        return senderUUID;
    }
    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

}
