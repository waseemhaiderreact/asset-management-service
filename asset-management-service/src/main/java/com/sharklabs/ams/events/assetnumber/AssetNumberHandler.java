package com.sharklabs.ams.events.assetnumber;

import com.sharklabs.ams.api.AssetService;
import com.sharklabs.ams.events.CustomChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


@EnableBinding(CustomChannels.class)
public class AssetNumberHandler {
    @Autowired
    AssetService assetService;

    @StreamListener("inBoundAssetNumber")
    public void loggerSink(AssetNumberModel assetNumberModel){
        System.out.println("Recieving Asset Number");
        switch(assetNumberModel.getAction()) {
            case "CREATE":
                System.out.println("**Setting Attribute Assigned of Vehicle True****");
//                assetService.setAssignedAttribute(assetNumberModel.getAssetNumber(),true);
                break;
            case "DELETE":
                System.out.println("**Setting Attribute Assigned of Vehicle False****");
//                assetService.setAssignedAttribute(assetNumberModel.getAssetNumber(),false);
                break;
        }
    }
}
