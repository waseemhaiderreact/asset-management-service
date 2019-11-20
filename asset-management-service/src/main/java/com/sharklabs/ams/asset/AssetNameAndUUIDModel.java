package com.sharklabs.ams.asset;

public class AssetNameAndUUIDModel {
    String name;
    String uuid;

    public AssetNameAndUUIDModel(){}

    public AssetNameAndUUIDModel(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
