package com.sharklabs.ams.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {
    @Input("inBoundUsageCreate")
    SubscribableChannel receiveUsageUnits();

    @Input("inBoundAssetSend")
    SubscribableChannel recieveAsset();

    @Output("outBoundAssetSend")
    MessageChannel sendAsset();
}
