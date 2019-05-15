package com.sharklabs.ams.response;

import com.sharklabs.ams.consumption.Consumption;
import org.springframework.data.domain.Page;

public class GetPaginatedConsumptionsByAssetsResponse {
    private Page<Consumption> consumptions;
    private String responseIdentifier;

    public Page<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(Page<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
