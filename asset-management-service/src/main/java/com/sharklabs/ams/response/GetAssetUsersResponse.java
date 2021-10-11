package com.sharklabs.ams.response;

import java.util.HashMap;
import java.util.Map;

public class GetAssetUsersResponse {

    Map<String,Object> users = new HashMap<>();

    public Map<String, Object> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Object> users) {
        this.users = users;
    }
}
