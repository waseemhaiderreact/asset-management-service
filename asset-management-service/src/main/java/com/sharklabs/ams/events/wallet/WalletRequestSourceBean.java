package com.sharklabs.ams.events.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/* Written By Kumail Khan*/
@Component
public class WalletRequestSourceBean {
    private static final Logger LOGGER = Logger.getLogger(WalletRequestSourceBean.class);
    private MessageChannel output;
    @Autowired
    public WalletRequestSourceBean(@Qualifier("outBoundWalletRequest")MessageChannel output){ this.output=output; }
    public void walletRequest(WalletRequestModel walletRequestModel){
        try {
            LOGGER.info("Adding in Wallet Request Source Bean to Asset Management service"+convertTOJOSN(walletRequestModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.output.send(MessageBuilder.withPayload(walletRequestModel).build());
    }
    public String convertTOJOSN (Object obj) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }
}
