package com.sharklabs.ams.api;

//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
////import com.sharklabs.ams.configuraion.AmazonClient;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import org.apache.commons.io.IOUtils;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetRepository;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.category.CategoryRepository;
import com.sharklabs.ams.field.Field;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import com.sharklabs.ams.fieldtemplate.FieldTemplateRepository;
import com.sharklabs.ams.inspectionitem.InspectionItem;
import com.sharklabs.ams.inspectionitemcategory.InspectionItemCategory;
import com.sharklabs.ams.message.Message;
import com.sharklabs.ams.request.*;
import com.sharklabs.ams.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssetService {
    private static final Logger LOGGER = LogManager.getLogger(AssetService.class);

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    FieldTemplateRepository fieldTemplateRepository;
    @Autowired
    AssetRepository assetRepository;

    /********************************************Category Functions**********************************************/
    //add category (AMS_UC_01)
    public DefaultResponse addCategory(AddCategoryRequest addCategoryRequest){
        try {
            LOGGER.debug("Inside add category service function");
            addCategoryRequest.getCategory().setUuid(UUID.randomUUID().toString());
            //if children exists
            if(addCategoryRequest.getCategory().getFieldTemplate()!=null) {
                //if children is not already saved in db
                if (addCategoryRequest.getCategory().getFieldTemplate().getId() == null) {
                    LOGGER.debug("Category has a field template");
                    addCategoryRequest.getCategory().getFieldTemplate().setUuid(UUID.randomUUID().toString());
                    addCategoryRequest.getCategory().getFieldTemplate().setCategory(addCategoryRequest.getCategory());
                    for (Field field : addCategoryRequest.getCategory().getFieldTemplate().getFields()) {
                        field.setUuid(UUID.randomUUID().toString());
                        field.setFieldTemplate(addCategoryRequest.getCategory().getFieldTemplate());
                    }
                }
            }
            //if children exists
            if(addCategoryRequest.getCategory().getInspectionTemplate()!=null) {
                //if children is not already saved in db
                if (addCategoryRequest.getCategory().getInspectionTemplate().getId() == null) {
                    LOGGER.debug("Category has an inspection template");
                    addCategoryRequest.getCategory().getInspectionTemplate().setUuid(UUID.randomUUID().toString());
                    addCategoryRequest.getCategory().getInspectionTemplate().setCategory(addCategoryRequest.getCategory());
                    for (InspectionItemCategory inspectionItemCategory : addCategoryRequest.getCategory().getInspectionTemplate().getInspectionItemCategories()) {
                        inspectionItemCategory.setUuid(UUID.randomUUID().toString());
                        inspectionItemCategory.setInspectionTemplate(addCategoryRequest.getCategory().getInspectionTemplate());
                        for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                            inspectionItem.setUuid(UUID.randomUUID().toString());
                            inspectionItem.setInspectionItemCategory(inspectionItemCategory);
                        }
                    }
                }
            }
            categoryRepository.save(addCategoryRequest.getCategory());
            LOGGER.info("Category Added Successfully");
            return new DefaultResponse("Success","Category added succssfully","200",addCategoryRequest.getCategory().getUuid());
        }
        catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while adding category" , e);
            return new DefaultResponse("Failure","Error in add category: "+e.getMessage(),"500");
        }
    }
    //delete category (AMS_UC_05)
    public DefaultResponse deleteCategory(String id){
        LOGGER.debug("Inside Service function of deleting category");
        try{
            Category category=categoryRepository.findCategoryByUuid(id);
            categoryRepository.deleteById(category.getId());
            LOGGER.info("Category deleted Successfully");
            return new DefaultResponse("Success","Category deleted Successfully","200");
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while deleting category" , e);
            return new DefaultResponse("Failure","Error in deleting category: "+e.getMessage(),"500");
        }
    }

    //get a category (AMS_UC_02)
    public GetCategoryResponse getCategory(String id){
        LOGGER.debug("Inside Service function of get category");
        GetCategoryResponse response = new GetCategoryResponse();
        try {
            response.setCategory(categoryRepository.findCategoryByUuid(id));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Category From database. Sending it to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting category from db. Category UUID: "+ id,e);
            return response;
        }
    }

    //get all categories (AMS_UC_03)
    public GetCategoriesResponse GetAllCategories(){
        LOGGER.debug("Inside Service function of get all categories");
        GetCategoriesResponse response = new GetCategoriesResponse();
        try {
            response.setCategories(categoryRepository.findAll());
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Categories From database. Sending it to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db.",e);
            return response;
        }
    }

    //edit category (AMS_UC_04)
    public EditCategoryResponse editCategory(EditCategoryRequest editCategoryRequest){
        LOGGER.debug("Inside Service function of edit category");
        EditCategoryResponse response = new EditCategoryResponse();
        try {
            Category category=categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid());
            //if id of category is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if(editCategoryRequest.getCategory().getId()==null){
                editCategoryRequest.getCategory().setId(category.getId());
                editCategoryRequest.getCategory().setFieldTemplate(category.getFieldTemplate());
                editCategoryRequest.getCategory().setAssets(category.getAssets());
                editCategoryRequest.getCategory().setInspectionTemplate(category.getInspectionTemplate());
            }
            //setting parent of children if exists
            if(editCategoryRequest.getCategory().getInspectionTemplate() !=null) {
                editCategoryRequest.getCategory().getInspectionTemplate().setCategory(editCategoryRequest.getCategory());
                for (InspectionItemCategory inspectionItemCategory : editCategoryRequest.getCategory().getInspectionTemplate().getInspectionItemCategories()) {
                    inspectionItemCategory.setInspectionTemplate(editCategoryRequest.getCategory().getInspectionTemplate());
                    for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                        inspectionItem.setInspectionItemCategory(inspectionItemCategory);
                    }
                }
            }
            if(editCategoryRequest.getCategory().getFieldTemplate()!=null) {
                editCategoryRequest.getCategory().getFieldTemplate().setCategory(editCategoryRequest.getCategory());
                for (Field field : editCategoryRequest.getCategory().getFieldTemplate().getFields()) {
                    field.setFieldTemplate(editCategoryRequest.getCategory().getFieldTemplate());
                }
            }

            categoryRepository.save(editCategoryRequest.getCategory());
            response.setCategory(categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid()));
            response.setResponseIdentifier("Success");
            LOGGER.info("Category Updated Successfully. Sending updated Category to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while updating category or getting updated category from db. ",e);
            return response;
        }
    }

    /*******************************************END Category Functions*******************************************/


    /*******************************************Field Template Functions*****************************************/
    //add field template (AMS_UC_06)
    public DefaultResponse addFieldTemplate(AddFieldTemplateRequest addFieldTemplateRequest){
        LOGGER.debug("Inside Service function of add field template");
        try {
            //get category from db
            Category category=null;
            if(addFieldTemplateRequest.getCategoryId()!=null) {
                category = categoryRepository.findCategoryByUuid(addFieldTemplateRequest.getCategoryId());
                addFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(addFieldTemplateRequest.getFieldTemplate());
            }
            else{
                LOGGER.error("category uuid is not given for field template");
                return new DefaultResponse("Failure", "Category id not present in request object","500");
            }
            //setting uuid
            addFieldTemplateRequest.getFieldTemplate().setUuid(UUID.randomUUID().toString());
            //setting parent of all children and also setting uuid
            for(Field field: addFieldTemplateRequest.getFieldTemplate().getFields()){
                field.setFieldTemplate(addFieldTemplateRequest.getFieldTemplate());
                field.setUuid(UUID.randomUUID().toString());
            }
            //saving in db
            categoryRepository.save(category);
            LOGGER.info("Field Template Added Successfully");
            return new DefaultResponse("Success","Field Template Added Successfully","200",addFieldTemplateRequest.getFieldTemplate().getUuid());
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while adding field template ",e);
            return new DefaultResponse("Failure", "Error in adding field template. Error: "+e.getMessage(),"500");
        }
    }

    //delete field template AMS_UC_09
    public DefaultResponse deleteFieldTemplate(String id){
        LOGGER.debug("Inside Service function of deleting field template");
        try{
            FieldTemplate fieldTemplate=fieldTemplateRepository.findByUuid(id);
            //setting parent of field template to null to not delete the parent alongwith the children
            fieldTemplate.setCategory(null);
            fieldTemplateRepository.save(fieldTemplate);
            //deleting
            fieldTemplateRepository.deleteById(fieldTemplate.getId());
            LOGGER.info("Field Template deleted Successfully");
            return new DefaultResponse("Success","Field Template deleted Successfully","200");
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while deleting field template" , e);
            return new DefaultResponse("Failure","Error in deleting field template: "+e.getMessage(),"500");
        }
    }

    //get field template AMS_UC_07
    public GetFieldTemplateResponse getFieldTemplate(String id){
        LOGGER.debug("Inside Service function of get field template");
        GetFieldTemplateResponse response = new GetFieldTemplateResponse();
        try {
            response.setFieldTemplate(fieldTemplateRepository.findByUuid(id));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Field Template From database. Sending it to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting field template from db. Field Template UUID: "+ id,e);
            return response;
        }
    }

    //edit field template AMS_UC_08
    public EditFieldTemplateResponse editFieldTemplate(EditFieldTemplateRequest editFieldTemplateRequest){
        LOGGER.debug("Inside Service function of edit field template");
        EditFieldTemplateResponse response=new EditFieldTemplateResponse();
        try{
            //get category from db
            Category category=null;
            if(editFieldTemplateRequest.getCategoryId()!=null) {
                category = categoryRepository.findCategoryByUuid(editFieldTemplateRequest.getCategoryId());
                editFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(editFieldTemplateRequest.getFieldTemplate());
            }
            else{
                LOGGER.error("category uuid is not given for field template");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //if id of field template is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if(editFieldTemplateRequest.getFieldTemplate().getId()==null){
                FieldTemplate fieldTemplate=fieldTemplateRepository.findByUuid(editFieldTemplateRequest.getFieldTemplate().getUuid());
                editFieldTemplateRequest.getFieldTemplate().setId(fieldTemplate.getId());
                editFieldTemplateRequest.getFieldTemplate().setFields(fieldTemplate.getFields());
            }
            //setting parent of all children and also setting uuid
            for(Field field: editFieldTemplateRequest.getFieldTemplate().getFields()){
                field.setFieldTemplate(editFieldTemplateRequest.getFieldTemplate());
            }
            //saving in db
            categoryRepository.save(category);
            response.setResponseIdentifier("Success");
            response.setFieldTemplate(category.getFieldTemplate());
            LOGGER.info("Field Template Edited Successfully");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing field template from db. Field Template UUID: "+ editFieldTemplateRequest.getFieldTemplate().getUuid(),e);
            return response;
        }
    }

    /*******************************************END Field Template Functions*************************************/


    /******************************************* Asset Functions ************************************************/
    //add asset (AMS_UC_10)
    public DefaultResponse addAsset(AddAssetRequest addAssetRequest){
        LOGGER.debug("Inside Service function of add asset");
        try {
            //get category from db
            Category category=null;
            if(addAssetRequest.getCategoryId()!=null) {
                category = categoryRepository.findCategoryByUuid(addAssetRequest.getCategoryId());
                addAssetRequest.getAsset().setCategory(category);
                category.addAsset(addAssetRequest.getAsset());
            }
            else{
                LOGGER.error("category uuid is not given for asset");
                return new DefaultResponse("Failure", "Category id not present in request object","500");
            }
            //setting uuid
            addAssetRequest.getAsset().setUuid(UUID.randomUUID().toString());
            //setting parent of children and also setting uuid
            for(AssetField assetField: addAssetRequest.getAsset().getAssetFields()){
                assetField.setAsset(addAssetRequest.getAsset());
                assetField.setUuid(UUID.randomUUID().toString());
            }
            //creating activity wall for that asset and also setting uuid
            ActivityWall activityWall=new ActivityWall();
            activityWall.setUuid(UUID.randomUUID().toString());
            activityWall.setAsset(addAssetRequest.getAsset());
            addAssetRequest.getAsset().setActivityWall(activityWall);
            //saving in db
            categoryRepository.save(category);
            LOGGER.info("Asset Added Successfully");
            return new DefaultResponse("Success","Asset Added Successfully","200",addAssetRequest.getAsset().getUuid());
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while adding asset ",e);
            return new DefaultResponse("Failure", "Error in adding asset. Error: "+e.getMessage(),"500");
        }
    }

    //edit asset AMS_UC_11
    public EditAssetResponse editAsset(EditAssetRequest editAssetRequest){
        LOGGER.debug("Inside Service function of edit asset");
        EditAssetResponse response=new EditAssetResponse();
        try{
            //get category from db
            Category category=null;
            if(editAssetRequest.getCategoryId()!=null) {
                category = categoryRepository.findCategoryByUuid(editAssetRequest.getCategoryId());
                editAssetRequest.getAsset().setCategory(category);
                //this code is for test library. This condition wont be true when request is sent by FE
                if(editAssetRequest.getAsset().getId()==null){
                    for(Asset asset: category.getAssets()){
                        if(asset.getUuid().equals(editAssetRequest.getAsset().getUuid())){
                            editAssetRequest.getAsset().setId(asset.getId());
                        }
                    }
                }
            }
            else{
                LOGGER.error("category uuid is not given for asset");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //setting parent of children
            for(AssetField assetField: editAssetRequest.getAsset().getAssetFields()){
                assetField.setAsset(editAssetRequest.getAsset());
            }
            if(editAssetRequest.getAsset().getActivityWall()!=null) {
                editAssetRequest.getAsset().getActivityWall().setAsset(editAssetRequest.getAsset());
                for (Message message : editAssetRequest.getAsset().getActivityWall().getMessages()){
                    message.setActivityWall(editAssetRequest.getAsset().getActivityWall());
                }
            }
            //saving in db
            assetRepository.save(editAssetRequest.getAsset());

            response.setResponseIdentifier("Success");
            response.setAsset(editAssetRequest.getAsset());
            LOGGER.info("Asset Edited Successfully");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing asset from db. Asset UUID: "+ editAssetRequest.getAsset().getUuid(),e);
            return response;
        }
    }

    //delete asset AMS_UC_12
    public DefaultResponse deleteAsset(String id) {
        LOGGER.debug("Inside Service function of deleting asset");
        try {
            Asset asset = assetRepository.findAssetByUuid(id);
            //setting parent of field template to null to not delete the parent alongwith the children
            asset.setCategory(null);
            assetRepository.save(asset);
            //deleting
            assetRepository.deleteById(asset.getId());
            LOGGER.info("Asset deleted Successfully");
            return new DefaultResponse("Success", "Asset deleted Successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting asset" , e);
            return new DefaultResponse("Failure", "Error in deleting asset: " + e.getMessage(), "500");

        }
    }

    //get asset AMS_UC_13
    public GetAssetResponse getAsset(String id){
        LOGGER.debug("Inside Service function of get asset");
        GetAssetResponse response=new GetAssetResponse();
        try{
            response.setAsset(assetRepository.findAssetByUuid(id));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Asset From database. Sending it to controller");
            return response;
        }catch(Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting asset from db. Asset UUID: "+ id,e);
            return response;
        }
    }

    //get assets AMS_UC_14
    public GetAssetsResponse getAssets(){
        LOGGER.debug("Inside Service function of get assets");
        GetAssetsResponse response=new GetAssetsResponse();
        try {
            response.setAssets(assetRepository.findAll());
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Categories From database. Sending it to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db.",e);
            return response;
        }

    }

    /******************************************* END Asset Functions ************************************************/
//    @Autowired
//    private VehicleRepository vehicleRepository;
//    @Autowired
//    IssueReportingRepository issueReportingRepository;
//    @Autowired
//    InspectionReportRepository inspectionReportRepository;
//    @Autowired
//    InspectionReportTemplateRepository inspectionReportTemplateRepository;
//    @Autowired
//    KafkaAsyncService kafkaAsyncService;
//    @Autowired
//    ServiceTaskRepository serviceTaskRepository;
//    @Autowired
//    ServiceEntryRepository serviceEntryRepository;
//    @Autowired
//    WorkOrderRepository workOrderRepository;
//    @Autowired
//    WorkOrderLineItemsRepository workOrderLineItemsRepository;
//    private AmazonS3 s3client;
//    @Value("${cloud.aws.credentials.accessKey}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    private String secretKey;
//
//    @Value("${cloud.aws.region}")
//    private String region;
//
//    @Value("${cloud.aws.bucketName}")
//    private String bucket;
////    @Autowired
////    private AmazonS3Client amazonS3Client;
////
////    @Value("${cloud.aws.s3.bucket}")
////    private String bucket;
//
//    //create a new vehicle
//    Vehicle createVehicle(Vehicle vehicle){
//        try{
//            vehicleRepository.save(vehicle);
//            vehicle.setType("Truck");
//            vehicle.setStatus("Active");
//            String assetNumber="FMS-AS-";
//            Long myId=1000L+vehicle.getId();
//            String formatted = String.format("%06d",myId);
//            vehicle.setAssetNumber(assetNumber+formatted);
//            vehicle.setCreatedAt(new Date());
//            vehicle.setAssigned(false);
//            vehicleRepository.save(vehicle);
//
//            //send vehicle to search-service
//            kafkaAsyncService.sendVehicle(vehicle,"CREATE");
//
//            return vehicle;
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    //get a vehicle by assetNumber
//    Vehicle getVehicle(String assetNumber){
//        return vehicleRepository.findByAssetNumber(assetNumber);
//    }
//
//    //update a vehicle
//    Vehicle updateVehicle(Vehicle vehicle){
//        try{
//            vehicle.setUpdatedAt(new Date());
//            vehicleRepository.save(vehicle);
//
//            //send updated vehicle to search-service
//            kafkaAsyncService.sendVehicle(vehicle,"UPDATE");
//
//            return vehicle;
//        }catch(Exception e){
//            return null;
//        }
//    }
//
//    //delete a vehicle by assetNumber
//    DefaultResponse deleteVehicle(String assetNumber){
//        Vehicle vehicle=vehicleRepository.findByAssetNumber(assetNumber);
//        if(vehicleRepository.deleteByAssetNumber(assetNumber)==1) {
//            //send vehicle to search-service to delete
//            kafkaAsyncService.sendVehicle(vehicle,"DELETE");
//            return new DefaultResponse("NA","Vehicle Deleted Successfully","200");
//        }
//        return new DefaultResponse("NA","Error in deleting vehicle","500");
//    }
//
//    //get Paginated list of vehicles
//    Page<Vehicle> getVehicles(int offset, int limit){
//        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
//    }
//
//    //get All Vehicles
//    Iterable<Vehicle> getAllVehicles(){
//        return vehicleRepository.findAll();
//    }
//
//    //get list of vehicles given their asset numbers
//    List<Vehicle> getVehiclesGivenAssetNumbers(List<String> assetNumbers){
//        List<Vehicle> vehicles=new ArrayList<>();
//        for(String assetNumber: assetNumbers){
//            vehicles.add(vehicleRepository.findByAssetNumber(assetNumber));
//        }
//        return vehicles;
//    }
//
//    //set assigned attribute of vehicle
//    public void setAssignedAttribute(String assetNumber,boolean assigned){
//        Vehicle vehicle=null;
//        vehicle=vehicleRepository.findByAssetNumber(assetNumber);
//        if(vehicle!=null){
//            vehicle.setAssigned(assigned);
//            vehicleRepository.save(vehicle);
//        }
//    }
//
//
//    //re-index vehicles
//    DefaultResponse reIndexVehicles(){
//        try{
//            List<Vehicle> vehicles=vehicleRepository.findAll();
//            for(Vehicle vehicle: vehicles){
//                kafkaAsyncService.sendVehicle(vehicle,"CREATE");
//            }
//            return new DefaultResponse("NA","Vehicles sent to search service","200");
//        }catch(Exception e){
//            return new DefaultResponse("NA","Error in getting vehicles or sending vehicles to search service","500");
//        }
//    }
//
//    //get vehicle by driver number
////    public Vehicle getVehicleByDriverNumber(String driverNumber){
////        return vehicleRepository.findByDriverNumber(driverNumber);
////    }
//
//    /*****************************Inspection Report***************************************/
//
//    /************Save Inspection Report*************/
//    public Vehicle saveInspectionReport(InspectionReport inspectionReport, String assetNumber){
//
//        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
////        vehicle.addInspectionReport(inspectionReport);
//        Date currentTime=new Date();
//        inspectionReport.setCreatedAt(currentTime);
//        inspectionReport.setVehicle(vehicle);
//        if(inspectionReport.getIssueReporting()!=null){
//            inspectionReport.getIssueReporting().setInspectionReport(inspectionReport);
//            inspectionReport.getIssueReporting().setVehicle(vehicle);
//            inspectionReport.getIssueReporting().setReportedAt(currentTime);
//            for(ImageVoice imageVoice: inspectionReport.getIssueReporting().getImageVoices()){
//                imageVoice.setIssue(inspectionReport.getIssueReporting());
//            }
//        }
//        for(InspectionReportField inspectionReportField: inspectionReport.getInspectionReportFields()) {
//            inspectionReportField.setInspectionReport(inspectionReport);
//        }
//        inspectionReportRepository.save(inspectionReport);
//
//        //assigning issue number to a issue
//        if (inspectionReport.getIssueReporting() != null) {
//            String issueNumber="FMS-ISS-";
//            Long myId=1000L+inspectionReport.getIssueReporting().getId();
//            String formatted = String.format("%06d",myId);
//            inspectionReport.getIssueReporting().setIssueNumber(issueNumber+formatted);
//            issueReportingRepository.save(inspectionReport.getIssueReporting());
//        }
//
//        //send inspection report to search service to save issues
//        kafkaAsyncService.sendInspectionReport(inspectionReport,"CREATE");
//
//        Vehicle vehicle1= vehicleRepository.findOne(vehicle.getId());
//        return vehicle1;
//
//    }
//
//    /************Get Inspection Reports by Asset Number*************/
//    Iterable<InspectionReport> getInspectionReports(String assetNo){
//
//        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNo);
//        return inspectionReportRepository.findByVehicle(vehicle);
//
//    }
//
//    /************Get All Inspection Reports*************/
//    Iterable<InspectionReport> getAllInspectionReports(){
//        return inspectionReportRepository.findAll();
//    }
//
//    //re-index inspection reports
//    DefaultResponse reIndexInspections(){
//        try{
//            List<InspectionReport> inspectionReports=inspectionReportRepository.findAll();
//            for(InspectionReport inspectionReport:inspectionReports){
//                kafkaAsyncService.sendInspectionReport(inspectionReport,"CREATE");
//            }
//            return new DefaultResponse("NA","Inspection reports sent to search service","200");
//        }catch(Exception e){
//            return new DefaultResponse("NA","Error in getting inspection reports or sending inspection reports to search service","500");
//        }
//    }
//
//    /***********************END of Inspection report functions*********************/
//
//    /*******************************************Inspection Report Template Functions********************************/
//    /**********Save inspection report template**************/
//    Vehicle saveInspectionReportTemplate(InspectionReportTemplate inspectionReportTemplate,String assetNumber){
//        Vehicle vehicle = vehicleRepository.findByAssetNumber(assetNumber);
//        vehicle.addInspectionReportTemplate(inspectionReportTemplate);
//        inspectionReportTemplate.setCreatedAt(new Date());
//        inspectionReportTemplate.setVehicle(vehicle);
//        for(InspectionReportTemplateField inspectionReportTemplateField: inspectionReportTemplate.getInspectionReportTemplateFields()) {
//            inspectionReportTemplateField.setInspectionReportTemplate(inspectionReportTemplate);
//        }
//        vehicleRepository.save(vehicle);
//        vehicle=vehicleRepository.findOne(vehicle.getId());
//        return vehicle;
//    }
//
//    /************Get inspection report templates of a vehicle*************/
//    Iterable<InspectionReportTemplate> getInspectionReportTemplates(String assetNumber){
//        Vehicle vehicle=vehicleRepository.findByAssetNumber(assetNumber);
//        return inspectionReportTemplateRepository.findByVehicle(vehicle);
//    }
//
//    /************Get Paginated inspection report templates*************/
//    Page<InspectionReportTemplate> getAllInspectionReportTemplates(int page,int size){
//        return inspectionReportTemplateRepository.findByIdNotNull(new PageRequest(page,size));
//    }
//    /************************END of Inspection report template functions*********************/
//
//    /************************* Issue Functions **********************************/
//    /***********************Get issues of a vehicle******************************/
//    Iterable<IssueReporting> getIssuesOfVehicle(String assetNumber){
//        return issueReportingRepository.findAllByVehicle_AssetNumber(assetNumber);
//    }
//
//    /************************ Get Paginated Issues******************************/
//    Page<IssueReporting> getPaginatedIssues(int page, int size){
//        return issueReportingRepository.findByIdNotNull(new PageRequest(page,size));
//    }
//
//    /************************* Get All Issues **********************************/
//    Iterable<IssueReporting> getAllIssues(){
//        return issueReportingRepository.findAll();
//    }
//
//    /************************* END Issue Functions **********************************/
//
//    /**************************** Service Task Functions*********************************/
//    //add service task
//    ServiceTask addServiceTask(ServiceTask serviceTask){
//        serviceTask.setCreatedAt(new Date());
//        //it means that service request has subtasks
//        if(serviceTask.getSubTasks().size()!=0){
//            for(ServiceTask serviceTask1: serviceTask.getSubTasks()){
//                serviceTask1.addTask(serviceTask);
//            }
//        }
//        else{
//            serviceTask.setSubTasks(null);
//        }
//        if(serviceTask.getTasks().size()==0){
//            serviceTask.setTasks(null);
//        }
//        serviceTaskRepository.save(serviceTask);
//        return serviceTaskRepository.findOne(serviceTask.getId());
//    }
//
//    //update service task
//    ServiceTask updateServiceTask(ServiceTask serviceTask){
//        serviceTask.setUpdatedAt(new Date());
//        //it means that service request has subtasks
//        if(serviceTask.getSubTasks()!=null){
//            for(ServiceTask serviceTask1: serviceTask.getSubTasks()){
//                serviceTask1.addTask(serviceTask);
//            }
//        }
//        serviceTaskRepository.save(serviceTask);
//        return serviceTaskRepository.findOne(serviceTask.getId());
//    }
//
//    //delete service task by id
//    DefaultResponse deleteServiceTask(Long id){
//        try {
//            serviceTaskRepository.delete(id);
//            return new DefaultResponse("","Service Task deleted Successfully","200");
//        }catch(Exception e){
//            return new DefaultResponse("","Error in deleting Service Task","500");
//        }
//    }
//
//    //get service task by id
//    ServiceTask getServiceTask(Long id){
//        return serviceTaskRepository.findOne(id);
//    }
//
//    //get list of service tasks
//    Page<ServiceTask> getServiceTasks(int page,int size){
//        return serviceTaskRepository.findByIdNotNull(new PageRequest(page,size));
//    }
//
//    /****************************END Service Task Functions******************************/
//
//    /**************************** Service Entry Functions*********************************/
//    //add service Entry
//    ServiceEntry addServiceEntry(ServiceEntry serviceEntry){
//        serviceEntry.setCreatedAt(new Date());
//        Vehicle vehicle=vehicleRepository.findByAssetNumber(serviceEntry.getVehicle().getAssetNumber());
//        serviceEntry.setVehicle(vehicle);
//        if(serviceEntry.getMeterEntry()!=null){
//            serviceEntry.getMeterEntry().setServiceEntry(serviceEntry);
//            serviceEntry.getMeterEntry().setVehicle(vehicle);
//        }
//        serviceEntryRepository.save(serviceEntry);
//        for(ServiceTask serviceTask:serviceEntry.getServiceTasks()){
//            serviceTask.addServiceEntry(serviceEntry);
//            serviceTaskRepository.save(serviceTask);
//        }
//        for(IssueReporting issueReporting:serviceEntry.getIssueReportings()) {
//            IssueReporting issue=issueReportingRepository.findByIssueNumber(issueReporting.getIssueNumber());
//            issue.setStatus("Resolved");
//            issue.setServiceEntry(serviceEntry);
//            issueReportingRepository.save(issue);
//        }
//        return serviceEntryRepository.findOne(serviceEntry.getId());
//    }
//
//    //update service Entry
//    ServiceEntry updateServiceEntry(ServiceEntry serviceEntry){
//        serviceEntry.setUpdatedAt(new Date());
//        if(serviceEntry.getMeterEntry()!=null){
//            serviceEntry.getMeterEntry().setServiceEntry(serviceEntry);
//        }
//        serviceEntryRepository.save(serviceEntry);
//        return serviceEntryRepository.findOne(serviceEntry.getId());
//    }
//
//    //delete service Entry by id
//    DefaultResponse deleteServiceEntry(Long id){
//        try {
//            serviceEntryRepository.delete(id);
//            return new DefaultResponse("","Service Entry deleted Successfully","200");
//        }catch(Exception e){
//            return new DefaultResponse("","Error in deleting Service Entry","500");
//        }
//    }
//
//    //get service Entry by id
//    ServiceEntry getServiceEntry(Long id){
//        return serviceEntryRepository.findOne(id);
//    }
//
//    //get list of service Entries
//    Page<ServiceEntry> getServiceEntries(int page,int size){
//        return serviceEntryRepository.findByIdNotNull(new PageRequest(page,size));
//    }
//
//    /****************************END Service Task Functions******************************/
//
//
//    /**************************** Work Order Functions*********************************/
//    //add work order
//    WorkOrder addWorkOrder(WorkOrder workOrder){
//        workOrder.setCreatedAt(new Date());
//        Vehicle vehicle=vehicleRepository.findByAssetNumber(workOrder.getVehicle().getAssetNumber());
//        workOrder.setVehicle(vehicle);
//        vehicle.addWorkOrder(workOrder);
//        for(WorkOrderLineItems workOrderLineItem: workOrder.getWorkOrderLineItems()){
//            workOrderLineItem.setWorkOrder(workOrder);
//            if(workOrderLineItem.getIssueReporting()!=null){
//                workOrderLineItem.getIssueReporting().setWorkOrderLineItems(workOrderLineItem);
//            }
//            if(workOrderLineItem.getServiceTask()!=null){
//                workOrderLineItem.getServiceTask().setWorkOrderLineItems(workOrderLineItem);
//            }
//        }
//        workOrderRepository.save(workOrder);
//        String workOrderNumber="FMS-WO-";
//        Long myId=1000L+workOrder.getId();
//        String formatted = String.format("%06d",myId);
//        workOrder.setWorkOrderNumber(workOrderNumber+formatted);
//        workOrderRepository.save(workOrder);
//        return workOrderRepository.findOne(workOrder.getId());
//    }
//
//    //update work order
//    WorkOrder updateWorkOrder(WorkOrder workOrder){
//        workOrder.setUpdatedAt(new Date());
//        Vehicle vehicle=vehicleRepository.findByAssetNumber(workOrder.getVehicle().getAssetNumber());
//        workOrder.setVehicle(vehicle);
//        workOrderRepository.save(workOrder);
//        return workOrderRepository.findOne(workOrder.getId());
//    }
//
//    //delete work order by id
//    DefaultResponse deleteWorkOrder(Long id){
//        try {
//            workOrderRepository.delete(id);
//            return new DefaultResponse("","Work Order deleted Successfully","200");
//        }catch(Exception e){
//            return new DefaultResponse("","Error in deleting Work Order","500");
//        }
//    }
//
//    //get work order by id
//    WorkOrder getWorkOrder(Long id){
//        return workOrderRepository.findOne(id);
//    }
//
//    //get list of work orders
//    Page<WorkOrder> getWorkOrders(int page,int size){
//        return workOrderRepository.findByIdNotNull(new PageRequest(page,size));
//    }
//
//    /****************************END Work Order Functions******************************/
//
//
//    /***************************Get file from s3*******************************/
//    ResponseEntity<byte[]> getFileFroms3(String url) throws IOException {
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//        this.s3client = new AmazonS3Client(credentials);
//        String[] parts=url.split("/");
//        String key=parts[parts.length-1];
//        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
//        S3Object s3Object = this.s3client.getObject(getObjectRequest);
//
//        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
//
//        byte[] bytes = IOUtils.toByteArray(objectInputStream);
//
//        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
////        File initialFile = new File("C:\\Users\\user\\Desktop\\sample_audio2.mp3");
////        InputStream targetStream = new FileInputStream(initialFile);
////        byte[] bytes=IOUtils.toByteArray(targetStream);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        httpHeaders.setContentLength(bytes.length);
//        httpHeaders.setContentDispositionFormData("attachment", "sample_audio.mp3");
//
//        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
//    }

}

