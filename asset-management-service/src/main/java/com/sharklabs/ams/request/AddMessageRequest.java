package com.sharklabs.ams.request;

import com.sharklabs.ams.message.Message;

public class AddMessageRequest {
    private Message message;
    private String assetId;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
