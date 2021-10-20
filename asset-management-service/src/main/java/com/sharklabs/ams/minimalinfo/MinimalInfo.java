package com.sharklabs.ams.minimalinfo;

import java.io.Serializable;

public class MinimalInfo implements Serializable {

    public interface AssetInfo{
        String getUuid();
        String getName();
    }

    public interface AssetGroupInfo{
        String getUuid();
        String getGroupName();
    }
}
