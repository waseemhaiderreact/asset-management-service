package com.sharklabs.ams.response;

import com.sharklabs.ams.usage.Usage;
import org.springframework.data.domain.Page;

public class GetPaginatedUsagesResponse {
    private String responseIdentifier;
    private Page<Usage> usages;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public Page<Usage> getUsages() {
        return usages;
    }

    public void setUsages(Page<Usage> usages) {
        this.usages = usages;
    }
}
