package com.sharklabs.ams.events.asset;

import com.sharklabs.ams.asset.AssetMapper;
import com.sharklabs.ams.asset.AssetMapperRepository;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannels.class)
public class AssetIssuesHandler {

    private static final Logger LOGGER = Logger.getLogger(AssetIssuesHandler.class);

    @Autowired
    AssetMapperRepository assetMapperRepository;

    @StreamListener("inBoundAssetIssues")
    public void assetIssues(AssetIssuesModel assetIssuesModel){
        try{
            LOGGER.info("Inside asset Issues kafka listener.");
            AssetMapper assetMapper = null;
            switch (assetIssuesModel.getAction()){
                case "OPEN":
                    LOGGER.info("Adding open Issue to Asset Cooked Table.");
                    assetMapper = assetMapperRepository.findByUuid(assetIssuesModel.getAssetUUID());
                    Integer count = Integer.valueOf(assetMapper.getOpenIssues()) + 1;
                    assetMapper.setOpenIssues((count).toString());
                    assetMapperRepository.save(assetMapper);
                    break;
                case "ASSIGN":
                    break;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred after receive call in AMS.");
        }
    }
}
