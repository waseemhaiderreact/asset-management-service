package com.sharklabs.ams.response;

import com.sharklabs.ams.message.Message;

public class EditMessageResponse {
    private Message message;
    private String responseIdentifier;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
