package com.sharklabs.ams.events.wallet;
/* Written By Kumail Khan*/
public class WalletNotificationModel {
    private String action;
    private WalletNotification walletNotification;
    private String walletName;

    public WalletNotification getWalletNotification() {
        return walletNotification;
    }

    public void setWalletNotification(WalletNotification walletNotification) {
        this.walletNotification = walletNotification;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WalletNotification getNotification() {
        return walletNotification;
    }

    public void setNotification(WalletNotification walletNotification) {
        this.walletNotification = walletNotification;
    }


}

