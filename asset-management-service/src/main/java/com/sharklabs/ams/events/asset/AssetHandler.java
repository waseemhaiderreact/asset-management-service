package com.sharklabs.ams.events.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharklabs.ams.api.KafkaAsyncService;
import com.sharklabs.ams.asset.AssetRepository;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@EnableBinding(CustomChannels.class)
public class AssetHandler {
    private static final Logger LOGGER = LogManager.getLogger(AssetHandler.class);

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    KafkaAsyncService kafkaAsyncService;

    @StreamListener("inBoundAssetSend")
    public void loggerSink(AssetModel assets){
        try {
            LOGGER.info("Receiving Asset UUIDs: " +convertTOJOSN(assets) );
            LOGGER.debug("Asset Model Object: " + convertTOJOSN(assets));
            switch (assets.getAction()) {
                case "SEND":
                    Map<String,String> newAssets = new HashMap<String,String>();
                    assets.getAssetUuid().forEach((uuid,name) -> {
                        newAssets.put(uuid,assetRepository.findAssetByUuid(uuid).getName());


                    });
                    assets.setAssetUuid(newAssets);
                    kafkaAsyncService.sendAsset(assets);
                    break;

            }
        }catch(Exception e){
            LOGGER.error("Error while recieving Asset UUIDs ",e);
        }
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
