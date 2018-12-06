package com.sharklabs.ams.events;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomChannels {
    @Output("outBoundVehicleCreate")
    MessageChannel outputVehicleCreate();

    @Output("outBoundInspectionReportCreate")
    MessageChannel outputInspectionReportCreate();

}
