package com.sharklabs.ams.response;

import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.wallet.WalletRequest;
import com.sharklabs.ams.wallet.WalletRequestsUserData;

import java.util.List;

public class GetRequestUserDataResponse {

    private WalletRequestModel walletRequestModel;
    private String responseIdentifier;
    private  Long totalElements;
    private List<WalletRequestsUserData> Array;

    public WalletRequestModel getWalletRequestModel() {
        return walletRequestModel;
    }
    public void setWalletRequestModel(WalletRequestModel walletRequestModel) {
        this.walletRequestModel = walletRequestModel;
    }
    public List<WalletRequestsUserData> getArray() {
        return Array;
    }
    public void setArray(List<WalletRequestsUserData> array) {
        Array = array;
    }
    public String getResponseIdentifier() {
        return responseIdentifier;
    }
    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
    public Long getTotalElements() {
        return totalElements;
    }
    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
