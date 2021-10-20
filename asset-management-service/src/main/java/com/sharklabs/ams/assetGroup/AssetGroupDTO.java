package com.sharklabs.ams.assetGroup;

import java.io.Serializable;

public class AssetGroupDTO implements Serializable {

    private String uuid;

    private String groupName;

    public AssetGroupDTO(String uuid, String groupName) {
        this.uuid = uuid;
        this.groupName = groupName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
