package com.sharklabs.ams.events.assetBasicDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AssetBasicDetailSourceBean {
    private static final Logger LOGGER = Logger.getLogger(AssetBasicDetailSourceBean.class);
    private MessageChannel output;

    @Autowired
    public AssetBasicDetailSourceBean(@Qualifier("outBoundGetAssetBasicDetail") MessageChannel output){this.output = output;}

    public void sendBasicAssetDetail(AssetBasicDetailModelResponse assetDetail) throws IOException {
        LOGGER.info("Sending asset basic detail");
        this.output.send(MessageBuilder.withPayload(assetDetail).build());
    }

    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
