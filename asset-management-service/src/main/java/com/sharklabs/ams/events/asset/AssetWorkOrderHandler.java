package com.sharklabs.ams.events.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharklabs.ams.asset.AssetMapper;
import com.sharklabs.ams.asset.AssetMapperRepository;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;

@EnableBinding(CustomChannels.class)
public class AssetWorkOrderHandler {

    private static final Logger LOGGER = Logger.getLogger(AssetWorkOrderHandler.class);

    @Autowired
    AssetMapperRepository assetMapperRepository;

    @StreamListener("inBoundAssetWorkOrder")
    public void receiveAssetWorkOrderUpdate(AssetWorkOrder assetWorkOrder) throws IOException{
        try{
            LOGGER.info("Receiving Asset work order. Details: " + convertToJSON(assetWorkOrder));
            if(assetWorkOrder.getAssetUUID() != null && !assetWorkOrder.getAssetUUID().isEmpty()) {
                AssetMapper assetMapper = assetMapperRepository.findByUuid(assetWorkOrder.getAssetUUID());
                if(assetMapper != null) {
                    switch (assetWorkOrder.getAction()) {
                        case "ADD":
                            LOGGER.info("Adding Asset Work order info to cooked table.");
                            Integer count = Integer.valueOf(assetMapper.getWorkorders());
                            assetMapper.setWorkorders(String.valueOf(count >= 0 ? count + 1 : 0));
                            assetMapperRepository.save(assetMapper);
                            break;
                        case "UPDATE":
                            LOGGER.info("Updating Asset Work order info to cooked table.");
                            Integer openCount = Integer.valueOf(assetMapper.getWorkorders());
                            Integer repairCount = Integer.valueOf(assetMapper.getRepairs());
                            //checking previous status of work order and updating data accordingly
                            if(assetWorkOrder.getPreviousStatus().trim().equalsIgnoreCase("open")){
                                assetMapper.setWorkorders(String.valueOf(openCount >= 0 ? openCount - 1 : 0));
                            } else if(assetWorkOrder.getPreviousStatus().trim().equalsIgnoreCase("in progress") || assetWorkOrder.getUpdatedStatus().trim().equalsIgnoreCase("closed")){
                                assetMapper.setRepairs(String.valueOf(repairCount >= 0 ? repairCount - 1 : 0));
                            }
                            //checking updated status of work order and updating data accordingly
                            if(assetWorkOrder.getUpdatedStatus().trim().equalsIgnoreCase("in progress")){
                                assetMapper.setRepairs(String.valueOf(repairCount >= 0 ? repairCount + 1 : 0));
                            } else if(assetWorkOrder.getUpdatedStatus().trim().equalsIgnoreCase("open") || assetWorkOrder.getPreviousStatus().trim().equalsIgnoreCase("closed")){
                                assetMapper.setWorkorders(String.valueOf(openCount >= 0 ? openCount + 1 : 0));
                            }
                            assetMapperRepository.save(assetMapper);
                            break;
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while receiving Asset work order.Details: " + convertToJSON(assetWorkOrder));
        }
    }

    private String convertToJSON(Object obj) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
