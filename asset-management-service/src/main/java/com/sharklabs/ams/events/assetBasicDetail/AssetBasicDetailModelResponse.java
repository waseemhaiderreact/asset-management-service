package com.sharklabs.ams.events.assetBasicDetail;

import java.util.ArrayList;
import java.util.List;

public class AssetBasicDetailModelResponse {
    List<AssetBasicDetailModel> assets = new ArrayList<>();

    public List<AssetBasicDetailModel> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetBasicDetailModel> assets) {
        this.assets = assets;
    }
}
