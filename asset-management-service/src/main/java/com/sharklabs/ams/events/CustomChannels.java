package com.sharklabs.ams.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {
    @Output("outBoundVehicleCreate")
    MessageChannel outputVehicleCreate();

    @Output("outBoundInspectionReportCreate")
    MessageChannel outputInspectionReportCreate();

    @Input("inBoundAssetNumber")
    SubscribableChannel receiveAssetNumber();
}
