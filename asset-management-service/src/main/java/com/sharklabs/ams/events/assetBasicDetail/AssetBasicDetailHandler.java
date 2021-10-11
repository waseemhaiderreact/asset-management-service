package com.sharklabs.ams.events.assetBasicDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharklabs.ams.api.AssetService;
import com.sharklabs.ams.api.KafkaAsyncService;
import com.sharklabs.ams.events.CustomChannels;
import com.sharklabs.ams.events.usage.UsageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;


@EnableBinding(CustomChannels.class)
public class AssetBasicDetailHandler {
    private static final Logger LOGGER = LogManager.getLogger(AssetBasicDetailHandler.class);

    @Autowired
    AssetService assetService;

    @StreamListener("inBoundGetAssetBasicDetail")
    public void loggerSink(String tenantUUID){
        try {
            LOGGER.info("Receiving request to get basic detail of an assets");
            assetService.fetchAssetNameAndType(tenantUUID);
        }catch(Exception e){
            LOGGER.error(" Error in Receiving request to get basic detail of an assets",e);
        }
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
