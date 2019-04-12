package com.sharklabs.ams.events.usage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharklabs.ams.api.AssetService;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;


@EnableBinding(CustomChannels.class)
public class UsageHandler {
    private static final Logger LOGGER = LogManager.getLogger(UsageHandler.class);

    @Autowired
    AssetService assetService;

    @StreamListener("inBoundUsageCreate")
    public void loggerSink(UsageModel usageModel){
        try {
            LOGGER.info("Receiving usage units of an asset. Asset UUID: " + usageModel.getUsage().getAssetUUID());
            LOGGER.debug("Usage Model Object: " + convertTOJOSN(usageModel));
            switch (usageModel.getAction()) {
                case "CREATE":
                    assetService.updateUsageUnits(usageModel.getUsage());
                    break;

            }
        }catch(Exception e){
            LOGGER.error("Error while receiving usage units of asset. Asset UUID: "+ usageModel.getUsage().getAssetUUID(),e);
        }
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
