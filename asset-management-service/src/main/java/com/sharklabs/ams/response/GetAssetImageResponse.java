package com.sharklabs.ams.response;

import com.sharklabs.ams.AssetImage.AssetImage;

import java.util.List;

public class GetAssetImageResponse {

    private List<AssetImage> assetImages;

    private String responseIdentifer;

    public List<AssetImage> getAssetImages() {
        return assetImages;
    }

    public void setAssetImages(List<AssetImage> assetImages) {
        this.assetImages = assetImages;
    }

    public String getResponseIdentifer() {
        return responseIdentifer;
    }

    public void setResponseIdentifer(String responseIdentifer) {
        this.responseIdentifer = responseIdentifer;
    }
}
