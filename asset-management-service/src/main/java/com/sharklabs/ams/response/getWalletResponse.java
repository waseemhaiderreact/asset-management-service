package com.sharklabs.ams.response;

import com.sharklabs.ams.wallet.Wallet;
import com.sharklabs.ams.wallet.WalletRequest;

import java.util.List;

public class getWalletResponse {
    private List<Wallet> Array;

    public List<Wallet> getArray() {
        return Array;
    }

    public void setArray(List<Wallet> array) {
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

    private String responseIdentifier;
    private  Long totalElements;
}
