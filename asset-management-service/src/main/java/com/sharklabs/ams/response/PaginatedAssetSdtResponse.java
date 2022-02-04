package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetMapper;

import java.util.ArrayList;
import java.util.List;

public class PaginatedAssetSdtResponse {

    private List<AssetMapper>  assetMappers = new ArrayList<>();

    private Long totalPages;

    private Long totalElements;

    private String responseIdentifier;

    public List<AssetMapper> getAssetMappers() {
        return assetMappers;
    }

    public void setAssetMappers(List<AssetMapper> assetMappers) {
        this.assetMappers = assetMappers;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
