package com.sharklabs.ams.events.usage;

import com.sharklabs.ams.usage.Usage;

public class UsageModel {
    private String action;
    private Usage usage;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
