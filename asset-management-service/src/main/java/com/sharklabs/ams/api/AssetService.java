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
import com.sharklabs.ams.servicetask.ServiceTask;
import com.sharklabs.ams.servicetask.ServiceTaskRepository;
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
    @Autowired
    KafkaAsyncService kafkaAsyncService;
    @Autowired
    ServiceTaskRepository serviceTaskRepository;

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

            //send vehicle to search-service
            kafkaAsyncService.sendVehicle(vehicle,"CREATE");

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

            //send updated vehicle to search-service
            kafkaAsyncService.sendVehicle(vehicle,"UPDATE");

            return vehicle;
        }catch(Exception e){
            return null;
        }
    }

    //delete a vehicle by assetNumber
    DefaultResponse deleteVehicle(String assetNumber){
        Vehicle vehicle=vehicleRepository.findByAssetNumber(assetNumber);
        if(vehicleRepository.deleteByAssetNumber(assetNumber)==1) {
            //send vehicle to search-service to delete
            kafkaAsyncService.sendVehicle(vehicle,"DELETE");
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

    //re-index vehicles
    DefaultResponse reIndexVehicles(){
        try{
            List<Vehicle> vehicles=vehicleRepository.findAll();
            for(Vehicle vehicle: vehicles){
                kafkaAsyncService.sendVehicle(vehicle,"CREATE");
            }
            return new DefaultResponse("NA","Vehicles sent to search service","200");
        }catch(Exception e){
            return new DefaultResponse("NA","Error in getting vehicles or sending vehicles to search service","500");
        }
    }

    //get vehicle by driver number
//    public Vehicle getVehicleByDriverNumber(String driverNumber){
//        return vehicleRepository.findByDriverNumber(driverNumber);
//    }

    /*****************************Inspection Report***************************************/

    /************Save Inspection Report*************/
    public Vehicle saveInspectionReport(InspectionReport inspectionReport, String assetNumber){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
//        vehicle.addInspectionReport(inspectionReport);
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
        inspectionReportRepository.save(inspectionReport);
//        vehicleRepository.save(vehicle);
//        vehicle=vehicleRepository.findOne(vehicle.getId());
//        InspectionReport lastElement=null;
//        Iterator<InspectionReport> iterator=vehicle.getInspectionReports().iterator();
//        while(iterator.hasNext()){
//            lastElement=iterator.next();
//        }
//        inspectionReport=lastElement;
        //assigning issue number to a issue
        for(InspectionReportField inspectionReportField: inspectionReport.getInspectionReportFields()){
            if (inspectionReportField.getIssueReporting() != null) {
                String issueNumber="FMS-ISS-";
                Long myId=1000L+inspectionReportField.getIssueReporting().getId();
                String formatted = String.format("%06d",myId);
                inspectionReportField.getIssueReporting().setIssueNumber(issueNumber+formatted);
                issueReportingRepository.save(inspectionReportField.getIssueReporting());
            }
        }
//        vehicle=vehicleRepository.findOne(vehicle.getId());
//        lastElement=null;
//        iterator=vehicle.getInspectionReports().iterator();
//        while(iterator.hasNext()){
//            lastElement=iterator.next();
//        }
//        inspectionReport=lastElement;

        //send inspection report to search service to save issues
        kafkaAsyncService.sendInspectionReport(inspectionReport,"CREATE");

        Vehicle vehicle1= vehicleRepository.findOne(vehicle.getId());
        return vehicle1;

    }

    /************Get Inspection Reports by Asset Number*************/
    Iterable<InspectionReport> getInspectionReports(String assetNo){

        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNo);
        return inspectionReportRepository.findByVehicle(vehicle);

    }

    /************Get All Inspection Reports*************/
    Iterable<InspectionReport> getAllInspectionReports(){
        return inspectionReportRepository.findAll();
    }

    //re-index inspection reports
    DefaultResponse reIndexInspections(){
        try{
            List<InspectionReport> inspectionReports=inspectionReportRepository.findAll();
            for(InspectionReport inspectionReport:inspectionReports){
                kafkaAsyncService.sendInspectionReport(inspectionReport,"CREATE");
            }
            return new DefaultResponse("NA","Inspection reports sent to search service","200");
        }catch(Exception e){
            return new DefaultResponse("NA","Error in getting inspection reports or sending inspection reports to search service","500");
        }
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

    /************************END of Inspection report template functions*********************/

    /**************************** Service Task Functions*********************************/
    //add service task
    ServiceTask addServiceTask(ServiceTask serviceTask){
        serviceTask.setCreatedAt(new Date());
        //it means that service request has subtasks
        if(serviceTask.getSubTasks().size()!=0){
            for(ServiceTask serviceTask1: serviceTask.getSubTasks()){
                serviceTask1.addTask(serviceTask);
            }
        }
        else{
            serviceTask.setSubTasks(null);
        }
        if(serviceTask.getTasks().size()==0){
            serviceTask.setTasks(null);
        }
        serviceTaskRepository.save(serviceTask);
        return serviceTaskRepository.findOne(serviceTask.getId());
    }

    //update service task
    ServiceTask updateServiceTask(ServiceTask serviceTask){
        serviceTask.setUpdatedAt(new Date());
        //it means that service request has subtasks
        if(serviceTask.getSubTasks()!=null){
            for(ServiceTask serviceTask1: serviceTask.getSubTasks()){
                serviceTask1.addTask(serviceTask);
            }
        }
        serviceTaskRepository.save(serviceTask);
        return serviceTaskRepository.findOne(serviceTask.getId());
    }

    //delete service task by id
    DefaultResponse deleteServiceTask(Long id){
        try {
            serviceTaskRepository.delete(id);
            return new DefaultResponse("","Service Task deleted Successfully","200");
        }catch(Exception e){
            return new DefaultResponse("","Error in deleting Service Task","500");
        }
    }

    //get service task by id
    ServiceTask getServiceTask(Long id){
        return serviceTaskRepository.findOne(id);
    }

    //get list of service tasks
    Page<ServiceTask> getServiceTasks(int page,int size){
        return serviceTaskRepository.findByIdNotNull(new PageRequest(page,size));
    }

    /****************************END Service Task Functions******************************/
}

