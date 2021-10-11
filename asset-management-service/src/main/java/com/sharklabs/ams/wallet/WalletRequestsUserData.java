package com.sharklabs.ams.wallet;

import java.util.Date;

public class WalletRequestsUserData {
    private Date dateTime;
    private String orgUUID;
    private String senderUUID;
    private String responderUUID;
    private String senderWalletUUID;
    private String responderWalletUUID;
    private Double Quantity;
    private String product;
    private String requestUUID;
    private Boolean approveFlag;
    private String requestPriority;
    private String walletType;
    private Boolean ignoreFlag;
    private Boolean withDrawFlag;
    private String requestType;
    private String senderName;
    private String responderName;
    private String senderEmail;
    private String responderEmail;
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrgUUID() {
        return orgUUID;
    }

    public void setOrgUUID(String orgUUID) {
        this.orgUUID = orgUUID;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public String getResponderUUID() {
        return responderUUID;
    }

    public void setResponderUUID(String responderUUID) {
        this.responderUUID = responderUUID;
    }

    public String getSenderWalletUUID() {
        return senderWalletUUID;
    }

    public void setSenderWalletUUID(String senderWalletUUID) {
        this.senderWalletUUID = senderWalletUUID;
    }

    public String getResponderWalletUUID() {
        return responderWalletUUID;
    }

    public void setResponderWalletUUID(String responderWalletUUID) {
        this.responderWalletUUID = responderWalletUUID;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    public Boolean getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(Boolean approveFlag) {
        this.approveFlag = approveFlag;
    }

    public String getRequestPriority() {
        return requestPriority;
    }

    public void setRequestPriority(String requestPriority) {
        this.requestPriority = requestPriority;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public Boolean getIgnoreFlag() {
        return ignoreFlag;
    }

    public void setIgnoreFlag(Boolean ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }

    public Boolean getWithDrawFlag() {
        return withDrawFlag;
    }

    public void setWithDrawFlag(Boolean withDrawFlag) {
        this.withDrawFlag = withDrawFlag;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getResponderName() {
        return responderName;
    }

    public void setResponderName(String responderName) {
        this.responderName = responderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getResponderEmail() {
        return responderEmail;
    }

    public void setResponderEmail(String responderEmail) {
        this.responderEmail = responderEmail;
    }


}
