package com.sharklabs.ams.events.asset;

import java.util.ArrayList;

public class AssetsNameModel {
    private ArrayList<String> assetName;
    private ArrayList<String> assetUUID;

    public ArrayList<String> getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(ArrayList<String> assetUUID) {
        this.assetUUID = assetUUID;
    }

    public void setAssetName(ArrayList<String> assetName) {
        this.assetName = assetName;
    }
    public ArrayList<String> getAssetName() {
        return assetName;
    }
}
