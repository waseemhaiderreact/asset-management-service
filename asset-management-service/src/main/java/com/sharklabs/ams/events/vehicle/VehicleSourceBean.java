package com.sharklabs.ams.events.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class VehicleSourceBean {
    //private Source source;
    private MessageChannel output;

    @Autowired
    public VehicleSourceBean(@Qualifier("outBoundVehicleCreate") MessageChannel output) {
        this.output = output;
    }

    public void sendVehicle(VehicleModel  vehicleModel){
        System.out.println("Sending vehicle to search service");
        this.output.send(MessageBuilder.withPayload(vehicleModel).build());
    }
}
