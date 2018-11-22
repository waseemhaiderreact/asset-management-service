package com.sharklabs.ams.api;


import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.issuesreporting.IssueReportingRepository;
import com.sharklabs.ams.vehicle.Vehicle;
import com.sharklabs.ams.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class AssetService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    IssueReportingRepository issueReportingRepository;

    //create a new vehicle
    Vehicle createVehicle(Vehicle vehicle){
        try{
            vehicleRepository.save(vehicle);
            String assetNumber="FMS-AS-";
            Long myId=1000L+vehicle.getId();
            String formatted = String.format("%06d",myId);
            vehicle.setAssetNumber(assetNumber+formatted);
            vehicle.setCreatedAt(new Date());
            vehicleRepository.save(vehicle);
            return vehicle;
        }catch(Exception e){
            return null;
        }
    }

    //get a vehicle by assetNumber
    Vehicle getVehicle(String assetNumber){
        return vehicleRepository.findByAssetNumber(assetNumber);
    }

    //update a vehicle
    Vehicle updateVehicle(Vehicle vehicle){
        try{
            vehicle.setUpdatedAt(new Date());
            vehicleRepository.save(vehicle);
            return vehicle;
        }catch(Exception e){
            return null;
        }
    }

    //delete a vehicle by assetNumber
    DefaultResponse deleteVehicle(String assetNumber){
        if(vehicleRepository.deleteByAssetNumber(assetNumber)==1) {
            return new DefaultResponse("NA","Vehicle Deleted Successfully","200");
        }
        return new DefaultResponse("NA","Error in deleting vehicle","500");
    }

    //get a vehicle by assetNumber
    Page<Vehicle> getVehicles(int offset, int limit){
        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
    }

    //get vehicle by driver number
//    public Vehicle getVehicleByDriverNumber(String driverNumber){
//        return vehicleRepository.findByDriverNumber(driverNumber);
//    }

    /*****************************Issue Reporting***************************************/
    public Vehicle saveIssue(IssueReporting issueReporting,String assetNumber){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
        vehicle.addIssueReporting(issueReporting);
        issueReporting.setVehicle(vehicle);
        for(ImageVoice imageVoice: issueReporting.getImageVoices()){
            imageVoice.setIssue(issueReporting);
        }
        vehicleRepository.save(vehicle);
        String issueNumber="FMS-ISS-";
        Long myId=1000L+issueReporting.getId();
        String formatted = String.format("%06d",myId);
        issueReporting.setIssueNumber(issueNumber+formatted);
        vehicleRepository.save(vehicle);
        Vehicle vehicle1= vehicleRepository.findOne(vehicle.getId());
        return vehicle1;

    }

    List<IssueReporting> getIssueReporting(String assetNo){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNo);
        return  issueReportingRepository.findByVehicle(vehicle);

    }
}

