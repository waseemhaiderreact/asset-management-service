package com.sharklabs.ams.minimalinfo;

public class MinimalInfo {

    public interface AssetInfo{
        String getUuid();
        String getName();
    }

    public interface AssetGroupInfo{
        String getUuid();
        String getGroupName();
    }
}
