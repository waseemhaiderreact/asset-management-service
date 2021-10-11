package com.sharklabs.ams.response;

import com.sharklabs.ams.fact.Fact;
import com.sharklabs.ams.wallet.Wallet;

import java.util.List;

public class getWalletByUserUUIDAndOrgUUIDResponse {
    public List<Wallet> getArray() {
        return Array;
    }
    public void setArray(List<Wallet> array) {
        Array = array;
    }
    private List<Wallet> Array;
    private String responseIdentifier;
    public String getResponseIdentifier() {
        return responseIdentifier;
    }
    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
