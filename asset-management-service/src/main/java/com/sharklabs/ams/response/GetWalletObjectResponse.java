package com.sharklabs.ams.response;

import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.wallet.Wallet;

public class GetWalletObjectResponse {

    private Wallet object;

    public String getAssetCategoryName() {
        return AssetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        AssetCategoryName = assetCategoryName;
    }

    private String AssetCategoryName;
    private String responseIdentifier;

    public Wallet getWallet() {
        return object;
    }

    public void setWallet(Wallet wallet) {
        this.object = wallet;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }


}
