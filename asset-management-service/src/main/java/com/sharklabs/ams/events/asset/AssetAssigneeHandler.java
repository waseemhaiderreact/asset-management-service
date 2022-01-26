package com.sharklabs.ams.events.asset;

import com.sharklabs.ams.asset.AssetMapper;
import com.sharklabs.ams.asset.AssetMapperRepository;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannels.class)
public class AssetAssigneeHandler {
    private static final Logger LOGGER = LogManager.getLogger(AssetAssigneeHandler.class);

    @Autowired
    AssetMapperRepository assetMapperRepository;

    @StreamListener("inBoundAssetAssignee")
    public void loggerSink(AssetAssigneeModel assigneeModel){
        try{
            LOGGER.info("Inside service function to Asset Assignee.");
            AssetMapper assetMapper = null;
            switch (assigneeModel.getAction()){
                case "ADD":
                    LOGGER.info("Adding the Asset Assignee");
                    assetMapper = assetMapperRepository.findByUuid(assigneeModel.getAssetUUID());
                    assetMapper.setAssignedTo(assetMapper.getAssignedTo().isEmpty() ? assigneeModel.getAssigneeName()
                            : assetMapper.getAssignedTo() + " " + assigneeModel.getAssigneeName());
                    assetMapperRepository.save(assetMapper);
                    break;
                case "UPDATE":
                    LOGGER.info("Updating Asset Assignee");
                    assetMapper = assetMapperRepository.findByUuid(assigneeModel.getAssetUUID());
                    if(!assetMapper.getAssignedTo().isEmpty()){
                        assetMapper.setAssignedTo(assetMapper.getAssignedTo().replaceAll(assigneeModel.getPreviousName(),"").trim());
                        assetMapper.setAssignedTo(assetMapper.getAssignedTo().isEmpty() ? assigneeModel.getAssigneeName()
                                : assetMapper.getAssignedTo() + " " + assigneeModel.getAssigneeName());
                        assetMapperRepository.save(assetMapper);
                    }
                    break;
                case "REMOVE":
                    LOGGER.info("Removing Assignee from Asset");
                    assetMapper = assetMapperRepository.findByUuid(assigneeModel.getAssetUUID());
                    if(!assetMapper.getAssignedTo().isEmpty()){
                        assetMapper.setAssignedTo(assetMapper.getAssignedTo().replaceAll(assigneeModel.getPreviousName(),"").trim());
                        assetMapperRepository.save(assetMapper);
                    }
                    break;
            }
        }catch (Exception e){
            LOGGER.error("An Error Occurred while updating Asset Assignee.");
        }
    }
}
