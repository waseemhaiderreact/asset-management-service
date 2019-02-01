package com.sharklabs.ams.response;

public class DefaultResponse {
    String responseIdentifier;
    String description;
    String responseCode;
    String objectId;

    /*
    * Constructors
     */
    public DefaultResponse(String responseIdentifier, String description, String responseCode,String objectId) {
        this.responseIdentifier = responseIdentifier;
        this.description = description;
        this.responseCode = responseCode;
        this.objectId=objectId;
    }
    public DefaultResponse(String responseIdentifier, String description, String responseCode) {
        this.responseIdentifier = responseIdentifier;
        this.description = description;
        this.responseCode = responseCode;

    }

    public DefaultResponse(){}

    public DefaultResponse(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
