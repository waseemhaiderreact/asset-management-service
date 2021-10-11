package com.sharklabs.ams.response;

import com.sharklabs.ams.fact.Fact;

import java.util.List;

public class GetSpentResponse {
    private List<Fact> Array;
    private String responseIdentifier;
    private  Long totalElements;

    public List<Fact> getFactList() {
        return Array;
    }

    public void setFactList(List<Fact> factList) {
        this.Array = factList;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
