package com.sharklabs.ams.response;

import com.sharklabs.ams.usage.Usage;

public class GetUsageByIdResponse {
    private String responseIdentifier;
    private Usage usage;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
