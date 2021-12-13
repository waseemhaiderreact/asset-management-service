package com.sharklabs.ams.request;

import java.util.HashMap;
import java.util.List;

public class GetPaginatedDataForSDTRequest {
    private String searchQuery;
    private List<HashMap<String,Object>> filters;
    private String sortField;
    private String sortDirection;
    private int offset;
    private int limit;
    private String tenantUUID;
    private String userUUID;

    public List<HashMap<String, Object>> getFilters() {
        return filters;
    }

    public void setFilters(List<HashMap<String, Object>> filters) {
        this.filters = filters;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
