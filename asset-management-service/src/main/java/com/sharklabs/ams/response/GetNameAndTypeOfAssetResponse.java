package com.sharklabs.ams.response;

public class GetNameAndTypeOfAssetResponse {
    private String name;
    private String type;
    private String assetNumber;
    private String uuid;

    public GetNameAndTypeOfAssetResponse(String name, String type, String assetNumber,String uuid) {
        this.name = name;
        this.type = type;
        this.assetNumber = assetNumber;
        this.uuid=uuid;
    }

    public GetNameAndTypeOfAssetResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
