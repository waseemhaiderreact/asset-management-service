package com.sharklabs.ams.response;

import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.wallet.Wallet;

import java.util.List;

public class getWalletsUserResponse {
    public WalletRequestModel getWalletRequestModel() {
        return walletRequestModel;
    }

    public void setWalletRequestModel(WalletRequestModel walletRequestModel) {
        this.walletRequestModel = walletRequestModel;
    }

    private WalletRequestModel walletRequestModel;

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

    private String responseIdentifier;
    private  Long totalElements;
}
