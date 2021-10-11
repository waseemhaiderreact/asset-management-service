package com.sharklabs.ams.wallet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity (name = "t_wallet_request")
public class WalletRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date dateTime;
    private String orgUUID;
    private String senderUUID;
    private String responderUUID;
    private String senderWalletUUID;
    private String receiverWalletUUID;
    private Double Quantity;
    private String product;
    private String requestUUID;
    private Boolean approveFlag;
    private String requestPriority;
    private String walletType;
    private Boolean ignoreFlag;
    private Boolean withDrawFlag;
    private String requestType;
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public String getResponderUUID() {
        return responderUUID;
    }
    public void setResponderUUID(String receiverUUID) {
        this.responderUUID = receiverUUID;
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
    public String getSenderUUID() {
        return senderUUID;
    }
    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
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
    public String getSenderWalletUUID() {
        return senderWalletUUID;
    }
    public void setSenderWalletUUID(String senderWalletUUID) {
        this.senderWalletUUID = senderWalletUUID;
    }
    public String getReceiverWalletUUID() {
        return receiverWalletUUID;
    }
    public void setReceiverWalletUUID(String receiverWalletUUID) {
        this.receiverWalletUUID = receiverWalletUUID;
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getApproveFlag() {
        return approveFlag;
    }
    public void setApproveFlag(Boolean approveFlag) {
        this.approveFlag = approveFlag;
    }
    public Boolean getIgnoreFlag() {
        return ignoreFlag;
    }
    public void setIgnoreFlag(Boolean ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }

}
