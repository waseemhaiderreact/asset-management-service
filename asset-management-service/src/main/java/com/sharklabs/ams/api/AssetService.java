package com.sharklabs.ams.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
//import com.sharklabs.ams.configuraion.AmazonClient;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.inspectionreport.InspectionReportRepository;
import com.sharklabs.ams.inspectionreportfield.InspectionReportField;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplateRepository;
import com.sharklabs.ams.inspectionreporttemplatefield.InspectionReportTemplateField;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.issuesreporting.IssueReportingRepository;
import com.sharklabs.ams.serviceentry.ServiceEntry;
import com.sharklabs.ams.serviceentry.ServiceEntryRepository;
import com.sharklabs.ams.servicetask.ServiceTask;
import com.sharklabs.ams.servicetask.ServiceTaskRepository;
import com.sharklabs.ams.vehicle.Vehicle;
import com.sharklabs.ams.vehicle.VehicleRepository;
import com.sharklabs.ams.workorder.WorkOrder;
import com.sharklabs.ams.workorder.WorkOrderRepository;
import com.sharklabs.ams.workorderlineitems.WorkOrderLineItems;
import com.sharklabs.ams.workorderlineitems.WorkOrderLineItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Date;

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
    @Autowired
    ServiceEntryRepository serviceEntryRepository;
    @Autowired
    WorkOrderRepository workOrderRepository;
    @Autowired
    WorkOrderLineItemsRepository workOrderLineItemsRepository;
    private AmazonS3 s3client;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.bucketName}")
    private String bucket;
//    @Autowired
//    private AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    //create a new vehicle
    Vehicle createVehicle(Vehicle vehicle){
        try{
            vehicleRepository.save(vehicle);
            vehicle.setType("Truck");
            vehicle.setStatus("Active");
            String assetNumber="FMS-AS-";
            Long myId=1000L+vehicle.getId();
            String formatted = String.format("%06d",myId);
            vehicle.setAssetNumber(assetNumber+formatted);
            vehicle.setCreatedAt(new Date());
            vehicle.setAssigned(false);
            vehicleRepository.save(vehicle);

            //send vehicle to search-service
            kafkaAsyncService.sendVehicle(vehicle,"CREATE");

            return vehicle;
        }catch(Exception e){
            e.printStackTrace();
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

    //get Paginated list of vehicles
    Page<Vehicle> getVehicles(int offset, int limit){
        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
    }

    //get All Vehicles
    Iterable<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }

    //get list of vehicles given their asset numbers
    List<Vehicle> getVehiclesGivenAssetNumbers(List<String> assetNumbers){
        List<Vehicle> vehicles=new ArrayList<>();
        for(String assetNumber: assetNumbers){
            vehicles.add(vehicleRepository.findByAssetNumber(assetNumber));
        }
        return vehicles;
    }

    //set assigned attribute of vehicle
    public void setAssignedAttribute(String assetNumber,boolean assigned){
        Vehicle vehicle=null;
        vehicle=vehicleRepository.findByAssetNumber(assetNumber);
        if(vehicle!=null){
            vehicle.setAssigned(assigned);
            vehicleRepository.save(vehicle);
        }
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
        Date currentTime=new Date();
        inspectionReport.setCreatedAt(currentTime);
        inspectionReport.setVehicle(vehicle);
        if(inspectionReport.getIssueReporting()!=null){
            inspectionReport.getIssueReporting().setInspectionReport(inspectionReport);
            inspectionReport.getIssueReporting().setVehicle(vehicle);
            inspectionReport.getIssueReporting().setReportedAt(currentTime);
            for(ImageVoice imageVoice: inspectionReport.getIssueReporting().getImageVoices()){
                imageVoice.setIssue(inspectionReport.getIssueReporting());
            }
        }
        for(InspectionReportField inspectionReportField: inspectionReport.getInspectionReportFields()) {
            inspectionReportField.setInspectionReport(inspectionReport);
        }
        inspectionReportRepository.save(inspectionReport);

        //assigning issue number to a issue
        if (inspectionReport.getIssueReporting() != null) {
            String issueNumber="FMS-ISS-";
            Long myId=1000L+inspectionReport.getIssueReporting().getId();
            String formatted = String.format("%06d",myId);
            inspectionReport.getIssueReporting().setIssueNumber(issueNumber+formatted);
            issueReportingRepository.save(inspectionReport.getIssueReporting());
        }

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

    /************Get Paginated inspection report templates*************/
    Page<InspectionReportTemplate> getAllInspectionReportTemplates(int page,int size){
        return inspectionReportTemplateRepository.findByIdNotNull(new PageRequest(page,size));
    }
    /************************END of Inspection report template functions*********************/

    /************************* Issue Functions **********************************/
    /***********************Get issues of a vehicle******************************/
    Iterable<IssueReporting> getIssuesOfVehicle(String assetNumber){
        return issueReportingRepository.findAllByVehicle_AssetNumber(assetNumber);
    }

    /************************ Get Paginated Issues******************************/
    Page<IssueReporting> getPaginatedIssues(int page, int size){
        return issueReportingRepository.findByIdNotNull(new PageRequest(page,size));
    }

    /************************* Get All Issues **********************************/
    Iterable<IssueReporting> getAllIssues(){
        return issueReportingRepository.findAll();
    }

    /************************* END Issue Functions **********************************/

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

    /**************************** Service Entry Functions*********************************/
    //add service Entry
    ServiceEntry addServiceEntry(ServiceEntry serviceEntry){
        serviceEntry.setCreatedAt(new Date());
        Vehicle vehicle=vehicleRepository.findByAssetNumber(serviceEntry.getVehicle().getAssetNumber());
        serviceEntry.setVehicle(vehicle);
        if(serviceEntry.getMeterEntry()!=null){
            serviceEntry.getMeterEntry().setServiceEntry(serviceEntry);
            serviceEntry.getMeterEntry().setVehicle(vehicle);
        }
        serviceEntryRepository.save(serviceEntry);
        for(ServiceTask serviceTask:serviceEntry.getServiceTasks()){
            serviceTask.addServiceEntry(serviceEntry);
            serviceTaskRepository.save(serviceTask);
        }
        for(IssueReporting issueReporting:serviceEntry.getIssueReportings()) {
            IssueReporting issue=issueReportingRepository.findByIssueNumber(issueReporting.getIssueNumber());
            issue.setStatus("Resolved");
            issue.setServiceEntry(serviceEntry);
            issueReportingRepository.save(issue);
        }
        return serviceEntryRepository.findOne(serviceEntry.getId());
    }

    //update service Entry
    ServiceEntry updateServiceEntry(ServiceEntry serviceEntry){
        serviceEntry.setUpdatedAt(new Date());
        if(serviceEntry.getMeterEntry()!=null){
            serviceEntry.getMeterEntry().setServiceEntry(serviceEntry);
        }
        serviceEntryRepository.save(serviceEntry);
        return serviceEntryRepository.findOne(serviceEntry.getId());
    }

    //delete service Entry by id
    DefaultResponse deleteServiceEntry(Long id){
        try {
            serviceEntryRepository.delete(id);
            return new DefaultResponse("","Service Entry deleted Successfully","200");
        }catch(Exception e){
            return new DefaultResponse("","Error in deleting Service Entry","500");
        }
    }

    //get service Entry by id
    ServiceEntry getServiceEntry(Long id){
        return serviceEntryRepository.findOne(id);
    }

    //get list of service Entries
    Page<ServiceEntry> getServiceEntries(int page,int size){
        return serviceEntryRepository.findByIdNotNull(new PageRequest(page,size));
    }

    /****************************END Service Task Functions******************************/


    /**************************** Work Order Functions*********************************/
    //add work order
    WorkOrder addWorkOrder(WorkOrder workOrder){
        workOrder.setCreatedAt(new Date());
        Vehicle vehicle=vehicleRepository.findByAssetNumber(workOrder.getVehicle().getAssetNumber());
        workOrder.setVehicle(vehicle);
        vehicle.addWorkOrder(workOrder);
        for(WorkOrderLineItems workOrderLineItem: workOrder.getWorkOrderLineItems()){
            workOrderLineItem.setWorkOrder(workOrder);
            if(workOrderLineItem.getIssueReporting()!=null){
                workOrderLineItem.getIssueReporting().setWorkOrderLineItems(workOrderLineItem);
            }
            if(workOrderLineItem.getServiceTask()!=null){
                workOrderLineItem.getServiceTask().setWorkOrderLineItems(workOrderLineItem);
            }
        }
        workOrderRepository.save(workOrder);
        String workOrderNumber="FMS-WO-";
        Long myId=1000L+workOrder.getId();
        String formatted = String.format("%06d",myId);
        workOrder.setWorkOrderNumber(workOrderNumber+formatted);
        workOrderRepository.save(workOrder);
        return workOrderRepository.findOne(workOrder.getId());
    }

    //update work order
    WorkOrder updateWorkOrder(WorkOrder workOrder){
        workOrder.setUpdatedAt(new Date());
        Vehicle vehicle=vehicleRepository.findByAssetNumber(workOrder.getVehicle().getAssetNumber());
        workOrder.setVehicle(vehicle);
        workOrderRepository.save(workOrder);
        return workOrderRepository.findOne(workOrder.getId());
    }

    //delete work order by id
    DefaultResponse deleteWorkOrder(Long id){
        try {
            workOrderRepository.delete(id);
            return new DefaultResponse("","Work Order deleted Successfully","200");
        }catch(Exception e){
            return new DefaultResponse("","Error in deleting Work Order","500");
        }
    }

    //get work order by id
    WorkOrder getWorkOrder(Long id){
        return workOrderRepository.findOne(id);
    }

    //get list of work orders
    Page<WorkOrder> getWorkOrders(int page,int size){
        return workOrderRepository.findByIdNotNull(new PageRequest(page,size));
    }

    /****************************END Work Order Functions******************************/


    /***************************Get file from s3*******************************/
    ResponseEntity<byte[]> getFileFroms3(String url) throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
        String[] parts=url.split("/");
        String key=parts[parts.length-1];
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
        S3Object s3Object = this.s3client.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
//        File initialFile = new File("C:\\Users\\user\\Desktop\\sample_audio2.mp3");
//        InputStream targetStream = new FileInputStream(initialFile);
//        byte[] bytes=IOUtils.toByteArray(targetStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", "sample_audio.mp3");

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

}

