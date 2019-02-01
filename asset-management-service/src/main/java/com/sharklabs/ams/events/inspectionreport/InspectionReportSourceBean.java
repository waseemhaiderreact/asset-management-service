package com.sharklabs.ams.events.inspectionreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class InspectionReportSourceBean {
    //private Source source;
    private MessageChannel output;

    @Autowired
    public InspectionReportSourceBean(@Qualifier("outBoundInspectionReportCreate") MessageChannel output) {
        this.output = output;
    }

//    public void sendInspectionReport(InspectionReportModel  inspectionReportModel){
//        System.out.println("Sending inspection report to search service");
//        this.output.send(MessageBuilder.withPayload(inspectionReportModel).build());
//    }
}
