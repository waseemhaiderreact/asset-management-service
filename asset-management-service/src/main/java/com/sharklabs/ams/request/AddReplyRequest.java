package com.sharklabs.ams.request;

public class AddReplyRequest {

    private String messageBody;
    private String messageUUID;
    private String userName;
    private String userUUID;
    private String workOrderUUID;//to get work order and get assignee uuids


    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageUUID() {
        return messageUUID;
    }

    public void setMessageUUID(String messageUUID) {
        this.messageUUID = messageUUID;
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

    public String getWorkOrderUUID() {
        return workOrderUUID;
    }

    public void setWorkOrderUUID(String workOrderUUID) {
        this.workOrderUUID = workOrderUUID;
    }
}
