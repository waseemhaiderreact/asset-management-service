package com.sharklabs.ams.events.wallet;

import java.util.List;

public class WalletRequestModel {

    public List<WalletUser> walletUser;
    public List<WalletUser> getWalletUser() {
        return walletUser;
    }
    public void setWalletUser(List<WalletUser> walletUserUUIDS) {
        this.walletUser = walletUserUUIDS;
    }

}
