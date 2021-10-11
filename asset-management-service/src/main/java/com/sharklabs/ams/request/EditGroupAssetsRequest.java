package com.sharklabs.ams.request;

import java.util.ArrayList;

public class EditGroupAssetsRequest {
    private ArrayList<String> assetUUIDs;
    private String groupUUID;
    private String groupName;
    private String editedByUserUUID;
    private String editedByUserName;


    public ArrayList<String> getAssetUUIDs() {
        return assetUUIDs;
    }

    public void setAssetUUIDs(ArrayList<String> assetUUIDs) {
        this.assetUUIDs = assetUUIDs;
    }

    public String getGroupUUID() {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID) {
        this.groupUUID = groupUUID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEditedByUserUUID() {
        return editedByUserUUID;
    }

    public void setEditedByUserUUID(String editedByUserUUID) {
        this.editedByUserUUID = editedByUserUUID;
    }

    public String getEditedByUserName() {
        return editedByUserName;
    }

    public void setEditedByUserName(String editedByUserName) {
        this.editedByUserName = editedByUserName;
    }
}

