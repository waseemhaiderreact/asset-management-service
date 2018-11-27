package com.sharklabs.ams.api;


import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.inspectionreport.InspectionReportRepository;
import com.sharklabs.ams.inspectionreportfield.InspectionReportField;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplateRepository;
import com.sharklabs.ams.inspectionreporttemplatefield.InspectionReportTemplateField;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.issuesreporting.IssueReportingRepository;
import com.sharklabs.ams.vehicle.Vehicle;
import com.sharklabs.ams.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class AssetService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    IssueReportingRepository issueReportingRepository;
    @Autowired
    InspectionReportRepository inspectionReportRepository;
    @Autowired
    InspectionReportTemplateRepository inspectionReportTemplateRepository;

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

    //get list of vehicles
    Page<Vehicle> getVehicles(int offset, int limit){
        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
    }

    //get list of vehicles given their asset numbers
    List<Vehicle> getVehiclesGivenAssetNumbers(List<String> assetNumbers){
        List<Vehicle> vehicles=new ArrayList<>();
        for(String assetNumber: assetNumbers){
            vehicles.add(vehicleRepository.findByAssetNumber(assetNumber));
        }
        return vehicles;
    }

    //get vehicle by driver number
//    public Vehicle getVehicleByDriverNumber(String driverNumber){
//        return vehicleRepository.findByDriverNumber(driverNumber);
//    }

    /*****************************Inspection Report***************************************/

    /************Save Inspection Report*************/
    public Vehicle saveInspectionReport(InspectionReport inspectionReport, String assetNumber){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
        vehicle.addInspectionReport(inspectionReport);
        inspectionReport.setCreatedAt(new Date());
        inspectionReport.setVehicle(vehicle);
        for(InspectionReportField inspectionReportField: inspectionReport.getInspectionReportFields()) {
            inspectionReportField.setInspectionReport(inspectionReport);
            if (inspectionReportField.getIssueReporting() != null) {
                inspectionReportField.getIssueReporting().setInspectionReportField(inspectionReportField);
                for(ImageVoice imageVoice: inspectionReportField.getIssueReporting().getImageVoices()){
                    imageVoice.setIssue(inspectionReportField.getIssueReporting());
                }
            }
        }
        vehicleRepository.save(vehicle);
        vehicle=vehicleRepository.findOne(vehicle.getId());
        InspectionReport lastElement=null;
        Iterator<InspectionReport> iterator=vehicle.getInspectionReports().iterator();
        while(iterator.hasNext()){
            lastElement=iterator.next();
        }
        inspectionReport=lastElement;
        for(InspectionReportField inspectionReportField: inspectionReport.getInspectionReportFields()){
            if (inspectionReportField.getIssueReporting() != null) {
                String issueNumber="FMS-ISS-";
                Long myId=1000L+inspectionReportField.getIssueReporting().getId();
                String formatted = String.format("%06d",myId);
                inspectionReportField.getIssueReporting().setIssueNumber(issueNumber+formatted);
                issueReportingRepository.save(inspectionReportField.getIssueReporting());
            }
        }

        Vehicle vehicle1= vehicleRepository.findOne(vehicle.getId());
        return vehicle1;

    }

    /************Get Inspection Reports by Asset Number*************/
    Iterable<InspectionReport> getInspectionReports(String assetNo){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNo);
        return inspectionReportRepository.findByVehicle(vehicle);

    }

    /***********************END of Inspection report functions*********************/

    /*******************************************Inspection Report Template Functions********************************/
    /**********Save inspection report template**************/
    Vehicle saveInspectionReportTemplate(InspectionReportTemplate inspectionReportTemplate,String assetNumber){
        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
        vehicle.addInspectionReportTemplate(inspectionReportTemplate);
        inspectionReportTemplate.setCreatedAt(new Date());
        inspectionReportTemplate.setVehicle(vehicle);
        for(InspectionReportTemplateField inspectionReportTemplateField: inspectionReportTemplate.getInspectionReportTemplateFields()) {
            inspectionReportTemplateField.setInspectionReportTemplate(inspectionReportTemplate);
        }
        vehicleRepository.save(vehicle);
        vehicle=vehicleRepository.findOne(vehicle.getId());
        return vehicle;
    }

    /************Get inspection report templates of a vehicle*************/
    Iterable<InspectionReportTemplate> getInspectionReportTemplates(String assetNumber){
        Vehicle vehicle=vehicleRepository.findByAssetNumber(assetNumber);
        return inspectionReportTemplateRepository.findByVehicle(vehicle);
    }
}

