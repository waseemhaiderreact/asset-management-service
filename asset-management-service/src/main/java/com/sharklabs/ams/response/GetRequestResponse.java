package com.sharklabs.ams.response;

import com.sharklabs.ams.fact.Fact;
import com.sharklabs.ams.wallet.WalletRequest;

import java.util.List;

public class GetRequestResponse {
    private List<WalletRequest> Array;
    private String responseIdentifier;
    private  Long totalElements;

    public List<WalletRequest> getArray() {
        return Array;
    }

    public void setArray(List<WalletRequest> array) {
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
