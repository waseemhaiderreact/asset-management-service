package com.sharklabs.ams.events.userOrganization;

import com.sharklabs.ams.api.AssetService;
import com.sharklabs.ams.events.CustomChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


@EnableBinding(CustomChannels.class)
public class AssignmentRecordHandler {

    @Autowired
    AssetService assetService;

    @StreamListener("inBoundAssignmentRecordCreate")
    public void loggerSink(AssignmentRecordModel assignmentRecordModel){
        System.out.println("Recieving Assignment Record model");
        System.out.println(assignmentRecordModel.getAction());
        switch(assignmentRecordModel.getAction()) {
            case "CREATE":
                try {
                    assetService.assignDriver(assignmentRecordModel.getAssignmentRecord());
                }catch (Exception e){
                    e.printStackTrace();;
                }
                break;

            case "UPDATE":
                break;
        }
    }

}
