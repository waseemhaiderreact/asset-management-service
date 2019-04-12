package com.sharklabs.ams.events.consumption;

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
public class ConsumptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(ConsumptionHandler.class);

    @Autowired
    AssetService assetService;

    @StreamListener("inBoundConsumptionCreate")
    public void loggerSink(ConsumptionModel consumptionModel){
        try {
            LOGGER.info("Receiving consumption units of an asset. Asset UUID: " + consumptionModel.getConsumption().getAssetUUID());
            LOGGER.debug("Consumption Model Object: " + convertTOJOSN(consumptionModel));
            switch (consumptionModel.getAction()) {
                case "CREATE":
                    assetService.updateConsumptionUnits(consumptionModel.getConsumption());
                    break;

            }
        }catch(Exception e){
            LOGGER.error("Error while receiving consumption units of asset. Asset UUID: "+consumptionModel.getConsumption().getAssetUUID(),e);
        }
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
