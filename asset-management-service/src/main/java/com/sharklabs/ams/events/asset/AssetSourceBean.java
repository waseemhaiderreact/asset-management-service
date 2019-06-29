package com.sharklabs.ams.events.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AssetSourceBean {
    private static final Logger LOGGER = Logger.getLogger(AssetSourceBean.class);
    private MessageChannel output;

    @Autowired
    public AssetSourceBean(@Qualifier("outBoundAssetSend") MessageChannel output){this.output = output;}

    public void sendAssetUuid(AssetModel assetNames) throws IOException {
        System.out.println("Sending asset names to workorder service for asset names");
        LOGGER.debug("Sending asset names to workorder service for asset names. Message Object: "+convertTOJOSN(assetNames));
        this.output.send(MessageBuilder.withPayload(assetNames).build());
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
