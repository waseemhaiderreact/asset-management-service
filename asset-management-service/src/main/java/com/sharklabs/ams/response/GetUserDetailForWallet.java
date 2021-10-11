package com.sharklabs.ams.response;


import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.events.wallet.WalletUser;

import java.util.List;

public class GetUserDetailForWallet {
    private String responseIdentifier;

    public WalletRequestModel getWalletRequestModel() {
        return walletRequestModel;
    }

    public void setWalletRequestModel(WalletRequestModel walletRequestModel) {
        this.walletRequestModel = walletRequestModel;
    }

    private WalletRequestModel walletRequestModel;

    public List<WalletUser> getWalletUser() {
        return walletUser;
    }

    public void setWalletUser(List<WalletUser> walletUser) {
        this.walletUser = walletUser;
    }

    private List<WalletUser> walletUser;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }



}
