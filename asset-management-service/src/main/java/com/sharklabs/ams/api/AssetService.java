package com.sharklabs.ams.api;


import com.sharklabs.ams.AssignmentRecord.AssignmentRecord;
import com.sharklabs.ams.AssignmentRecord.AssignmentRecordRepository;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.issuesreporting.IssueReportingRepository;
import com.sharklabs.ams.vehicle.Vehicle;
import com.sharklabs.ams.vehicle.VehicleRepository;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AssetService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private AssignmentRecordRepository assignmentRecordRepository;
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
    Integer deleteVehicle(String assetNumber){
        return vehicleRepository.deleteByAssetNumber(assetNumber);
    }

    //get a vehicle by assetNumber
    Page<Vehicle> getVehicles(int offset, int limit){
        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
    }

    //assign vehicle a driver
    public Vehicle assignDriver(AssignmentRecord assignmentRecord){
        try{
            Vehicle vehicle =vehicleRepository.findByAssetNumber(assignmentRecord.getAssetNumber());
            if(vehicle.getDriverNumber()!=null){
                AssignmentRecord assignmentRecord1=null;
                assignmentRecord1=assignmentRecordRepository.findByAssetNumberAndDriverNumber(assignmentRecord.getAssetNumber(),assignmentRecord.getDriverNumber());
                if(assignmentRecord1==null){
                    assignmentRecord1=new AssignmentRecord();
                    assignmentRecord1.setAssetNumber(vehicle.getAssetNumber());
                    assignmentRecord1.setDriverNumber(vehicle.getDriverNumber());
                    assignmentRecordRepository.save(assignmentRecord1);
                }
            }
            vehicle.setDriverNumber(assignmentRecord.getDriverNumber());
            vehicleRepository.save(vehicle);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*****************************Issue Reporting***************************************/
    public IssueReporting saveIssue(IssueReporting issueReporting){

        issueReportingRepository.save(issueReporting);
        Vehicle vehicle = vehicleRepository.findByAssetNumber(issueReporting.getVehicle().getAssetNumber());
        vehicle.addIssueReporting(issueReporting);
        vehicleRepository.save(vehicle);
        IssueReporting issue = issueReportingRepository.findOne(issueReporting.getId());
        return issue;

    }

    List<IssueReporting> getIssueReporting(String assetNo){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNo);
        return  issueReportingRepository.findByVehicle(vehicle);

    }
}

