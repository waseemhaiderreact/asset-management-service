package com.sharklabs.ams.response;

import com.sharklabs.ams.assetGroup.AssetGroup;
import org.springframework.data.domain.Page;

public class GetPaginatedAssetGroupsResponse {

    private Page<AssetGroup> assetGroups;
    private String responseIdentifier;

    public Page<AssetGroup> getAssetGroups() {
        return assetGroups;
    }

    public void setAssetGroups(Page<AssetGroup> assetGroups) {
        this.assetGroups = assetGroups;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
