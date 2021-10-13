package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetDetailResponse;
import com.sharklabs.ams.asset.AssetResponse;
import com.sharklabs.ams.fieldtemplate.FieldTemplateResponse;

public class GetAssetDetailResponse {
    private AssetDetailResponse assetDetail;
    private FieldTemplateResponse fieldTemplate;
    private String responseIdentifier;

//    public AssetDetailResponse getAsset() {
//        return assetDetail;
//    }

    public AssetDetailResponse getAssetDetail() {
        return assetDetail;
    }

    public void setAssetDetail(AssetDetailResponse assetDetail) {
        this.assetDetail = assetDetail;
    }

    public FieldTemplateResponse getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplateResponse fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
