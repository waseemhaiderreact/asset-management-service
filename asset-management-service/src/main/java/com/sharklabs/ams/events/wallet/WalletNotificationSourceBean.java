package com.sharklabs.ams.events.wallet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/* Written By Kumail Khan*/
@Component
public class WalletNotificationSourceBean {
    private static final Logger LOGGER = Logger.getLogger(WalletNotificationSourceBean.class);
    private MessageChannel output;
    @Autowired
    public WalletNotificationSourceBean(@Qualifier("outBoundWalletNotification")MessageChannel output){ this.output=output; }
    public void walletNotification(WalletNotificationModel walletNotificationModel){
        LOGGER.info("Adding in Wallet Notification Source Bean to Asset Management service");
        this.output.send(MessageBuilder.withPayload(walletNotificationModel).build());
    }
}