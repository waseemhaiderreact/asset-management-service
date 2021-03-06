package com.sharklabs.ams.request;

public class AddMessageRequest {

    private String messageBody;
    private String title;
    private String assetWallUUID;
    private String userName;
    private String userUUID;
    private String priority;


    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssetWallUUID() {
        return assetWallUUID;
    }

    public void setAssetWallUUID(String assetWallUUID) {
        this.assetWallUUID = assetWallUUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
