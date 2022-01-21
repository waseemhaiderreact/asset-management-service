package com.sharklabs.ams.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.AssetImage.AssetImageRepository;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.activitywall.ActivityWallRepository;
import com.sharklabs.ams.asset.*;
import com.sharklabs.ams.assetGroup.AssetGroup;
import com.sharklabs.ams.assetGroup.AssetGroupDTO;
import com.sharklabs.ams.assetGroup.AssetGroupRepository;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.assetfield.AssetFieldRepository;
import com.sharklabs.ams.assetimport.AssetImport;
import com.sharklabs.ams.assetimport.AssetImportRepository;
import com.sharklabs.ams.attachment.Attachment;
import com.sharklabs.ams.attachment.AttachmentRepository;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.category.CategoryFieldDTO;
import com.sharklabs.ams.category.CategoryRepository;
import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.consumption.ConsumptionRepository;
import com.sharklabs.ams.events.CustomChannels;
import com.sharklabs.ams.events.asset.AssetsNameModel;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModel;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModelResponse;
import com.sharklabs.ams.events.wallet.WalletNotification;
import com.sharklabs.ams.events.wallet.WalletNotificationModel;
import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.events.wallet.WalletUser;
import com.sharklabs.ams.fact.Fact;
import com.sharklabs.ams.fact.FactRepository;
import com.sharklabs.ams.feign.ApsServiceProxy;
import com.sharklabs.ams.feign.AuthServiceProxy;
import com.sharklabs.ams.field.Field;
import com.sharklabs.ams.field.FieldDTO;
import com.sharklabs.ams.field.FieldRepository;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import com.sharklabs.ams.fieldtemplate.FieldTemplateRepository;
import com.sharklabs.ams.fieldtemplate.FieldTemplateResponse;
import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.imagevoice.ImageVoiceRepository;
import com.sharklabs.ams.importrecord.ImportRecord;
import com.sharklabs.ams.importrecord.ImportRecordRepository;
import com.sharklabs.ams.importtemplate.ImportTemplate;
import com.sharklabs.ams.importtemplate.ImportTemplateDTO;
import com.sharklabs.ams.importtemplate.ImportTemplateRepository;
import com.sharklabs.ams.inspectionitem.InspectionItem;
import com.sharklabs.ams.inspectionitemcategory.InspectionItemCategory;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplateRepository;
import com.sharklabs.ams.message.Message;
import com.sharklabs.ams.message.MessageRepository;
import com.sharklabs.ams.model.assignment.Assignment;
import com.sharklabs.ams.model.assignment.AssignmentHistory;
import com.sharklabs.ams.model.issue.Issue;
import com.sharklabs.ams.model.workorder.WorkOrder;
import com.sharklabs.ams.page.AssetPage;
import com.sharklabs.ams.reply.Reply;
import com.sharklabs.ams.reply.ReplyRepository;
import com.sharklabs.ams.request.*;
import com.sharklabs.ams.response.*;
import com.sharklabs.ams.security.InterceptorConfig;
import com.sharklabs.ams.security.PrivilegeHandler;
import com.sharklabs.ams.security.SCIM2Util;
import com.sharklabs.ams.usage.Usage;
import com.sharklabs.ams.usage.UsageRepository;
import com.sharklabs.ams.util.AccessDeniedException;
import com.sharklabs.ams.util.Constant;
import com.sharklabs.ams.util.Util;
import com.sharklabs.ams.wallet.*;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import shark.commons.util.ApplicationException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.sharklabs.ams.util.Constant.*;

//import org.json.simple.JSONObject;

@Service
@EnableBinding(CustomChannels.class)
public class   AssetService {
    private static final Logger LOGGER = LogManager.getLogger(AssetService.class);
    @Autowired
    WalletRespository walletRespository;
    @Autowired
    FactRepository factRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    FieldTemplateRepository fieldTemplateRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    InspectionTemplateRepository inspectionTemplateRepository;
    @Autowired
    ActivityWallRepository activityWallRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConsumptionRepository consumptionRepository;
    @Autowired
    UsageRepository usageRepository;
    @Autowired
    ImageVoiceRepository imageVoiceRepository;
    @Autowired
    AssetFieldRepository assetFieldRepository;
    @Autowired
    AssetImageRepository assetImageRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WalletRequestRepository walletRequestRepository;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    KafkaAsyncService kafkaAsyncService;
    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetImportRepository assetImportRepository;

    @Autowired
    ImportRecordRepository importRecordRepository;

    @Autowired
    InterceptorConfig resourceServerConfiguration;

    @Autowired
    ApsServiceProxy apsServiceProxy;
    
    @Autowired
    SCIM2Util scim2Util;

    @Autowired
    PrivilegeHandler privilegeHandler;

    @Autowired
    AuthServiceProxy authServiceProxy;

    @Autowired
    ImportTemplateRepository importTemplateRepository;

    @Autowired
    AssetMapperRepository assetMapperRepository;

    private WalletRequestModel walletRequestModel=null;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region}")
    private String region;
    @Value("${cloud.aws.bucketName}")
    private String bucket;
    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriver = "";
    @Value("${spring.datasource.url}")
    private String dataSourceUrl = "";
    @Value("${spring.datasource.username}")
    private String dataSourceUserName = "";
    @Value("${spring.datasource.password}")
    private String dataSourcePassword = "";
    private static final String s3EnpointUrl = "https://fms-issue-assets.s3.eu-west-2.amazonaws.com";
    private static final String primaryUsageType = "1";
    private static final String secondaryUsageType = "2";
    private AmazonS3 s3client;
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }
    /********************************************Category Functions**********************************************/
    //add category (AMS_UC_01)
    /*
    Purpose of this function is to add a category.
    Category has children like field template and inspection template and assets.
    So, we need to set the parent of children so that children are saved automatically if parent is saved
    UUID is also set before adding category in the db.
    Then category is saved in db
    */
//    @HasCategory
    DefaultResponse addCategory(AddCategoryRequest addCategoryRequest) throws IOException, AccessDeniedException {

        if(!privilegeHandler.hasCategory())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse defaultResponse = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered service method to add category, details: "+convertToJSON(addCategoryRequest));
            Category category = categoryRepository.findByTenantUUIDAndName(addCategoryRequest.getCategory().getTenantUUID(),addCategoryRequest.getCategory().getName());
            if(category != null){
                throw  new Exception("Category Name Already Exists.");
            }
            addCategoryRequest.getCategory().setUuid(UUID.randomUUID().toString());
            //if children exists
            if (addCategoryRequest.getCategory().getFieldTemplate() != null) {
                //if children is not already saved in db
                if (addCategoryRequest.getCategory().getFieldTemplate().getId() == null) {
                    LOGGER.debug("Category has a field template");
                    addCategoryRequest.getCategory().getFieldTemplate().setUuid(UUID.randomUUID().toString());
                    addCategoryRequest.getCategory().getFieldTemplate().setCategory(addCategoryRequest.getCategory());
                    List<String> labels = new ArrayList<>();
                    for (Field field : addCategoryRequest.getCategory().getFieldTemplate().getFields()) {
                        if(labels.size() !=0){
                            if(labels.contains(field.getLabel())){
                                throw new Exception("Fields cannot have same labels");
                            }else{
                                labels.add(field.getLabel());
                            }
                        }else{
                            labels.add(field.getLabel());
                        }
                        field.setUuid(UUID.randomUUID().toString());
                        field.setFieldTemplateUUID(addCategoryRequest.getCategory().getFieldTemplate().getUuid());
                    }
                }
            }
            //if children exists
            if (addCategoryRequest.getCategory().getInspectionTemplate() != null) {
                //if children is not already saved in db
                if (addCategoryRequest.getCategory().getInspectionTemplate().getId() == null) {
                    LOGGER.debug("Category has an inspection template");
                    addCategoryRequest.getCategory().getInspectionTemplate().setUuid(UUID.randomUUID().toString());
                    addCategoryRequest.getCategory().getInspectionTemplate().setCategory(addCategoryRequest.getCategory());
                    for (InspectionItemCategory inspectionItemCategory : addCategoryRequest.getCategory().getInspectionTemplate().getInspectionItemCategories()) {
                        inspectionItemCategory.setUuid(UUID.randomUUID().toString());
                        inspectionItemCategory.setInspectionTemplateUUID(addCategoryRequest.getCategory().getInspectionTemplate().getUuid());
                        for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                            inspectionItem.setUuid(UUID.randomUUID().toString());
                            inspectionItem.setInspectionItemCategoryUUID(inspectionItemCategory.getUuid());
                        }
                    }
                }
            }
            //if children exists
            for (Asset asset : addCategoryRequest.getCategory().getAssets()) {
                asset.setCategoryUUID(addCategoryRequest.getCategory().getUuid());
                asset.setUuid(UUID.randomUUID().toString());
                ActivityWall activityWall = new ActivityWall();
                activityWall.setUuid(UUID.randomUUID().toString());
                activityWall.setAssetUuid(asset.getUuid());
                activityWallRepository.save(activityWall);
                activityWall = null;
                for (AssetField assetField : asset.getAssetFields()) {
                    assetField.setUuid(UUID.randomUUID().toString());
                    assetField.setAssetUUID(asset.getUuid());
                }
            }
            categoryRepository.save(addCategoryRequest.getCategory());
            LOGGER.info("Category Added Successfully");
            defaultResponse = new DefaultResponse("Success", "Category added succssfully", "200", addCategoryRequest.getCategory().getUuid());
        } catch (Exception e) {
            LOGGER.error("Error while adding category, details: "+convertToJSON(addCategoryRequest), e);
            defaultResponse = new DefaultResponse("Failure", "Error in add category: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of add category");
            util.clearThreadContextForLogging();
            util = null;
            addCategoryRequest = null;
        }

        return defaultResponse;
    }
    //delete category (AMS_UC_05)
    /*
        This function deletes a category from db and all it's children(if a category is deleted
        then it's assets,field template, inspection template will also be deleted)
     */
//    @HasDelete
    DefaultResponse deleteCategory(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasDelete())
            throw new AccessDeniedException();

        Util util = new Util();
        Category category = null;
        ArrayList<String> assetUUIDs = null;
        DefaultResponse defaultResponse = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info(" Entered service method  for deleting category of uuid: "+uuid);
            category = categoryRepository.findCategoryByUuid(uuid);
            assetUUIDs = new ArrayList<>();
            for (Asset asset : category.getAssets()) {
                assetUUIDs.add(asset.getUuid());
                asset = null;
            }
            activityWallRepository.deleteAllByAssetUuidIn(assetUUIDs);
            categoryRepository.deleteById(category.getId());
            LOGGER.info("Category deleted Successfully");
            defaultResponse = new DefaultResponse("Success", "Category deleted Successfully", "200");
        } catch (Exception e) {
            LOGGER.error("Error while deleting category of uuid: "+uuid, e);
            defaultResponse = new DefaultResponse("Failure", "Error in deleting category: " + e.getMessage(),
                    "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of deleting category.");
            util.clearThreadContextForLogging();
            category = null;
            assetUUIDs = null;
        }

        return defaultResponse;
    }
    //get a category (AMS_UC_02)
    /*
        This function gets a category from db. We pass it the uuid of category and in response we get a category
     */
//    @HasRead
    GetCategoryResponse getCategory(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();

        GetCategoryResponse response = new GetCategoryResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method to get Category by uuid: "+uuid);
            response.setCategory(categoryRepository.findCategoryByUuid(uuid));
            response.setResponseIdentifier("Success");
            response.getCategory().setAssets(null);
            LOGGER.info("Received Category From database. Sending it to controller");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting category from db. Category UUID: " + uuid, e);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //get all categories (AMS_UC_03)
    /*
        This function just returns all the categories (List of categories) from db
     */
//    @HasRead
    GetCategoriesResponse GetAllCategories(String tenantUUID) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();

        GetCategoriesResponse response = new GetCategoriesResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method for fetching Categories by tenantUUID: "+tenantUUID);
            response.setCategories(categoryRepository.findByTenantUUID(tenantUUID));
            for (Category category : response.getCategories()) {
                category.setAssets(null);
            }
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Categories From database. Sending it to controller");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db.", e);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //edit category (AMS_UC_04)
    /*
        Purpose of this function is to edit category
        It works same as the add function of category. First, we set the parent of category children and then save it
        This will update the category
        You will notice an extra if in the code which will only be executed if request is sent through Test Library.
        This is because in Test Library, we don't know the id of created object, we only know it's uuid as uuid is returned with default response
        when a object is created therefore, this additional code sets the id of the object. If we don't set the id of object then the old category
        won't be updated instead a new one will be created
     */
//    @HasCategory
    EditCategoryResponse editCategory(EditCategoryRequest editCategoryRequest) throws AccessDeniedException {

        if(!privilegeHandler.hasCategory())
            throw new AccessDeniedException();

        Util util = new Util();
        Category category = null;
        EditCategoryResponse response = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method for editing Category of uuid:  "+editCategoryRequest.getCategory().getUuid());
            category = categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid());
            //if id of category is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if (editCategoryRequest.getCategory().getId() == null) {
                editCategoryRequest.getCategory().setId(category.getId());
                editCategoryRequest.getCategory().setFieldTemplate(category.getFieldTemplate());
                editCategoryRequest.getCategory().setAssets(category.getAssets());
                editCategoryRequest.getCategory().setInspectionTemplate(category.getInspectionTemplate());
            }

            category.setDescription(editCategoryRequest.getCategory().getDescription());
            category.setName(editCategoryRequest.getCategory().getName());
            List<String> labels = new ArrayList<>();
            for(Field field : editCategoryRequest.getCategory().getFieldTemplate().getFields()){
                if(field.getId() == null){
                    if(labels.size() !=0){
                        if(labels.contains(field.getLabel().trim())){
                            throw new Exception("Fields cannot have same labels");
                        }else{
                            labels.add(field.getLabel().trim());
                        }
                    }else{
                        labels.add(field.getLabel().trim());
                    }
                    field.setUuid(UUID.randomUUID().toString());
                    field.setFieldTemplateUUID(editCategoryRequest.getCategory().getFieldTemplate().getUuid());
                }else {
                    labels.add(field.getLabel().trim());
                }
            }
            category.setFieldTemplate(editCategoryRequest.getCategory().getFieldTemplate());

            //setting parent of children if exists
            if (editCategoryRequest.getCategory().getInspectionTemplate() != null) {
                editCategoryRequest.getCategory().getInspectionTemplate().setCategory(editCategoryRequest.getCategory());
                for (InspectionItemCategory inspectionItemCategory : editCategoryRequest.getCategory().getInspectionTemplate().getInspectionItemCategories()) {
                    inspectionItemCategory.setInspectionTemplateUUID(editCategoryRequest.getCategory().getInspectionTemplate().getUuid());
                    for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                        inspectionItem.setInspectionItemCategoryUUID(inspectionItemCategory.getUuid());
                    }
                }
            }
            if (editCategoryRequest.getCategory().getFieldTemplate() != null) {
                editCategoryRequest.getCategory().getFieldTemplate().setCategory(editCategoryRequest.getCategory());
                for (Field field : editCategoryRequest.getCategory().getFieldTemplate().getFields()) {
                    field.setFieldTemplateUUID(editCategoryRequest.getCategory().getFieldTemplate().getUuid());
                }
            }
                if(editCategoryRequest.getCategory().getAssets() != null){
                    for (Asset asset : editCategoryRequest.getCategory().getAssets()) {
                        asset.setCategoryUUID(editCategoryRequest.getCategory().getUuid());
                        for (AssetField assetField : asset.getAssetFields()) {
                            assetField.setAssetUUID(asset.getUuid());
                        }
                    }
                }

            editCategoryRequest.getCategory().setAssets(category.getAssets());
            categoryRepository.save(editCategoryRequest.getCategory());
            response = new EditCategoryResponse();
            response.setCategory(categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid()));
            response.setResponseIdentifier("Success");
            LOGGER.info("Category Updated Successfully. Sending updated Category to controller");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while updating category or getting updated category from db. ", e);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            category = null;
            editCategoryRequest = null;
        }

        return response;
    }

    //service function to delete category field and their related asset field values
    public  DefaultResponse deleteCategoryFields(String categoryName, String tenantUuid, String fieldName) throws ApplicationException{
        Util util = new Util();
        DefaultResponse response = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<Predicate> clauses = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of delete category field.");
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Category.class);
            root = query.from(Category.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("name"),categoryName));
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),tenantUuid));

            Category category = (Category) entityManager.createQuery(query.select(root).where(clauses.toArray(new Predicate[]{}))).getSingleResult();
            if(category != null){
                Set<Field> fields = category.getFieldTemplate().getFields();
                String foundFieldUuid = null;
                for(Field field: fields){
                    if(field.getLabel().equals(fieldName)){
                        foundFieldUuid = field.getUuid();
                        break;
                    }
                }
                if(foundFieldUuid != null) {
                    query = criteriaBuilder.createQuery(AssetField.class);
                    root = query.from(AssetField.class);

                    List<AssetField> assetFields = (List<AssetField>) entityManager.createQuery(query.select(root).where(criteriaBuilder.equal(root.get("fieldId"), foundFieldUuid))).getResultList();

                    if (assetFields.size() != 0) {
                        assetFieldRepository.deleteInBatch(assetFields);
                        fieldRepository.deleteByUuid(foundFieldUuid);
                        response = new DefaultResponse("Success","Category field with Name: " + fieldName,"F200");
                    }else{
                        fieldRepository.deleteByUuid(foundFieldUuid);
                        response = new DefaultResponse("Success","Category field with Name: " + fieldName,"F200");
                    }
                }else{
                    response = new DefaultResponse("Failure","No field found with the name: " + fieldName,"F500");
                }

            }else{
                response = new DefaultResponse("Failure","No Category found with the name: " + categoryName,"F500");
            }
        }catch (Exception e){
            throw new ApplicationException("An error occurred while deleting category field. Category Name: " + categoryName + " Tenant uuid: " + tenantUuid + " Field Name: " +fieldName,e);
        }finally {
            LOGGER.info("Returning to delete category field controller.");
            util.clearThreadContextForLogging();
            criteriaBuilder = null;
            query = null;
            root = null;
            clauses = null;
        }
        return  response;
    }

    public GetAssetGroupsAndAssetsResponse getAssetGroupsAndAssetsByCategoryUUID(String categoryUUID, String tenantUUID) throws ApplicationException,AccessDeniedException{
        //check if user have read category privilege
        if(!privilegeHandler.hasRead()){
            new AccessDeniedException();
        }

        Util util = new Util();
        GetAssetGroupsAndAssetsResponse response = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<AssetGroup> assetGroups = null;
        List<Predicate> clauses = null;
        List<AssetNameAndUUIDModel> assetNameAndUUIDModels = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get Asset groups and Assets by category uuid: " + categoryUUID);

            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Asset.class);
            root = query.from(Asset.class);
            clauses = new ArrayList<>();
            response = new GetAssetGroupsAndAssetsResponse();
            clauses.add(criteriaBuilder.equal(root.get("categoryUUID"),categoryUUID));
            clauses.add(criteriaBuilder.isNull(root.get("removeFromCategoryUUID")));
            clauses.add(criteriaBuilder.isNull(root.get("removeFromGroupUUID")));

            response.setAssets((List<AssetNameAndUUIDModel>) entityManager.createQuery(query.select(criteriaBuilder.construct(AssetNameAndUUIDModel.class,root.get("name"),root.get("uuid")))
                    .where(clauses.toArray(new Predicate[]{})).orderBy(criteriaBuilder.desc(root.get("id")))).getResultList());

            clauses.clear();
            query = criteriaBuilder.createQuery(AssetGroup.class);
            root = query.from(AssetGroup.class);
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),tenantUUID));
            clauses.add(criteriaBuilder.isNull(root.get("deletefromGroupUUID")));

            assetGroups = (List<AssetGroup>) entityManager.createQuery(query.select(root).where(clauses.toArray(new Predicate[]{}))
                    .orderBy(criteriaBuilder.desc(root.get("id")))).getResultList();
            assetNameAndUUIDModels = new ArrayList<>();
            for(AssetGroup assetGroup: assetGroups){
                AssetNameAndUUIDModel model = new AssetNameAndUUIDModel();
                assetGroup.getAssets().forEach(asset -> {
                    if(asset.getCategoryUUID().equals(categoryUUID)){
                        model.setName(assetGroup.getGroupName());
                        model.setUuid(assetGroup.getUuid());
                    }
                });
                if(model.getName() != null && model.getUuid() != null) {
                    assetNameAndUUIDModels.add(model);
                }
            }
            response.setAssetGroups(assetNameAndUUIDModels);
            response.setResponseIdentifier(SUCCESS);
            LOGGER.info("Successfully got Assets and Asset groups by category uuid.");

        }catch (Exception e){
            throw new ApplicationException("An Error occurred while getting Asset groups and Assets by category uuid.",e);
        }finally {
            LOGGER.info("Returning to controller of get Asset groups and Assets by category UUID");
            util.clearThreadContextForLogging();
            util = null;
            criteriaBuilder = null;
            query = null;
            root = null;
        }
        return response;
    }

    public  CategoriesListResponse getCategoriesListByTenantUUID(String tenantUUID) throws ApplicationException,AccessDeniedException{
        if(!privilegeHandler.hasCategory()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        CategoriesListResponse response = null;
        try{
            LOGGER.info("Inside service function of get categories list by tenant uuid: " + tenantUUID);
            response = new CategoriesListResponse();
            response.setCategoryDTOS(categoryRepository.findCategoriesListByTenantUUID(tenantUUID));
            response.setResponseIdentifier(SUCCESS);
        }catch (Exception e){
            LOGGER.error("An Error occurred while getting categories list.",e);
            throw new ApplicationException("An Error occurred while getting categories list.",e);
        }finally {
            LOGGER.info("Returning to controller get categories list by tenant uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public  CategoriesFieldsListResponse getCategoriesFieldsListByTenantUUID(String uuid) throws ApplicationException,AccessDeniedException{
        if(!privilegeHandler.hasCategory()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        CategoriesFieldsListResponse response = new CategoriesFieldsListResponse();
        try{
            LOGGER.info("Inside service function of get categories fields list by tenant uuid: " + uuid);
            List<CategoryFieldDTO> categoryFieldDTOS = categoryRepository.findCategoriesFieldsByTenantUUID(uuid);
            response.setCategoryFieldDTOS(categoryFieldDTOS);
            response.setResponseIdentifier(SUCCESS);
        }catch (Exception e){
            response.setResponseIdentifier(FAILURE);
            LOGGER.error("An Error occurred while getting categories fields list.",e);
            throw new ApplicationException("An Error occurred while getting categories fields list.",e);
        }finally {
            LOGGER.info("Returning to controller get categories fields list by tenant uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }
    /*******************************************END Category Functions*******************************************/

    /*******************************************Field Template Functions*****************************************/
    //add field template (AMS_UC_06)
    /*
    This function adds a field template in a category
    We pass it category uuid and Field template object
    First of all, we find the category with that uuid and set it as the parent of field template that we have to add.
    Then we assign a new uuid to field template and set the parent of it's children and also assigning uuid of children.
    Then save the object
     */
//    @HasRead
    DefaultResponse addFieldTemplate(AddFieldTemplateRequest addFieldTemplateRequest) throws IOException,AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = null;
        Category category = null;
        DefaultResponse response = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method of adding Field Template, details: "+convertToJSON(addFieldTemplateRequest));
            //get category from db

            if (addFieldTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(addFieldTemplateRequest.getCategoryId());
                addFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(addFieldTemplateRequest.getFieldTemplate());
            } else {
                throw new Exception("category uuid is not given for field template");
            }
            //setting uuid
            addFieldTemplateRequest.getFieldTemplate().setUuid(UUID.randomUUID().toString());
            //setting parent of all children and also setting uuid
            for (Field field : addFieldTemplateRequest.getFieldTemplate().getFields()) {
                field.setFieldTemplateUUID(addFieldTemplateRequest.getFieldTemplate().getUuid());
                field.setUuid(UUID.randomUUID().toString());
            }
            //saving in db
            categoryRepository.save(category);
            LOGGER.info("Field Template Added Successfully");
            response = new DefaultResponse("Success", "Field Template Added Successfully", "200", addFieldTemplateRequest.getFieldTemplate().getUuid());
        } catch (Exception e) {
            LOGGER.error("Error while adding field template, details: "+convertToJSON(addFieldTemplateRequest), e);
            response = new DefaultResponse("Failure", "Error in adding field template. Error: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of adding Field Template");
            util.clearThreadContextForLogging();
            util = null;
            category = null;
            addFieldTemplateRequest = null;
        }

        return response;
    }
    //delete field template AMS_UC_09
    /*
    This function deletes a field template
    UUID of field template is passed to this function
    First, we find the field template with that id
    Then, the parent of field template is set to null because we don't need to delete it's parent(Category)
    Then, field template is deleted
     */
//    @HasDelete
    DefaultResponse deleteFieldTemplate(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasDelete())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse defaultResponse = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method for deleting Field Template of uuid: "+uuid);

            //deleting
            fieldTemplateRepository.deleteByUuid(uuid);
            LOGGER.info("Field Template deleted Successfully");
            defaultResponse = new DefaultResponse("Success", "Field Template deleted Successfully", "200");
        } catch (Exception e) {
            LOGGER.error("Error while deleting field template of uuid: "+uuid, e);
            defaultResponse = new DefaultResponse("Failure", "Error in deleting field template: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of deleting Field Template");
            util.clearThreadContextForLogging();
            util = null;
        }

        return defaultResponse;
    }
    //get field template AMS_UC_07
    /*
    This function gets a field template by uuid
    UUID is passed to this function and field template is returned with that UUID
     */
//    @HasRead
    GetFieldTemplateResponse getFieldTemplate(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();

        GetFieldTemplateResponse response = new GetFieldTemplateResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method to get Field Template of uuid: "+uuid);
            response.setFieldTemplate(fieldTemplateRepository.findByUuid(uuid));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Field Template From database. Sending it to controller");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting field template from db. Field Template UUID: " + uuid, e);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //edit field template AMS_UC_08
    /*
    This function edits a field template
    We pass it category uuid and Field template object that is to be updated
    First of all, we find the category with that uuid and set it as the parent of field template that we have to add.
    Then we assign a new uuid to field template and set the parent of it's children and also assigning uuid of children.
    Then save the object
    You will notice an extra if in the code which will only be executed if request is sent through Test Library.
    This is because in Test Library, we don't know the id of created object, we only know it's uuid as uuid is returned with default response
    when a object is created therefore, this additional code sets the id of the object. If we don't set the id of object then the old field template
    won't be updated instead a new one will be created
     */
//    @HasUpdate
    EditFieldTemplateResponse editFieldTemplate(EditFieldTemplateRequest editFieldTemplateRequest) throws AccessDeniedException {

        if(!privilegeHandler.hasUpdate())
            throw new AccessDeniedException();

        Util util = new Util();

        EditFieldTemplateResponse response = null;
        Category category = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method for editing Field Template, details: "+convertToJSON(editFieldTemplateRequest));
            //get category from db
            category = null;
            if (editFieldTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(editFieldTemplateRequest.getCategoryId());
                editFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(editFieldTemplateRequest.getFieldTemplate());
            } else {
                throw new Exception("category uuid is not given for field template");
            }
            //if id of field template is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if (editFieldTemplateRequest.getFieldTemplate().getId() == null) {
                FieldTemplate fieldTemplate = fieldTemplateRepository.findByUuid(editFieldTemplateRequest.getFieldTemplate().getUuid());
                editFieldTemplateRequest.getFieldTemplate().setId(fieldTemplate.getId());
                editFieldTemplateRequest.getFieldTemplate().setFields(fieldTemplate.getFields());
                fieldTemplate = null;
            }
            //setting parent of all children and also setting uuid
            for (Field field : editFieldTemplateRequest.getFieldTemplate().getFields()) {
                field.setFieldTemplateUUID(editFieldTemplateRequest.getFieldTemplate().getUuid());
            }
            //saving in db
            categoryRepository.save(category);

            response = new EditFieldTemplateResponse();
            response.setResponseIdentifier("Success");
            response.setFieldTemplate(category.getFieldTemplate());
            LOGGER.info("Field Template Edited Successfully");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing field template from db. Field Template UUID: " + editFieldTemplateRequest.getFieldTemplate().getUuid(), e);
            e = null;
        }finally{
            LOGGER.info("Returning to controller of editing Field Template");
            util.clearThreadContextForLogging();
            util = null;
            category = null;
            editFieldTemplateRequest = null;
        }

        return response;
    }
    /*******************************************END Field Template Functions*************************************/

    /*******************************************Import Template Functions*************************************/

    public DefaultResponse addAssetImportTemplate(ImportTemplateRequest request) throws ApplicationException, AccessDeniedException{
        if(!privilegeHandler.hasCreate()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        DefaultResponse response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of add Asset import template. Details: " + convertToJSON(request));
            ImportTemplate importTemplate = new ImportTemplate();
            importTemplate.setUuid(UUID.randomUUID().toString());
            importTemplate.setCreatedDate(new Date());
            importTemplate.setImportType("CSV");
            importTemplate.setImportedData("Asset");
            importTemplate.setTemplateName(request.getTemplateName());
            importTemplate.setCategoryName(request.getCategoryName());
            importTemplate.setUserUUID(request.getUserUUID());
            importTemplate.setUserName(request.getUserName());
            importTemplate.setCategoryUUID(request.getCategoryUUID());
            importTemplate.setTenantUUID(request.getTenantUUID());
            importTemplate.setCsvColumnData(stringToByteCompress(request.getCsvColumnData()));
            importTemplateRepository.save(importTemplate);
            importTemplate = importTemplateRepository.findByUuid(importTemplate.getUuid());
            importTemplate.setTemplateNumber(generateAssetImportTemplateNumber(importTemplate.getId()));
            importTemplateRepository.save(importTemplate);
            response = new DefaultResponse(SUCCESS,"Successfully Added Asset Import Template.","F200");
            LOGGER.info("Successfully Added Asset Import Template.");
        }catch (Exception e){
            response = new DefaultResponse(FAILURE,"An Error occurred while adding Asset template.","F500");
            LOGGER.info("An Error Occurred while adding Asset import template.",e);
            throw new ApplicationException("An Error Occurred while adding Asset import template.",e);
        }finally {
            LOGGER.info("Returning to controller of add Asset import template.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public byte[] stringToByteCompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        System.out.println("String length : " + str.length());
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return obj.toByteArray();
    }

    public String generateAssetImportTemplateNumber(Long id){
        String assetNumber = "7";
        String formatted = String.format("%06d", id);
        return assetNumber + formatted;
    }

    public GetPaginatedDataForSDTResponse getPaginatedAssetImportTemplatesForSDT(GetPaginatedDataForSDTRequest request) throws AccessDeniedException,ApplicationException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        GetPaginatedDataForSDTResponse response = new GetPaginatedDataForSDTResponse();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<Predicate> clauses = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get paginated Asset import template for sdt." + convertToJSON(request));
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Long.class);
            root = query.from(ImportTemplate.class);
            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));
            clauses.add(criteriaBuilder.equal(root.get("userUUID"),request.getUserUUID()));
            clauses.add(criteriaBuilder.equal(root.get("importType"),"CSV"));
            clauses.add(criteriaBuilder.equal(root.get("importedData"),"Asset"));

            clauses = addInspectionFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());

            response.getSdtData().put(TOTAL_ELEMENTS,(Long) entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

            List<ImportTemplate> importTemplates = (List<ImportTemplate>) entityManager.createQuery(query.select(root).where(clauses.toArray(new Predicate[]{}))
                    .orderBy(criteriaBuilder.desc(root.get(request.getSortField()))))
                    .setFirstResult(request.getLimit() * request.getOffset())
                    .setMaxResults(request.getLimit())
                    .getResultList();

            response.getSdtData().put(CONTENT,new ArrayList<>());
            for(ImportTemplate importTemplate: importTemplates){
                ((ArrayList) response.getSdtData().get(CONTENT)).add(new HashMap<>());

                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("uuid", importTemplate.getUuid());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("categoryUUID", importTemplate.getCategoryUUID());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("categoryName", importTemplate.getCategoryName());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("userUUID", importTemplate.getUserUUID());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("userName", importTemplate.getUserName());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("tenantUUID", importTemplate.getTenantUUID());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("csvColumnData", stringDecompress(importTemplate.getCsvColumnData()));
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("templateName", importTemplate.getTemplateName());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("templateNumber", importTemplate.getTemplateNumber());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("createdDate", importTemplate.getCreatedDate());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("importType", importTemplate.getImportType());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("importedData", importTemplate.getImportedData());

            }

            response.getSdtData().put(TOTAL_PAGES,((Long)response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);
            if((Long)response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                response.getSdtData().replace(TOTAL_PAGES,(Long)response.getSdtData().get(TOTAL_PAGES) - 1);
            response.setResponseIdentifier(SUCCESS);
            LOGGER.info("Page of Asset Import templates got successfully");
        }catch (Exception e){
            LOGGER.error("An Error Occurred while get paginated Asset import template for sdt.",e);
            throw new ApplicationException("An Error Occurred while get paginated Asset import template for sdt.",e);
        }finally {
            LOGGER.info("Returning from controller of get paginated Asset import templates for sdt.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public String stringDecompress(byte[] str) throws Exception {
        if (str == null ) {
            return null;
        }

        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
        return outStr;
    }

    public ImportTemplateListResponse getListOfImportTemplateByUserUUIDAndTenantUUID(String userUUID, String tenantUUID) throws ApplicationException,AccessDeniedException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        ImportTemplateListResponse response = new ImportTemplateListResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get list of import templates by user uuid: " + userUUID + " and tenantUUID: " + tenantUUID);
            List<ImportTemplateDTO> importTemplates = importTemplateRepository.findListByTenantUUIDAndUserUUID(tenantUUID,userUUID);
            for(ImportTemplateDTO importTemplate: importTemplates){
                importTemplate.setColumnData(stringDecompress(importTemplate.getCsvColumnData()));
            }
            response.setImportTemplates(importTemplates);
            response.setResponseIdentifier(SUCCESS);
        }catch (Exception e){
            response.setResponseIdentifier(FAILURE);
            LOGGER.error("An Error occurred while getting list of import template by user uuid and tenant uuid.",e);
            throw new ApplicationException("An Error occurred while getting list of import template by user uuid and tenant uuid.",e);
        }finally {
            LOGGER.info("Returning from controller of get list of import template by user uuid and tenant uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public  GetFileResponse downloadAssetImportTemplate(DownloadCSVTemplateRequest request) throws ApplicationException,AccessDeniedException {
        if(!privilegeHandler.hasCreate()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }

        Util util = new Util();
        GetFileResponse fileResponse = new GetFileResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of download Asset import template. Details: " + convertToJSON(request));
            String [] headers = request.getColumnsData().split(",");
            try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), CSVFormat.DEFAULT.withHeader(headers))) {
                printer.flush();
                byte [] file = stream.toByteArray();
                fileResponse.setResponseIdentifier(SUCCESS);
                fileResponse.setFileName(request.getTemplateName()+".csv");
                fileResponse.setContent(file);
                fileResponse.setContentLength(file.length);
            } catch (final IOException e) {
                LOGGER.error("An Error occurred while writing CSV.",e);
                throw new RuntimeException("Csv writing error: " + e.getMessage());
            }

        }catch (Exception e){
            fileResponse.setResponseIdentifier(FAILURE);
            LOGGER.error("An Error Occurred while downloading Asset import template.",e);
            throw  new ApplicationException("An Error Occurred while downloading Asset import template.",e);
        }finally {
            LOGGER.info("Returning to controller of download Asset import template.");
            util.clearThreadContextForLogging();
            util = null;
        }

        return fileResponse;
    }

    @Transactional
    public ImportBulkAssetResponse importBulkAssetsByCSV(MultipartFile file, ImportBulkAssetRequest request) throws ApplicationException,AccessDeniedException{
        if(!privilegeHandler.hasCreate()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        ImportBulkAssetResponse response = new ImportBulkAssetResponse();
        AssetImport assetImport = new AssetImport();
        List<Asset> assets = new ArrayList<>();
        List<Asset> updateAssets = new ArrayList<>();
        List<ImportRecord> importRecords = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        String accessAssetNumber = "";
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of import bulk Asset by CSV. Details: " + convertToJSON(request));
            //checking if file is right type(csv)
            if(!file.getContentType().equals("text/csv")){
                LOGGER.error("File Type is not compatible.");
                response.setResponseIdentifier(FAILURE);
                response.setDescription("Only CSV file Accepted.");
                return response;
            }
            //getting all category of an organinzation
            categories = categoryRepository.findByTenantUUID(request.getTenantUUID());
            //checking if organization have any categories
            if(categories != null && categories.size() <=0){
                LOGGER.error("No Category Exists for this organization.");
                response.setDescription("No Category Exists for this organization.");
                response.setResponseIdentifier(FAILURE);
                return response;
            }
            //reading csv file
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

                List<CSVRecord> csvRecords = csvParser.getRecords(); // getting records from file
                List<String> headers = csvParser.getHeaderNames().stream()
                        .map(String::toLowerCase).map(String::trim)
                        .collect(Collectors.toList()); // getting headers from file in lower case
                //setting up import entity
                assetImport.setImportName(file.getOriginalFilename() + " on " + new Date().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
                assetImport.setImportDate(new Date());
                assetImport.setUuid(UUID.randomUUID().toString());
                assetImport.setColumns(stringToByteCompress(StringUtils.join(csvParser.getHeaderNames(),",")));
                assetImport.setTenantUUID(request.getTenantUUID());
                assetImport.setUserName(request.getUserName());
                assetImport.setUserUUID(request.getUserUUID());
                assetImport.setStatus(IMPORT_SUCCESS);
                //checking if it's Add or update import
                if(request.getImportType().equals("Add")){
                    //checking if file contain all required fields
                    if(!headers.containsAll(ASSET_REQUIRED_FIELDS)){
                        LOGGER.error("Missing Required Fields Column.");
                        response.setDescription("Missing Required Fields Column.");
                        response.setResponseIdentifier(FAILURE);
                        return response;
                    }
                }else if(request.getImportType().equals("Update")){
                    //checking if file contain Asset Number for update/add when Asset Number is empty or not
                    if(!headers.contains("Asset Number".toLowerCase()) && !headers.contains("Asset #".toLowerCase())){
                        LOGGER.error("Missing Required Fields Asset Number/Asset #.");
                        response.setDescription("Missing Required Fields Asset Number/ Asset #.");
                        response.setResponseIdentifier(FAILURE);
                        return response;
                    }
                }
                boolean updateCheck = false;
                //checking if Asset Number in headers then extract and fetch Assets for update scenario
                if(headers.contains("Asset Number".toLowerCase()) || headers.contains("Asset #".toLowerCase())){
                    int index = headers.indexOf("Asset #".toLowerCase()); // getting Asset Number index
                    if(index == -1){
                        index = headers.indexOf("Asset Number".toLowerCase()); // checking if Asset Number column heading like (Asset #) and getting index
                        accessAssetNumber = "Asset Number";
                    }else{
                        accessAssetNumber = "Asset #";
                    }

                    //creating asset Number list for fetching assets
                    List<String> assetNumber = new ArrayList<>();
                    for(CSVRecord record:csvRecords){
                        assetNumber.add(record.get(index));
                    }
                    //fetching Assets
                    if(assetNumber.size() > 0){
                        updateAssets = assetRepository.findAssetsByAssetNumberIn(assetNumber);
                        updateCheck = true;
                    }
                }
                List<String> assetUUIDS = new ArrayList<>(); // asset uuids list for getting assets after save so we can generate their Asset Number
                //reading record one by one and generating Asset object and then adding in the Asset List initialize above
                for(CSVRecord record: csvRecords){
                    List<AssetField> assetFields = new ArrayList<>(); // asset fields list for saving Asset Fields for Assets
                    Asset asset = new Asset(); // Asset object for saving all fields and then will be added in the Asset list if all required condition are met
                    ImportRecord importRecord = new ImportRecord();// Import record for saving per record
                    asset.setUuid(UUID.randomUUID().toString());
                    importRecord.setUuid(UUID.randomUUID().toString());
                    Boolean success = true;
                    Category assetCategory = null;
                    //getting a record in Map so can read column by header name
                    for(Map.Entry<String,String> column: record.toMap().entrySet()){
                        //checking if column is Asset Number or not
                        // if it is asset number then we will update the asset info of that asset number
                        if(updateCheck && !record.get(accessAssetNumber).isEmpty()){
                            String finalAccessAssetNumber = accessAssetNumber;
                            Asset updateAsset = updateAssets.stream().filter(a -> a.getAssetNumber().toLowerCase().equals(record.get(finalAccessAssetNumber).toLowerCase().trim())).findFirst().orElse(null);
                            if(updateAsset != null){
                                int index = updateAssets.indexOf(updateAsset);
                                importRecord.setMessage("Successfully updated Asset: " + record.get(accessAssetNumber));
                                if(ASSET_INFO.contains(column.getKey().toLowerCase().trim())){
                                    if(column.getKey().toLowerCase().trim().equals("asset name".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setName(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("model #".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setModelNumber(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("manufacturer name/id".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setManufacture(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("purchased date".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        try {
                                            DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                                                    .parseCaseInsensitive()
                                                    .appendPattern("dd/MM/yyyy")
                                                    .toFormatter(Locale.ENGLISH);
                                            LocalDate localDate = LocalDate.parse(column.getValue(), dtf);
                                            ZoneId defaultZoneId = ZoneId.systemDefault();
                                            Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                            updateAssets.get(index).setPurchaseDate(date);
                                        } catch (DateTimeParseException dt) {
                                            LOGGER.error("An Error Occurred while parsing date.",dt);
                                        }
                                    }else if(column.getKey().toLowerCase().trim().equals("status".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setStatus(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("warranty".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setWarranty(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("primary usage unit".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setPrimaryUsageUnit(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("secondary usage unit".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setSecondaryUsageUnit(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("consumption unit".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setConsumptionUnit(column.getValue());
                                    }else if(column.getKey().toLowerCase().trim().equals("write asset description here".toLowerCase().trim()) && !column.getValue().isEmpty()){
                                        updateAssets.get(index).setDescription(column.getValue());
                                    }
                                }else{
                                    if(!ASSET_INFO.contains(column.getKey().toLowerCase().trim())){
                                        if(headers.contains("category name")){
                                            Category category = categories.stream().filter(c -> c.getName().toLowerCase().trim().equals(record.get("category name").toLowerCase().trim())).findFirst().orElse(null);
                                            if(category != null){
                                                Field field = category.getFieldTemplate().getFields().stream().filter(field1 -> field1.getLabel().toLowerCase().trim().equals(column.getKey().toLowerCase().trim())).findFirst().orElse(null);
                                                List<AssetField> assetFieldSet = new ArrayList<AssetField>(updateAssets.get(index).getAssetFields());
                                                if(field != null) {
                                                    AssetField assetField = assetFieldSet.stream().filter(a -> a.getFieldId().equals(field.getUuid())).findFirst().orElse(null);
                                                    if (assetField != null) {
                                                        int fieldIndex = assetFieldSet.indexOf(assetField);
                                                        assetFieldSet.get(fieldIndex).setFieldValue("{\"values\":[\"" + column.getValue() + "\"]}");
                                                    }
                                                    updateAssets.get(index).setAssetFields(new HashSet<>(assetFieldSet));
                                                }
                                            }
                                        }
                                    }
                                }
                                importRecord.setStatus(IMPORT_SUCCESS);
                            }else{
                                importRecord.setMessage("Invalid Asset Number: " + record.get(accessAssetNumber));
                                importRecord.setStatus(IMPORT_FAILURE);
                                break;
                            }
                        }else { // adding asset
                            if (ASSET_INFO.contains(column.getKey().toLowerCase().trim())) {
                                if (ASSET_REQUIRED_FIELDS.contains(column.getKey().toLowerCase().trim())) {
                                    if (column.getKey().toLowerCase().equals("category name")) {
                                        LOGGER.info("Checking Category Field validation.");
                                        if (!column.getValue().isEmpty()) {
                                            Category category = categories.stream().filter(c -> c.getName().toLowerCase().trim().equals(column.getValue().toLowerCase().trim())).findFirst().orElse(null);
                                            if (category == null) {
                                                importRecord.setMessage("Invalid Category For Asset: " + column.getValue());
                                                importRecord.setStatus(IMPORT_FAILURE);
                                                success = false;
                                                break;
                                            } else {
                                                asset.setCategoryUUID(category.getUuid());
                                                assetCategory = category;
                                            }
                                        } else {
                                            importRecord.setMessage("Required Field Category Name is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("asset name")) {
                                        LOGGER.info("Checking Asset name Field validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setName(column.getValue());
                                            importRecord.setAssetName(column.getValue());
                                            importRecord.setMessage("Successfully imported Asset: " + column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Asset Name is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("model #")) {
                                        LOGGER.info("Checking Model Number Field Validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setModelNumber(column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Model Number is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("manufacturer name/id")) {
                                        LOGGER.info("Checking Manufacturer Field Validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setManufacture(column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Manufacturer name/id is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("warranty")) {
                                        LOGGER.info("Checking Warranty Field Validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setWarranty(column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Warranty is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("primary usage unit")) {
                                        LOGGER.info("Checking Primary Usage Unit Field Validation .");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setPrimaryUsageUnit(column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Primary Usage Unit is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("secondary usage unit")) {
                                        LOGGER.info("Checking Secondary Usage Unit Field Validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setSecondaryUsageUnit(column.getKey());
                                        } else {
                                            importRecord.setMessage("Required Field Secondary Usage Unit is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    } else if (column.getKey().toLowerCase().trim().equals("consumption unit")) {
                                        LOGGER.info("Checking Consumption Unit Field Validation.");
                                        if (!column.getValue().isEmpty()) {
                                            asset.setConsumptionUnit(column.getValue());
                                        } else {
                                            importRecord.setMessage("Required Field Consumption Unit is Empty.");
                                            importRecord.setStatus(IMPORT_FAILURE);
                                            success = false;
                                            break;
                                        }
                                    }
                                } else {
                                    if (column.getKey().toLowerCase().trim().equals("status") && !column.getValue().isEmpty()) {
                                        asset.setStatus(ASSET_STATUSES.contains(column.getValue().toLowerCase()) ? column.getValue() : null);
                                    } else if (column.getKey().toLowerCase().trim().equals("purchased date") && !column.getValue().isEmpty()) {
                                        try {
                                            DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                                                    .parseCaseInsensitive()
                                                    .appendPattern("dd/MM/yyyy")
                                                    .toFormatter(Locale.ENGLISH);
                                            LocalDate localDate = LocalDate.parse(column.getValue(), dtf);
                                            ZoneId defaultZoneId = ZoneId.systemDefault();
                                            Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                            asset.setPurchaseDate(date);
                                        } catch (DateTimeParseException dt) {
                                            asset.setPurchaseDate(null);
                                        }
                                    } else if (column.getKey().toLowerCase().trim().contains("description") && !column.getValue().isEmpty()) {
                                        asset.setDescription(column.getValue());
                                    }
                                }
                            } else {
                                if (!ASSET_INFO.contains(column.getKey().toLowerCase().trim())  && request.getImportType().equals("Add")) {
                                    AssetField assetField = new AssetField();
                                    Category category = categories.stream().filter(c -> c.getName().toLowerCase().trim().equals(record.get("category name").toLowerCase().trim())).findFirst().orElse(null);
                                    if (category != null) {
                                        assetCategory = category;
                                        Field field = category.getFieldTemplate().getFields().stream().filter(field1 -> field1.getLabel().toLowerCase().trim().equals(column.getKey().toLowerCase().trim())).findFirst().orElse(null);
                                        if (field != null) {
                                            if (field.isMandatory() && !column.getValue().isEmpty()) {
                                                assetField.setAssetUUID(asset.getUuid());
                                                assetField.setFieldId(field.getUuid());
                                                assetField.setFieldTemplateId(field.getFieldTemplateUUID());
                                                assetField.setUuid(UUID.randomUUID().toString());
                                                assetField.setFieldValue("{\"values\":[\"" + column.getValue() + "\"]}");
                                                assetFields.add(assetField);
                                            } else if (!field.isMandatory()) {
                                                assetField.setAssetUUID(asset.getUuid());
                                                assetField.setFieldId(field.getUuid());
                                                assetField.setFieldTemplateId(field.getFieldTemplateUUID());
                                                assetField.setUuid(UUID.randomUUID().toString());
                                                assetField.setFieldValue("{\"values\":[\"" + column.getValue() + "\"]}");
                                                assetFields.add(assetField);
                                            } else {
                                                importRecord.setMessage("Empty required Additional Field: " + column.getKey());
                                                importRecord.setStatus(IMPORT_FAILURE);
                                                success = false;
                                                break;
                                            }
                                        }
                                    } else {
                                        importRecord.setMessage("Invalid Category For Asset: " + column.getValue());
                                        importRecord.setStatus(IMPORT_FAILURE);
                                        success = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //checking if Asset required fields are fulfilled then will add in the asset list
                    if(success && assetCategory != null) {
                        asset.setTenantUUID(request.getTenantUUID());
                        asset.setAssetFields(Sets.newHashSet(assetFields));
                        assets.add(asset);
                        assetUUIDS.add(asset.getUuid());
                        importRecord.setStatus(IMPORT_SUCCESS);
                        if (assetCategory != null) {
                            categories.get(categories.indexOf(assetCategory)).getAssets().add(asset);
                        }
                    }
                    //setting import record remaining data
                    importRecord.setRowNumber((int) record.getRecordNumber());
                    importRecord.setImportUUID(assetImport.getUuid());
                    importRecord.setData(stringToByteCompress(StringUtils.join(record.iterator(),",")));
                    importRecord.setImportedDate(assetImport.getImportDate());
                    importRecords.add(importRecord);
                }
                //cheking if there is any import record then will compute them for Saving per import info
                if(importRecords.size() > 0){
                    float totalRecords = importRecords.size() ;
                    float successRecords = assets.size() + updateAssets.size();
                    float calculatePercentage = (float) ((successRecords/totalRecords) * 100);
                    if(calculatePercentage == 100.00){
                        assetImport.setStatus(IMPORT_COMPLETE);
                    }else{
                        assetImport.setStatus(IMPORT_IN_COMPLETE);
                    }
                    String percentageComplete = (Math.round(calculatePercentage)) +"%";
                    assetImport.setPercentageComplete(percentageComplete);
                    assetImport.setMessage((int)successRecords + " of " + (int)totalRecords + " records imported Successfully.");
                }else{
                    assetImport.setPercentageComplete("0%");
                    assetImport.setMessage("0 of 0 records imported Successfully.");
                }
                //saving assets if any in the list
                if(assets != null && assets.size() > 0){
                    categoryRepository.save(categories);
                    Set<Asset> savedAssets = assetRepository.findAssetsByUuidIn(assetUUIDS);
                    for(Asset asset: savedAssets){
                        asset.setAssetNumber(this.genrateAssetNumber(asset.getId()));
                    }
                    assetRepository.save(savedAssets);
                }

                //saving updated Asset
                if(updateAssets != null && updateAssets.size() > 0){
                    assetRepository.save(updateAssets);
                }
                assetImportRepository.save(assetImport);
                if(importRecords != null && importRecords.size() > 0) {
                    importRecordRepository.save(importRecords);
                }
                response.setAssetImport(assetImport);
                response.setImportRecords(importRecords);
                response.setResponseIdentifier(SUCCESS);
                response.setDescription("Import Asset CSV Successfully.");
                LOGGER.info("Import Asset CSV Successfully.");
            }catch (Exception e){
                LOGGER.error("An Error occurred while reading csv file.",e);
                throw new ApplicationException("An Error occurred while reading csv file..",e);
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while importing bulk Asset by CSV.",e);
            throw new ApplicationException("An Error occurred while importing bulk Assets by CSV.",e);
        }finally {
            LOGGER.info("Returning to controller of import bulk Assets by csv.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public GetFileResponse downloadFailureImports(String importUUID) throws ApplicationException,AccessDeniedException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        GetFileResponse fileResponse = new GetFileResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of download failure imports by import uuid: " + importUUID);
            AssetImport assetImport = assetImportRepository.findByUuid(importUUID);
            String columns = stringDecompress(assetImport.getColumns());
            List<String> headers = new LinkedList<String>(Arrays.asList(columns.split(",")));
            headers.add(0,"Error");
            List<ImportRecord> importRecords = importRecordRepository.findImportRecordsByImportUUIDAndStatus(importUUID,FAILURE);
            try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
                for(ImportRecord importRecord:importRecords){
                    List<String> data = new LinkedList<String>(Arrays.asList(stringDecompress(importRecord.getData()).split(",")));
                    data.add(0,importRecord.getMessage());
                    printer.printRecord(data);
                }
                printer.flush();
                byte [] file = stream.toByteArray();
                fileResponse.setResponseIdentifier(SUCCESS);
                fileResponse.setFileName(assetImport.getImportName()+".csv");
                fileResponse.setContent(file);
                fileResponse.setContentLength(file.length);
            } catch (final IOException e) {
                LOGGER.error("An Error occurred while writing CSV.",e);
                throw new RuntimeException("Csv writing error: " + e.getMessage());
            }
        }catch (Exception e){
            LOGGER.error("An Error Occurred while downloading failure imports.",e);
            throw new ApplicationException("An Error Occurred while downloading failure imports.",e);
        }finally {
            LOGGER.info("Returning to controller of download failure imports.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return fileResponse;
    }

    public GetPaginatedDataForSDTResponse getPaginatedLastImports(GetPaginatedDataForSDTRequest request) throws AccessDeniedException,ApplicationException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        GetPaginatedDataForSDTResponse response = new GetPaginatedDataForSDTResponse();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<Predicate> clauses = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get paginated last Asset imports for sdt." + convertToJSON(request));
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Long.class);
            root = query.from(AssetImport.class);
            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));


            clauses = addInspectionFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());

            response.getSdtData().put(TOTAL_ELEMENTS,(Long) entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

            List<AssetImport> assetImports = (List<AssetImport>) entityManager.createQuery(query.select(root).where(clauses.toArray(new Predicate[]{}))
                            .orderBy(criteriaBuilder.desc(root.get(request.getSortField()))))
                    .setFirstResult(request.getLimit() * request.getOffset())
                    .setMaxResults(request.getLimit())
                    .getResultList();

            response.getSdtData().put(CONTENT,new ArrayList<>());
            for(AssetImport assetImport: assetImports){
                ((ArrayList) response.getSdtData().get(CONTENT)).add(new HashMap<>());

                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("uuid", assetImport.getUuid());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("importName", assetImport.getImportName());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("importDate", assetImport.getImportDate());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("percentageComplete", assetImport.getPercentageComplete());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("message", assetImport.getMessage());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("status", assetImport.getStatus());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("userUUID", assetImport.getUserUUID());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("userName", assetImport.getUserName());
                ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("tenantUUID", assetImport.getTenantUUID());
            }

            response.getSdtData().put(TOTAL_PAGES,((Long)response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);
            if((Long)response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                response.getSdtData().replace(TOTAL_PAGES,(Long)response.getSdtData().get(TOTAL_PAGES) - 1);
            response.setResponseIdentifier(SUCCESS);
            LOGGER.info("Page of Last Asset Imports got successfully");
        }catch (Exception e){
            LOGGER.error("An Error Occurred while get paginated last Asset imports for sdt.",e);
            throw new ApplicationException("An Error Occurred while get paginated last Asset imports for sdt.",e);
        }finally {
            LOGGER.info("Returning from controller of get paginated last Asset import for sdt.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public GetFileResponse exportAssetDetailInBulk(ExportAssetInBulkRequest request) throws AccessDeniedException,ApplicationException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        GetFileResponse response = new GetFileResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of export Asset detail in bulk. Detail: " +convertToJSON(request));
            Set<Asset> assets = assetRepository.findAssetsByUuidInOrderByIdDesc(request.getAssetUUIDS());
            List<Category> categories = categoryRepository.findByTenantUUID(request.getTenantUUID());
            List<String> headers = new LinkedList<String>(ASSET_INFO.stream().map(s -> WordUtils.capitalize(s)).collect(Collectors.toList()));
            headers.add(0,"Asset Number");
            List<String> fieldIds = assetFieldRepository.findFieldIdByAssetUUIDIn(request.getAssetUUIDS());
            List<Field> fields = fieldRepository.findFieldsByUuidIn(fieldIds);
            for(Field field:fields){
                if(!headers.contains(field.getLabel())){
                    headers.add(field.getLabel());
                }
            }
            try(final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))){
                for(Asset asset:assets){
                    List<String> records = new ArrayList<>();
                    Category category = categories.stream().filter(f -> f.getUuid().equals(asset.getCategoryUUID())).findFirst().orElse(null);
                    records.add(asset.getAssetNumber() != null ? asset.getAssetNumber() : "");
                    records.add(category != null ? category.getName() : "");
                    records.add(asset.getName() != null ? asset.getName() : "");
                    records.add(asset.getModelNumber() != null ? asset.getModelNumber() : "");
                    records.add(asset.getManufacture() != null ? asset.getManufacture() : "");
                    records.add(asset.getPurchaseDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(asset.getPurchaseDate()):"");
                    records.add(asset.getStatus() != null ? asset.getStatus() : "");
                    records.add("");
                    records.add(asset.getWarranty() != null ? asset.getWarranty() : "");
                    records.add(asset.getPrimaryUsageUnit() != null ? asset.getPrimaryUsageUnit() : "");
                    records.add(asset.getSecondaryUsageUnit() != null ? asset.getSecondaryUsageUnit() : "");
                    records.add(asset.getConsumptionUnit() != null ? asset.getConsumptionUnit() : "");
                    records.add(asset.getDescription() != null ? asset.getDescription(): "");
                    int startIndex = records.size();
                    for(AssetField assetField:asset.getAssetFields()){
                        String fieldName = fields.stream().filter(f -> f.getUuid().equals(assetField.getFieldId())).map(Field::getLabel).findFirst().orElse(null);
                        if(fieldName != null){
                            int indexInHeaders = 0;
                            indexInHeaders = headers.indexOf(fieldName);
                            try{
                                records.set(indexInHeaders,this.parseAssetFieldValue(assetField.getFieldValue()));
                            }catch (IndexOutOfBoundsException ie){
                                for(int i = records.size() - 1; i < indexInHeaders; i++){
                                    records.add("");
                                }
                                records.set(indexInHeaders,this.parseAssetFieldValue(assetField.getFieldValue()));
                            }
                        }
                    }
                    printer.printRecord(records);
                }
                printer.flush();
                byte [] file = stream.toByteArray();
                response.setResponseIdentifier(SUCCESS);
                response.setFileName("Asset Export - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())+".csv");
                response.setContent(file);
                response.setContentLength(file.length);
            }catch (Exception e){
                LOGGER.error("An Error occurred while writing CSV.",e);
                throw new RuntimeException("Csv writing error: " + e.getMessage());
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while exporting Asset detail in bulk",e);
            throw new ApplicationException("An Error occurred while exporting Asset detail in bulk",e);
        }finally {
            LOGGER.info("Returning to controller of export Asset detail in bulk.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    private String parseAssetFieldValue(String assetValue) throws ApplicationException{
        String value = null;
        try{
            LOGGER.info("Inside function of parse Asset field value. Detail: " + assetValue);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(assetValue);
            JSONArray jsonArray = (JSONArray) jsonObject.get("values");
            value = jsonArray.get(0) != null ? jsonArray.get(0).toString() : null;
        }catch (Exception e){
            LOGGER.error("An Error occurred while parsing Asset field value",e);
            throw new ApplicationException("An Error occurred while parsing Asset field value",e);
        }
        return value != null ? value : "";
    }

    /*******************************************Import Template Functions*************************************/

    /******************************************* Asset Functions ************************************************/

    //add asset (AMS_UC_10)
    /*
    This function adds a new asset
    Category uuid and asset object is passed to this function
    First, we get the category with that uuid and set it as a parent of asset
    The, uuid is assigned to asset and it's children. Also, parent of children is set so that children are saved automatically when parent is saved
     */
//    @HasCreate
    DefaultResponse addAsset(AddAssetRequest addAssetRequest/*, OAuth2Authentication oAuth2Authentication*/) throws IOException,AccessDeniedException {

        if(!privilegeHandler.hasCreate())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse response = null;
        Category category = null;
        Asset savedAsset = null;
        ActivityWall activityWall = null;
        AssetField assetFieldForInventoryAsset = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method to add Asset,details: request: "+convertToJSON(addAssetRequest));

            Map<String,Object> oAuth2Authentication = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));

            //check if article number is exist in db
            String domain = (String)((Map<String,Object>)oAuth2Authentication).get("domain");

            //get category from db

            if (addAssetRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(addAssetRequest.getCategoryId());
                addAssetRequest.getAsset().setCategoryUUID(category.getUuid());
                category.addAsset(addAssetRequest.getAsset());
            } else {
                throw new Exception("Category has not been provided.");
            }
            //setting uuid
            addAssetRequest.getAsset().setUuid(UUID.randomUUID().toString());

            //setting parent of children and also setting uuid
            for (AssetField assetField : addAssetRequest.getAsset().getAssetFields()) {
                if(category.getName().contains("Linen")){
                    Field field = fieldRepository.findByUuid(assetField.getFieldId());
                    if(field.getLabel().equals("# of Washes")){
                        assetFieldForInventoryAsset = assetField;
                    }else{
                        assetField.setAssetUUID(addAssetRequest.getAsset().getUuid());
                        assetField.setUuid(UUID.randomUUID().toString());
                    }
                }else {
                    assetField.setAssetUUID(addAssetRequest.getAsset().getUuid());
                    assetField.setUuid(UUID.randomUUID().toString());
                }
            }
            if(assetFieldForInventoryAsset != null){
                addAssetRequest.getAsset().getAssetFields().remove(assetFieldForInventoryAsset);
            }
            for (AssetImage assetImage : addAssetRequest.getAsset().getAssetImages()) {
                assetImage.setAssetUUID(addAssetRequest.getAsset().getUuid());
            }
            for (Attachment attachment : addAssetRequest.getAsset().getAttachments()) {
                attachment.setAssetUUID(addAssetRequest.getAsset().getUuid());
            }
            //creating activity wall for that asset and also setting uuid
            activityWall = new ActivityWall();
            activityWall.setUuid(UUID.randomUUID().toString());
            activityWall.setAssetUuid(addAssetRequest.getAsset().getUuid());
            activityWall.setCreatedAt(new Date());
//            addAssetRequest.getAsset().setActivityWall(activityWall);
            //saving in db
            categoryRepository.save(category);

            //set asset number
            savedAsset = assetRepository.findAssetByUuid(addAssetRequest.getAsset().getUuid());
            savedAsset.setAssetNumber(this.genrateAssetNumber(savedAsset.getId()));
            assetRepository.save(savedAsset);
            activityWallRepository.save(activityWall);
            //saving Asset info in Asset Cooked Table for SDT.
            AssetMapper assetMapper = new AssetMapper(savedAsset.getUuid(),savedAsset.getAssetNumber(),savedAsset.getName(),
                    category.getName(),savedAsset.getStatus(),0,null,savedAsset.getTenantUUID());
            assetMapperRepository.save(assetMapper);

            LOGGER.info("Asset Added Successfully");
            response = new DefaultResponse("Success", "Asset Added Successfully", "200", addAssetRequest.getAsset().getUuid());
        } catch (Exception e) {

            LOGGER.error("Error while adding asset, details: request: "+convertToJSON(addAssetRequest), e);
            response = new DefaultResponse("Failure", "Error in adding asset. Error: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returned to controller of adding Asset");
            util.clearThreadContextForLogging();
            util = null;
            savedAsset = null;
            activityWall = null;
            addAssetRequest = null;
        }

        return response;
    }

    //edit asset AMS_UC_11
    /*
    This function edits an asset
    It works same as add asset
    category uuid and asset object is passed to this function
    First, we find the category with that uuid and set it as the parent of asset
    Then, parent of the children is set so that children are saved automatically
    You will notice an extra if in the code which will only be executed if request is sent through Test Library.
    This is because in Test Library, we don't know the id of created object, we only know it's uuid as uuid is returned with default response
    when a object is created therefore, this additional code sets the id of the object. If we don't set the id of object then the old asset
    won't be updated instead a new one will be created
     */
//    @HasUpdate
    EditAssetResponse editAsset(EditAssetRequest editAssetRequest) throws AccessDeniedException {

        if(!privilegeHandler.hasUpdate())
            throw new AccessDeniedException();

        Util util = new Util();
        Category category = null;
        EditAssetResponse response = null;
        Asset asset = null;
        AssetMapper assetMapper = null;
        Long countDiff;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside Service function of edit asset, details: request: "+convertToJSON(editAssetRequest));

            asset = assetRepository.findByUuid(editAssetRequest.getAsset().getUuid());
            assetMapper = assetMapperRepository.findByUuid(editAssetRequest.getAsset().getUuid());
            if(editAssetRequest.getAsset().getName() != null)
                if(!editAssetRequest.getAsset().getName().isEmpty()) {
                    asset.setName(editAssetRequest.getAsset().getName());
                    assetMapper.setName(editAssetRequest.getAsset().getName());
                }
            if(editAssetRequest.getAsset().getModelNumber() != null)
                if(!editAssetRequest.getAsset().getModelNumber().isEmpty())
                    asset.setModelNumber(editAssetRequest.getAsset().getModelNumber());

            if(editAssetRequest.getAsset().getManufacture() != null)
                if(!editAssetRequest.getAsset().getManufacture().isEmpty())
                    asset.setManufacture(editAssetRequest.getAsset().getManufacture());

            if(editAssetRequest.getAsset().getPurchaseDate() != null )
                asset.setPurchaseDate(editAssetRequest.getAsset().getPurchaseDate());

            if(editAssetRequest.getAsset().getExpiryDate() != null)
                asset.setExpiryDate(editAssetRequest.getAsset().getExpiryDate());

            if(editAssetRequest.getAsset().getWarranty() != null)
                if(!editAssetRequest.getAsset().getWarranty().isEmpty())
                    asset.setWarranty(editAssetRequest.getAsset().getWarranty());

            if(editAssetRequest.getAsset().getDescription() != null)
                if(!editAssetRequest.getAsset().getDescription().isEmpty())
                    asset.setDescription(editAssetRequest.getAsset().getDescription());

            if(editAssetRequest.getAsset().getPrimaryUsageUnit() != null)
                if(!editAssetRequest.getAsset().getPrimaryUsageUnit().isEmpty())
                    asset.setPrimaryUsageUnit(editAssetRequest.getAsset().getPrimaryUsageUnit());

            if(editAssetRequest.getAsset().getSecondaryUsageUnit() != null)
                if(!editAssetRequest.getAsset().getSecondaryUsageUnit().isEmpty())
                    asset.setSecondaryUsageUnit(editAssetRequest.getAsset().getSecondaryUsageUnit());

            if(editAssetRequest.getAsset().getConsumptionUnit() != null)
                if(!editAssetRequest.getAsset().getConsumptionUnit().isEmpty())
                    asset.setConsumptionUnit(editAssetRequest.getAsset().getConsumptionUnit());

            if(editAssetRequest.getAsset().getAssetImages() != null)
                if(!editAssetRequest.getAsset().getAssetImages().isEmpty())
                    asset.setAssetImages(editAssetRequest.getAsset().getAssetImages());

            if(editAssetRequest.getAsset().getStatus() != null)
                if(!editAssetRequest.getAsset().getStatus().isEmpty()){
                    asset.setStatus(editAssetRequest.getAsset().getStatus());
                    assetMapper.setStatus(editAssetRequest.getAsset().getStatus());
                }

            if(editAssetRequest.getAsset().getAssetFields() != null && editAssetRequest.getAsset().getAssetFields().size() > 0){
                asset.setAssetFields(editAssetRequest.getAsset().getAssetFields());
            }

            //saving in db
            assetRepository.save(asset);
            assetMapperRepository.save(assetMapper);
            response = new EditAssetResponse();
            response.setResponseIdentifier("Success");
            response.setAsset(editAssetRequest.getAsset());
            LOGGER.info("Asset Edited Successfully");

        } catch (Exception e) {
            response = new EditAssetResponse();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing asset from db. Asset UUID: " + editAssetRequest.getAsset().getUuid(), e);
            e = null;
        }finally{
            LOGGER.info("Returning to controller of edit Asset");
            util.clearThreadContextForLogging();
            util = null;
            category = null;
            editAssetRequest = null;
            asset = null;
        }

        return response;
    }

    //delete asset AMS_UC_12
    /*
    This function deletes an asset
    asset uuid is passed to this function
    First, we get the asset with that uuid
    then, we set it's parent to null and save it because we don't want to delete parent alongwith the children
    then, asset is deleted by id
     */
//    @HasDelete
    DefaultResponse deleteAsset(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasDelete())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse response = null;

        try {
            LOGGER.info("Inside Service function of deleting asset of uuid: "+uuid);
//            Asset asset = assetRepository.findAssetByUuid(id);
//            //setting parent of field template to null to not delete the parent alongwith the children
//            asset.setCategory(null);
//            assetRepository.save(asset);
            //deleting
            //delete wall
            activityWallRepository.deleteActivityWallByAssetUuid(uuid);
            assetRepository.deleteByUuid(uuid);

            LOGGER.info("Asset deleted Successfully");
            response = new DefaultResponse("Success", "Asset deleted Successfully", "200");
        } catch (Exception e) {

            LOGGER.error("Error while deleting asset of uuid"+uuid, e);
            response = new DefaultResponse("Failure", "Error in deleting asset: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of delete Asset");
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }

    // delete single image from assets.....qasim
    public DefaultResponse deleteAssetImages(Long id){
        Util util = new Util();
        DefaultResponse response = null;
        AssetImage assetImages = null;
        Asset asset = null;
        try {
            LOGGER.info("Inside Service function of deleting asset image of uuid: "+id);
            assetImages = assetImageRepository.findAllById(id);
            assetImageRepository.delete(id);
            LOGGER.info("Asset deleted Successfully");
            response = new DefaultResponse("Success", "Asset Image deleted Successfully", "200");
        } catch (Exception e) {
            LOGGER.error("Error while deleting asset of uuid"+id, e);
            response = new DefaultResponse("Failure", "Error in deleting asset: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of delete Asset");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    // delete Attachments from assets.....qasim
    public DefaultResponse deleteAssetAttachment(Long id){
        Util util = new Util();
        DefaultResponse response = null;
        Attachment attachment = null;
        Asset asset = null;
        try {
            LOGGER.info("Inside Service function of deleting asset image of uuid: "+id);
            attachment = attachmentRepository.findById(id);
            attachmentRepository.delete(id);
            LOGGER.info("Asset Attachment deleted Successfully");
            response = new DefaultResponse("Success", "Asset Attachment deleted Successfully", "200");
        } catch (Exception e) {
            LOGGER.error("Error while deleting asset of uuid"+id, e);
            response = new DefaultResponse("Failure", "Error in deleting asset Attachment: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of delete Asset Attachment");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    //archive or delete asset by UUID.....
    public DefaultResponse archiveOrDeleteAssetByUuid(String uuid, String type) throws ApplicationException{
        Util util = new Util();
        DefaultResponse response = null;
        Asset asset = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        AssetMapper assetMapper = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of archive or delete Asset by uuid: " + uuid);
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Asset.class);
            root = query.from(Asset.class);

            asset = (Asset) entityManager.createQuery(query.select(root).where(criteriaBuilder.equal(root.get("uuid"),uuid))).getSingleResult();
            assetMapper = assetMapperRepository.findByUuid(uuid);
            // getting category to remove asset from it
            Category category = categoryRepository.findCategoryByUuid(asset.getCategoryUUID());
            Asset finalAsset = asset;
            //removing asset
            category.getAssets().removeIf(asset1 -> (asset1.getUuid().equals(finalAsset.getUuid())));
            //getting assets group by assets
            List<Asset> assets = new ArrayList<>();
            assets.add(asset);
            List<AssetGroup> assetGroups = assetGroupRepository.findAssetGroupByAssetsIn(assets);
            String groupUUIDs = "";
            //removing assets from assets group and making comma separated groupUUIDs string to save groups uuid from which it get deleted
            for(AssetGroup assetGroup:assetGroups){
                for(Iterator<Asset> iterator = assetGroup.getAssets().iterator(); iterator.hasNext();){
                    Asset asset1 = iterator.next();
                    if(asset1.getUuid().equals(asset.getUuid())){
                        iterator.remove();
                        if(groupUUIDs.equals("")){
                            groupUUIDs = groupUUIDs + (assetGroup.getUuid() != null ? assetGroup.getUuid() : assetGroup.getDeletefromGroupUUID());
                        }else{
                            groupUUIDs = groupUUIDs + "," + (assetGroup.getUuid() != null ? assetGroup.getUuid() : assetGroup.getDeletefromGroupUUID());
                        }
                    }
                }
            }
            if(type.equals("archive")){
                asset.setArchive(1);
                assetMapper.setArchive(true);
            }
            asset.setRemoveFromCategoryUUID(category.getUuid()); // setting remove category uuid unarchive
            asset.setRemoveFromGroupUUID(groupUUIDs);// setting remove groups uuid's for unarchive
            assetMapper.setRemoveFromCategoryUUID(category.getUuid());
            categoryRepository.save(category);
            assetGroupRepository.save(assetGroups);
            assetRepository.save(asset);
            assetMapperRepository.save(assetMapper);
            category = null;
            assets = null;
            assetGroups = null;
            response = new DefaultResponse("Success","Successfully " + (type.equals("archive") ? "archive" : "delete") + " Asset.","F200");
            LOGGER.info("Successfully " + (type.equals("archive") ? "archive" : "delete") + " Asset.");
        }catch (Exception e){
            throw new ApplicationException("An Error occurred while archive or delete Asset by uuid",e);
        }finally {
            LOGGER.info("Returning to controller.");
            util.clearThreadContextForLogging();
            util = null;
            asset = null;
            criteriaBuilder = null;
            query = null;
            root = null;
        }
        return response;
    }

    //get asset AMS_UC_13
    /*
    This function gets an asset by uuid
    uuid of asset is passed to this function
    We get the asset with that uuid from db and return it
     */
//    @HasRead
    GetAssetResponse getAsset(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        util.setThreadContextForLogging(scim2Util);
        Category category = null;
        String status = null;
        GetAssetResponse response = new GetAssetResponse();
        Usage usagecombined = new Usage();
       List <Usage> usageObj = new ArrayList<>();

        try {
            LOGGER.info("Inside Service function of get asset of uuid: "+uuid);
            //find asset with that uuid
            response.setAsset(new AssetResponse());

            response.getAsset().setAsset(assetRepository.findAssetByUuid(uuid));

            status = assetRepository.getAssetStatusByUUID(response.getAsset().getUuid());

            response.getAsset().setStatus(status);

            response.getAsset().setAssetImages(assetImageRepository.findAllByAssetUUID(uuid));

            response.getAsset().setActivityWall(activityWallRepository.findActivityWallByAssetUuid(response.getAsset().getUuid()));
            if (response.getAsset().getActivityWall() == null) {
                response.getAsset().setActivityWall(new ActivityWall());
                response.getAsset().getActivityWall().setCreatedAt(new Date());
                response.getAsset().getActivityWall().setUuid(UUID.randomUUID().toString());
                response.getAsset().getActivityWall().setAssetUuid(response.getAsset().getUuid());
                activityWallRepository.save(response.getAsset().getActivityWall());
            }
             usageObj = usageRepository.findUsageByAssetUUIDAndMaxPrimaryUsageValue(response.getAsset().getUuid());
            LOGGER.info("Primary Meter Usage Value"+convertToJSON(usageObj));
            if(usageObj != null && usageObj.size() > 0){
                int count = usageObj.size();
                response.setPrimaryUsageValue(usageObj.get(count-1));
                usagecombined.setPrimaryUsageValue(usageObj.get(count-1).getPrimaryUsageValue());
            }
            usageObj = usageRepository.findUsageByAssetUUIDAndMaxSecondaryUsageValue(response.getAsset().getUuid());
            LOGGER.info("Secondary Meter Usage Value"+convertToJSON(usageObj));
            if(usageObj != null && usageObj.size() > 0){
                int count = usageObj.size();
                response.setSecondaryUsageValue(usageObj.get(count-1));
                usagecombined.setSecondaryUsageValue(usageObj.get(count-1).getSecondaryUsageValue());
            }
            if((usagecombined != null)  ){
                response.getAsset().setLastUsage(usagecombined);
            }
            category = categoryRepository.findByAssetsUuid(response.getAsset().getUuid());
            response.setCategoryId(response.getAsset().getCategoryUUID());
            //set field template of asset
            response.setFieldTemplate(new FieldTemplateResponse());
            if (category.getFieldTemplate() != null) {
                response.getFieldTemplate().setFieldTemplate(category.getFieldTemplate());
                Collections.sort(response.getFieldTemplate().getFields());
            }
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Asset From database. Sending it to controller");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting asset from db. Asset UUID: " + uuid, e);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            category = null;
        }

        return response;
    }

    // Get Asset with provided Asset Detail
    /* The function retrieves selective Asset Detail
     * @Parmm - Asset Uuid
     * @Param - Requested Asset Detail
     */
//    @HasRead
    GetAssetDetailResponse getAssetDetail(AssetDetailRequest assetDetailRequest, String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        Attachment attachment = null;
        GetFileResponse getFileResponse = null;
        GetAssetDetailResponse response = new GetAssetDetailResponse();
        response.setAssetDetail(new AssetDetailResponse());

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method to get Asset Detail, details: request: "+convertToJSON(assetDetailRequest)+", uuid: "+uuid);

            //Retrieve Basic Detail of Asset
             response.getAssetDetail().setAssetDetail(assetRepository.getBasicAssetDetailByUuid(uuid));

            // Additions as requested by AssetDetail
            if (assetDetailRequest.isActivityWall()) {
                response.getAssetDetail().setActivityWall(activityWallRepository.findActivityWallByAssetUuid(uuid));
                if (response.getAssetDetail().getActivityWall() == null) {
                    response.getAssetDetail().setActivityWall(new ActivityWall());
                    response.getAssetDetail().getActivityWall().setCreatedAt(new Date());
                    response.getAssetDetail().getActivityWall().setUuid(UUID.randomUUID().toString());
                    response.getAssetDetail().getActivityWall().setAssetUuid(uuid);
                    activityWallRepository.save(response.getAssetDetail().getActivityWall());
                }     }


            if (assetDetailRequest.isAssetFields())
                response.getAssetDetail().setAssetField(assetFieldRepository.findAllByAssetUUID(uuid));

            if (assetDetailRequest.isAssetImages())
                response.getAssetDetail().setAssetImage(assetImageRepository.findAllByAssetUUID(uuid));

//            if(response.getAssetDetail().getAssetImage()!=null){
//                response.getAssetDetail().getAssetImage().stream().forEach((image) -> {
//                    GetFileResponse getFileResponse = null;
//                    getFileResponse = getFile(image.getImageUrl());
//                    image.setContent(getFileResponse.getContent());
//                });
//            }

            if (assetDetailRequest.isAttachments())
                response.getAssetDetail().setAttachment(attachmentRepository.findAllByAssetUUID(uuid));

            if (assetDetailRequest.isConsumptions())
                response.getAssetDetail().setConsumption(consumptionRepository.findByAssetUUID(uuid));

            if (assetDetailRequest.isUsages())
                response.getAssetDetail().setUsage(usageRepository.findByAssetUUIDOrderByIdDesc(uuid));

            if (assetDetailRequest.isCategory())
                response.getAssetDetail().setCategory(categoryRepository.findByAssetsUuid(uuid));

            // set last usage required for meter reading, to get last usage
            if(assetDetailRequest.isUsages()){
                if(response.getAssetDetail().getUsage().size()>0){

                    for (Iterator<Usage> it = response.getAssetDetail().getUsage().iterator(); it.hasNext(); ) {
                        Usage usage = it.next();
                        if(usage.getPrimaryUsageValue()!=null){
                            response.getAssetDetail().setLastUsage(usage);
                            usage = null;
                            break;
                        }
                    }
                }
            }

            response.setResponseIdentifier("Success");
            LOGGER.info("Received Asset Detail From database. Sending it to controller");
        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting asset detail from db. Asset UUID: " + uuid, e);
            e = null;
        } finally {
            LOGGER.info("Returning to controller of get Asset Detail");
            util.clearThreadContextForLogging();
            util = null;
            assetDetailRequest = null;
        }

        return response;
    }

    //get assets AMS_UC_14
    /*
    This function gets all assets
    It simply fetches all assets from db and return it
     */
//    @HasRead
    GetAssetsResponse getAssets(String tenantUUID) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();

        GetAssetsResponse response = new GetAssetsResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside Service function of get assets by tenantUUID "+tenantUUID);

            response.setAssets(assetRepository.getAssetNameAndUUIDByTenantUUID(tenantUUID));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets From database. Sending it to controller");

        } catch (Exception e) {

            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db by tenantUUID: "+tenantUUID, e);
            e = null;
        }finally{
            LOGGER.info("Returning to controller of get Asses by tenantUUID: "+tenantUUID);
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }

    public AssetsNameAndUUIDResponse getAssetsAndAssetGroupsNameAndUUIDByTenantUUID(String tenantUUID, String accessKey, String assetUUID) throws AccessDeniedException,ApplicationException{
        if(!accessKey.equals(Constant.SECRET_KEY)){
            LOGGER.info("Access is Denied.");
            throw new AccessDeniedException();
        }
        AssetsNameAndUUIDResponse response = null;
        String assetCategory = null;
        try{
            LOGGER.info("Inside service function of get Assets and Asset groups name and uuid by tenant uuid: " + tenantUUID);
            if(assetUUID != null){
                assetCategory = assetRepository.findCategoryNameByAssetUUID(assetUUID);
            }
            response = new AssetsNameAndUUIDResponse(assetRepository.findAssetByTenantUUIDAndRemoveFromCategoryUUIDIsNull(tenantUUID),
                        assetGroupRepository.findAssetGroupByTenantUUIDAndDeletefromGroupUUIDIsNull(tenantUUID),assetCategory,SUCCESS);
            LOGGER.info("Successfully got Assets and Asset groups name and uuid.");
        }catch (Exception e){
            LOGGER.error("An Error occurred while getting Assets and Asset groups name and uuid by tenant uuid.",e);
            throw new ApplicationException("An Error occurred while getting Assets and Asset groups name and uuid by tenant uuid.",e);
        }finally {
            LOGGER.info("Returning to controller of get Assets and Asset groups name and uuid by tenant uuid.");
        }
        return response;
    }

    public AssetGroupByAssetResponse getAssetGroupsByAssets (AssetGroupByAssetUUIDsRequest request) throws AccessDeniedException,ApplicationException{
        if(!privilegeHandler.hasRead()){
            throw new AccessDeniedException();
        }
        Util util = new Util();
        AssetGroupByAssetResponse response =new AssetGroupByAssetResponse();
        List<AssetGroupDTO> assetGroupDTOList = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            if(request.getAssetUUIDs().size()>0){
                assetGroupDTOList=assetGroupRepository.findAssetGroupByAssetsInAndDeletefromGroupUUIDIsNull(request.getAssetUUIDs());
                response.setAssetGroupDTOS(assetGroupDTOList);
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid.");
            }
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset groups name and uuid by Assets.",e);
            throw new ApplicationException("An Error occurred while getting Assets groups name and uuid by Assets..",e);
        }finally {
            LOGGER.info("Returning to controller of get Asset groups name and uuid by Assets..");
        }
        return response;

    }
    public AssetAndAssetGroupResponse getAssetAndAssetGroup (AssetAndAssetGroupRequest request) throws AccessDeniedException,ApplicationException{
        if(!request.getAccessKey().equals(Constant.SECRET_KEY)){
            LOGGER.info("Access is Denied.");
            throw new AccessDeniedException();
        }
        AssetAndAssetGroupResponse response =new AssetAndAssetGroupResponse();
        try{
            LOGGER.info("Inside service function of get Asset and Asset Group.");
            if(request.getAssetUUIDs().size()>0){

                response.setAssetDTOS(assetRepository.findAssetByUuidAndRemoveFromCategoryUUIDIsNull(request.getAssetUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));
            }
            if(request.getAssetGroupUUIDs().size()>0){
                response.setAssetGroupDTOS(assetGroupRepository.findAssetGroupByUuidInAndDeletefromGroupUUIDIsNull(request.getAssetGroupUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));

            }
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset and Asset groups name and uuid by Assets.",e);
            throw new ApplicationException("An Error occurred while getting Asset and Assets groups name and uuid by Assets..",e);
        }finally {
            LOGGER.info("Returning to controller of get Asset and Asset groups name and uuid by Assets..");
        }
        return response;
    }

    public AssetAndAssetGroupResponse getAssetAndAssetGroupUseruuid (AssetAndAssetGroupRequest request) throws AccessDeniedException,ApplicationException{
        if(!request.getAccessKey().equals(Constant.SECRET_KEY)){
            LOGGER.info("Access is Denied.");
            throw new AccessDeniedException();
        }
        AssetAndAssetGroupResponse response =new AssetAndAssetGroupResponse();
        try{

            LOGGER.info("Inside service function of get Asset and Asset Group.");
            if(request.getAssetUUIDs().size()>0){

                response.setAssetDTOS(assetRepository.findAssetByUuidAndRemoveFromCategoryUUIDIsNull(request.getAssetUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));
            }
            if(request.getAssetGroupUUIDs().size()>0){
                response.setAssetGroupDTOS(assetGroupRepository.findAssetGroupByUuidInAndDeletefromGroupUUIDIsNull(request.getAssetGroupUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));

            }
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset and Asset groups name and uuid by Assets.",e);
            throw new ApplicationException("An Error occurred while getting Asset and Assets groups name and uuid by Assets..",e);
        }finally {
            LOGGER.info("Returning to controller of get Asset and Asset groups name and uuid by Assets..");
        }
        return response;
    }

    public AssetAndAssetGroupResponse getAssetAndAssetGroupTenantuuid (AssetAndAssetGroupRequest request) throws AccessDeniedException,ApplicationException{
        if(!request.getAccessKey().equals(Constant.SECRET_KEY)){
            LOGGER.info("Access is Denied.");
            throw new AccessDeniedException();
        }
        AssetAndAssetGroupResponse response =new AssetAndAssetGroupResponse();
        try{
            LOGGER.info("Inside service function of get Asset and Asset Group.");
            if(request.getAssetUUIDs().size()>0){

                response.setAssetDTOS(assetRepository.findAssetByUuidAndRemoveFromCategoryUUIDIsNull(request.getAssetUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));
            }
            if(request.getAssetGroupUUIDs().size()>0){
                response.setAssetGroupDTOS(assetGroupRepository.findAssetGroupByUuidInAndDeletefromGroupUUIDIsNull(request.getAssetGroupUUIDs()));
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Assets and Asset groups name and uuid."+convertToJSON(response));

            }
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset and Asset groups name and uuid by Assets.",e);
            throw new ApplicationException("An Error occurred while getting Asset and Assets groups name and uuid by Assets..",e);
        }finally {
            LOGGER.info("Returning to controller of get Asset and Asset groups name and uuid by Assets..");
        }
        return response;
    }
    //get asset basic detail by tenant AMS_UC_31
//    @HasRead
    GetBasicAssetDetailByTenantResponse getBasicAssetDetailByTenant(String tenantUUID) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetBasicAssetDetailByTenantResponse response = new GetBasicAssetDetailByTenantResponse();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<GetNameAndTypeOfAssetResponse> query = null;
        Root<Asset> asset = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting basic details of asset by tenant. Tenant UUID: " + tenantUUID);

//            JdbcTemplate jt;
//            jt = new JdbcTemplate(this.dataSource());
//
//            //String sql = "select a.id as id,a.uuid as uuid,a.asset_number as asset_number,a.name as asset_name,a.primary_usage_unit as primary_usage_unit,a.secondary_usage_unit as secondary_usage_unit,a.consumption_unit as consumption_unit,a.consumption_points as consumption_points,c.uuid as category_uuid " +
//                    "from t_asset a inner join t_category c on a.category_id=c.id " +
//                    "where a.tenantuuid=?";
//            List<Map<String, Object>> assetsResponse = jt.queryForList(sql, tenantUUID);
//            List<GetNameAndTypeOfAssetResponse> assets = new ArrayList<>();
//            for (Map<String, Object> assetResponse : assetsResponse) {
//                GetNameAndTypeOfAssetResponse asset = new GetNameAndTypeOfAssetResponse();
//                asset.setName(String.valueOf(assetResponse.get("asset_name")));
//                asset.setCategoryUUID(String.valueOf(assetResponse.get("category_uuid")));
//                asset.setUuid(String.valueOf(assetResponse.get("uuid")));
//                asset.setAssetNumber(String.valueOf(assetResponse.get("asset_number")));
//                asset.setConsumptionUnit(String.valueOf(assetResponse.get("consumption_unit")));
//                asset.setPrimaryUsageUnit(String.valueOf(assetResponse.get("primary_usage_unit")));
//                asset.setSecondaryUsageUnit(String.valueOf(assetResponse.get("secondary_usage_unit")));
//                asset.setConsumptionPoints((int) assetResponse.get("consumption_points"));
//                assets.add(asset);
//            }


            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(GetNameAndTypeOfAssetResponse.class);
            asset = query.from(Asset.class);

            response.setAssets(entityManager.createQuery(query.select(criteriaBuilder.construct(GetNameAndTypeOfAssetResponse.class,asset.get("name"),asset.get("categoryUUID"),asset.get("assetNumber"),asset.get("uuid"),asset.get("primaryUsageUnit"),asset.get("secondaryUsageUnit"),asset.get("consumptionUnit"),asset.get("consumptionPoints"),asset.get("modelNumber"))).where(criteriaBuilder.equal(asset.get("tenantUUID"),tenantUUID),criteriaBuilder.isNull(asset.get("removeFromCategoryUUID")))).getResultList());

            CriteriaQuery catQuery = criteriaBuilder.createQuery(String.class);
            Root<Category> category = catQuery.from(Category.class);

            for(GetNameAndTypeOfAssetResponse entry : response.getAssets()){
                entry.setType((String)entityManager.createQuery(catQuery.select(category.get("name")).where(criteriaBuilder.equal(category.get("uuid"),entry.getCategoryUUID()))).getSingleResult());
            }

            catQuery = null;
            category = null;

            response.setResponseIdentifier("Success");
            LOGGER.info("Basic detail of asset got successfully");

        } catch (Exception e) {

            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting basic detail of asset by tenant. TenantUUID: " + tenantUUID,e);
            e = null;
        }finally{
            LOGGER.info("Returning to controller to get Basic Asset Info");
            util.clearThreadContextForLogging();
            util = null;
            criteriaBuilder = null;
            asset = null;
            query = null;
        }

        return response;
    }

    //get page of assets AMS_UC_22
    /*
     * This function gets limit and offset and returns page of assets
     */
//    @HasRead
    GetPaginatedAssetsResponse getPaginatedAssets(int offset, int limit, String tenantuuid) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<AssetModelForTableView> query = null;
        Root<Asset> asset = null;
        GetPaginatedAssetsResponse response = new GetPaginatedAssetsResponse();
        response.setAssets(new AssetPage());
        CriteriaQuery query1 = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get page of assets by tenantUUID: "+tenantuuid+", offset: ");

//            JdbcTemplate jt;
//            jt = new JdbcTemplate(this.dataSource());
//            int lowerLimit = offset * limit;
//            int upperLimit = limit;
//            AssetPage assets = new AssetPage();
//            //get total count of assets
//            String sql = "select count(*) as count from t_asset a where a.tenantuuid=?";
//            Map<String, Object> totalAssetsResponse = jt.queryForMap(sql, tenantuuid);
//            assets.setTotalElements((Long) (totalAssetsResponse.get("count")));
//            //getting page of assets
//            sql = "select a.id as id, a.asset_number as assetNumber, a.description as description, a.inventory as inventory, " +
//                    " a.manufacture as manufacture, a.model_number as modelNumber, a.name as name, a.purchase_date as purchaseDate, a.tenantuuid as tenantUUID, a.uuid as uuid, " +
//                    " a.warranty as warranty, a.primary_usage_unit as primaryUsageUnit, a.secondary_usage_unit as secondaryUsageUnit, a.consumption_unit as consumptionUnit " +
//                    " from t_asset a where a.tenantuuid=? " +
//                    "limit ?,?";
//            List<Map<String, Object>> assetsResponse = jt.queryForList(sql, tenantuuid, lowerLimit, upperLimit);
//            List<AssetModelForTableView> assetModelList = new ArrayList<>();
//            for (Map<String, Object> assetResponse : assetsResponse) {
//                AssetModelForTableView assetModel = new AssetModelForTableView();
//                assetModel.setId((Long) assetResponse.get("id"));
//                assetModel.setAssetNumber(String.valueOf(assetResponse.get("assetNumber")));
//                assetModel.setConsumptionUnit(String.valueOf(assetResponse.get("consumptionUnit")));
//                assetModel.setDescription(String.valueOf(assetResponse.get("description")));
//                assetModel.setInventory(String.valueOf(assetResponse.get("inventory")));
//                assetModel.setManufacture(String.valueOf(assetResponse.get("manufacture")));
//                assetModel.setModelNumber(String.valueOf(assetResponse.get("modelNumber")));
//                assetModel.setName(String.valueOf(assetResponse.get("name")));
//                assetModel.setPrimaryUsageUnit(String.valueOf(assetResponse.get("primaryUsageUnit")));
//                assetModel.setSecondaryUsageUnit(String.valueOf(assetResponse.get("secondaryUsageUnit")));
//                assetModel.setTenantUUID(String.valueOf(assetResponse.get("tenantUUID")));
//                assetModel.setUuid(String.valueOf(assetResponse.get("uuid")));
//                assetModel.setWarranty(String.valueOf(assetResponse.get("warranty")));
//                assetModel.setPurchaseDate((Date) assetResponse.get("purchaseDate"));
//                assetModel.setExpiryDate((Date) assetResponse.get("expiryDate"));
//
//                assetModelList.add(assetModel);
//            }

            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(AssetModelForTableView.class);
            asset = query.from(Asset.class);

            response.getAssets().setContent(entityManager.createQuery(query.select(criteriaBuilder.construct(AssetModelForTableView.class,asset.get("id"),asset.get("assetNumber"),asset.get("uuid"),asset.get("name"),asset.get("modelNumber"),asset.get("manufacture"),asset.get("purchaseDate"),asset.get("expiryDate"),asset.get("warranty"),asset.get("description"),asset.get("tenantUUID"),asset.get("primaryUsageUnit"),asset.get("secondaryUsageUnit"),asset.get("consumptionUnit"),asset.get("status"))).where(criteriaBuilder.equal(asset.get("tenantUUID"),tenantuuid),criteriaBuilder.isNull(asset.get("removeFromCategoryUUID"))).orderBy(criteriaBuilder.desc(asset.get("id"))))
                    .setFirstResult(offset * limit)
                    .setMaxResults(limit)
                    .getResultList());


//            response.getAssets().setContent(assetModelList);

            query1 = criteriaBuilder.createQuery(Long.class);
            asset = query1.from(Asset.class);
            response.getAssets().setTotalElements((Long)entityManager.createQuery(query1.select(criteriaBuilder.count(asset)).where(criteriaBuilder.equal(asset.get("tenantUUID"),tenantuuid))).getSingleResult());
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets from database. Returning to controller");

        } catch (Exception e) {
            LOGGER.error("Error while getting page of assets by tenantUUID: "+tenantuuid+", offset: "+offset+", limit: "+limit, e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of Get Paginated Assets");
            util.clearThreadContextForLogging();
            util = null;
            query = null;
            criteriaBuilder = null;
            asset = null;
        }

        return response;
    }

    public GetPaginatedAssetsResponse getPaginatedBulkOrSingleAssets(String tenantuuid, int offset, int limit, boolean isBulk) throws  ApplicationException,IOException{
        Util util = new Util();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<AssetModelForTableView> query = null;
        Root<Asset> asset = null;
        CriteriaQuery query1 = null;
        GetPaginatedAssetsResponse response = new GetPaginatedAssetsResponse();
        response.setAssets(new AssetPage());
        List<Predicate> clauses = null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get page of bulk or single assets by tenantUUID: "+tenantuuid+", offset: ");

            clauses = new ArrayList<>();
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(AssetModelForTableView.class);
            asset = query.from(Asset.class);
            if(isBulk){
                clauses.add(criteriaBuilder.greaterThan(asset.get("quantity"),1));
            }else{
                clauses.add(criteriaBuilder.equal(asset.get("quantity"),1));
            }
            clauses.add(criteriaBuilder.equal(asset.get("tenantUUID"),tenantuuid));

            response.getAssets().setContent(entityManager.createQuery(query.select(criteriaBuilder.construct(AssetModelForTableView.class,asset.get("id"),asset.get("assetNumber"),asset.get("uuid"),asset.get("name"),asset.get("modelNumber"),asset.get("inventory"),asset.get("manufacture"),asset.get("purchaseDate"),asset.get("expiryDate"),asset.get("warranty"),asset.get("description"),asset.get("tenantUUID"),asset.get("primaryUsageUnit"),asset.get("secondaryUsageUnit"),asset.get("consumptionUnit"),asset.get("quantity"))).where(clauses.toArray(new Predicate[]{})).orderBy(criteriaBuilder.desc(asset.get("id"))))
                    .setFirstResult(offset * limit)
                    .setMaxResults(limit)
                    .getResultList());

            query1 = criteriaBuilder.createQuery(Long.class);
            asset = query1.from(Asset.class);
            response.getAssets().setTotalElements((Long)entityManager.createQuery(query1.select(criteriaBuilder.count(asset)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets from database. Returning to controller");

        } catch (Exception e) {
            throw new ApplicationException("Unexpected error occurred while getting page of bulk  or single assets", e);
        }finally{
            LOGGER.info("Returning to controller of Get Paginated Bulk or Single Assets");
            util.clearThreadContextForLogging();
            util = null;
            query = null;
            criteriaBuilder = null;
            asset = null;
        }
        return response;
    }

    //get page of assets for SDT
//    @HasRead
    GetPaginatedAssetsResponse getPaginatedAssetsForSDT(GetPaginatedDataForSDTRequest request) throws IOException,AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<AssetModelForTableView> query = null;
        Root<Asset> asset = null;
        GetPaginatedAssetsResponse response = new GetPaginatedAssetsResponse();
        response.setAssets(new AssetPage());
        CriteriaQuery query1 = null;
        List<Predicate> clauses = null;
        List<AssetModelForTableView> assetModelForTableViews = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get page of assets for SDT,details: "+convertToJSON(request));

            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(AssetModelForTableView.class);
            asset = query.from(Asset.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(asset.get("tenantUUID"),request.getTenantUUID()));
            clauses.add(criteriaBuilder.isNull(asset.get("removeFromCategoryUUID")));

            // Add filters
            clauses = addFilters(criteriaBuilder,asset,clauses,request.getFilters(),request.getSearchQuery());

            assetModelForTableViews = (List<AssetModelForTableView>) entityManager.createQuery(query.select(criteriaBuilder.construct(AssetModelForTableView.class,asset.get("id"),asset.get("assetNumber"),asset.get("uuid"),asset.get("name"),asset.get("modelNumber"),asset.get("manufacture"),asset.get("purchaseDate"),asset.get("expiryDate"),asset.get("warranty"),asset.get("description"),asset.get("tenantUUID"),asset.get("primaryUsageUnit"),asset.get("secondaryUsageUnit"),asset.get("consumptionUnit"),asset.get("status"))).where(clauses.toArray( new Predicate[]{})).orderBy(
                    (javax.persistence.criteria.Order) CriteriaBuilder.class.getDeclaredMethod(request.getSortDirection(), Expression.class)
                            .invoke(criteriaBuilder,asset.get(request.getSortField()))))
                    .setFirstResult(request.getLimit() * request.getOffset())
                    .setMaxResults(request.getLimit())
                    .getResultList();
            List<String> assetIds = new ArrayList<>();
            for(AssetModelForTableView view : assetModelForTableViews){
                assetIds.add(view.getUuid());
            }
            GetAssetUsersResponse usersResponse = getAssetUsersByAssetIds(assetIds);
            for(AssetModelForTableView view: assetModelForTableViews){
                if(usersResponse.getUsers().containsKey(view.getUuid())){
                    view.setAssignees(usersResponse.getUsers().get(view.getUuid()).toString());
                }else{
                    view.setAssignees("");
                }
            }
            response.getAssets().setContent(assetModelForTableViews);
            query1 = criteriaBuilder.createQuery(Long.class);
            asset = query1.from(Asset.class);
            response.getAssets().setTotalElements((Long)entityManager.createQuery(query1.select(criteriaBuilder.count(asset)).where(clauses.toArray( new Predicate[]{}))).getSingleResult());
            response.getAssets().setTotalPages(((Long) response.getAssets().getTotalElements() / request.getLimit()) + 1);

            if ((Long) response.getAssets().getTotalElements() == request.getLimit())
                response.getAssets().setTotalPages((Long) response.getAssets().getTotalPages() - 1);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets for SDT from database. Returning to controller");

        } catch (Exception e) {
            LOGGER.error("Error while getting page of assets for SDT,details: "+convertToJSON(request), e);
            response.setResponseIdentifier(SUCCESS);
            e = null;
        }finally{
            LOGGER.info("Returning to controller of Get Paginated Assets for SDT");
            util.clearThreadContextForLogging();
            util = null;
            query = null;
            criteriaBuilder = null;
            asset = null;
            clauses.clear();
            clauses = null;
        }

        return response;
    }

    //get name and type of assets AMS_UC_23
    /*
     * This will be used by Inspection Table View FE Screen
     * This function gets a list of uuids. First, it finds assets by those uuids from db and then creates a hashmap of assets
     * inwhich a name and type of asset is stored against uuid of asset and returned
     */
//    @HasRead
    GetNameAndTypeOfAssetsByUUIDSResponse getNameAndTypeOfAssetsByUUIDS(GetNameAndTypeOfAssetsByUUIDSRequest request) throws IOException,AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetNameAndTypeOfAssetsByUUIDSResponse response = new GetNameAndTypeOfAssetsByUUIDSResponse();
        response.setAssets(new HashMap<>());

        try {
            util.setThreadContextForLogging(scim2Util);

            LOGGER.info("Inside service function of get get name and type of assets by uuids, request: "+convertToJSON(request));

            if(request.getUuids().isEmpty()){
                LOGGER.warn("Asset uuids is Empty");
            }else{
                List<GetNameAndTypeOfAssetResponse> responses = assetRepository.findAssetDetailByAssetUUIDS(request.getUuids());
                response.setAssets((HashMap<String, GetNameAndTypeOfAssetResponse>) responses.stream().collect(Collectors.toMap(GetNameAndTypeOfAssetResponse::getUuid, Function.identity())));
            }

            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets from database. Returning to controller");
        } catch (Exception e) {
            LOGGER.error("Error while getting assets by uuids, details: "+convertToJSON(request), e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }


        return response;
    }

    /*************************************Feign Service Method Start****************************************************/
   // @HasRead
    AssetsNameModel getNameByUUIDS(GetNameAndTypeOfAssetsByUUIDSRequest request) throws IOException {
  //      Util util = new Util();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
//        CriteriaQuery<GetNameAndTypeOfAssetResponse> query = null;
//        Root<Category> category = null;
//        EntityType<Asset> Asset_ = null;
        AssetsNameModel assetsNameModel = new AssetsNameModel();
        try {
//            util.setThreadContextForLogging(scim2Util);

            LOGGER.info("Inside service function of  get name and type of assets by uuids, request: "+convertToJSON(request));

            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Asset.class);
            root = query.from(Asset.class);

            List<Asset> assets = (List<Asset>) entityManager.createQuery(query.select(root).where(root.get("uuid").in(request.getUuids()))).getResultList();
            assetsNameModel.setAssetName(new ArrayList<>());
            assetsNameModel.setAssetUUID(new ArrayList<>());
            for(Asset asset: assets){
                assetsNameModel.getAssetName().add(asset.getName());
                assetsNameModel.getAssetUUID().add(asset.getUuid());
            }
//            Asset_ = entityManager.getMetamodel().entity(Asset.class);
//            EntityType<Category> Category_ = entityManager.getMetamodel().entity(Category.class);
//            SetJoin<Category,Asset> asset = category.join(Category_.getSet("assets",Asset.class),JoinType.INNER);
//            SetJoin<Asset,AssetImage> assetImage = asset.join(Asset_.getSet("assetImages",AssetImage.class),JoinType.LEFT);
//
//           // assetsNameModel=new AssetsNameModel();
//
//
//
//            if(request.getUuids().isEmpty()){
//                LOGGER.warn("Asset uuids is Emply");
//            }else{
//                entityManager.createQuery(query.select(criteriaBuilder.construct(GetNameAndTypeOfAssetResponse.class,asset.get("name"),category.get("name"),asset.get("assetNumber"),asset.get("uuid"),assetImage.get("imageUrl"),asset.get("primaryUsageUnit"),asset.get("secondaryUsageUnit"),asset.get("consumptionUnit"),asset.get("consumptionPoints"))).where(asset.get("uuid").in(request.getUuids())).distinct(true))
//                        .getResultList().forEach( (assetNameAndType) -> {
//                    assetNameAndType.setLastUsage(usageRepository.findFirstByAssetUUIDOrderByIdDesc(assetNameAndType.getUuid()));
//                   assetName.add(assetNameAndType.getName());
//                   assetUUID.add(assetNameAndType.getUuid());
//                });
//            }

            assets = null;
            LOGGER.info("Received assets from database. Returning to controller");
        } catch (Exception e) {
            LOGGER.error("Error while getting assets by uuids, details: "+convertToJSON(request), e);
            e = null;
        }finally{
          //  util.clearThreadContextForLogging();
           // util = null;
            criteriaBuilder = null;
            query = null;
            root = null;
        }
        return assetsNameModel;
    }

     /*************************************Feign Service Method Ended****************************************************/

//     @HasRead
    GetNameAndUUIDOfAssetResponse getNameAndUUIDOfAssetByTenantUUID(String tenantUUID) throws ApplicationException,AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetNameAndUUIDOfAssetResponse response = new GetNameAndUUIDOfAssetResponse();
        response.setAssets(new ArrayList<>());
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method to Get Name and UUID of Asset by TenantUUID: "+tenantUUID);
//            ArrayList<Asset> assets = assetRepository.findAssetsByTenantUUID(tenantUUID).f;
            assetRepository.findAssetsByTenantUUID(tenantUUID).forEach((asset) -> {
                response.getAssets().add(new AssetNameAndUUIDModel(asset.getName(), asset.getUuid()));
            });

//            };
//            ArrayList<AssetNameAndUUIDModel> assetNameAndUUIDModels = new ArrayList<>();
//
//            for (Asset asset : assets) {
//                assetNameAndUUIDModels.add(new AssetNameAndUUIDModel(asset.getName(), asset.getUuid()));
//            }

            response.setResponseCode("200");
            response.setResponseIdentifier("Success");

        } catch (Exception e) {
            throw new ApplicationException("Unexpected error occurred while getting name and uuid of asset by tenant uuid", e);
        }finally{
            LOGGER.info("Returning to controller of Get Name and UUID of Asset.");
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //get asset uuids by name
//    @HasRead
    GetAssetUUIDsByNameResponse getAssetUUIDsByName(String name) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetAssetUUIDsByNameResponse response = new GetAssetUUIDsByNameResponse();

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function to get asset uuids by name: "+name);

            response.setAssetUUIDs(assetRepository.findAssetUUIDByAssetName(name));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets uuids from database. Returning to controller");

        } catch (Exception e) {

            LOGGER.error("Error while getting assets by uuids by name: "+name, e);
            response.setResponseIdentifier("Failure");
            e = null;
        }

        return response;
    }
    // Update Asset Fields
//    @HasUpdate
    DefaultResponse updateAssetFields(UpdateAssetFieldsRequest updateAssetFieldsRequest) throws ApplicationException, JsonProcessingException,AccessDeniedException {

        if(!privilegeHandler.hasUpdate())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse defaultResponse = null;
        Asset asset = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info(" Entered service method to update all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()));

            // Obtain Asset
            asset = assetRepository.findAsset(updateAssetFieldsRequest.getAssetUUID());

            // Set Asset in AssetFields
            for(AssetField assetField : updateAssetFieldsRequest.getAssetFields()){
                assetField.setAssetUUID(asset.getUuid());
            }

            // Set updated Asset fields in Asset
            asset.setAssetFields(updateAssetFieldsRequest.getAssetFields());

            // Save the Asset
            assetRepository.save(asset);

            LOGGER.info("Successfully updated all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()));
            defaultResponse = new DefaultResponse("Success", "Successfully Updated all Asset Fields", "F200");
        } catch (DataAccessException dae) {
            throw new ApplicationException("An unexpected Error occurred while updating all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields())+ ".Error Code AMS-5624", dae);
        } catch (Exception e) {
            throw new ApplicationException("An Error occurred while update all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()) + ".Error Code AMS-5624", e);
        }finally{
            LOGGER.info("Returning from service method of updating all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()));
            util.clearThreadContextForLogging();
            util = null;
            asset = null;
        }

        return defaultResponse;
    }

    GetAssetFeildResponse getAssetFields(List<String> assetUUIDs) throws ApplicationException{
        Util util = new Util();
        GetAssetFeildResponse response = null;
        List<AssetField> assetFields = null;
        Field field = null;
        HashMap<String,String> fieldAsset = new HashMap<>();
        HashMap<String,HashMap<String,String>> fields = new HashMap<>();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside  the service function of getting asset fields" + convertToJSON(assetUUIDs));

            for(String assetuuid : assetUUIDs){
                assetFields = assetFieldRepository.findAssetFieldsByAssetUUID(assetuuid);
                if(assetFields.size() >= 1){
                    for(AssetField assetField : assetFields){
                        field = fieldRepository.findByUuid(assetField.getFieldId());
                        if(field.getType().equals("select")) {
                            fieldAsset.put(field.getLabel(), assetField.getFieldValue());
                            fields.put(assetField.getAssetUUID(), fieldAsset);
                        }
                        field = null;

                    }
                }
                assetFields = null ;
                fieldAsset = new HashMap<>();

            }

            response = new GetAssetFeildResponse();
            response.setAssetFields(fields);
            response.setResponseIdentifier("Success");

        }catch(Exception e){
            throw new ApplicationException("Error while getting assets fields. Error code AMS-003",e);
        }finally {
            LOGGER.info("Returning to controller after getting asset fields");
            util = null;
            assetFields = null;
            field = null;
            fieldAsset = null;
            fields = null;

        }
        return response;
    }

    //purpose of function to compile Assets name and uuid for Request from Aps
    public GetUserAssetsResponse getAssetNameAndUUIDForAps(List<String> assetUUIDs) throws ApplicationException{
        Util util = new Util();
        GetUserAssetsResponse response = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;

        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get Assets name and uuid for Aps. Details: " + convertToJSON(assetUUIDs));
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Asset.class);
            root = query.from(Asset.class);
            List<Asset> assets = (List<Asset>) entityManager.createQuery(query.select(root).where(root.get("uuid").in(assetUUIDs)).distinct(true)).getResultList();
            if(assets != null){
                response = new GetUserAssetsResponse();
                response.setAssets(new ArrayList<>());
                for(Asset asset:assets){
                    response.getAssets().add(new HashMap<>());
                    response.getAssets().get(response.getAssets().size() - 1).put("name",asset.getName());
                    response.getAssets().get(response.getAssets().size() - 1).put("uuid",asset.getUuid());
                }
            }
        }catch (Exception e){
            throw new ApplicationException("An Error occurred while getting Assets name and uuid for Aps.",e);
        }finally {
            LOGGER.info("Returning to controller of get Assets name and uuid for Aps.");
            util.clearThreadContextForLogging();
            util = null;
            criteriaBuilder = null;
            query = null;
            root = null;
        }

        return response;
    }

    //purpose of function to get Asset name and number and category by Asset uuid
    public AssetNameAndNumberResponse getAssetNameAndNumberAndCategoryByAssetUUID(String uuid) throws AccessDeniedException,ApplicationException{
        //checking if user have read access
        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        AssetNameAndNumberResponse response = null;
        AssetInfoDTO asset = null;
        Category category = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get Asset name and number and category by Asset uuid: " + uuid);
            response = new AssetNameAndNumberResponse();
            asset = assetRepository.findAssetInfoByAssetUUID(uuid);
            if(asset != null) {
                response.setAssetInfoDTO(asset);
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Successfully got Asset Name and number and category by uuid: " + uuid);
            }else{
                response.setAssetInfoDTO(null);
                response.setResponseIdentifier(FAILURE);
                LOGGER.info("No Asset found with uuid: " + uuid);
            }
        }catch (Exception e){
            throw new ApplicationException("An Error Occurred while getting Asset name and number and category by Asset uuid: " + uuid,e);
        }finally {
            LOGGER.info("Returning to controller of get Asset name and number and category by Asset uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    //purpose of function to get Asset Images
    public GetAssetImageResponse getAssetImageByAssetUUID(String uuid) throws AccessDeniedException,ApplicationException{
        //check if user have read access
        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();
        Util util = new Util();
        GetAssetImageResponse response = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<AssetImage> assetImages = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of get Asset Image by Asset uuid: " + uuid);
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(AssetImage.class);
            root = query.from(AssetImage.class);

            assetImages = (List<AssetImage>) entityManager.createQuery(query.select(root).where(criteriaBuilder.equal(root.get("assetUUID"),uuid)).orderBy(criteriaBuilder.desc(root.get("id")))).getResultList();
            response = new GetAssetImageResponse();
            if(assetImages != null && assetImages.size() > 0){
                for(AssetImage assetImage: assetImages){
                    assetImage.setContent(getFile(assetImage.getImageUrl()).getContent());
                }
                response.setAssetImages(assetImages);
                response.setResponseIdentifer(SUCCESS);
                LOGGER.info("Successfully got Asset Image by Asset uuid: " + uuid);
            }else {
                response.setAssetImages(new ArrayList<>());
                response.setResponseIdentifer(FAILURE);
                LOGGER.info("No Asset image with Asset uuid: " + uuid);
            }
        }catch (Exception e){
            throw new ApplicationException("An Error Occurred while getting Asset image by Asset uuid: " + uuid,e);
        }finally {
            LOGGER.info("Returning to controller of get Asset iamage by Asset uuid.");
            util.clearThreadContextForLogging();
            util = null;
            criteriaBuilder = null;
            query = null;
            root = null;
            assetImages = null;
        }
        return response;
    }

    //purpose of function to export sample excel
    public ExportSampleExcelResponse exportExcelSample(ExportSampleExcelRequest request) throws ApplicationException,AccessDeniedException {

        //check if user have access read Assets
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied.");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        ExportSampleExcelResponse response = new ExportSampleExcelResponse();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Asset");
        Row row = null;
        Cell cell = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function export Excel sample. Details: " + convertToJSON(request));
//            sheet.setColumnWidth(0,256 * 30);
//            sheet.setColumnWidth(1,256 * 30);
            createHeading(row,sheet,"Asset Info",0,6);
            createRow(row,sheet,"Asset Name",request.getAssetExcelData().getName(),false,null,3);
            createRow(row,sheet,"Category",request.getAssetExcelData().getCategory(),false,null,4);
            createRow(row,sheet,"Model Number",request.getAssetExcelData().getModelNumber(),false,null,5);
            createRow(row,sheet,"Manufacturer",request.getAssetExcelData().getManufacturer(),false,null,6);
            createRow(row,sheet,"Purchase Date",null,true,request.getAssetExcelData().getPurchaseDate(),7);
            createRow(row,sheet,"Status",request.getAssetExcelData().getStatus(),false,null,8);
            createRow(row,sheet,"Warranty",request.getAssetExcelData().getWarranty(),false,null,9);
            createRow(row,sheet,"Warranty Unit",request.getAssetExcelData().getWarrantyUnit(),false,null,10);
            createRow(row,sheet,"Primary Usage Unit",request.getAssetExcelData().getPrimaryUsageUnit(),false,null,11);
            createRow(row,sheet,"Secondary Usage Unit",request.getAssetExcelData().getSecondaryUsageUnit(),false,null,12);
            createRow(row,sheet,"Consumption Unit",request.getAssetExcelData().getConsumptionUnit(),false,null,13);
            createRow(row,sheet,"Description",request.getAssetExcelData().getDescription(),false,null,14);
            createHeading(row,sheet,"Additional Details",16,6);
            int j = 19;
            for(FieldDTO fieldDTO:request.getAssetExcelData().getAdditionalFields()){
                createRow(row,sheet,fieldDTO.getFieldLabel(),fieldDTO.getFieldValue(),false,null,j);
                j += 1;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            out.close();
            workbook.close();

            byte [] file = out.toByteArray();
            response.setResponseIdentifier(SUCCESS);
            response.setFileName("Asset");
            response.setFile(file);
            response.setContentLength(file.length);
        }catch (Exception e){
            LOGGER.error("An Error occurred while exporting excel sample file.",e);
        }finally {
            LOGGER.info("Returning to controller of export excel sample.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    //purpose of function to create heading for Export Excel
    public void createHeading(Row row,Sheet sheet, String value, int i,int endCol) throws ApplicationException {
        try{
            LOGGER.info("Inside function of create heading.");
            row = sheet.createRow(i);
            Row row2 = sheet.createRow(i + 1);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            setBorders(cellStyle);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            for (int j = 0; j < endCol; j++){
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                Cell cell2 = row2.createCell(j);
                cell2.setCellStyle(cellStyle);
                if(j == 0)
                    cell.setCellValue(value);
            }

            sheet.addMergedRegion( new CellRangeAddress(i,i + 1,0,endCol - 1));
        }catch (Exception e){
            LOGGER.error("An Error occurred while creating heading.",e);
            throw new ApplicationException("An Error occurred while creating heading.",e);
        }
    }

    //purpose of function to create rows for Export Excel
    public void createRow(Row row,Sheet sheet,String fieldName, String fieldValue, boolean isDate, Date date, int i) throws ApplicationException {
        try{
            LOGGER.info("Inside function of create row.");
            row = sheet.createRow(i);
            createLabelCell(row,fieldName,sheet,i);
            if(!isDate){
                createValueCell(row,fieldValue,sheet,i);
            }else {
                createDateValueCell(row,date,sheet,i);
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while creating row for Asset sheet.",e);
            throw new ApplicationException("An Error occurred while creating row for Asset sheet.",e);
        }
    }

    //purpose of function to create Labels
    public void createLabelCell(Row row,String fieldName, Sheet sheet, int i) throws ApplicationException {
        Cell cellLabel = null;
        try{
            LOGGER.info("Inside function of create label cell.");
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 14);
            cellStyle.setFont(font);

            cellLabel = row.createCell(0);
            cellLabel.setCellStyle(cellStyle);
            cellLabel.setCellValue(fieldName);
            sheet.addMergedRegion( new CellRangeAddress(i,i,0,2));

        }catch (Exception e){
            LOGGER.error("An Error occurred while creating cell.",e);
            throw new ApplicationException("An Error occurred while creating cell.",e);
        }
    }

    //purpose of function to create Values
    public void createValueCell(Row row,String fieldValue,Sheet sheet, int i) throws  ApplicationException {
        Cell cellFieldValue = null;
        try{
            LOGGER.info("Inside service function of create Value Cell.");
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setFontHeightInPoints((short) 14);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);

            cellFieldValue = row.createCell(3);
            cellFieldValue.setCellStyle(cellStyle);
            cellFieldValue.setCellValue(fieldValue != null ? fieldValue: "");
            sheet.addMergedRegion( new CellRangeAddress(i,i,3,5));

        }catch (Exception e){
            LOGGER.error("An Error occurred while creating value cell",e);
            throw new ApplicationException("An Error occurred while creating value cell",e);
        }
    }

    //purpose of function of create Date values
    public void createDateValueCell(Row row,Date fieldValue,Sheet sheet, int i) throws  ApplicationException {
        Cell cellFieldValue = null;
        try{
            LOGGER.info("Inside service function of create date Value Cell.");
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setFontHeightInPoints((short) 14);
            cellStyle.setFont(font);
            cellStyle.setDataFormat((short) 14);
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);

            cellFieldValue = row.createCell(3);
            cellFieldValue.setCellStyle(cellStyle);
            Date value = fieldValue != null ? fieldValue : new Date();
            cellFieldValue.setCellValue(value);
            sheet.addMergedRegion( new CellRangeAddress(i,i,3,5));

        }catch (Exception e){
            LOGGER.error("An Error occurred while creating date value cell",e);
            throw new ApplicationException("An Error occurred while creating date value cell",e);
        }
    }

    //purpose of function of import Excel file
    public ImportExcelResponse importExcelSample(MultipartFile file, String category) throws AccessDeniedException, ApplicationException{
        if(!privilegeHandler.hasCreate()){
            LOGGER.error("Access is Denied");
            throw new AccessDeniedException();
        }

        Util util = new Util();
        ImportExcelResponse response = new ImportExcelResponse();
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of import excel sample.");
            InputStream in = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(in);
            Sheet assetSheet = workbook.getSheetAt(0);
            if(!(assetSheet.getRow(0).getCell(0).getCellTypeEnum() == CellType.STRING && assetSheet.getRow(0).getCell(0).getStringCellValue().equalsIgnoreCase("Asset Info"))){
                LOGGER.info("Invalid Excel Format");
                response.setResponseIdentifier(FAILURE);
                throw new ApplicationException("Invalid Excel Format");
            }

            if(!(assetSheet.getRow(16).getCell(0).getCellTypeEnum() == CellType.STRING && assetSheet.getRow(16).getCell(0).getStringCellValue().equalsIgnoreCase("Additional Details"))){
                LOGGER.info("Invalid Excel Format");
                response.setResponseIdentifier(FAILURE);
                throw new ApplicationException("Invalid Excel Format");
            }
            response.setAssetExcelData(new AssetExcelData());
            for(int i = 3; i < 15; i++){
                Row row = assetSheet.getRow(i);
                if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Asset Name")){
                    response.getAssetExcelData().setName(row.getCell(3).getStringCellValue() != null ? row.getCell(3).getStringCellValue() : "");
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Category")){
                    if(!row.getCell(3).getStringCellValue().equalsIgnoreCase(category)){
                        LOGGER.info("Invalid Excel Format");
                        response.setResponseIdentifier(FAILURE);
                        throw new ApplicationException("Invalid Excel Format");
                    }
                    response.getAssetExcelData().setCategory(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Model Number")){
                    response.getAssetExcelData().setModelNumber(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Manufacturer")){
                    response.getAssetExcelData().setManufacturer(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Purchase Date")){
                    response.getAssetExcelData().setPurchaseDate(row.getCell(3).getDateCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Status")){
                    response.getAssetExcelData().setStatus(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Warranty")){
                    response.getAssetExcelData().setWarranty(String.valueOf(row.getCell(3).getNumericCellValue()));
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Warranty Unit")){
                    response.getAssetExcelData().setWarrantyUnit(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Primary Usage Unit")){
                    response.getAssetExcelData().setPrimaryUsageUnit(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Secondary Usage Unit")){
                    response.getAssetExcelData().setSecondaryUsageUnit(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Consumption Unit")){
                    response.getAssetExcelData().setConsumptionUnit(row.getCell(3).getStringCellValue());
                }else if(row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equalsIgnoreCase("Description")){
                    response.getAssetExcelData().setDescription(row.getCell(3).getStringCellValue());
                }else{
                    LOGGER.info("Invalid Excel Format");
                    response.setResponseIdentifier(FAILURE);
                    throw new ApplicationException("Invalid Excel Format");
                }
            }
            response.getAssetExcelData().setAdditionalFields(new ArrayList<>());
            for(int i = 19; i < assetSheet.getLastRowNum() + 1; i++){
                Row row = assetSheet.getRow(i);
                if(row.getCell(0).getCellTypeEnum() == CellType.STRING && !row.getCell(0).getStringCellValue().equals("")){
                    FieldDTO fieldDTO = new FieldDTO();
                    fieldDTO.setFieldLabel(row.getCell(0).getStringCellValue());
                    fieldDTO.setFieldValue(row.getCell(3).getStringCellValue());
                    response.getAssetExcelData().getAdditionalFields().add(fieldDTO);
                }else{
                    LOGGER.info("Invalid Excel Format");
                    response.setResponseIdentifier(FAILURE);
                    throw new ApplicationException("Invalid Excel Format");
                }
            }
            response.setResponseIdentifier(SUCCESS);
        }catch (Exception e){
            response.setResponseIdentifier(FAILURE);
            LOGGER.info("An Error occurred while importing excel file.",e);
            throw new ApplicationException("An Error occurred while importing excel file.",e);
        }finally {

        }
        return response;
    }

    //purpose of function to export Asset Details
    public ExportSampleExcelResponse exportAssetDetails(ExportAssetDetailRequest request) throws AccessDeniedException, ApplicationException{
        if(!privilegeHandler.hasRead()){
            LOGGER.error("Access is Denied");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        ExportSampleExcelResponse response = new ExportSampleExcelResponse();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Asset Detail");
        Row row = null;
        Cell cell = null;
        int lastIndex = 0; // purpose to get last created row index in Excel
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of export Asset details: " + convertToJSON(request));
            lastIndex = addAssetBasicInfo(row,sheet,request.getAssetExcelData(),lastIndex);
            lastIndex = addAssignmentInfo(row,sheet,request.getAssignments(),lastIndex);
            lastIndex = addAssignmentHistoryInfo(row,sheet,request.getAssignmentHistories(),lastIndex);
            lastIndex = addAssetIssuesInfo(row,sheet,request.getIssues(),lastIndex);
            lastIndex = addAssetWorkOrdersInfo(row,sheet,request.getWorkOrders(),lastIndex);
            lastIndex = addAssetConsumptionsInfo(row,sheet,request.getConsumptions(),lastIndex);
            addAssetUsagesInfo(row,sheet,request.getUsages(),lastIndex,request.getAssetExcelData().getPrimaryUsageUnit(),request.getAssetExcelData().getSecondaryUsageUnit());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            out.close();
            workbook.close();

            byte [] file = out.toByteArray();
            response.setResponseIdentifier(SUCCESS);
            response.setFileName("Asset Details");
            response.setFile(file);
            response.setContentLength(file.length);
        }catch (Exception e){

        }finally {

        }
        return  response;
    }

    //purpose of function to set borders around cells
    public void setBorders(CellStyle cellStyle) throws ApplicationException{
        try{
            LOGGER.info("Inside service function of set borders.");
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        }catch (Exception e){
            LOGGER.error("An Error occurred while settings borders.",e);
            throw new ApplicationException("An Error occurred while settings borders.",e);
        }
    }

    //purpose of function to create headers row for table type data
    public void createHeadersRow(Row row, Sheet sheet, String [] headerRow, int lastIndex) throws ApplicationException {
        try{
            LOGGER.info("Inside function of create headers row.");
            row = sheet.createRow(lastIndex);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 14);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setFont(font);
            setBorders(cellStyle);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            int cellIndex = 0;
            for(int i = 0; i < headerRow.length; i++){
                Cell cell = row.createCell(cellIndex * 3);
                cell.setCellValue(headerRow[i]);
                cell.setCellStyle(cellStyle);
                Cell blankCell = row.createCell((cellIndex * 3) + 1);
                blankCell.setCellStyle(cellStyle);
                Cell blankCell2 = row.createCell((cellIndex * 3) + 2);
                blankCell2.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,i*3,(i * 3) + 2));
                cellIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while creating headers row.",e);
            throw new ApplicationException("An Error occurred while creating headers row.",e);
        }
    }

    //purpose of function to create string type cells style
    public CellStyle createStringCellStyle(Sheet sheet) throws ApplicationException{
        CellStyle stringStyle = null;
        try{
            LOGGER.info("Inside function of create string cell style");
            Font font = sheet.getWorkbook().createFont();
            font.setFontHeightInPoints((short) 14);
            stringStyle = sheet.getWorkbook().createCellStyle();
            stringStyle.setAlignment(HorizontalAlignment.RIGHT);
            stringStyle.setFont(font);
        }catch (Exception e){
            LOGGER.error("An Error occurred while creating string cell style.",e);
            throw new ApplicationException("An Error occurred while creating string cell style.",e);
        }
        return stringStyle;
    }

    //purpose of function to create date type cells style
    public CellStyle createDateCellStyle(Sheet sheet) throws ApplicationException{
        CellStyle dateStyle = null;
        try{
            LOGGER.info("Inside function of create date cell style.");
            Font font = sheet.getWorkbook().createFont();
            font.setFontHeightInPoints((short) 14);
            dateStyle = sheet.getWorkbook().createCellStyle();
            dateStyle.setFont(font);
            dateStyle.setAlignment(HorizontalAlignment.RIGHT);
            dateStyle.setDataFormat((short) 14);
        }catch (Exception e){
            LOGGER.error("An error occurred while creating date cell style.",e);
            throw new ApplicationException("An error occurred while creating date cell style.",e);
        }
        return dateStyle;
    }

    //purpose of function to add Asset Usages info
    public int addAssetUsagesInfo(Row row, Sheet sheet, List<com.sharklabs.ams.model.usage.Usage> usages, int lastIndex, String primaryUnit, String secondaryUnit) throws ApplicationException{
        try{
            LOGGER.info("Inside function of add Asset usages info. Details: " + convertToJSON(usages));
            String [] headersRow = {"Created At","Primary Usage ("+primaryUnit+")" ,"Secondary Usage ("+secondaryUnit+")"};
            createHeading(row,sheet,"Usages",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(com.sharklabs.ams.model.usage.Usage usage: usages){
                row = sheet.createRow(lastIndex);
                Cell createdAtCell = row.createCell(0);
                createdAtCell.setCellValue(usage.getDate());
                createdAtCell.setCellStyle(dateStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,0,2));
                Cell primaryUsageCell = row.createCell(3);
                primaryUsageCell.setCellStyle(cellStyle);
                primaryUsageCell.setCellValue(usage.getValue());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,3,5));
                Cell secondaryValueCell = row.createCell(6);
                secondaryValueCell.setCellValue(usage.getSecondaryValue());
                secondaryValueCell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,6,8));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.info("An Error occurred while adding Asset usages info.",e);
            throw new ApplicationException("An Error occurred while adding Asset usages info.",e);
        }
        return lastIndex += 1;
    }

    //adding Asset consumption info to Excel file
    public int addAssetConsumptionsInfo(Row row, Sheet sheet, List<com.sharklabs.ams.model.consumption.Consumption> consumptions, int lastIndex) throws ApplicationException{
        try{
            LOGGER.info("Inside service function add Asset consumption info. Details: " + convertToJSON(consumptions));
            String [] headersRow = {"Created At","Consumption Value","Consumption Unit"};
            createHeading(row,sheet,"Consumptions",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(com.sharklabs.ams.model.consumption.Consumption consumption:consumptions){
                row = sheet.createRow(lastIndex);
                Cell createdAtCell = row.createCell(0);
                createdAtCell.setCellStyle(dateStyle);
                createdAtCell.setCellValue(consumption.getDate());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,0,2));
                Cell consumptionValueCell = row.createCell(3);
                consumptionValueCell.setCellStyle(cellStyle);
                consumptionValueCell.setCellValue(consumption.getConsumptionValue());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,3,5));
                Cell consumptionUnit = row.createCell(6);
                consumptionUnit.setCellStyle(cellStyle);
                consumptionUnit.setCellValue(consumption.getUnit());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,6,8));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while adding consumption info.",e);
            throw new ApplicationException("An Error occurred while adding consumption info.",e);
        }
        return lastIndex += 1;
    }

    //adding Asset Work orders info to Excel file
    public int addAssetWorkOrdersInfo(Row row, Sheet sheet, List<WorkOrder> workOrders, int lastIndex) throws ApplicationException{
        try{
            LOGGER.info("Inside function of add Asst work Orders info. Details: " + convertToJSON(workOrders));
            String [] headersRow = {"Work Order #","Asset Name","Issues","Created At","Priority","Assigned To","Status"};
            createHeading(row,sheet,"Work Orders",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(WorkOrder workOrder: workOrders){
                row = sheet.createRow(lastIndex);
                Cell workOrderCell = row.createCell(0);
                workOrderCell.setCellStyle(cellStyle);
                workOrderCell.setCellValue(workOrder.getWorkOrderNumber());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,0,2));
                Cell assetName = row.createCell(3);
                assetName.setCellValue(workOrder.getAssetName());
                assetName.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,3,5));
                Cell issues = row.createCell(6);
                issues.setCellStyle(cellStyle);
                issues.setCellValue(workOrder.getIssues());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,6,8));
                Cell createdAt = row.createCell(9);
                createdAt.setCellValue(workOrder.getCreatedAt());
                createdAt.setCellStyle(dateStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,9,11));
                Cell priority = row.createCell(12);
                priority.setCellStyle(cellStyle);
                priority.setCellValue(workOrder.getPriority());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,12,15));
                Cell assignedTo = row.createCell(16);
                assignedTo.setCellStyle(cellStyle);
                assignedTo.setCellValue(workOrder.getAssignedTo());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,16,18));
                Cell status = row.createCell(19);
                status.setCellValue(workOrder.getStatus());
                status.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,19,21));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while adding Asset work orders info.",e);
            throw new ApplicationException("An Error occurred while adding Asset work orders info.",e);
        }
        return lastIndex += 1;
    }

    //adding Asset Issues info to Excel file
    public int addAssetIssuesInfo(Row row, Sheet sheet, List<Issue> issues, int lastIndex) throws ApplicationException{
        try{
            LOGGER.info("Inside function of add Asset issues info. Details: " + convertToJSON(issues));
            String [] headersRow = {"Issue #","Name","Status","Reported At","Work Order #","Reported By"};
            createHeading(row,sheet,"Issues",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(Issue issue:issues){
                row = sheet.createRow(lastIndex);
                Cell issueNumCell = row.createCell(0);
                issueNumCell.setCellStyle(cellStyle);
                issueNumCell.setCellValue(issue.getIssueNumber());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,0,2));
                Cell nameCell = row.createCell(3);
                nameCell.setCellValue(issue.getIssueName());
                nameCell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,3,5));
                Cell statusCell = row.createCell(6);
                statusCell.setCellValue(issue.getStatus());
                statusCell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,6,8));
                Cell reportedAtCell = row.createCell(9);
                reportedAtCell.setCellStyle(dateStyle);
                reportedAtCell.setCellValue(issue.getReportedAt());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,9,11));
                Cell workOrderCell = row.createCell(12);
                workOrderCell.setCellStyle(cellStyle);
                workOrderCell.setCellValue(issue.getWorkOrderNumber());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,12,14));
                Cell reportedByCell = row.createCell(15);
                reportedByCell.setCellStyle(cellStyle);
                reportedByCell.setCellValue(issue.getReportedBy());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,15,17));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while add Asset Issues info.",e);
            throw new ApplicationException("An Error occurred while add Asset Issues info.",e);
        }
        return lastIndex += 1;
    }

    //adding Assignment History into to Excel file
    public int addAssignmentHistoryInfo(Row row, Sheet sheet, List<AssignmentHistory> assignmentHistories, int lastIndex) throws ApplicationException{
        try{
            LOGGER.info("Inside function of adding assignment history info. Details: " + convertToJSON(assignmentHistories));
            String [] headersRow = {"Name","Duration","Start Date","Ending Date"};
            createHeading(row,sheet,"Assignment History",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(AssignmentHistory assignmentHistory: assignmentHistories){
                row = sheet.createRow(lastIndex);
                Cell nameCell = row.createCell(0);
                nameCell.setCellStyle(cellStyle);
                nameCell.setCellValue(assignmentHistory.getName());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,0,2));
                Cell durationCell = row.createCell(3);
                durationCell.setCellValue(assignmentHistory.getDuration());
                durationCell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,3,5));
                Cell startDateCell = row.createCell(6);
                startDateCell.setCellValue(assignmentHistory.getStartDate());
                startDateCell.setCellStyle(dateStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,6,8));
                Cell endDateCell = row.createCell(9);
                endDateCell.setCellStyle(dateStyle);
                endDateCell.setCellValue(assignmentHistory.getEndDate());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex,lastIndex,9,11));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while adding assignment history info.",e);
            throw new ApplicationException("An Error occurred while adding assignment history info.",e);
        }
        return lastIndex += 1;
    }

    //adding Assignment Info to Excel file
    public int addAssignmentInfo(Row row, Sheet sheet, List<Assignment> assignments, int lastIndex) throws ApplicationException {
        try{
            LOGGER.info("Inside service function of add assignment info. Details: " + convertToJSON(assignments));
            String [] headersRow = {"Name","Started Date","Status","No.of Time Assigned"};
            createHeading(row,sheet,"Assignments",lastIndex,headersRow.length * 3);
            createHeadersRow(row,sheet,headersRow,lastIndex + 2);
            lastIndex += 3;
            CellStyle cellStyle = createStringCellStyle(sheet);
            CellStyle dateStyle = createDateCellStyle(sheet);
            for(Assignment assignment: assignments) {
                row = sheet.createRow(lastIndex);
                Cell nameCell = row.createCell(0);
                nameCell.setCellStyle(cellStyle);
                nameCell.setCellValue(assignment.getName());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex, lastIndex, 0, 2));
                Cell dateCell = row.createCell(3);
                dateCell.setCellStyle(dateStyle);
                dateCell.setCellValue(assignment.getStartDate());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex, lastIndex, 3, 5));
                Cell statusCell = row.createCell(6);
                statusCell.setCellStyle(cellStyle);
                statusCell.setCellValue(assignment.getStatus());
                sheet.addMergedRegion(new CellRangeAddress(lastIndex, lastIndex, 6, 8));
                Cell assignedStatus = row.createCell(9);
                assignedStatus.setCellValue(assignment.getAssigned());
                assignedStatus.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastIndex, lastIndex, 9, 11));
                lastIndex++;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred while add assignment info.",e);
            throw new ApplicationException("An Error occurred while add assignment info.",e);
        }
        return lastIndex += 1;
    }

    //adding Asset basic info
    public int addAssetBasicInfo(Row row, Sheet sheet, AssetExcelData assetExcelData, int lastIndex) throws ApplicationException{
        try{
            LOGGER.info("Inside function of add Asset Basic info. Details: " + convertToJSON(assetExcelData));
            createHeading(row,sheet,"Asset Info",0,6);
            createRow(row,sheet,"Asset Name",assetExcelData.getName(),false,null,2);
            createRow(row,sheet,"Model Number",assetExcelData.getModelNumber(),false,null,3);
            createRow(row,sheet,"Manufacturer",assetExcelData.getManufacturer(),false,null,4);
            createRow(row,sheet,"Purchase Date",null,true,assetExcelData.getPurchaseDate(),5);
            createRow(row,sheet,"Status",assetExcelData.getStatus(),false,null,6);
            createRow(row,sheet,"Warranty",assetExcelData.getWarranty(),false,null,7);
            createRow(row,sheet,"Primary Usage Unit",assetExcelData.getPrimaryUsageUnit(),false,null,8);
            createRow(row,sheet,"Secondary Usage Unit",assetExcelData.getSecondaryUsageUnit(),false,null,9);
            createRow(row,sheet,"Consumption Unit",assetExcelData.getConsumptionUnit(),false,null,10);
            createRow(row,sheet,"Consumption Unit",assetExcelData.getConsumptionUnit(),false,null,11);
            createRow(row,sheet,"Description",assetExcelData.getDescription(),false,null,12);
            createHeading(row,sheet,"Additional Details",14,6);
            int j = 16;
            for(FieldDTO fieldDTO:assetExcelData.getAdditionalFields()){
                createRow(row,sheet,fieldDTO.getFieldLabel(),fieldDTO.getFieldValue(),false,null,j);
                j += 1;
            }
            lastIndex = j + 1;
        }catch (Exception e){
            LOGGER.error("An Error occurred while adding Asset basic info.",e);
            throw new ApplicationException("An Error occurred while adding Asset basic info.",e);
        }
        return lastIndex;
    }

    /******************************************* END Asset Functions ************************************************/

    /*******************************************Asset Mapper Functions ************************************************/

    public DefaultResponse mapAssetsBasicInfoToAssetCookedTable(String orgId) throws ApplicationException, AccessDeniedException {
        if(!privilegeHandler.hasRead()){
            LOGGER.info("Access is Denied to read Assets");
            throw new AccessDeniedException();
        }
        Util util = new Util();
        DefaultResponse response = new DefaultResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of map Assets to Cooked Table.");
            List<String> mappedUUIDs = assetMapperRepository.findByTenantUUID(orgId);
            if(mappedUUIDs != null && mappedUUIDs.isEmpty()){
                List<AssetMapper> assetMappers = assetRepository.findAssetsInfoByTenantUUID(orgId);
                assetMapperRepository.save(assetMappers);
                response = new DefaultResponse(SUCCESS,"Successfully Mapped Assets Data.","F200");
            }else{
                List<AssetMapper> assetMappers = assetRepository.findAssetsByUuidNotIn(mappedUUIDs,orgId);
                assetMapperRepository.save(assetMappers);
                response = new DefaultResponse(SUCCESS,"Successfully Mapped un-mapped Assets Data.","F200");
            }
        }catch (Exception e){
            response = new DefaultResponse(FAILURE,"Error While Mapping Assets.","F500");
            throw new ApplicationException("An Error Occurred while mapping data to Cooked Table.",e);
        }finally {
            LOGGER.info("Returning to controller");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    /******************************************* END Asset Mapper Functions ************************************************/

    /******************************************* Consumption Functions **********************************************/
    //this functions adds a consumption unit of an asset e.g fuel entries of vehicles AMS_UC_25
    /*
     * Request object contains asset uuid of which this entry is to be made. We find the asset by uuid and add consumption entry in it's array
     * and save the updated object
     */
    /*  Before changes into Consumption */
//    @HasCreate
//    DefaultResponse addConsumptionUnits(AddConsumptionUnitsRequest request) {
//        Util util = new Util();
//        Asset asset = null;
//        Usage usage = null;
//        DefaultResponse response = null;
//        // String userUUID=null;
//
//        try {
//            util.setThreadContextForLogging(scim2Util);
//            LOGGER.info("Inside service function of adding consumption units of asset. AssetUUID: " + request.getAssetUUID());
//
//            //find asset by uuid
//            asset = assetRepository.findAssetByUuid(request.getAssetUUID());
//            //add consumption unit in the array of consumptions of asset
//            asset.addConsumption(request.getConsumption());
//            request.getConsumption().setAssetUUID(request.getAssetUUID());
//            request.getConsumption().setCreatedAt(new Date());
//            request.getConsumption().setUuid(UUID.randomUUID().toString());
//
//            usage=new Usage();
//            usage.setCreatedAt(new Date());
//            usage.setAssetUUID(request.getAssetUUID());
//            usage.setTenantUUID(request.getConsumption().getTenantUUID());
//            if(request.getConsumption().getMeterType()!=null) {
//                if (request.getConsumption().getMeterType().equals(primaryUsageType)) {
//                    usage.setPrimaryUsageLat(request.getConsumption().getLat());
//                    usage.setPrimaryUsageLng(request.getConsumption().getLng());
//                    usage.setPrimaryUsageTime(request.getConsumption().getCreatedAt());
//                    usage.setPrimaryUsageValue(request.getConsumption().getMeterValue());
//                } else if (request.getConsumption().getMeterType().equals(secondaryUsageType)) {
//                    usage.setSecondaryUsageLat(request.getConsumption().getLat());
//                    usage.setSecondaryUsageLng(request.getConsumption().getLng());
//                    usage.setSecondaryUsageTime(request.getConsumption().getCreatedAt());
//                    usage.setSecondaryUsageValue(request.getConsumption().getMeterValue());
//                }
//            }
//            asset.addUsage(usage);
//
//            assetRepository.save(asset);
//
//            //save attachment
//            imageVoiceRepository.save(request.getImageVoices());
//
//            response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
//            LOGGER.info("Successfully Added Consumption");
//        } catch (Exception e) {
//
//            LOGGER.error("Error while adding consumption unit of asset. AssetUUID: " + request.getAssetUUID(), e);
//            response = new DefaultResponse("Failure", "Error while adding consumption unit of asset. Error Message: " + e.getMessage(), "500");
//            e = null;
//        }finally{
//            LOGGER.info("Returning to controller of adding Consumption");
//            util.clearThreadContextForLogging();
//            util = null;
//            asset = null;
//            usage = null;
//            request = null;
//        }
//
//        return response;
//    }


//    @HasCreate get image work by qasim...
    DefaultResponse addConsumptionUnits(AddConsumptionUnitsRequest request) throws AccessDeniedException {

        if(!privilegeHandler.hasCreate())
            throw new AccessDeniedException();

        Util util = new Util();
        Asset asset = null;
        Usage usage = null;
        DefaultResponse response = null;
       // String userUUID=null;

        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of adding consumption units of asset. AssetUUID: " + request.getAssetUUID());

            //find asset by uuid qasim
            asset = assetRepository.findAssetByUuid(request.getAssetUUID());
            //add consumption unit in the array of consumptions of asset
            asset.addConsumption(request.getConsumption());
            request.getConsumption().setAssetUUID(request.getAssetUUID());
            request.getConsumption().setCreatedAt(new Date());
            request.getConsumption().setUuid(UUID.randomUUID().toString());
            if(request.getImageVoices().size() > 0){
                for(int i = 0; i < request.getImageVoices().size(); i++) {
                    request.getImageVoices().get(i).setConsumptionUUID(request.getConsumption().getUuid());
                }
            }
            usage=new Usage();
            usage.setCreatedAt(new Date());
            usage.setAssetUUID(request.getAssetUUID());
            usage.setTenantUUID(request.getConsumption().getTenantUUID());
            if(request.getConsumption().getMeterType()!=null) {
                if (request.getConsumption().getMeterType().equals(primaryUsageType)) {
                    usage.setPrimaryUsageLat(request.getConsumption().getLat());
                    usage.setPrimaryUsageLng(request.getConsumption().getLng());
                    usage.setPrimaryUsageTime(request.getConsumption().getCreatedAt());
                    usage.setPrimaryUsageValue(request.getConsumption().getMeterValue());
                } else if (request.getConsumption().getMeterType().equals(secondaryUsageType)) {
                    usage.setSecondaryUsageLat(request.getConsumption().getLat());
                    usage.setSecondaryUsageLng(request.getConsumption().getLng());
                    usage.setSecondaryUsageTime(request.getConsumption().getCreatedAt());
                    usage.setSecondaryUsageValue(request.getConsumption().getMeterValue());
                }
            }
            asset.addUsage(usage);
            try{
                incorporateWalletInConsumption(request);
                assetRepository.save(asset);
                //save attachment
                imageVoiceRepository.save(request.getImageVoices());
                response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
                LOGGER.info("Successfully Added Consumption");
            }catch(ApplicationException e){
                LOGGER.info("Wallet Balance not Sufficient in consumption Function ");
                response = new DefaultResponse("Failure", "Error while adding consumption unit of asset. Error Message: " + e.getMessage(), "500");
            }

        } catch (Exception e)
        {
            LOGGER.error("Error while adding consumption unit of asset. AssetUUID: " + request.getAssetUUID(), e);
            response = new DefaultResponse("Failure", "Error while adding consumption unit of asset. Error Message: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of adding Consumption");
            util.clearThreadContextForLogging();
            util = null;
            asset = null;
            usage = null;
            request = null;
        }

        return response;
    }

    // delete single image from consumption by qasim...
    public DefaultResponse deleteConsumptionImages(Long id){
        Util util = new Util();
        DefaultResponse response = null;
        ImageVoice imageVoice = null;
        try { LOGGER.info("Inside Service function of deleting consumption image of id: "+id);
            imageVoice = imageVoiceRepository.findById(id);
            imageVoiceRepository.delete(id);
            LOGGER.info("Image deleted Successfully");
            response = new DefaultResponse("Success", "Image deleted Successfully", "200");
        } catch (Exception e) {
            LOGGER.error("Error while deleting Image of uuid"+id, e);
            response = new DefaultResponse("Failure", "Error in deleting Image: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of delete Image");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public void incorporateWalletInConsumption(AddConsumptionUnitsRequest request) throws ApplicationException{
        String userUUID=request.getUserUUID();
        Wallet wallet=new Wallet();
        Double balance=0.0;
        Fact fact=new Fact();
        try{
            wallet=walletRespository.findByUserUUIDAndWalletType(request.getUserUUID(), "Fuel");
            if (wallet!=null) {
                if (wallet.getBalance()>Double.parseDouble(request.getConsumption().getConsumptionValue())){
                        balance = wallet.getBalance() - Double.parseDouble(request.getConsumption().getConsumptionValue());
                        wallet.setBalance(balance);
                        fact.setWalletUUID(wallet.getWalletUUID());
                        fact.setFactUUID(UUID.randomUUID().toString());
                        fact.setDateTime(new Date());
                        fact.setQuantity(decimaltoTwoPoint(Double.parseDouble(request.getConsumption().getConsumptionValue())));
                        fact.setQuantityUnit(wallet.getCapacityUnit());
                        fact.setTransactiontype("spend");
                        fact.setUserUUID(request.getUserUUID());
                        fact.setDescription("Consumption Entry");
                        factRepository.save(fact);
                        walletRespository.save(wallet);
                        LOGGER.info("Wallet Found", convertToJSON(wallet));
                        sendEmail(wallet);

                } else {
                    LOGGER.info("Wallet Balance not Sufficient ");
                    throw new ApplicationException("Required Consumption Not Added Because of Insufficient Balance in Wallet");
                }
            } else {
                LOGGER.info("Wallet Not Found");
            }
        }catch(Exception e) {
            throw new ApplicationException("Required Consumption Not Added Because of Insufficient Balance in Wallet");
            //LOGGER.info("Wallet Not Found Catch","Wallet Not Found Catch");
        }

    }
//    void saveConsumption(AddConsumptionUnitsRequest request){
//    //Written By Kumail Khan//
//        Asset asset = null;
//        Usage usage = null;
//        BigDecimal bd=null;
//        Double newInput=null;
//        Wallet wallet=new Wallet();
//        Fact fact=new Fact();
//        Page<Fact> factpage;
//        try{
//        String orgUUID=request.getConsumption().getTenantUUID();
//        LOGGER.info("orgUUID"+orgUUID);
//        wallet=walletRespository.findByOrgUUIDAndWalletType(orgUUID,"fuel");
//        Double balance=wallet.getBalance();
//        String walletUUID=wallet.getWalletUUID();
//        Double consumptionValue= Double.valueOf(request.getConsumption().getConsumptionValue());
//        balance=balance-consumptionValue;
//        wallet.setBalance(balance);
//
//        factpage=factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletUUID,"spend",new PageRequest(0,1));
//        Double currentAverage=factpage.getContent().get(0).getCurrentAverage();
//        Double totalPrice=consumptionValue*currentAverage;
//        fact.setCurrentAverage(decimaltoTwoPoint(currentAverage));
//        fact.setTransactiontype("transfer");
//        fact.setTotal(decimaltoTwoPoint(totalPrice));
//        fact.setVolume(decimaltoTwoPoint(factpage.getContent().get(0).getVolume()));
//        fact.setWalletUUID(walletUUID);
//        fact.setFactUUID(UUID.randomUUID().toString());
//        fact.setDateTime(new Date());
//        fact.setQuantity(decimaltoTwoPoint(consumptionValue));
//        fact.setQuantityUnit("LTR");
//        fact.setRate(decimaltoTwoPoint(currentAverage));
//        fact.setRateBasisUnit("LTR");
//        fact.setRateCurrency(request.getConsumption().getCurrency());
//        LOGGER.info("Inside service function of adding consumption units of asset. AssetUUID: " + request.getAssetUUID());
//
//        //find asset by uuid
//        asset = assetRepository.findAssetByUuid(request.getAssetUUID());
//        //add consumption unit in the array of consumptions of asset
//        asset.addConsumption(request.getConsumption());
//        request.getConsumption().setAssetUUID(request.getAssetUUID());
//        request.getConsumption().setCreatedAt(new Date());
//        request.getConsumption().setUuid(UUID.randomUUID().toString());
//
//        usage=new Usage();
//        usage.setCreatedAt(new Date());
//        usage.setAssetUUID(request.getAssetUUID());
//        usage.setTenantUUID(request.getConsumption().getTenantUUID());
//        if(request.getConsumption().getMeterType()!=null) {
//            if (request.getConsumption().getMeterType().equals(primaryUsageType)) {
//                usage.setPrimaryUsageLat(request.getConsumption().getLat());
//                usage.setPrimaryUsageLng(request.getConsumption().getLng());
//                usage.setPrimaryUsageTime(request.getConsumption().getCreatedAt());
//                usage.setPrimaryUsageValue(request.getConsumption().getMeterValue());
//            } else if (request.getConsumption().getMeterType().equals(secondaryUsageType)) {
//                usage.setSecondaryUsageLat(request.getConsumption().getLat());
//                usage.setSecondaryUsageLng(request.getConsumption().getLng());
//                usage.setSecondaryUsageTime(request.getConsumption().getCreatedAt());
//                usage.setSecondaryUsageValue(request.getConsumption().getMeterValue());
//            }
//        }
//        asset.addUsage(usage);
//        walletRespository.save(wallet);
//        assetRepository.save(asset);
//        factRepository.save(fact);
//        //save attachment
//        imageVoiceRepository.save(request.getImageVoices());
//        }catch (Exception e){
//            LOGGER.error("Error while adding consumption unit of asset. AssetUUID: " + request.getAssetUUID(), e);
//        }finally {
//            bd=null;
//            newInput=null;
//            asset = null;
//            usage = null;
//            request = null;
//            wallet=null;
//            fact=null;
//            factpage=null;
//        }
//
//    }
//    //Written By Kumail Khan//
//    //@HasCreate
//    DefaultResponse addConsumptionUnits(AddConsumptionUnitsRequest request) {
//        //Written By Kumail Khan//
//        Util util = new Util();
//        Asset asset = new Asset();
//        Usage usage = null;
//        DefaultResponse response = null;
//        Wallet wallet=new Wallet();
//        WalletNotificationModel walletNotificationModel=new WalletNotificationModel();
//        WalletNotification notification=new WalletNotification();
//        EmailService emailService=null;
//        Fact fact=new Fact();
//        Page<Fact> factpage;
//        try {
//            util.setThreadContextForLogging(scim2Util);
//            LOGGER.info("Entered in Add consumption in Asset Service");
//            String orgUUID=request.getConsumption().getTenantUUID();
//           // LOGGER.info("orgUUID"+orgUUID);
//            wallet=walletRespository.findByOrgUUIDAndWalletType(orgUUID,"fuel");
//            asset=assetRepository.findByAssetNumber(wallet.getAssetUUID());
//            //LOGGER.info("Wallet"+convertToJSON(wallet));
//            Double balance=wallet.getBalance();
//            String walletUUID=wallet.getWalletUUID();
//            Double consumptionValue= Double.valueOf(request.getConsumption().getConsumptionValue());
//            balance=balance-consumptionValue;
//            wallet.setBalance(balance);
//            walletNotificationModel.setAction("Wallet Low Alert");
//            walletNotificationModel.setWalletName(wallet.getWalletName());
//            notification.setAssetUUID(asset.getUuid());
//            notification.setMessage("Wallet Running Low");
//            notification.setNotificationsetting(9);
//            notification.setNotificationTime(new Date());
//            notification.setReadStatus(true);
//            notification.setTenantUUID(wallet.getOrgUUID());
//            notification.setTitle("Wallet "+wallet.getWalletName()+"Running Low");
//            notification.setType("");
////            double thresholdDouble=wallet.getThresholdValue();
////            int thresholdint=(int)thresholdDouble;
////            if((thresholdDouble*100)==(thresholdint*100)){
////                notification.setThresholdValue();
////            }else{
////                notification.setThresholdValue(decimaltoTwoPoint(wallet.getThresholdValue()));
////            }
//            //LOGGER.info("Threshold Value"+convertToJSON(decimaltoTwoPoint(wallet.getThresholdValue())));
//            notification.setThresholdValue(decimaltoTwoPoint(wallet.getThresholdValue()));
//            notification.setUsername(wallet.getWalletName());
//            notification.setCurrencyValue(wallet.getCapacityUnit());
//            walletNotificationModel.setNotification(notification);
//
//            if(wallet.getAllowedNegative().equals(false))
//            {
//                //LOGGER.info("get Allowed Negative False");
//                if(wallet.getThresholdType().equals("lower"))
//                {
//                   // LOGGER.info("get Allowed Negative False And threshold lower");
//                    if(wallet.getThresholdValue()>balance)
//                    {
//                       // LOGGER.info("get Allowed Negative False And threshold lower and balance is less than threshold");
//                        if(balance<0){
//                            LOGGER.info("get Allowed Negative False And threshold lower and balance is less than threshold and balance is less than 0");
//                            //notification bhi add nahi gi
//                            response = new DefaultResponse("Failure", "Consumption Unit Not Added", "500");
//                            LOGGER.info("Added Consumption Failed");
//                            //consumption add nahi ho gii
//                        }else{
//                           // LOGGER.info("get Allowed Negative False And threshold lower and balance is less than threshold and balance is greater than 0");
//                            //notification
//                            //consumption add ho gi
//                            kafkaAsyncService.sendWalletNotification(walletNotificationModel);
//                            saveConsumption(request);
//                            response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
//                            LOGGER.info("Successfully Added Consumption");
//                        }
//                    }else{
//                       // LOGGER.info("get Allowed Negative False And threshold lower and balance is greter than threshold");
//                        saveConsumption(request);
//                        response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
//                        LOGGER.info("Successfully Added Consumption");
//                       //consumption add kar do
//                    }
//                }else if(wallet.getThresholdType().equals("higher"))
//                {
//
//                }else if(wallet.getThresholdType().equals("between"))
//                {
//
//                }
//            }else {
//                //LOGGER.info("get Allowed Negative true");
//                if (wallet.getThresholdType().equals( "lower") ){
//                    //LOGGER.info("get Allowed Negative true And threshold lower");
//                    if (wallet.getThresholdValue() > balance) {
//                       // LOGGER.info("get Allowed Negative true And threshold lower and balance is less than threshold");
//                        //notification
//                        // consumption add ho gi
//                        saveConsumption(request);
//                        response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
//                        LOGGER.info("Successfully Added Consumption");
//                    } else {
//                        //LOGGER.info("get Allowed Negative true And threshold lower and balance is greter than threshold");
//                        saveConsumption(request);
//                        response = new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
//                        LOGGER.info("Successfully Added Consumption");
//                        //consumption add kar do
//                    }
//                } else if (wallet.getThresholdType().equals( "higher")) {
//
//                } else if (wallet.getThresholdType().equals("between")) {
//
//                }
//            }
//        } catch (Exception e) {
//
//            LOGGER.error("Error while adding consumption unit of asset. AssetUUID: " + request.getAssetUUID(), e);
//            response = new DefaultResponse("Failure", "Error while adding consumption unit of asset. Error Message: " + e.getMessage(), "500");
//
//        }finally{
//            LOGGER.info("Returning to controller of adding Consumption");
//            util.clearThreadContextForLogging();
//            util = null;
//            asset = null;
//            usage = null;
//            request = null;
//            wallet=null;
//            fact=null;
//            factpage=null;
//        }
//
//        return response;
//    }
    //get paginated consumptions
//    @HasRead
    GetPaginatedConsumptionsResponse getPaginatedConsumptionsByAsset(String uuid, int offset, int limit) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetPaginatedConsumptionsResponse response = new GetPaginatedConsumptionsResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service to get consumption units of asset. Asset UUID: " + uuid + " with offset: " + offset + " and limit: " + limit);

            response.setConsumptions(consumptionRepository.findByAssetUUIDOrderByCreatedAt(uuid, new PageRequest(offset, limit)));
            response.setResponseIdentifier("Success");
            LOGGER.info("Successfully got paginated consumptions");

        } catch (Exception e) {
            LOGGER.error("An Error occurred while fetching paginated consumptions, details: Asset UUID: "+uuid+", offset: "+offset+", limit; "+limit,e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of fetching paginated consumptions");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }
    //this function deletes a consumption unit by uuid AMS_UC_26
//    @HasDelete
    DefaultResponse deleteConsumptionUnits(String uuid) throws AccessDeniedException {

        if(!privilegeHandler.hasDelete())
            throw new AccessDeniedException();

        Util util = new Util();
        DefaultResponse response = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of deleting consumption unit by uuid. UUID: " + uuid);
//            //find consumption by uuid
//            Consumption consumption = consumptionRepository.findConsumptionByUuid(uuid);
            //delete by id
            consumptionRepository.deleteByUuid(uuid);

            response = new DefaultResponse("Success", "Consumption unit deleted successfully", "200");
            LOGGER.info("Consumption units deleted Successfully");
        } catch (Exception e) {

            LOGGER.error("Error while deleting consumption unit by uuid: UUID: " + uuid, e);
            response = new DefaultResponse("Failure", "Error while deleting consumption unit by uuid. Error Message: " + e.getMessage(), "500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of deleting consumption ");
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //get paginated consumptions AMS_UC_29
//    @HasRead
    GetPaginatedConsumptionsResponse getPaginatedConsumptions(GetPaginatedConsumptionsRequest request) throws IOException,AccessDeniedException{

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetPaginatedConsumptionsResponse response=new GetPaginatedConsumptionsResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting paginated consumptions.  TenantUUID: "+request.getTenantUUID()+" Offset: "+request.getOffset()+" Limit: "+request.getLimit()+"  AssetID: "+request.getAssetUUID()+" Start Date: "+request.getStartDate()+" End Date: "+request.getEndDate());
            if(request.getEndDate()!=null){
                request.setEndDate(setTimeToEnd(request.getEndDate()));
            }
            if(request.getStartDate()!=null){
                request.setStartDate(setTimeToEnd(request.getStartDate()));
            }

            response.setConsumptions(consumptionRepository.filterConsumptions(request.getAssetUUID(),request.getTenantUUID(),request.getStartDate(),request.getEndDate(),new PageRequest(request.getOffset(),request.getLimit())));
            response.setResponseIdentifier("Success");
            LOGGER.info("Page of consumptions got successfully.");

        }catch(Exception e){

            LOGGER.error("Error while getting paginated consumptions, details: requset: "+convertToJSON(request),e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of get paginated consumptions");
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return response;
    }
    //get paginated consumptions by asset uuids AMS_UC_31
//    @HasRead
    GetPaginatedConsumptionsByAssetsResponse getPaginatedConsumptionsByAssets(GetPaginatedConsumptionsByAssetsRequest request) throws AccessDeniedException{

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetPaginatedConsumptionsByAssetsResponse response=new GetPaginatedConsumptionsByAssetsResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting paginated consumptions by asset uuids. Offset: "+request.getOffset()+ "Limit: "+request.getLimit());


            response.setConsumptions(consumptionRepository.findByAssetUUIDInOrderByIdDesc(request.getAssetUUIDS(),new PageRequest(request.getOffset(),request.getLimit())));
            response.setResponseIdentifier("Success");
            LOGGER.info("Page of consumptions by asset uuids got successfully.");

        }catch(Exception e){

            LOGGER.error("Error while getting paginated consumptions by asset uuids. Offset: "+request.getOffset()+ " Limit: "+request.getLimit());
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of get paginated consumptions");
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return response;
    }
    //get consumption by id AMS_UC_39
//    @HasRead
    GetConsumptionByIdResponse getConsumptionById(Long id) throws AccessDeniedException{

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetConsumptionByIdResponse response=new GetConsumptionByIdResponse();
        List<ImageVoice> imageVoices = null;
        GetFileResponse getFileResponse=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting consumption by id. ID: "+id);
            //find consumption by id

            response.setConsumption(consumptionRepository.findOne(id));
            imageVoices = imageVoiceRepository.findByConsumptionUUID(response.getConsumption().getUuid());
            if(imageVoices!=null){
                for(int i=0;i<imageVoices.size();i++){
//                    if(imageVoices.get(i)){
                        getFileResponse=getFile(imageVoices.get(i).getContentUrl());
                        imageVoices.get(i).setContent(getFileResponse.getContent());
//                    }
                }
            }
            response.setImageVoices(imageVoices);

            response.setResponseIdentifier("Success");
            LOGGER.info("Successfully got usages");
        }catch(Exception e){

            LOGGER.error("Error while getting consumption by id: "+id,e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of get usages");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public GetPaginatedDataForSDTResponse getPaginatedConsumptionsForSDT(GetPaginatedDataForSDTRequest request) throws ApplicationException,IOException{
        Util util = new Util();
        GetPaginatedDataForSDTResponse response=new GetPaginatedDataForSDTResponse();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<Predicate> clauses = null;

        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside Service function of getting page of Consumptions for SDT,details:"+convertToJSON(request));
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Long.class);

            root = query.from(Consumption.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));

            // Add filters
            clauses = addFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());

            response.getSdtData().put(TOTAL_ELEMENTS,(Long)entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

            List<Consumption> consumptions = entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))
                    .orderBy(
                            (javax.persistence.criteria.Order) CriteriaBuilder.class.getDeclaredMethod(request.getSortDirection(), Expression.class)
                                    .invoke(criteriaBuilder,root.get(request.getSortField()))
                    ))
                    .setFirstResult(request.getLimit() * request.getOffset())
                    .setMaxResults(request.getLimit())
                    .getResultList();
            root=null;
            clauses=null;
            query=null;
            query = criteriaBuilder.createQuery(Long.class);

            root = query.from(Asset.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));
            List<Asset> assets=entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))).getResultList();
            clauses.clear();

            root=null;
            query=null;
            query = criteriaBuilder.createQuery(Long.class);
            root = query.from(Category.class);
            // Construct Rows List
            response.getSdtData().put(CONTENT,new ArrayList<>());

            for(Consumption consumption:consumptions) {
                ((ArrayList) response.getSdtData().get(CONTENT)).add(new HashMap<>());
                for (Asset asset : assets) {
                    if (asset.getUuid().equals(consumption.getAssetUUID())) {
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("createdAt", consumption.getCreatedAt());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetName", asset.getName());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetNumber", asset.getAssetNumber());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("meterType", consumption.getMeterType());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("consumptionValue", consumption.getConsumptionValue());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("id", consumption.getId());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("consumptionUnit", asset.getConsumptionUnit());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("price", consumption.getPrice());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("currency", consumption.getCurrency());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("consumptionLevel", consumption.getUpdatedConsumptionPoints()+"/8");
                        clauses.add(criteriaBuilder.equal(root.get("uuid"),asset.getCategoryUUID()));

                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetCategory", (String)entityManager.createQuery(query.select(root.get("name")).where(clauses.toArray( new Predicate[]{}))).getSingleResult());
                        clauses.clear();

                        break;
                    }
                }
            }

            response.getSdtData().put(TOTAL_PAGES, ((Long) response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);

            if ((Long) response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                response.getSdtData().replace(TOTAL_PAGES, (Long) response.getSdtData().get(TOTAL_PAGES) - 1);

            response.setResponseIdentifier(SUCCESS);
            LOGGER.info("Page of Consumptions for SDT got successfully. Returning it to controller");


        }catch(Exception e){
            LOGGER.error("An Error occurred in getting page of Consumptions for SDT, "+convertToJSON(request),e);

        }finally{
            util.clearThreadContextForLogging();
            query = null;
            root = null;
            clauses = null;
            criteriaBuilder = null;
            util = null;
        }

        return response;
    }

    public DefaultResponse editConsumption(EditConsumptionRequest request) throws ApplicationException,IOException{
        Util util = new Util();
        DefaultResponse response = null;
        Consumption consumption = null;

        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("In service method of editing consumption, details: "+convertToJSON(request));

            if(request.getUuid() != null){
                if(!request.getUuid().isEmpty()){
                    consumption = consumptionRepository.findConsumptionByUuid(request.getUuid());

                    if(consumption == null)
                        throw new ApplicationException("No Consumption found against provided uuid: "+request.getUuid());

                    if(request.getConsumptionValue() != null)
                        if(!request.getConsumptionValue().isEmpty())
                            consumption.setConsumptionValue(request.getConsumptionValue());

                    if(request.getPrice() != null)
                        consumption.setPrice(request.getPrice());

                    if(request.getCurrency() != null)
                        if(!request.getCurrency().isEmpty())
                            consumption.setCurrency(request.getCurrency());

                    if(request.getUpdatedConsumptionPoints() != 0)
                            consumption.setUpdatedConsumptionPoints(request.getUpdatedConsumptionPoints());

                    consumptionRepository.save(consumption);

                    response = new DefaultResponse(SUCCESS,"Successfully Edited Consumption","F200");

                    LOGGER.info("Successfully Edited Consumption of uuid: "+request.getUuid());

                }else
                    throw new ApplicationException("Consumption UUID is Empty");

            }else
                throw new ApplicationException("Consumption UUID is not provided");

        }catch(ApplicationException ae){
            LOGGER.error("A Known Exception occurred while editing consumption, details: "+convertToJSON(request),ae);
            response = new DefaultResponse(FAILURE,"Sorry, a known error occurred while updating consumption","F500");
            ae = null;
        }catch(Exception e){
            LOGGER.error("A unknown Exception occurred while editing consumption, details: "+convertToJSON(request),e);
            response = new DefaultResponse(FAILURE,"Sorry, an unknown error occurred while updating consumption","F500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of editing consumption");
            util.clearThreadContextForLogging();
            util = null;
            consumption = null;
        }

        return response;
    }

    /******************************************* END Consumption Functions **********************************************/

    /******************************************* Usages Functions ****************************************************/
    //add usage
//    @HasCreate
    DefaultResponse addUsage(AddUsageRequest request) throws AccessDeniedException{

        if(!privilegeHandler.hasCreate())
            throw new AccessDeniedException();

        Util util = new Util();
        Asset asset = null;

        DefaultResponse response=new DefaultResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function to add usage in asset. AssetUUID: " + request.getUsage().getAssetUUID());

            asset=assetRepository.findAssetByUuid(request.getUsage().getAssetUUID());
            request.getUsage().setAssetUUID(asset.getUuid());
            request.getUsage().setCreatedAt(new Date());
            asset.addUsage(request.getUsage());
            assetRepository.save(asset);
            response.setResponseIdentifier("Success");
            response.setDescription("Usage added successfully");
            response.setResponseCode("200");
            LOGGER.info("Successfully Added Usage");
        }
        catch (Exception e){

            LOGGER.error("Error while saving usage",e);
            response.setResponseIdentifier("Failure");
            response.setResponseCode("500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller");
            util.clearThreadContextForLogging();
            util = null;
            asset = null;
            request = null;
        }

        return response;

    }
    //edit usage
    DefaultResponse editUsage(EditUsageRequest request) throws IOException{
        Util util = new Util();

        DefaultResponse response=new DefaultResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function to edit usage in asset. AssetUUID: " + request.getUsage().getAssetUUID());
            usageRepository.save(request.getUsage());
            response.setResponseIdentifier("Success");
            response.setDescription("Usage edited successfully");
            response.setResponseCode("200");
            LOGGER.info("Successfully edited Usage");
        }
        catch (Exception e){

            LOGGER.error("Error while editing usage, details: request: "+convertToJSON(request),e);
            response.setResponseIdentifier("Failure");
            response.setResponseCode("500");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of edit Usage");
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return response;
    }
    //get usages by asset AMS_UC_27
    /*
     * this function will get a page of usages by asset uuid. asset uuid, offset and limit are passed to this function
     * this function finds the usages by asset and returns them
     */
//    @HasRead
    GetPaginatedUsagesByAssetResponse getPaginatedUsagesByAsset(String assetUUID,int offset,int limit) throws AccessDeniedException{

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetPaginatedUsagesByAssetResponse response=new GetPaginatedUsagesByAssetResponse();
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting paginated usages by asset. Asset UUID: "+assetUUID+" Offset: "+offset+"Limit: "+limit);

            response.setUsages(usageRepository.findByAssetUUIDOrderByCreatedAtDesc(assetUUID,new PageRequest(offset,limit)));
            response.setResponseIdentifier("Success");
            LOGGER.info("Paginated Usages by asset got successfully.");

        }catch(Exception e){

            LOGGER.error("Error while getting paginated usages by asset. Asset UUID: "+assetUUID,e);
            response.setResponseIdentifier("Failure");
            e = null;
        }finally{
            LOGGER.info("Returning to controller of getting paginted usages by asset uuid");
            util.clearThreadContextForLogging();
            util = null;

        }

        return response;
    }

    //get paginated usages AMS_UC_28
    /*
     * this function is used to get usages by tenant or filter usages by asset uuid and date
     */
//    @HasRead
    GetPaginatedUsagesResponse getPaginatedUsages(GetPaginatedUsagesRequest request) throws IOException,AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
        GetPaginatedUsagesResponse response = new GetPaginatedUsagesResponse();
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of getting paginated usages. TenantUUID: " + request.getTenantUUID() + " Offset: " + request.getOffset() + " Limit: " + request.getLimit() + "  AssetUUID: " + request.getAssetUUID() + " Start Date: " + request.getStartDate() + " End Date: " + request.getEndDate());


            response.setUsages(usageRepository.filterUsages(request.getAssetUUID(), request.getTenantUUID(), request.getCategory(), request.getStartDate(), request.getEndDate(), new PageRequest(request.getOffset(), request.getLimit())));
            response.setResponseIdentifier("Success");
            LOGGER.info("Usages got successfully");

        } catch (Exception e) {

            LOGGER.error("Error while getting paginated usages, details: request: " + convertToJSON(request), e);
            response.setResponseIdentifier("Failure");
            e = null;
        } finally {
            LOGGER.info("Returning to controller of get paginated usages");
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return response;
    }

    public GetPaginatedDataForSDTResponse getPaginatedUsagesForSDT(GetPaginatedDataForSDTRequest request) throws ApplicationException,IOException{
        Util util = new Util();
        GetPaginatedDataForSDTResponse response=new GetPaginatedDataForSDTResponse();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;
        List<Predicate> clauses = null;

        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside Service function of getting page of Usages for SDT,details:"+convertToJSON(request));
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Long.class);

            root = query.from(Usage.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));

            // Add filters
            clauses = addFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());

            response.getSdtData().put(TOTAL_ELEMENTS,(Long)entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

            List<Usage> usages = entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))
                    .orderBy(
                            (javax.persistence.criteria.Order) CriteriaBuilder.class.getDeclaredMethod(request.getSortDirection(), Expression.class)
                                    .invoke(criteriaBuilder,root.get(request.getSortField()))
                    ))
                    .setFirstResult(request.getLimit() * request.getOffset())
                    .setMaxResults(request.getLimit())
                    .getResultList();
            root=null;
            clauses=null;
            query=null;
            query = criteriaBuilder.createQuery(Long.class);

            root = query.from(Asset.class);

            clauses = new ArrayList<>();
            clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));
            List<Asset> assets=entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))).getResultList();
            clauses.clear();

            // Construct Rows List
            response.getSdtData().put(CONTENT,new ArrayList<>());

            root=null;
            query=null;
            query = criteriaBuilder.createQuery(Long.class);
            root = query.from(Category.class);



            for(Usage usage:usages) {
                ((ArrayList) response.getSdtData().get(CONTENT)).add(new HashMap<>());
                for (Asset asset : assets) {
                    if (asset.getUuid().equals(usage.getAssetUUID())) {
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("primaryUsageUnit", asset.getPrimaryUsageUnit());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("secondaryUsageUnit", asset.getSecondaryUsageUnit());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("createdAt", usage.getCreatedAt());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("primaryUsageValue", usage.getPrimaryUsageValue());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("secondaryUsageValue", usage.getSecondaryUsageValue());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("category", usage.getCategory());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("id", usage.getId());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetName", asset.getName());
                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetNumber", asset.getAssetNumber());
                        clauses.add(criteriaBuilder.equal(root.get("uuid"),asset.getCategoryUUID()));


                        ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetCategory", (String)entityManager.createQuery(query.select(root.get("name")).where(clauses.toArray( new Predicate[]{}))).getSingleResult());
                        clauses.clear();
                        break;
                    }
                }
            }

                response.getSdtData().put(TOTAL_PAGES, ((Long) response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);

                if ((Long) response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                    response.getSdtData().replace(TOTAL_PAGES, (Long) response.getSdtData().get(TOTAL_PAGES) - 1);

                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Page of Usages for SDT got successfully. Returning it to controller");


            }catch(Exception e){
                LOGGER.error("An Error occurred in getting page of Usages for SDT, "+convertToJSON(request),e);

            }finally{
                util.clearThreadContextForLogging();
                query = null;
                root = null;
                clauses = null;
                criteriaBuilder = null;
                util = null;
            }

            return response;
        }



        //get paginated usages by asset uuids AMS_UC_33
//        @HasRead
        GetPaginatedUsagesByAssetsAndCategoryResponse getPaginatedUsagesByAssetsAndType
        (GetPaginatedUsagesByAssetsAndCategoryRequest request) throws AccessDeniedException{

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

            Util util = new Util();
            GetPaginatedUsagesByAssetsAndCategoryResponse response=new GetPaginatedUsagesByAssetsAndCategoryResponse();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of fetching paginated usages by asset and category, details: request: "+convertToJSON(request));
                response.setUsages(usageRepository.findByAssetUUIDInAndCategory(request.getAssetUUIDS(),request.getCategory(),new PageRequest(request.getOffset(),request.getLimit())));
                response.setResponseIdentifier("Success");
                LOGGER.info("Page of usages by asset uuids got successfully");

            }catch (Exception e){
                LOGGER.error("Error while getting paginated usages by asset uuids. Offset: "+request.getOffset()+" Limit: "+request.getLimit()+" and category: "+request.getCategory());
                response.setResponseIdentifier("Failure");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of getting paginated usages by asset nd category");
                util.clearThreadContextForLogging();
                util = null;
                request = null;
            }

            return response;
        }


        //get usage by id
//        @HasRead
        GetUsageByIdResponse getUsageById(Long id) throws AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        Util util = new Util();
            GetUsageByIdResponse response = new GetUsageByIdResponse();
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method to get Usages by id: " + id);
                response.setUsage(usageRepository.findOne(id));
                response.setResponseIdentifier("Success");
                LOGGER.info("Usage by id got successfully");

            } catch (Exception e) {
                LOGGER.error("Error while getting usage by id: " + id, e);
                response.setResponseIdentifier("Failure");
                e = null;
            } finally {
                LOGGER.info("Returning to controller of getting usages");
                util.clearThreadContextForLogging();
                util = null;
            }

            return response;
        }
        /******************************************* END Usages Functions ****************************************************/

        /******************************************** Inspection Template Functions **************************************/
        //post inspection template AMS_UC_15
    /*
    This function adds a inspection template of a category
    Category uuid and inspection template object is passed to this function
    First, we find the category with that uuid
    Then, we set that category as the parent of inspection template
    Then, we set uuid of inspection template and it's children and also we set the parent of children so that children are saved automatically when parent is saved
    Then, we save the inspection template
     */
//        @HasCreate
        DefaultResponse postInspectionTemplate(PostInspectionTemplateRequest postInspectionTemplateRequest) throws IOException,AccessDeniedException {

            if(!privilegeHandler.hasCreate())
                throw new AccessDeniedException();

            Util util = new Util();
            Category category = null;
            DefaultResponse response = null;

            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of adding Inspection Template, details: request: "+convertToJSON(postInspectionTemplateRequest));;
                //get category from db
                category = null;
                if (postInspectionTemplateRequest.getCategoryId() != null) {
                    category = categoryRepository.findCategoryByUuid(postInspectionTemplateRequest.getCategoryId());
                    postInspectionTemplateRequest.getInspectionTemplate().setCategory(category);
                    category.setInspectionTemplate(postInspectionTemplateRequest.getInspectionTemplate());
                } else {
                    throw new Exception("category uuid is not given for inspection template");
                }
                //setting uuid
                postInspectionTemplateRequest.getInspectionTemplate().setUuid(UUID.randomUUID().toString());
                //setting parent of children and also setting uuid
                for (InspectionItemCategory inspectionItemCategory : postInspectionTemplateRequest.getInspectionTemplate().getInspectionItemCategories()) {
                    inspectionItemCategory.setInspectionTemplateUUID(postInspectionTemplateRequest.getInspectionTemplate().getUuid());
                    inspectionItemCategory.setUuid(UUID.randomUUID().toString());
                    for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                        inspectionItem.setInspectionItemCategoryUUID(inspectionItemCategory.getUuid());
                        inspectionItem.setUuid(UUID.randomUUID().toString());
                    }
                }
                //saving in db
                categoryRepository.save(category);
                LOGGER.info("Inspection Template Added Successfully");
                response = new DefaultResponse("Success", "Inspection Template Added Successfully", "200", postInspectionTemplateRequest.getInspectionTemplate().getUuid());
            } catch (Exception e) {
                LOGGER.error("Error while adding inspection template, details: request: "+convertToJSON(postInspectionTemplateRequest), e);
                response = new DefaultResponse("Failure", "Error while adding inspection template. Reason: " + e.getMessage(), "500");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of getting Inspection Template");
                util.clearThreadContextForLogging();
                util = null;
                category = null;
                postInspectionTemplateRequest = null;
            }

            return response;
        }
        //Get Inspection Template AMS_UC_16
    /*
    This function gets an inspection template by uuid
    uuid of inspection template is passed to this function
    then, we get inspection template with that uuid from db and return it
     */
//        @HasRead
        GetInspectionTemplateResponse getInspectionTemplate(String uuid) throws AccessDeniedException {

            if(!privilegeHandler.hasRead())
                throw new AccessDeniedException();

            Util util = new Util();
            GetInspectionTemplateResponse response = new GetInspectionTemplateResponse();
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method to get Inspection Template of uuid: "+uuid);
                response.setInspectionTemplate(inspectionTemplateRepository.findInspectionTemplateByUuid(uuid));
                response.setResponseIdentifier("Success");
                LOGGER.info("Received Inspection Template from database. Sending it to controller");

            } catch (Exception e) {

                LOGGER.error("Error while getting inspection template of uuid: "+uuid, e);
                response.setResponseIdentifier("Failure");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of get Inspection Template");
                util.clearThreadContextForLogging();
                util = null;
            }

            return response;
        }
        //edit inspection template AMS_UC_17
    /*
    This functione edits an inspection template
    It works same as add inspection template function. Category uuid and inspection template object is passed to this function
    First, we get the category with that uuid from db and set it as a parent of inspection template
    Then, we set the parent of children of inspection template so that they are also updated when parent is updated
    Then, we save that object which will update the object and return the updated object
    You will notice an extra if in the code which will only be executed if request is sent through Test Library.
    This is because in Test Library, we don't know the id of created object, we only know it's uuid as uuid is returned with default response
    when a object is created therefore, this additional code sets the id of the object. If we don't set the id of object then the old inspection template
    won't be updated instead a new one will be created
     */
//        @HasUpdate
        EditInspectionTemplateResponse editInspectionTemplate(EditInspectionTemplateRequest editInspectionTemplateRequest) throws IOException, AccessDeniedException {

            if(!privilegeHandler.hasUpdate())
                throw new AccessDeniedException();

            Util util = new Util();
            Category category = null;

            EditInspectionTemplateResponse response = new EditInspectionTemplateResponse();
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of editing Inspection Template, details: request: "+convertToJSON(editInspectionTemplateRequest));
                //get category from db
                category = null;
                if (editInspectionTemplateRequest.getCategoryId() != null) {
                    category = categoryRepository.findCategoryByUuid(editInspectionTemplateRequest.getCategoryId());
                    editInspectionTemplateRequest.getInspectionTemplate().setCategory(category);
                    category.setInspectionTemplate(editInspectionTemplateRequest.getInspectionTemplate());
                } else {
                    throw new Exception("category uuid is not given for inspection template");
                }
                //if id of inspection template is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
                //This if is for Test Library only
                if (editInspectionTemplateRequest.getInspectionTemplate().getId() == null) {
                    InspectionTemplate inspectionTemplate = inspectionTemplateRepository.findInspectionTemplateByUuid(editInspectionTemplateRequest.getInspectionTemplate().getUuid());
                    editInspectionTemplateRequest.getInspectionTemplate().setId(inspectionTemplate.getId());
                    editInspectionTemplateRequest.getInspectionTemplate().setInspectionItemCategories(inspectionTemplate.getInspectionItemCategories());
                    inspectionTemplate = null;
                }
                //setting parent of all children
                for (InspectionItemCategory inspectionItemCategory : editInspectionTemplateRequest.getInspectionTemplate().getInspectionItemCategories()) {
                    inspectionItemCategory.setInspectionTemplateUUID(editInspectionTemplateRequest.getInspectionTemplate().getUuid());
                    for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                        inspectionItem.setInspectionItemCategoryUUID(inspectionItemCategory.getUuid());
                    }
                }
                //saving in db
                categoryRepository.save(category);
                response.setInspectionTemplate(editInspectionTemplateRequest.getInspectionTemplate());
                response.setResponseIdentifier("Success");
                LOGGER.info("Inspection Template Edited Successfully");

            } catch (Exception e) {

                LOGGER.error("Error while editing inspection template, details: editInspectionTemplate: "+convertToJSON(editInspectionTemplateRequest), e);
                response.setResponseIdentifier("Failure");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of editing Inspection Template");
                util.clearThreadContextForLogging();
                util = null;
                category = null;
                editInspectionTemplateRequest = null;
            }

            return response;
        }
        //delete inspection template AMS_UC_18
    /*
    This function will delete a inspection template
    uuid of inspection template is passed
    First, we get the inspection template with that uuid
    Then, we set it's parent to null because we don't want to delete it's parent alongwith the children
    Then, we delete inspection template by id
     */
//        @HasDelete
        DefaultResponse deleteInspectionTemplate(String uuid) throws AccessDeniedException {

            if(!privilegeHandler.hasDelete())
                throw new AccessDeniedException();

            Util util = new Util();
            DefaultResponse response = null;

            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method for deleting Inspection Template of uuid: "+uuid);
//            InspectionTemplate inspectionTemplate = inspectionTemplateRepository.findInspectionTemplateByUuid(id);
//            //setting parent of inspection template to null to not delete the parent alongwith the children
//            inspectionTemplate.setCategory(null);
//            inspectionTemplateRepository.save(inspectionTemplate);
                //deleting
                inspectionTemplateRepository.deleteByUuid(uuid);
                LOGGER.info("Inspection Template deleted Successfully");
                response = new DefaultResponse("Success", "Inspection Template deleted Successfully", "200");
            } catch (Exception e) {
                LOGGER.error("Error while deleting inspection template of uuid: "+uuid, e);
                response = new DefaultResponse("Failure", "Error while deleting inspection template. Reason: " + e.getMessage(), "500");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of deleting Inspection Template");
                util.clearThreadContextForLogging();
                util = null;
            }

            return response;
        }
        /********************************************END Inspection Template Functions ***********************************/
        /********************************************Activity Wall Functions**********************************************/
        //add message in an activity wall AMS_UC_19
    /*
    This function will add a message in an activity wall of an asset
    Asset uuid and message object is passed to this function
    First, We find the asset with that asset uuid and set parent of message (Set activity wall of asset as parent)
    Then, we set the uuid of asset and save it in db
    */
//        @HasCreate
        DefaultResponse addMessage(AddMessageRequest addMessageRequest) throws IOException,AccessDeniedException {

            if(!privilegeHandler.hasCreate())
                throw new AccessDeniedException();

            Util util = new Util();
            DefaultResponse response = null;
            ActivityWall activityWall = null;
            Message message = null;
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of adding Message, details: request: "+convertToJSON(addMessageRequest));

                //if activity wall uuid is passed in the request then set parent of message
                if (addMessageRequest.getAssetWallUUID() != null) {
                    activityWall = activityWallRepository.findActivityWallByUuid(addMessageRequest.getAssetWallUUID());
                    message=new Message();
                    message.setUuid(UUID.randomUUID().toString());
                    message.setUserUUID(addMessageRequest.getUserUUID());
                    message.setUserName(addMessageRequest.getUserName());
                    message.setMessageTime(new Date());
                    message.setPriority("normal");
                    message.setMessageBody(addMessageRequest.getMessageBody());
                    message.setReadStatus(false);
                    activityWall.getMessages().add(message);
                    activityWallRepository.save(activityWall);

                    LOGGER.info("Message Added Successfully");
                    response = new DefaultResponse("Success", "Message Added Successfully", "200", message.getUuid());
                } else {
                    LOGGER.error("Wall uuid is not passed in the request");
                    response = new DefaultResponse("Failure", "asset wall uuid is not passed in the request", "500");
                }
            } catch (Exception e) {
                LOGGER.error("Error while adding message, details: request: "+convertToJSON(addMessageRequest), e);
                response = new DefaultResponse("Failure", "Error while adding message. Reason " + e.getMessage(), "500");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of adding Message");
                util.clearThreadContextForLogging();
                util = null;
                message = null;
                activityWall = null;
                addMessageRequest = null;
            }

            return response;
        }
        //edit message in activity wall AMS_UC_20
    /*
    This function updated a message of activity wall
    asset uuid and message object is passed to this function
    First, we find the asset with that asset uuid and set the parent of message(Activity wall of that asset)
    Then, we save the message and return updated message
    You will notice an extra if in the code which will only be executed if request is sent through Test Library.
    This is because in Test Library, we don't know the id of created object, we only know it's uuid as uuid is returned with default response
    when a object is created therefore, this additional code sets the id of the object. If we don't set the id of object then the old message
    won't be updated instead a new one will be created
     */
//        @HasUpdate
        EditMessageResponse editMessage(EditMessageRequest editMessageRequest) throws AccessDeniedException {

            if(!privilegeHandler.hasUpdate())
                throw new AccessDeniedException();

            Util util = new Util();
            Asset asset = null;
            EditMessageResponse response = new EditMessageResponse();
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of editing message uuid: "+editMessageRequest.getMessage().getUuid());

                //if activity wall uuid is passed in the request then set parent of message
                if (editMessageRequest.getAssetId() != null) {
                    asset = assetRepository.findAssetByUuid(editMessageRequest.getAssetId());
//                editMessageRequest.getMessage().setActivityWall(asset.getActivityWall());
//                //This if will be executed for test library
//                if (editMessageRequest.getMessage().getId() == null) {
//                    for (Message message : asset.getActivityWall().getMessages()) {
//                        if (message.getUuid().equals(editMessageRequest.getMessage().getUuid())) {
//                            editMessageRequest.getMessage().setId(message.getId());
//                        }
//                    }
//                }//TODO:editMessage function to be converted to new activity style
                } else {
                    throw new Exception("Asset uuid is not provided in the request");
                }
                //saving it in db
                messageRepository.save(editMessageRequest.getMessage());
                LOGGER.info("Message Edited Successfully");
                response.setMessage(editMessageRequest.getMessage());
                response.setResponseIdentifier("Success");

            } catch (Exception e) {
                LOGGER.error("Error while editing message", e);
                response.setResponseIdentifier("Failure");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of editing message");
                util.clearThreadContextForLogging();
                util = null;
                editMessageRequest = null;
                asset = null;
            }

            return response;
        }
        //delete message from activity wall AMS_UC_21
    /*
    This function will delete message of an activity wall
    message uuid is passed to this function
    First, we get that message from db
    Then, we set the parent of message to null because we don't want to delete the parent alongwith the children
    we save this change and then delete the message
     */
//        @HasDelete
        DefaultResponse deleteMessage(String uuid) throws AccessDeniedException {

            if(!privilegeHandler.hasDelete())
                throw new AccessDeniedException();

            Util util = new Util();
            DefaultResponse response = null;

            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method for deleting message of uuid: "+uuid);
                //get message by uuid
//            message = messageRepository.findMessageByUuid(id);
                //saving the change
//            messageRepository.save(message);
                //now deleting it
                messageRepository.deleteByUuid(uuid);
                LOGGER.info("Message deleted Successfully");
                response = new DefaultResponse("Success", "Message deleted Successfully", "200");
            } catch (Exception e) {
                LOGGER.error("Error while deleting message of uuid: "+uuid, e);
                response = new DefaultResponse("Failure", "Ërror while deleting message", "500");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of deleting message");
                util.clearThreadContextForLogging();
                util = null;
            }

            return response;
        }
//        @HasCreate
        DefaultResponse addReplyToMessage(AddReplyRequest request) throws ApplicationException,AccessDeniedException {

            if(!privilegeHandler.hasCreate())
                throw new AccessDeniedException();

            Util util = new Util();
            Message message = null;
            Reply reply = null;
            DefaultResponse response = null;

            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In servie method for adding reply, details: request; "+convertToJSON(request));
                message=messageRepository.findMessageByUuid(request.getMessageUUID());
                if(message!=null){
                    reply=new Reply();
                    reply.setMessageBody(request.getMessageBody());
                    reply.setReadStatus(false);
                    reply.setReplyTime(new Date());
                    reply.setMessageUUID(message.getUuid());
                    reply.setUserName(request.getUserName());
                    reply.setUserUUID(request.getUserUUID());
                    reply.setUuid(UUID.randomUUID().toString());
                    replyRepository.save(reply);
                    response = new DefaultResponse("Success","Reply added successfully!","200",reply.getUuid());
                    LOGGER.info("Successfully add reply to message uuid: "+message.getUuid());
                }
                else{
                    response = new DefaultResponse("Failure","Message not found!","404");
                }
            }catch(DataAccessException e){
                throw new ApplicationException("Error occurred while adding reply to a message.",e);
            }
            catch(Exception e){
                throw new ApplicationException("Unexpected error occurred while adding reply to a message",e);
            }finally{
                LOGGER.info("Returning to controller of adding reply");
                util.clearThreadContextForLogging();
                util = null;
                reply = null;
                message = null;
                request = null;
            }

            return response;
        }
        /********************************************END Activity Wall Functions*******************************************/
        /******************************************** s3 Functions *********************************************************/
        //upload file to s3
//    private String uploadFile(String byteArray,int index) throws IOException {
//        String fileName = generateFileName(index);
//        String fileUrl=s3EnpointUrl+"/asset-images/"+fileName;
////        File byteFile=new File("asset_image_byte_array_"+index);
//        File file=new File("asset_image_"+index+".png");
//        try {
////            os.write(byteArray.getBytes());
//            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray.getBytes());
//            OutputStream os = new FileOutputStream(file);
//            IOUtils.copy(bis,os);
//
////            BufferedImage image = ImageIO.read(byteFile);
////            ImageIO.write(image, "png", file);
//
//            this.s3client.putObject(new PutObjectRequest(this.bucket+"/asset-images",fileName,file));
//            return fileUrl;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            LOGGER.error("Error while uploading asset image",e);
//            return null;
//        }
//        finally {
////            os.close();
//            file.delete();
////            byteFile.delete();
//        }
//    }
//        @HasCreate
        public UploadFileResponse uploadFile(MultipartFile file) throws IOException, AccessDeniedException {

            if(!privilegeHandler.hasCreate())
                throw new AccessDeniedException();

            Util util = new Util();
            UploadFileResponse response = null;
            File convFile = null;
            FileOutputStream fos = null;
            String fileName = null;
            String fileUrl = null;
            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of uploading file name : "+file.getName());
                convFile = new File(file.getOriginalFilename());
                convFile.createNewFile();
                fos = new FileOutputStream(convFile);
                fos.write(file.getBytes());
                fos.close();
                fileName = generateFileName(file.getOriginalFilename());
                fileUrl = s3EnpointUrl + "/asset-images/" + fileName;
                this.s3client.putObject(new PutObjectRequest(this.bucket + "/asset-images", fileName, convFile));
                response = new UploadFileResponse();
                response.setResponseIdentifier("Success");
                response.setFileUrl(fileUrl);
                response.setFileName(fileName);
                LOGGER.info("File uploaded Successfully");
                convFile.delete();

            } catch (Exception e) {
                LOGGER.error("Error while uploading file to s3, details: file: "+convertToJSON(file), e);

                response.setResponseIdentifier("Failure");
                convFile.delete();
                e = null;
            }finally{
                LOGGER.info("Returning to controller of uploading file.");
                util.clearThreadContextForLogging();
                util = null;
                convFile = null;
                file = null;
                fos = null;
                fileName = null;
                fileUrl = null;
            }

            return response;
        }
//        @HasDelete
        public DefaultResponse removeFile(String fileName) throws AccessDeniedException {

            if(!privilegeHandler.hasDelete())
                throw new AccessDeniedException();

            Util util = new Util();
            DefaultResponse response = new DefaultResponse();


            try {
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In service method of removing file :"+fileName);
                this.s3client.deleteObject(new DeleteObjectRequest(this.bucket+"/asset-images",fileName));
                response.setResponseIdentifier("Success");
                response.setDescription("Successfully deleted file!");
                LOGGER.info("File deleted Successfully");

            } catch (Exception e) {
                LOGGER.error("Error while deleting file from s3", e);
                response.setResponseIdentifier("Failure");
                response.setDescription("Failed to delete file!");
                e = null;
            }finally{
                LOGGER.info("Returning to controller of removing file");
                util.clearThreadContextForLogging();
                util = null;
            }

            return response;
        }
        /******************************************** END s3 Functions *********************************************************/

        /******************************************* Class Functions *****************************************************/
        //generate asset number
        private String genrateAssetNumber(Long id) {
            String assetNumber = "6";
            String formatted = String.format("%06d", id);
            return assetNumber + formatted;
        }
        private String generateFileName(String filename) {
            return new Date().getTime() + "-" + "asset_image_" + filename;
        }
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();

            dataSource.setDriverClassName(dataSourceDriver);
            dataSource.setUrl(dataSourceUrl);
            dataSource.setUsername(dataSourceUserName);
            dataSource.setPassword(dataSourcePassword);
            return dataSource;
        }
        /******************************************* END Class Functions **************************************************/
        /******************************************* Kafka Functions ******************************************************/
        //this function will add the usage units of asset e.g odometer meter values
        public void updateUsageUnits(Usage usage) throws IOException{
            Asset asset = null;
            try {
                asset = assetRepository.findAssetByUuid(usage.getAssetUUID());
                //whether to update primary usage unit and secondary usage unit
                boolean update = false;
                if (usage.getPrimaryUsageValue() != null) {
                    update = true;
                }
                if (usage.getSecondaryUsageValue() != null) {
                    update = true;
                }
                //update
                if (update) {
                    usage.setCategory("normal");//default category is normal when usage is added through inspection
                    asset.addUsage(usage);
                    usage.setAssetUUID(asset.getUuid());
                    usage.setCreatedAt(new Date());
                    usage.setTenantUUID(asset.getTenantUUID());
                    assetRepository.save(asset);
                }
            } catch (Exception e) {
                LOGGER.error("Error while updating usage units of asset, details: usage: "+convertToJSON(usage), e);
                e = null;
            }finally{
                asset = null;
                usage = null;
            }
        }
        /*******************************************END Kafka Functions ******************************************************/
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
        /************************************Start Inventory Asset Function********************************************/

        public static Date setTimeToEnd(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        public String convertToJSON (Object obj) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        }

        //HTTP client from inspections
        public AssetBasicDetailModelResponse fetchAssetNameAndType(String tenantUUID){

            AssetBasicDetailModelResponse response = new AssetBasicDetailModelResponse();

            try{
                LOGGER.info("In method to fetch List of Asset Name and Type");
                // getting assets data for inspection reports,service requests
                List<AssetBasicDetailModel> assetBasicDetailModels = assetRepository.findAssetBasicDetailByTenantUUID(tenantUUID);
                List<AssetBasicDetailModel> categories = categoryRepository.findCategoriesByTenantUUID(tenantUUID);
                response.setAssets(assetBasicDetailModels);
                response.getAssets().addAll(categories);
                LOGGER.info("Fetched List of Asset Name and Type.");
            }catch(Exception e){
                LOGGER.error("Error while fetching List of Asset Name and Type",e);
                e = null;
            }

            return response;
        }

        /*****************************************************End Inventory Asset Function********************************************/
        /* Wriiten By Kumail Khan*/

        /**************************************** Asset Functions Start *****************************************/

//        @HasCreate
        @Transactional
        public DefaultResponse addAssetGroup(AddAssetGroupRequest request) throws IOException,AccessDeniedException{

            if(!privilegeHandler.hasCreate())
                throw new AccessDeniedException();

            LOGGER.debug("Inside service function, to add asset group");
            DefaultResponse response=new DefaultResponse();
            try {
                Set<Asset> assets=assetRepository.findAssetsByUuidIn(request.getAssetUUIDs());
                AssetGroup assetGroup=new AssetGroup();
                assetGroup.setCreatedByUserName(request.getCreatedByUserName());
                assetGroup.setCreatedByUserUUID(request.getCreatedByUserUUID());
                assetGroup.setAssetCategory(request.getCategory());
                assetGroup.setCreatedAt(new Date());
                assetGroup.setAssets(new HashSet<>());
                assetGroup.setGroupName(request.getGroupName());
                assetGroup.setTenantUUID(request.getTenantUUID());

                for(Asset asset:assets){
                    assetGroup.getAssets().add(asset);
                }

                assetGroup.setTotalMembers(assets.size());
                assetGroup.setUuid(UUID.randomUUID().toString());
                assetGroupRepository.save(assetGroup);
                response.setResponseIdentifier("Success");
                response.setObjectId(assetGroup.getUuid());

            }catch (Exception e){
                LOGGER.error("An Error occurred while saving Asset groups, request: "+convertToJSON(request),e);
                response.setResponseIdentifier("Failure");
                response.setDescription("Failed to save");
            }

            return response;
        }

//        @HasUpdate
        public DefaultResponse updateAssetGroup(EditGroupAssetsRequest request) throws ApplicationException,IOException,AccessDeniedException{

            if(!privilegeHandler.hasUpdate())
                throw new AccessDeniedException();

            Util util = new Util();
            DefaultResponse response = null;
            AssetGroup assetGroup = null;
            Set<Asset> newAssets = null;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Inside Service method of updating asset group.Details: " + convertToJSON(request));
                assetGroup = assetGroupRepository.findAssetGroupByUuid(request.getGroupUUID());
                if(assetGroup==null){
                    return new DefaultResponse("Failure","Cannot find existing asset group","F01");
                }
                assetGroup.getAssets().clear();
                newAssets = assetRepository.findAssetsByUuidIn(request.getAssetUUIDs());
                for(Asset newAsset: newAssets){
                    assetGroup.getAssets().add(newAsset);
                }
                assetGroup.setGroupName(request.getGroupName());
                assetGroup.setLastEditedByUserName(request.getEditedByUserName());
                assetGroup.setLastEditedByUserUUID(request.getEditedByUserUUID());
                assetGroup.setTotalMembers(assetGroup.getAssets().size());
                assetGroupRepository.save(assetGroup);

                response = new DefaultResponse("Success","Successfully update user group by asset groupUUID: " + request.getGroupUUID(),"S01");
            }catch (Exception e){
                throw new ApplicationException("An Error Occurred while updating asset group.",e);
            }finally {
                LOGGER.info("Returning to controller of edit asset group.");
                util = null;
                assetGroup = null;
                newAssets = null;
            }

            return response;
        }


//    @HasRead
    public GetAssetGroupResponse getAssetGroupByUUID(String uuid) throws AccessDeniedException{

            if(!privilegeHandler.hasRead())
                throw new AccessDeniedException();

        Util util = new Util();
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery query = null;
        Root root = null;


        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function to get asset group by uuid");

            AssetGroup assetGroup=assetGroupRepository.findAssetGroupByUuid(uuid);

            // Organize Assets into Categories
            List<Category> categories = new ArrayList<>();

            categories.add(new Category());

            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Long.class);
            root = query.from(Category.class);

            int groupMembers = 0;
            Set<Asset> assets = new HashSet<>();
            for(Asset asset : assetGroup.getAssets())
                assets.add(asset);

            for(Asset asset : assetGroup.getAssets()){
                groupMembers = assets.size();
                for(Category category : categories){
                    if(category.getUuid() == null){
                        category.setUuid(asset.getCategoryUUID());
                        category.setName((String)entityManager.createQuery(query.select(root.get("name")).where(criteriaBuilder.equal(root.get("uuid"),category.getUuid()))).getSingleResult());
                        asset.setName(asset.getName()+" - "+category.getName());
                        category.getAssets().add(asset);
                        assets.remove(asset);
                    }else if(category.getUuid().equals(asset.getCategoryUUID())){
                        asset.setName(asset.getName()+" - "+category.getName());
                        category.getAssets().add(asset);
                        assets.remove(asset);
                    }
                }

                if(groupMembers == assets.size()) { // Category did not exist, so add missing category.
                    categories.add(new Category());
                    categories.get(categories.size()-1).setUuid(asset.getCategoryUUID());
                    categories.get(categories.size()-1).setName((String)entityManager.createQuery(query.select(root.get("name")).where(criteriaBuilder.equal(root.get("uuid"),asset.getCategoryUUID()))).getSingleResult());
                    asset.setName(asset.getName()+" - "+categories.get(categories.size()-1).getName());
                    categories.get(categories.size()-1).addAsset(asset);
                    assets.remove(asset);
                }
                if(assets.size() == 0)
                    break;

            }

            // Construct Custom Response
            HashMap<String,Object> response = new HashMap<>();
            response.put("uuid",assetGroup.getUuid());
            response.put("groupName",assetGroup.getGroupName());
            response.put("lastEditedByUserUUID",assetGroup.getLastEditedByUserUUID());
            response.put("lastEditedByUserName",assetGroup.getLastEditedByUserName());
            response.put("categories",categories);

            LOGGER.info("Successfully got Asset Group of uuid: "+uuid);

            return new GetAssetGroupResponse("Success",response);
        }catch (Exception e){
            LOGGER.error("Error occurred while getting an Asset Group, uuid: "+uuid,e);
            return new GetAssetGroupResponse("Failure",null);
        }
    }

//    @HasDelete
    public DefaultResponse deleteAssetGroup(String uuid,String type) throws AccessDeniedException {

            if(!privilegeHandler.hasDelete())
                throw new AccessDeniedException();

        Util util = new Util();
        util.setThreadContextForLogging(scim2Util);
        DefaultResponse response = null;
        try {
            LOGGER.info("Inside service function of deleting inspection template with templateUUID: " + uuid );
            //find by uuid
            AssetGroup assetGroup = assetGroupRepository.findAssetGroupByUuid(uuid);
            if(type.equals("archive")){
                assetGroup.setArchive(true);
                assetGroup.setDeletefromGroupUUID(assetGroup.getUuid());
                assetGroupRepository.save(assetGroup);
                LOGGER.info("Assets Group archived Successfully");
                response = new DefaultResponse("Success", "Assets Group archived Successfully", "200");
            }
            else if(type.equals("delete")){
                assetGroup.setDeletefromGroupUUID(assetGroup.getUuid());
                assetGroup.setUuid(null);
                assetGroupRepository.save(assetGroup);
                LOGGER.info("Assets Group deleted Successfully");
                response = new DefaultResponse("Success", "Assets Group deleted Successfully", "200");
            }


        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting Inspection Template", e);
            response = new DefaultResponse("Failure", e.getMessage(), "500");
        }finally {
            LOGGER.info("Returning to Controller");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    public GetPaginatedAssetGroupsResponse getPaginatedAssetGroups(GetPaginatedAssetGroupsRequest request) throws IOException, AccessDeniedException {

        if(!privilegeHandler.hasRead())
            throw new AccessDeniedException();

        LOGGER.debug("Inside service function to get paginated asset groups");

        GetPaginatedAssetGroupsResponse response=new GetPaginatedAssetGroupsResponse();
        try {

            Page<AssetGroup> assetGroups=assetGroupRepository.findAssetGroupByTenantUUIDOrderByCreatedAtDesc(request.getTenantUUID(),new PageRequest(request.getOffset(),request.getLimit()));
            //set assets to null to reduce size of response, assets are not needed in table, when viewing single group, then assets are required.
            for (AssetGroup assetGroup : assetGroups.getContent()) {
                assetGroup.setAssets(null);
            }
            response.setAssetGroups(assetGroups);
            response.setResponseIdentifier("Success");

        }catch (Exception e){
            LOGGER.error("Error occurred while getting paginated asset groups, request: "+convertToJSON(request),e);
            response.setResponseIdentifier("Failure");
        }
        return response;
    }

//    public AssetGroupsNameAndUUIDResponse getAssetGroupsNameAndUUIDByTenantUUID(String tenantUUID) throws AccessDeniedException, ApplicationException{
//        if(!privilegeHandler.hasRead()){
//            LOGGER.error("Access is Denied to read Asset group.");
//            throw new AccessDeniedException();
//        }
//        Util util = new Util();
//        AssetGroupsNameAndUUIDResponse response = null;
//        List<MinimalInfo.AssetGroupInfo> assetGroupInfos = null;
//        try{
//            util.setThreadContextForLogging(scim2Util);
//            LOGGER.info("Inside service function of get Asset groups name and uuid by tenant uuid: " + tenantUUID);
//            assetGroupInfos = assetGroupRepository.findAssetGroupByTenantUUIDAndDeletefromGroupUUIDIsNull(tenantUUID);
//            response = new AssetGroupsNameAndUUIDResponse();
//            response.setResponseIdentifer(SUCCESS);
//            response.setAssetGroupInfos(assetGroupInfos);
//            LOGGER.info("Successfully got Asset groups name and uuid by tenant uuid.");
//        }catch (Exception e){
//            LOGGER.error("An Error Occurred while getting Asset groups name and uuid by tenant uuid.",e);
//            throw new ApplicationException("An Error Occurred while getting Asset groups name and uuid by tenant uuid.",e);
//        }finally {
//            LOGGER.info("Returning to controller of get Asset groups name and uuid by tenant uuid.");
//            util.clearThreadContextForLogging();
//            util = null;
//        }
//        return response;
//    }

    /**************************************** Asset Functions End *****************************************/

        /*****************************************************Start Wallet Function***************************************************/
        public DefaultResponse createWallet (CreateWalletRequest wallet) throws ApplicationException {
            DefaultResponse response=null;
            Util util =new Util();
            Wallet walletType=new Wallet();
            Wallet previouswallet=new Wallet();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In Method of create Wallet in Asset Service"+" "+convertToJSON(wallet));
                try{
                    previouswallet=walletRespository.findByUserUUIDAndWalletType(wallet.getUserUUID(),wallet.getWalletType());
                    if(previouswallet == null){
                        if(wallet.getCapacity()>wallet.getThresholdValue()){
                            walletType.setWalletUUID(UUID.randomUUID().toString());
                            walletType.setDateTime(new Date());
                            walletType.setOrgUUID(wallet.getOrgUUID());
                            walletType.setProductType(wallet.getProductType());
                            walletType.setAssetUUID(wallet.getAssetUUID());
                            walletType.setCapacity(decimaltoTwoPoint(wallet.getCapacity()));
                            walletType.setCapacityUnit(wallet.getCapacityUnit());
                            walletType.setBalance(decimaltoTwoPoint(0.0));
                            walletType.setUserUUID(wallet.getUserUUID());
                            walletType.setWalletType(wallet.getWalletType());
                            walletType.setWalletName(wallet.getWalletName());
                            walletType.setThresholdType(wallet.getThresholdType());
                            walletType.setThresholdValue(wallet.getThresholdValue());
                            walletType.setAllowedNegative(wallet.getAllowedNegative());
                            walletRespository.save(walletType);
                            response=new DefaultResponse("Success","Wallet added Successfully","F200");
                        }else{
                            response=new DefaultResponse("Failure","Threshold value cannot be greater than or equal to capacity","F500");
                        }
                    }else{
                        response=new DefaultResponse("Failure","Wallet Exist in this Type","F500");
                    }
                }catch(Exception e){
                    return new DefaultResponse("Failure", "Wallet Not Added Successfully ", "F500");
                }
            }catch (Exception e)
            {
                throw new ApplicationException("An Error occurred while adding wallet",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to create Wallet in controller");
                util=null;
                wallet=null;
                walletType=null;
            }
            return response;
        }
        public DefaultResponse EditWallet (EditWalletRequest wallet) throws ApplicationException {
            DefaultResponse response=null;
            Util util =new Util();
            Wallet previouswallet=new Wallet();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("In Method of Edit  Wallet in Asset Service"+" "+convertToJSON(wallet));
                try{
                    previouswallet=walletRespository.findByWalletUUID(wallet.getWalletUUID());
                    if(!(previouswallet == null))
                    {
                        if(wallet.getCapacity()>wallet.getThresholdValue())
                        {
                            previouswallet.setAssetUUID(wallet.getAssetUUID());
                            previouswallet.setCapacity(decimaltoTwoPoint(wallet.getCapacity()));
                            previouswallet.setCapacityUnit(wallet.getCapacityUnit());
                            previouswallet.setUserUUID(wallet.getUserUUID());
                            previouswallet.setWalletName(wallet.getWalletName());
                            previouswallet.setThresholdType(wallet.getThresholdType());
                            previouswallet.setThresholdValue(wallet.getThresholdValue());
                            previouswallet.setAllowedNegative(wallet.getAllowedNegative());
                            walletRespository.save(previouswallet);
                            response=new DefaultResponse("Success","Wallet Edited Successfully","F200");
                        }else{
                            response=new DefaultResponse("Failure","Threshold value cannot be greater than or equal to capacity","F500");
                        }

                    }else{
                        response=new DefaultResponse("Failure","Wallet Not Exist","F500");
                    }
                }catch(Exception e){
                    return new DefaultResponse("Failure", "Wallet Not Edited Successfully ", "F500");
                }
            }catch (Exception e)
            {
                throw new ApplicationException("An Error occurred while Editing wallet",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to Editing Wallet in controller");
                util=null;
                wallet=null;
                previouswallet=null;
            }
            return response;
        }

    //archive or delete wallet by UUID qasim.....
        public  DefaultResponse  archiveOrDeleteWalletByUUID(String uuid, String type) throws ApplicationException,AccessDeniedException{

            if(!privilegeHandler.hasDelete())
                throw new AccessDeniedException();

            Util util = new Util();
            util.setThreadContextForLogging(scim2Util);

            DefaultResponse response=null;

            try {
                LOGGER.info("Inside service function of deleting inspection template with templateUUID: " + uuid );
                //find by uuid
               Wallet wallet  = walletRespository.findByWalletUUID(uuid);
                if(type.equals("archive")){
                    wallet.setArchived(true);
                    walletRespository.save(wallet);
                    LOGGER.info("Wallet archived Successfully");
                    response = new DefaultResponse("Success", "Wallet Archived Successfully", "200");
                }
                else if(type.equals("delete")){
                    wallet.setDeletedWalletUUID(wallet.getWalletUUID());
                    wallet.setWalletUUID(null);
                    walletRespository.save(wallet);
                    LOGGER.info("Wallet deleted Successfully");
                    response = new DefaultResponse("Success", "Wallet deleted Successfully", "200");
                }

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Error while deleting Wallet", e);
                response = new DefaultResponse("Failure", e.getMessage(), "500");
            }finally {
                LOGGER.info("Returning to Controller");
                util.clearThreadContextForLogging();
                util = null;
            }
            return response;
        }
        public Double decimaltoTwoPoint(Double value){
            BigDecimal bd=null;
            Double newInput=null;
            bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
            newInput = bd.doubleValue();
            return newInput;
        }
        public DefaultResponse addPurchase (AddPurchaseRequest purchaseRequest) throws ApplicationException {
            DefaultResponse response=null;
            Util util =new Util();
            Wallet wallet=new Wallet();
            Fact fact=new Fact();
            Page<Fact> factpage,factpage1;

            try{
                //count by wallet UUID and Transaction Type
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method Add Purchase in Asset service");
                wallet=walletRespository.findByWalletUUID(purchaseRequest.getWalletUUID());
                String walletUUID=wallet.getWalletUUID();
                long count=factRepository.countByWalletUUIDAndTransactiontypeIsNotContaining(wallet.getWalletUUID(),"spend");
                Double previousTotal=0.0;
                Double previousQuantity=wallet.getBalance();
                Double currentAverage=0.0;
                Double currentRate=0.0;
                if(count==0){
                    currentAverage=(purchaseRequest.getRate());
                }else{
                    factpage=factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletUUID,"spend",new PageRequest(0,1));
                    previousTotal=(factpage.getContent().get(0).getCurrentAverage()) * (wallet.getBalance());
                    Double currentTotal=purchaseRequest.getTotal()+previousTotal;
                    Double currentQuantity=purchaseRequest.getQuantity()+previousQuantity;
                    currentAverage=currentTotal/currentQuantity;
                }

                Double balance=wallet.getBalance()+purchaseRequest.getQuantity();
                Double capacity=wallet.getCapacity();
                if(balance<=capacity){
                    fact.setWalletUUID(wallet.getWalletUUID());
                    fact.setCurrentAverage(decimaltoTwoPoint(currentAverage));
                    fact.setVolume(decimaltoTwoPoint(wallet.getCapacity()));
                    fact.setTotal(decimaltoTwoPoint(purchaseRequest.getTotal()));
                    fact.setFactUUID(UUID.randomUUID().toString());
                    fact.setDateTime(new Date());
                    fact.setQuantity(decimaltoTwoPoint(purchaseRequest.getQuantity()));
                    fact.setQuantityUnit(purchaseRequest.getQuantityUnit());
                    fact.setRate(decimaltoTwoPoint(purchaseRequest.getRate()));
                    fact.setRateBasisUnit(purchaseRequest.getRateBasisUnit());
                    fact.setRateCurrency(purchaseRequest.getRateCurrency());
                    fact.setTransactiontype(purchaseRequest.getTransactionType());
                    fact.setUserUUID(purchaseRequest.getUserUUId());
                    fact.setDescription(purchaseRequest.getDescription());
                    factRepository.save(fact);
                    wallet.setBalance(decimaltoTwoPoint(balance));
                    walletRespository.save(wallet);
                    response=new DefaultResponse("Success","Purchase added Successfully","F200");
                }else{
                    response=new DefaultResponse("Success","Sorry,Exceeded Wallet Limit","F200");
                }
            }catch (Exception e)
            {
                throw new ApplicationException("An Error occurred while adding Purchase",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to Add Purchase in controller");
                util=null;
                wallet=null;
                fact=null;
                factpage=null;
                purchaseRequest=null;
            }
            return response;
        }
        public DefaultResponse addSpent (AddSpendRequest Spent) throws ApplicationException {
            DefaultResponse response=null;
            Util util =new Util();
            Wallet wallet=new Wallet();
            Fact fact=new Fact();
            Page<Fact> factpage;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method Add Spent in Asset service");
                wallet=walletRespository.findByWalletUUID(Spent.getWalletUUID());
                if(Spent.getQuantity()<wallet.getBalance()){
                    Double balance=wallet.getBalance()-Spent.getQuantity();
                    fact.setWalletUUID(wallet.getWalletUUID());
                    fact.setFactUUID(UUID.randomUUID().toString());
                    fact.setDateTime(new Date());
                    fact.setQuantity(decimaltoTwoPoint(Spent.getQuantity()));
                    fact.setQuantityUnit(Spent.getQuantityUnit());
                    fact.setTransactiontype(Spent.getTransactionType());
                    fact.setUserUUID(Spent.getUserUUId());
                    fact.setDescription(Spent.getDescription());
                    factRepository.save(fact);
                    wallet.setBalance(decimaltoTwoPoint(balance));
                    walletRespository.save(wallet);
                    sendEmail(wallet);
                    response=new DefaultResponse("Success","Spent added Successfully","F200");
                } else{
                    response=new DefaultResponse("Success","Sorry,Exceeded Wallet Balance Limit","F200");
                }
            }catch (Exception e)
            {
                throw new ApplicationException("An Error occurred while adding Spent",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to Add Spent in controller");
                util=null;
                wallet=null;
                fact=null;
                factpage=null;
                Spent=null;
            }
            return response;
        }
        public GetSpentResponse getSpent(String walletUUID,int offset,int limit) throws ApplicationException{
            GetSpentResponse response=null;
            Util util =new Util();
            List<Fact> fact=new ArrayList<>();
            Page<Fact> factpage=null;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Request get Transaction Method in Asset Service");
                factpage=factRepository.findByWalletUUIDAndTransactiontypeOrderByDateTimeDesc(walletUUID,"spend",new PageRequest(offset,limit));
                response=new GetSpentResponse();
                response.setResponseIdentifier("Success");
                response.setFactList(factpage.getContent());

            }catch(Exception e){
                response=new GetSpentResponse();
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while Get Transaction",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Transaction in Wallet Method in Asset Service");
                util=null;
                fact=null;
            }
            return response;

        }
        public getPurchasesResponse  getPurchase (String UserUUID,String Transactiontype) throws ApplicationException {
            getPurchasesResponse response=null;
            Util util =new Util();
            Wallet wallet=new Wallet();
            List<Fact> factList=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in get Purchase Method in Asset Service");
                wallet=walletRespository.findByUserUUID(UserUUID);
                String walletUUID=wallet.getWalletUUID();
                factList=factRepository.findAllByWalletUUIDAndTransactiontype(walletUUID,Transactiontype);
                long count=factList.stream().count();
                response=new getPurchasesResponse();
                response.setResponseIdentifier("Success");
                response.setFactList(factList);
                response.setTotalElements(count);
            }catch (Exception e)
            {
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Purchases",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Purchase in controller");
                util=null;
                wallet=null;
                factList=null;
                UserUUID=null;
                Transactiontype=null;
            }
            return response;
        }
        public DefaultResponse addRequest  (AddRequestInWallet addRequestInWallet) throws ApplicationException{
            DefaultResponse response=null;
            Util util =new Util();
            WalletRequest walletRequest=new WalletRequest();
            Wallet senderWallet=new Wallet();
            Wallet receiverWallet=new Wallet();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Add Request in Wallet Method in Asset Service"+addRequestInWallet.getRequestType());
                senderWallet=walletRespository.findByUserUUIDAndWalletType(addRequestInWallet.getSenderUUID(),addRequestInWallet.getWalletType());
                receiverWallet=walletRespository.findByUserUUIDAndWalletType(addRequestInWallet.getReceiverUUID(),addRequestInWallet.getWalletType());
                Double senderAvailableLimit=senderWallet.getCapacity()-senderWallet.getBalance();
                if(addRequestInWallet.getRequestType().equals("Transfer In")){
                    if(senderAvailableLimit>=addRequestInWallet.getQuantity()){
                        walletRequest.setApproveFlag(false);
                        walletRequest.setDateTime(new Date());
                        walletRequest.setOrgUUID(senderWallet.getOrgUUID());
                        walletRequest.setProduct(senderWallet.getProductType());
                        walletRequest.setQuantity(addRequestInWallet.getQuantity());
                        walletRequest.setReceiverWalletUUID(receiverWallet.getWalletUUID());
                        walletRequest.setSenderUUID(addRequestInWallet.getSenderUUID());
                        walletRequest.setRequestUUID(UUID.randomUUID().toString());
                        walletRequest.setSenderWalletUUID(senderWallet.getWalletUUID());
                        walletRequest.setRequestPriority("normal");
                        walletRequest.setWalletType(senderWallet.getWalletType());
                        walletRequest.setResponderUUID(addRequestInWallet.getReceiverUUID());
                        walletRequest.setIgnoreFlag(false);
                        walletRequest.setRequestType(addRequestInWallet.getRequestType());
                        walletRequest.setWithDrawFlag(false);
                        walletRequest.setDescription(addRequestInWallet.getDescription());
                        walletRequestRepository.save(walletRequest);
                        response=new DefaultResponse("Success","Request Added Successfully","F200");
                    }else{
                        response=new DefaultResponse("Success","Request Failed! Insufficient Available limit","F200");
                    }
                }else if(addRequestInWallet.getRequestType().equals("Transfer Out")){
                    if(senderWallet.getBalance()>=addRequestInWallet.getQuantity()){
                        walletRequest.setApproveFlag(false);
                        walletRequest.setDateTime(new Date());
                        walletRequest.setOrgUUID(senderWallet.getOrgUUID());
                        walletRequest.setProduct(senderWallet.getProductType());
                        walletRequest.setQuantity(addRequestInWallet.getQuantity());
                        walletRequest.setReceiverWalletUUID(receiverWallet.getWalletUUID());
                        walletRequest.setSenderUUID(addRequestInWallet.getSenderUUID());
                        walletRequest.setRequestUUID(UUID.randomUUID().toString());
                        walletRequest.setSenderWalletUUID(senderWallet.getWalletUUID());
                        walletRequest.setRequestPriority("normal");
                        walletRequest.setWalletType(senderWallet.getWalletType());
                        walletRequest.setResponderUUID(addRequestInWallet.getReceiverUUID());
                        walletRequest.setIgnoreFlag(false);
                        walletRequest.setRequestType(addRequestInWallet.getRequestType());
                        walletRequest.setWithDrawFlag(false);
                        walletRequest.setDescription(addRequestInWallet.getDescription());
                        walletRequestRepository.save(walletRequest);
                        response=new DefaultResponse("Success","Request Added Successfully","F200");
                    }
                    else{
                        response=new DefaultResponse("Success","Request Failed! Insufficient Available limit","F200");
                    }
                }

            }catch (Exception e){
                throw new ApplicationException("An Error occurred while add request in Wallet",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to add Request in  Wallet Method in Asset controller");
                util=null;
                walletRequest=null;
            }
            return response;
        }
        public DefaultResponse requestApproveOrIgnore(RequestApproveOrIgnore requestApproveOrIgnore) throws ApplicationException {
            DefaultResponse response=null;
            Util util =new Util();
            WalletRequest walletRequest=new WalletRequest();
            Wallet responderWallet=new Wallet();
            Wallet senderWallet=new Wallet();
            Fact senderFact=new Fact();
            Fact responderFact=new Fact();
            Page<Fact> factpage=null;
            Page<Fact> responderfactpage=null;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Request Approve or Ignore in Wallet Method in Asset Service"+convertToJSON(requestApproveOrIgnore));
                if((requestApproveOrIgnore.getIgnore().equals(true))&&(requestApproveOrIgnore.getApprove().equals(false))){
                    walletRequest=walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                    if((walletRequest.getApproveFlag().equals(false))&&(walletRequest.getIgnoreFlag().equals(false))&&(walletRequest.getWithDrawFlag().equals(false))){
                        walletRequest.setApproveFlag(requestApproveOrIgnore.getApprove());
                        walletRequest.setIgnoreFlag(requestApproveOrIgnore.getIgnore());
                        walletRequest.setResponderUUID(requestApproveOrIgnore.getResponderUUID());
                        walletRequestRepository.save(walletRequest);
                        response=new DefaultResponse("Success","Request Ignored Successfully","F200");
                    }else{
                        response=new DefaultResponse("Success","Request Already Respond","F200");
                    }

                }else {
                    walletRequest = walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                    if ((walletRequest.getApproveFlag().equals(false)) && (walletRequest.getIgnoreFlag().equals(false)) && (walletRequest.getWithDrawFlag().equals(false))) {

                        if (walletRequest.getRequestType().equals("Transfer In")) {

                            responderWallet = walletRespository.findByWalletUUID(walletRequest.getReceiverWalletUUID());

                            senderWallet = walletRespository.findByWalletUUID(walletRequest.getSenderWalletUUID());

                            //check available limit for sender & check responder ka balance
                            if (senderWallet.getCapacity() - senderWallet.getBalance() >= walletRequest.getQuantity() && responderWallet.getBalance() >= walletRequest.getQuantity()) {
                                factpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(senderWallet.getWalletUUID(),"spend", new PageRequest(0, 1));
                                responderfactpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletRequest.getReceiverWalletUUID(), "spend",new PageRequest(0, 1));
                                if(factpage.getContent().size()>0 && responderfactpage.getContent().size()>0){
                                    //Sender Purchase
                                    senderFact.setCurrentAverage(factpage.getContent().get(0).getCurrentAverage());
                                    senderFact.setQuantity(walletRequest.getQuantity());
                                    senderFact.setRateCurrency(factpage.getContent().get(0).getRateCurrency());
                                    senderFact.setRate(factpage.getContent().get(0).getRate());
                                    senderFact.setRateBasisUnit(factpage.getContent().get(0).getRateBasisUnit());
                                    senderFact.setQuantityUnit(factpage.getContent().get(0).getQuantityUnit());
                                    senderFact.setDateTime(new Date());
                                    senderFact.setWalletUUID(walletRequest.getSenderWalletUUID());
                                    senderFact.setFactUUID(UUID.randomUUID().toString());
                                    senderFact.setTotal(((walletRequest.getQuantity()) * (factpage.getContent().get(0).getCurrentAverage())));
                                    senderFact.setVolume(factpage.getContent().get(0).getVolume());
                                    senderFact.setTransactiontype("Transfer In");
                                    senderFact.setUserUUID(walletRequest.getSenderUUID());
                                    senderFact.setDescription(walletRequest.getDescription());
                                    factRepository.save(senderFact);
                                    senderWallet = walletRespository.findByWalletUUID(walletRequest.getSenderWalletUUID());
                                    Double Balance = senderWallet.getBalance() + walletRequest.getQuantity();
                                    senderWallet.setBalance(Balance);
                                    walletRespository.save(senderWallet);
                                    // responder tranfer
                                    responderFact.setCurrentAverage(responderfactpage.getContent().get(0).getCurrentAverage());
                                    responderFact.setQuantity(walletRequest.getQuantity());
                                    responderFact.setRateCurrency(responderfactpage.getContent().get(0).getRateCurrency());
                                    responderFact.setRate(responderfactpage.getContent().get(0).getRate());
                                    responderFact.setRateBasisUnit(responderfactpage.getContent().get(0).getRateBasisUnit());
                                    responderFact.setQuantityUnit(responderfactpage.getContent().get(0).getQuantityUnit());
                                    responderFact.setDateTime(new Date());
                                    responderFact.setWalletUUID(walletRequest.getReceiverWalletUUID());
                                    responderFact.setFactUUID(UUID.randomUUID().toString());
                                    responderFact.setTotal(((walletRequest.getQuantity()) * (responderfactpage.getContent().get(0).getCurrentAverage())));
                                    responderFact.setVolume(responderfactpage.getContent().get(0).getVolume());
                                    responderFact.setTransactiontype("Transfer Out");
                                    responderFact.setUserUUID(walletRequest.getResponderUUID());
                                    responderFact.setDescription(walletRequest.getDescription());
                                    factRepository.save(responderFact);
                                    responderWallet = walletRespository.findByWalletUUID(walletRequest.getReceiverWalletUUID());
                                    Balance = responderWallet.getBalance() - walletRequest.getQuantity();
                                    responderWallet.setBalance(Balance);
                                    if(responderWallet.getThresholdValue()<responderWallet.getBalance()){

                                    }else{
                                        sendEmail(responderWallet);
                                        //generater email
                                    }
                                    walletRespository.save(responderWallet);
                                    walletRequest = walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                                    walletRequest.setApproveFlag(requestApproveOrIgnore.getApprove());
                                    walletRequest.setIgnoreFlag(requestApproveOrIgnore.getIgnore());
                                    walletRequest.setResponderUUID(requestApproveOrIgnore.getResponderUUID());
                                    walletRequestRepository.save(walletRequest);
                                    response = new DefaultResponse("Success", "Request Approved Successfully", "F200");
                                }else{
                                    if(factpage.getContent().size()>0){
                                        return new DefaultResponse("Success", "Please Add Purchase first", "F250");
                                    }else{
                                        return new DefaultResponse("Success", "Sender need to add purchase first", "F260");
                                    }
                                }
                            } else {
                                walletRequest = walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                                walletRequest.setApproveFlag(false);
                                walletRequest.setIgnoreFlag(true);
                                walletRequest.setResponderUUID(requestApproveOrIgnore.getResponderUUID());
                                walletRequestRepository.save(walletRequest);
                                response = new DefaultResponse("Success", "Request Ignored.Insufficient Balance", "F200");
                            }
                        } else {
                            //check available limit for responder & check sender ka balance
                            //responder main purchase ho ga
                            //sender main transfer ho ga
                            responderWallet = walletRespository.findByWalletUUID(walletRequest.getReceiverWalletUUID());
                            senderWallet = walletRespository.findByWalletUUID(walletRequest.getSenderWalletUUID());
                            if (responderWallet.getCapacity() - responderWallet.getBalance() >= walletRequest.getQuantity() && senderWallet.getBalance() >= walletRequest.getQuantity()) {
                                factpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(senderWallet.getWalletUUID(), "spend",new PageRequest(0, 1));
                                responderfactpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletRequest.getReceiverWalletUUID(),"spend", new PageRequest(0, 1));
                              if(factpage.getContent().size()>0 && responderfactpage.getContent().size()>0){
                                  senderFact = new Fact();
                                  responderFact = new Fact();
                                  //Sender Purchase
                                  senderFact.setCurrentAverage(factpage.getContent().get(0).getCurrentAverage());
                                  senderFact.setQuantity(walletRequest.getQuantity());
                                  senderFact.setRateCurrency(factpage.getContent().get(0).getRateCurrency());
                                  senderFact.setRate(factpage.getContent().get(0).getRate());
                                  senderFact.setRateBasisUnit(factpage.getContent().get(0).getRateBasisUnit());
                                  senderFact.setQuantityUnit(factpage.getContent().get(0).getQuantityUnit());
                                  senderFact.setDateTime(new Date());
                                  senderFact.setWalletUUID(walletRequest.getSenderWalletUUID());
                                  senderFact.setFactUUID(UUID.randomUUID().toString());
                                  senderFact.setTotal(((walletRequest.getQuantity()) * (factpage.getContent().get(0).getCurrentAverage())));
                                  senderFact.setVolume(factpage.getContent().get(0).getVolume());
                                  senderFact.setTransactiontype("Transfer Out");
                                  senderFact.setUserUUID(walletRequest.getSenderUUID());
                                  senderFact.setDescription(walletRequest.getDescription());
                                  factRepository.save(senderFact);
                                  senderWallet = walletRespository.findByWalletUUID(walletRequest.getSenderWalletUUID());
                                  Double Balance = senderWallet.getBalance() - walletRequest.getQuantity();
                                  senderWallet.setBalance(Balance);
                                  if(senderWallet.getThresholdValue()<senderWallet.getBalance()){

                                  }else{
                                      //generater email
                                      sendEmail(senderWallet);
                                  }
                                  walletRespository.save(senderWallet);
                                  // responder tranfer
                                  responderFact.setCurrentAverage(responderfactpage.getContent().get(0).getCurrentAverage());
                                  responderFact.setQuantity(walletRequest.getQuantity());
                                  responderFact.setRateCurrency(responderfactpage.getContent().get(0).getRateCurrency());
                                  responderFact.setRate(responderfactpage.getContent().get(0).getRate());
                                  responderFact.setRateBasisUnit(responderfactpage.getContent().get(0).getRateBasisUnit());
                                  responderFact.setQuantityUnit(responderfactpage.getContent().get(0).getQuantityUnit());
                                  responderFact.setDateTime(new Date());
                                  responderFact.setWalletUUID(walletRequest.getReceiverWalletUUID());
                                  responderFact.setFactUUID(UUID.randomUUID().toString());
                                  responderFact.setTotal(((walletRequest.getQuantity()) * (responderfactpage.getContent().get(0).getCurrentAverage())));
                                  responderFact.setVolume(responderfactpage.getContent().get(0).getVolume());
                                  responderFact.setTransactiontype("Transfer In");
                                  responderFact.setUserUUID(walletRequest.getResponderUUID());
                                  responderFact.setDescription(walletRequest.getDescription());
                                  factRepository.save(responderFact);
                                  responderWallet = walletRespository.findByWalletUUID(walletRequest.getReceiverWalletUUID());
                                  Balance = responderWallet.getBalance() + walletRequest.getQuantity();
                                  responderWallet.setBalance(Balance);
                                  walletRespository.save(responderWallet);
                                  walletRequest = walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                                  walletRequest.setApproveFlag(requestApproveOrIgnore.getApprove());
                                  walletRequest.setIgnoreFlag(requestApproveOrIgnore.getIgnore());
                                  walletRequest.setResponderUUID(requestApproveOrIgnore.getResponderUUID());
                                  walletRequestRepository.save(walletRequest);
                                  response = new DefaultResponse("Success", "Request Approved Successfully", "F200");
                              }else{
                                  if(factpage.getContent().size()>0){
                                      return new DefaultResponse("Success", "Please Add Purchase first", "F250");
                                  }else{
                                      return new DefaultResponse("Success", "Sender need to add purchase first", "F260");
                                  }
                              }
                            } else {
                                walletRequest = walletRequestRepository.findByRequestUUID(requestApproveOrIgnore.getRequestUUID());
                                walletRequest.setApproveFlag(false);
                                walletRequest.setIgnoreFlag(true);
                                walletRequest.setResponderUUID(requestApproveOrIgnore.getResponderUUID());
                                walletRequestRepository.save(walletRequest);
                                response = new DefaultResponse("Success", "Request Ignored. Insufficient Balance", "F200");
                            }
                        }
                    }else{
                        response=new DefaultResponse("Success","Request Already Respond","F200");
                    }
                }
            }catch (Exception e){
                response=new DefaultResponse("Failure", "An Error occurred while Request Approve or Ignore in Wallet in Asset Service","F500");
                throw new ApplicationException("An Error occurred while Request Approve or Ignore in Wallet in Asset Service",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to Request Approve or Ignore in  Wallet Method in Asset Service");
                util=null;
                walletRequest=null;
            }
            return response;
        }
        public void AddTransfer(WalletRequest walletRequest) {
            Page<Fact> factpage;
            Fact fact = new Fact();
            Wallet wallet = new Wallet();
            factpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletRequest.getReceiverWalletUUID(),"spend", new PageRequest(0, 1));
            fact.setCurrentAverage(factpage.getContent().get(0).getCurrentAverage());
            fact.setQuantity(walletRequest.getQuantity());
            fact.setRateCurrency(factpage.getContent().get(0).getRateCurrency());
            fact.setRate(factpage.getContent().get(0).getRate());
            fact.setRateBasisUnit(factpage.getContent().get(0).getRateBasisUnit());
            fact.setQuantityUnit(factpage.getContent().get(0).getQuantityUnit());
            fact.setDateTime(new Date());
            fact.setWalletUUID(walletRequest.getReceiverWalletUUID());
            fact.setFactUUID(UUID.randomUUID().toString());
            fact.setTotal(((walletRequest.getQuantity()) * (factpage.getContent().get(0).getCurrentAverage())));
            fact.setVolume(factpage.getContent().get(0).getVolume());
            fact.setTransactiontype("transfer");
            fact.setUserUUID(null);
            factRepository.save(fact);
            wallet = walletRespository.findByWalletUUID(walletRequest.getReceiverWalletUUID());
            Double Balance = wallet.getBalance() - walletRequest.getQuantity();
            wallet.setBalance(Balance);
            walletRespository.save(wallet);
        }
        public void AddPurchase(WalletRequest walletRequest){
            Page<Fact> factpage;
            Fact fact = new Fact();
            Wallet wallet = new Wallet();
            factpage = factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletRequest.getReceiverWalletUUID(),"spend", new PageRequest(0, 1));
            fact.setCurrentAverage(factpage.getContent().get(0).getCurrentAverage());
            fact.setQuantity(walletRequest.getQuantity());
            fact.setRateCurrency(factpage.getContent().get(0).getRateCurrency());
            fact.setRate(factpage.getContent().get(0).getRate());
            fact.setRateBasisUnit(factpage.getContent().get(0).getRateBasisUnit());
            fact.setQuantityUnit(factpage.getContent().get(0).getQuantityUnit());
            fact.setDateTime(new Date());
            fact.setWalletUUID(walletRequest.getSenderWalletUUID());
            fact.setFactUUID(UUID.randomUUID().toString());
            fact.setTotal(((walletRequest.getQuantity()) * (factpage.getContent().get(0).getCurrentAverage())));
            fact.setVolume(factpage.getContent().get(0).getVolume());
            fact.setTransactiontype("purchase");
            fact.setUserUUID(walletRequest.getSenderUUID());
            factRepository.save(fact);
            wallet = walletRespository.findByWalletUUID(walletRequest.getSenderWalletUUID());
            Double Balance = wallet.getBalance()+walletRequest.getQuantity();
            wallet.setBalance(Balance);
            walletRespository.save(wallet);
        }
        public void sendEmail(Wallet wallet) throws ApplicationException {
            Asset asset = new Asset();
            WalletNotificationModel walletNotificationModel=new WalletNotificationModel();
            WalletNotification notification=new WalletNotification();
            try{
                Double balance=wallet.getBalance();
                walletNotificationModel.setAction("Wallet Low Alert");
                walletNotificationModel.setWalletName(wallet.getWalletName());
                notification.setAssetUUID(wallet.getAssetUUID());
                notification.setMessage("Wallet Running Low");
                notification.setNotificationsetting(9);
                notification.setNotificationTime(new Date());
                notification.setReadStatus(true);
                notification.setTenantUUID(wallet.getOrgUUID());
                notification.setTitle("Wallet "+wallet.getWalletName()+"Running Low");
                notification.setType("");
                notification.setThresholdValue(decimaltoTwoPoint(wallet.getThresholdValue()));
                notification.setUsername(wallet.getWalletName());
                notification.setCurrencyValue(wallet.getCapacityUnit());
                walletNotificationModel.setNotification(notification);
                if(wallet.getThresholdType().equals("Lower"))
                {
                    if(wallet.getThresholdValue()>balance)
                    {
                        kafkaAsyncService.sendWalletNotification(walletNotificationModel);
                    }else{

                    }
                }else if(wallet.getThresholdType().equals("higher"))
                {
                    if(wallet.getThresholdValue()<balance)
                    {
                        kafkaAsyncService.sendWalletNotification(walletNotificationModel);
                    }else{

                    }
                }else if(wallet.getThresholdType().equals("between"))
                {
                    if(wallet.getThresholdValue().equals(balance))
                    {
                        kafkaAsyncService.sendWalletNotification(walletNotificationModel);
                    }else{

                    }
                }
            }catch (Exception e){
                throw new ApplicationException("An Error occurred while Send Email in Wallet in Asset Service",e);
            }finally {
                LOGGER.info("Returning to while Send Email in  Wallet Method in Asset Service");
            }
        }
        public getWalletByUserUUIDAndOrgUUIDResponse getWalletsByUserUUIDandOrgUUID(String userUUID,String orgUUID) throws ApplicationException {
            getWalletByUserUUIDAndOrgUUIDResponse response=null;
            Util util =new Util();
            List<Wallet> wallet=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get Wallet By UserUUID and OrgUUID in Asset service");
                wallet=walletRespository.findByUserUUIDAndOrgUUID(userUUID,orgUUID);
                response=new getWalletByUserUUIDAndOrgUUIDResponse();
                response.setArray(wallet);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Wallet By UserUUID and OrgUUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Wallet By UserUUID and OrgUUID in controller");
                util=null;
                wallet=null;
                userUUID=null;
                orgUUID=null;
            }
            return  response;
        }
        public getWalletByUserUUIDAndOrgUUIDResponse getWalletsByUserUUIDandOrgUUIDandWalletType(String userUUID,String orgUUID,String walletUUID) throws ApplicationException {
            getWalletByUserUUIDAndOrgUUIDResponse response=null;
            Util util =new Util();
            List<Wallet> wallet=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get Wallet By UserUUID and OrgUUID in Asset service");
                wallet=walletRespository.findByUserUUIDAndOrgUUIDAndWalletType(userUUID,orgUUID,walletUUID);
                response=new getWalletByUserUUIDAndOrgUUIDResponse();
                response.setArray(wallet);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Wallet By UserUUID and OrgUUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Wallet By UserUUID and OrgUUID in controller");
                util=null;
                wallet=null;
                userUUID=null;
                orgUUID=null;
            }
            return  response;
        }
        public GetRequestResponse getAllRequestByWalletUUID(String walletUUID,Boolean approve,Boolean ignore) throws ApplicationException {
            GetRequestResponse response=null;
            Util util =new Util();
            List<WalletRequest> walletRequestList=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get Wallet By UserUUID and OrgUUID in Asset service");
                walletRequestList=walletRequestRepository.findBySenderWalletUUIDAndApproveFlagAndIgnoreFlag(walletUUID,approve,ignore);
                response=new GetRequestResponse();
                response.setArray(walletRequestList);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Wallet By UserUUID and OrgUUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Wallet By UserUUID and OrgUUID in controller");
                util=null;
                walletUUID=null;
            }
            return  response;
        }
        public GetRequestResponse getAllRequestByWalletUUID(String walletUUID,String walletType,String orgUUID) throws ApplicationException {
            GetRequestResponse response=null;
            Util util =new Util();
            List<WalletRequest> walletRequestList=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get Request By Wallet UUID in Asset service");
                walletRequestList=walletRequestRepository.findBySenderWalletUUIDOrReceiverWalletUUIDAndWalletTypeAndOrgUUID(walletUUID,walletUUID,walletType,orgUUID);
                response=new GetRequestResponse();
                response.setArray(walletRequestList);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                LOGGER.error("Error while get Wallet By UserUUID and OrgUUID  in Asset Service");
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Request By Wallet UUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Request By Wallet UUID in controller");
                util=null;
                walletUUID=null;
            }
            return  response;
        }
        public GetRequestUserDataResponse getAllRequestWithUserDataByWalletUUID(String walletUUID,Boolean approve,Boolean ignore,Boolean withdraw) throws ApplicationException {
            GetRequestUserDataResponse response=null;
            Util util =new Util();
            List<WalletRequest> walletRequestList=new ArrayList<>();
            List<WalletRequest> walletRequestListresponder=new ArrayList<>();
            walletRequestModel =null;
            List <WalletRequestsUserData> walletRequestsUserDataList=new ArrayList<>();
            WalletRequestsUserData walletRequestsUserData;
            List<WalletUser> walletUsers=new ArrayList<>();
            WalletUser user;
            WalletRequestModel walletRequest=new WalletRequestModel();
            GetUserDetailForWallet userDetailForWallet  = new GetUserDetailForWallet();
            GetUserDetailForWallet walletRequestModel  = new GetUserDetailForWallet();

            try{
                util.setThreadContextForLogging(scim2Util);
                walletRequestList=walletRequestRepository.findByApproveFlagAndIgnoreFlagAndWithDrawFlagAndSenderWalletUUID(approve,ignore,withdraw,walletUUID);
                walletRequestListresponder=walletRequestRepository.findByApproveFlagAndIgnoreFlagAndWithDrawFlagAndReceiverWalletUUID(approve,ignore,withdraw,walletUUID);
                for(int rlindex=0;rlindex<walletRequestListresponder.size();rlindex++){
                    walletRequestList.add(walletRequestListresponder.get(rlindex));
                }
                for(int wrIndex=0;wrIndex<walletRequestList.size();wrIndex++){
                    walletRequestsUserData=new WalletRequestsUserData();
                    walletRequestsUserData.setApproveFlag(walletRequestList.get(wrIndex).getApproveFlag());
                    walletRequestsUserData.setDateTime(walletRequestList.get(wrIndex).getDateTime());
                    walletRequestsUserData.setIgnoreFlag(walletRequestList.get(wrIndex).getIgnoreFlag());
                    walletRequestsUserData.setOrgUUID(walletRequestList.get(wrIndex).getOrgUUID());
                    walletRequestsUserData.setProduct(walletRequestList.get(wrIndex).getProduct());
                    walletRequestsUserData.setQuantity(walletRequestList.get(wrIndex).getQuantity());
                    walletRequestsUserData.setRequestPriority(walletRequestList.get(wrIndex).getRequestPriority());
                    walletRequestsUserData.setRequestType(walletRequestList.get(wrIndex).getRequestType());
                    walletRequestsUserData.setRequestUUID(walletRequestList.get(wrIndex).getRequestUUID());
                    walletRequestsUserData.setResponderUUID(walletRequestList.get(wrIndex).getResponderUUID());
                    walletRequestsUserData.setResponderWalletUUID(walletRequestList.get(wrIndex).getReceiverWalletUUID());
                    walletRequestsUserData.setSenderUUID(walletRequestList.get(wrIndex).getSenderUUID());
                    walletRequestsUserData.setSenderWalletUUID(walletRequestList.get(wrIndex).getSenderWalletUUID());
                    walletRequestsUserData.setWalletType(walletRequestList.get(wrIndex).getWalletType());
                    walletRequestsUserData.setWithDrawFlag(walletRequestList.get(wrIndex).getWithDrawFlag());
                    walletRequestsUserData.setDescription(walletRequestList.get(wrIndex).getDescription());
                    walletRequestsUserDataList.add(walletRequestsUserData);
                }
                for( int index=0;index<walletRequestsUserDataList.size();index++){
                    user=new WalletUser();
                    user.setUuid(walletRequestsUserDataList.get(index).getSenderUUID());
                    walletUsers.add(user);
                    user=new WalletUser();
                    user.setUuid(walletRequestsUserDataList.get(index).getResponderUUID());
                    walletUsers.add(user);
                }
                walletRequest.setWalletUser(walletUsers);
                userDetailForWallet = apsServiceProxy.getUserDetail(walletRequest);
                walletRequestModel=userDetailForWallet;

                if(walletRequestModel.getWalletUser().size()>0){
                    for(int index=0;index<walletRequestsUserDataList.size();index++){
                        for( int uindex=0;uindex<walletRequestModel.getWalletUser().size();uindex++){
                            if(walletRequestsUserDataList.get(index).getSenderUUID()!=null)
                            {
                                if(walletRequestsUserDataList.get(index).getSenderUUID().equals(walletRequestModel.getWalletUser().get(uindex).getUuid())){
                                    walletRequestsUserDataList.get(index).setSenderName(walletRequestModel.getWalletUser().get(uindex).getName());
                                    walletRequestsUserDataList.get(index).setSenderEmail(walletRequestModel.getWalletUser().get(uindex).getEmail());
                                }
                            }
                            if(walletRequestsUserDataList.get(index).getResponderUUID()!=null)
                            {
                                if(walletRequestsUserDataList.get(index).getResponderUUID().equals(walletRequestModel.getWalletUser().get(uindex).getUuid())){
                                    walletRequestsUserDataList.get(index).setResponderName(walletRequestModel.getWalletUser().get(uindex).getName());
                                    walletRequestsUserDataList.get(index).setResponderEmail(walletRequestModel.getWalletUser().get(uindex).getEmail());
                                }
                            }
                        }
                    }
                    response=new GetRequestUserDataResponse();
                    response.setWalletRequestModel(walletRequestModel.getWalletRequestModel());
                    response.setResponseIdentifier("Success");
                }else{
                    response=new GetRequestUserDataResponse();
                    response.setWalletRequestModel(walletRequestModel.getWalletRequestModel());
                    response.setResponseIdentifier("Success");
                }
                response=new GetRequestUserDataResponse();

                response.setArray(walletRequestsUserDataList);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                LOGGER.error("Error while get Wallet By UserUUID and OrgUUID  in Asset Service"+ e);
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Request By Wallet UUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Request By Wallet UUID in controller");
                util=null;
                walletUUID=null;
            }
            return  response;
        }
        public getWalletResponse getAllWalletsByWalletTypeAndOrgUUID(String walletType,String orgUUID) throws ApplicationException {
            getWalletResponse response=null;
            Util util =new Util();
            List<Wallet> walletList=new ArrayList<>();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get Wallet By walletType and OrgUUID in Asset service");
                walletList=walletRespository.findByWalletTypeAndOrgUUID(walletType,orgUUID);
                response=new getWalletResponse();
                response.setArray(walletList);
                response.setResponseIdentifier("Success");
            }catch (Exception e){
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Wallet By walletType and OrgUUID and OrgUUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Wallet By walletType and OrgUUID and OrgUUID in controller");
                util=null;
                walletType=null;
                orgUUID=null;
            }
            return  response;
        }
        @StreamListener("inBoundReceivingWalletRequestModel")
        public void setWalletRequestModel(WalletRequestModel walletRequest) {
            try {
                LOGGER.info("Entered in Method set Wallet By Wallet Request  in Asset service"+convertToJSON(walletRequest));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.walletRequestModel = walletRequest;
            try {
                LOGGER.info("Entered in Method set Wallet By Wallet Request  in Asset service"+convertToJSON(this.walletRequestModel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public getWalletsUserResponse getAllUserByWalletsTypeAndOrgUUIDAndProductType(String walletType,String orgUUID,String productType,String userUUID) throws ApplicationException {
            getWalletsUserResponse response=null;
            Util util =new Util();
            walletRequestModel=null;
            List<Wallet> walletList=new ArrayList<>();
            List<String> userList=new ArrayList<>();
            List<WalletUser> walletUsers=new ArrayList<>();
            WalletUser user=new WalletUser();
            WalletRequestModel walletRequest=new WalletRequestModel();
            GetUserDetailForWallet userDetailForWallet = new GetUserDetailForWallet();
            GetUserDetailForWallet walletRequestModel = new GetUserDetailForWallet();

            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Method get User By walletType and OrgUUID in Asset service");
                walletList=walletRespository.findByWalletTypeAndOrgUUIDAndProductType(walletType,orgUUID,productType);
                for(int index=0;index<walletList.size();index++){
                    if(!(userUUID.equals(walletList.get(index).getUserUUID()))){
                        user=new WalletUser();
                        userList.add(walletList.get(index).getUserUUID());
                        user.setUuid(walletList.get(index).getUserUUID());
                        walletUsers.add(user);
                    }}
                walletRequest.setWalletUser(walletUsers);
                walletRequest.setWalletUser(walletUsers);
                userDetailForWallet = apsServiceProxy.getUserDetail(walletRequest);
                walletRequestModel=userDetailForWallet;
                walletRequestModel.getWalletRequestModel().setWalletUser(walletRequestModel.getWalletUser());
                if(walletRequestModel.getWalletUser().size()>0){
                    response=new getWalletsUserResponse();
                    response.setWalletRequestModel(walletRequestModel.getWalletRequestModel());
                    response.setResponseIdentifier("Success");
                }else{
                    response=new getWalletsUserResponse();
                    response.setWalletRequestModel(walletRequestModel.getWalletRequestModel());
                    response.setResponseIdentifier("Success");
                }
            }catch (Exception e){
                LOGGER.error("Error while get Wallet By UserUUID and OrgUUID  in Asset Service");
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while get Wallet By walletType and OrgUUID and OrgUUID",e);
            }finally {
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get User By walletType and OrgUUID and OrgUUID in controller");
                util=null;
                walletType=null;
                orgUUID=null;
                walletRequestModel=null;
                walletList=null;
                userList=null;
                walletUsers=null;
                user=null;
                walletRequest=null;
                productType=null;
            }
            return  response;
        }
        public DefaultResponse withDrawnRequest(WithDrawnRequest withDrawnRequest) throws ApplicationException{
            DefaultResponse response=null;
            Util util =new Util();
            WalletRequest walletRequest=new WalletRequest();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Request withdraw  Method in Asset Service");
                walletRequest=walletRequestRepository.findByRequestUUID(withDrawnRequest.getRequestUUID());
                if((walletRequest.getApproveFlag().equals(false))&&(walletRequest.getIgnoreFlag().equals(false))&&(walletRequest.getWithDrawFlag().equals(false))){
                    walletRequest.setWithDrawFlag(true);
                    walletRequestRepository.save(walletRequest);
                    response=new DefaultResponse("Success","Request Withdraw Successfully","F200");
                }else{
                    response=new DefaultResponse("Success","Request Already Respond","F200");
                }
            }catch(Exception e){
                response=new DefaultResponse("Failure","An Error occurred while withdraw request","F500");
                throw new ApplicationException("An Error occurred while withdraw request",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to Request withdraw in  Wallet Method in Asset Service");
                util=null;
                walletRequest=null;
            }
            return response;
        }
        public GetTransactionResponse getTransaction(String walletUUID,int offset,int limit) throws ApplicationException{
            GetTransactionResponse response=null;
            Util util =new Util();
            List<Fact> fact=new ArrayList<>();
            Page<Fact> pagefact=null;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Request get Transaction Method in Asset Service");
                pagefact=factRepository.findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(walletUUID,"spend",new PageRequest(offset,limit));
                response=new GetTransactionResponse();
                response.setResponseIdentifier("Success");
                response.setFactList(pagefact.getContent());

            }catch(Exception e){
                response=new GetTransactionResponse();
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while Get Transaction",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Transaction in Wallet Method in Asset Service");
                util=null;
                fact=null;
            }
            return response;

        }
        public GetWalletObjectResponse getWallet(String walletUUID) throws ApplicationException{
            GetWalletObjectResponse response=null;
            Util util =new Util();
            Wallet wallet=new Wallet();
            Category category=new Category();
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Entered in Request get Wallet Method in Asset Service");
                wallet=walletRespository.findByWalletUUID(walletUUID);
                String categoryUUID = assetRepository.getAssetCategoryUUIDByUUID(wallet.getAssetUUID());
                response=new GetWalletObjectResponse();
                response.setResponseIdentifier("Success");
                response.setWallet(wallet);
                response.setAssetCategoryName(categoryRepository.getCategoryNameByUUID(categoryUUID));

            }catch(Exception e){
                response=new GetWalletObjectResponse();
                response.setResponseIdentifier("Failure");
                throw new ApplicationException("An Error occurred while fetching Get Wallet",e);
            }finally{
                util.clearThreadContextForLogging();
                LOGGER.info("Returning to get Wallet Method in Asset Service");
                util=null;
                wallet=null;
                category=null;
            }
            return response;

        }
        /**********************************************************Wallet Ending******************************************************
         Written By: Kumail Khan
         *****************************************************************************************************************************/

        // For Adding SDT Filters
        public List<Predicate> addFilters(CriteriaBuilder criteriaBuilder, Root issues, List<Predicate> clauses, List<HashMap<String,Object>> filters,String searchQuery){
            Predicate searchClause = null;
            for(HashMap<String,Object> filter : filters) {
                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_EQUALS) && filter.get(Constant.SDT_VALUE1).toString() != "") {

                    if (filter.get(Constant.SDT_VALUE1).toString().contains(Constant.SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(Constant.SDT_VALUE1).toString().split(Constant.SDT_SEPARATOR);
                        Predicate clause = null;

                        clause = criteriaBuilder.or(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[0].trim()), criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[1].trim()));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.or(clause, criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[i].trim()));


                        clauses.add(clause);

                    } else{
                        if(filter.get(Constant.SDT_TYPE).toString().equals("date")) {
                            Date endDate =  (Date) filter.get(Constant.SDT_VALUE1);
                            Calendar c = Calendar.getInstance();
                            c.setTime(endDate);
                            c.add(Calendar.DATE, 1);
                            endDate = c.getTime();

                            //decrementing date by one
                            Date startDate =(Date) filter.get(Constant.SDT_VALUE1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(startDate);
                            int daysToDecrement = -1;
                            cal.add(Calendar.DATE, daysToDecrement);
                            startDate = cal.getTime();
                            clauses.add(criteriaBuilder.greaterThan(issues.<Date>get((String) filter.get(SDT_FIELD)), startDate));
                            clauses.add(criteriaBuilder.lessThan(issues.<Date>get((String) filter.get(SDT_FIELD)), endDate));

                        }
                        else if(filter.get(Constant.SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filter.get(Constant.SDT_VALUE1).toString().trim()));

                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else {
                            try{
                                clauses.add(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));

                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }

                    }


                }

                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_NOT_EQUALS) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if (filter.get(Constant.SDT_VALUE1).toString().contains(Constant.SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(Constant.SDT_VALUE1).toString().split(Constant.SDT_SEPARATOR);
                        Predicate clause = criteriaBuilder.and(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[0].trim()), criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[1].trim()));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.and(clause, criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[i].trim()));

                        clauses.add(clause);
                    } else{
                        if(filter.get(Constant.SDT_TYPE).toString().equals("date"))
                            clauses.add(criteriaBuilder.notEqual(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(Constant.SDT_VALUE1)));
                        else if(filter.get(Constant.SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filter.get(Constant.SDT_VALUE1).toString().trim()));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else{
                            try{
                                clauses.add(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }
                    }


                }

                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_LIKE) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if (filter.get(Constant.SDT_VALUE1).toString().contains(Constant.SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(Constant.SDT_VALUE1).toString().split(Constant.SDT_SEPARATOR);
                        Predicate clause = criteriaBuilder.or(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[0].trim() + "%"), criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[1].trim() + "%"));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.or(clause, criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[i].trim() + "%"));

                        clauses.add(clause);
                    } else{
                        if(filter.get(Constant.SDT_TYPE).toString().equals("date"))
                            clauses.add(criteriaBuilder.like(issues.<Date>get((String) filter.get(SDT_FIELD)).as(String.class),"%" + filter.get(Constant.SDT_VALUE1).toString().trim() + "%"));
                        else if(filter.get(Constant.SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filter.get(Constant.SDT_VALUE1).toString().trim() + "%"));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else {
                            try{
                                clauses.add(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim()) + "%"));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }
                    }


                }

                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_GREATER_THAN) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if(filter.get(Constant.SDT_TYPE).toString().equals("date")){
                        try{
                            clauses.add(criteriaBuilder.greaterThan(issues.<Date>get((String) filter.get(SDT_FIELD)),(Date)filter.get(Constant.SDT_VALUE1)));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.greaterThan(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }


                }

                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_LESS_THAN) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if(filter.get(Constant.SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.lessThan(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date) filter.get(Constant.SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.lessThan(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if (filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_BETWEEN) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if(filter.get(Constant.SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.between(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date) filter.get(Constant.SDT_VALUE1), (Date) filter.get(Constant.SDT_VALUE2)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try {
                            clauses.add(criteriaBuilder.between(issues.<Date>get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim()), Integer.valueOf(filter.get(Constant.SDT_VALUE2).toString().trim())));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if(filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_GREATER_THAN_EQUAL_TO) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if (filter.get(Constant.SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.greaterThanOrEqualTo(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(Constant.SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.greaterThanOrEqualTo(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if(filter.get(Constant.COMPARISON_TYPE).equals(Constant.SDT_LESS_THAN_EQUAL_TO) && filter.get(Constant.SDT_VALUE1).toString() != "") {
                    if (filter.get(Constant.SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.lessThanOrEqualTo(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(Constant.SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.lessThanOrEqualTo(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(Constant.SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }
                // For Search, Adding Like clauses for all columns if searchQuery contains data.
                if(searchQuery != null){
                    if(searchQuery != ""){
                        if(searchClause != null){
                            searchClause = criteriaBuilder.or(searchClause,criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + searchQuery.toString() + "%"));
                        }else{
                            searchClause = criteriaBuilder.or(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + searchQuery.toString() + "%"));
                        }
                    }
                }
            }
            if(searchClause !=null){
                clauses.add(searchClause);
            }
            return clauses;
        }
        /*************************************Written by Nouman Afzaal*******************************************************/
        /******************************************* Wallet SDT Start ***************************************************/
        public GetPaginatedDataForSDTResponse getPaginatetWalletforSDT(GetPaginatedDataForSDTRequest request) throws ApplicationException,IOException{
            Util util = new Util();
            GetPaginatedDataForSDTResponse response=new GetPaginatedDataForSDTResponse();
            CriteriaBuilder criteriaBuilder = null;
            CriteriaQuery query = null;
            Root root = null;
            List<Predicate> clauses = null;
            List<WalletUser> walletUsers=new ArrayList<>();
            WalletUser user=null;
            WalletRequestModel walletRequest=new WalletRequestModel();
            try{

                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Inside Service function of getting page of Wallet for SDT, "+convertToJSON(request));
                criteriaBuilder = entityManager.getCriteriaBuilder();
                query = criteriaBuilder.createQuery(Long.class);

                // Setting corresponding column name of respective db table
                if(request.getSortField().equals("createdDate"))
                    request.setSortField("dateTime");
                else if(request.getSortField().equals("assetName"))
                    request.setSortField("assetUUID");
                else if(request.getSortField().equals("userName"))
                    request.setSortField("userUUID");

                for(HashMap filter : request.getFilters()){
                    if(filter.get(SDT_FIELD).equals("createdDate")) {
                        filter.replace(SDT_FIELD, "dateTime");
                    }
                    else if(filter.get(SDT_FIELD).equals("assetName")) {
                        filter.replace(SDT_FIELD, "assetUUID");
                    }
                    else if(filter.get(SDT_FIELD).equals("userName")) {
                        filter.replace(SDT_FIELD, "userUUID");
                    }
                }

                root = query.from(Wallet.class);

                clauses = new ArrayList<>();
                clauses.add(criteriaBuilder.equal(root.get("orgUUID"),request.getTenantUUID()));
                // Add filters
                clauses = addFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());
                clauses.add(criteriaBuilder.isNull(root.get("archived")));

                response.getSdtData().put(TOTAL_ELEMENTS,(Long)entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

                List<Wallet> wallets = entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))
                        .orderBy(
                                (javax.persistence.criteria.Order) CriteriaBuilder.class.getDeclaredMethod(request.getSortDirection(), Expression.class)
                                        .invoke(criteriaBuilder,root.get(request.getSortField()))
                        ))
                        .setFirstResult(request.getLimit() * request.getOffset())
                        .setMaxResults(request.getLimit())
                        .getResultList();
                root=null;
                clauses=null;
                query=null;
                query = criteriaBuilder.createQuery(Long.class);

                root = query.from(Asset.class);

                clauses = new ArrayList<>();
                clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));
                List<Asset> assets=entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))).getResultList();

                // Construct Rows List
                response.getSdtData().put(CONTENT,new ArrayList<>());
                for(Wallet wallet:wallets){
                    user=new WalletUser();
                    user.setUuid(wallet.getUserUUID());
                    walletUsers.add(user);
                }
                walletRequest.setWalletUser(walletUsers);

                //  if(!request.getFilters().isEmpty()){
                // kafkaAsyncService.sendWalletUserUUIDs(walletRequest);

                //while(walletRequestModel == null);
                //while (walletRequestModel.getWalletUser().isEmpty());
                // }
                WalletRequestModel walletRequestModel=walletRequestModel(walletRequest);
                for(Wallet wallet:wallets){
                    ((ArrayList)response.getSdtData().get(CONTENT)).add(new HashMap<>());
                    for(Asset asset:assets){
                        if(asset.getUuid().equals(wallet.getAssetUUID())){
                            ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("assetName",asset.getName());
                        }
                    }
                    for(int i=0;i<walletRequestModel.getWalletUser().size();i++){
                        if(walletRequestModel.getWalletUser().get(i).getUuid().equals(wallet.getUserUUID())){
                            ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("userName",walletRequestModel.getWalletUser().get(i).getName());
                        }
                    }
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("walletName",wallet.getWalletName());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("balance",wallet.getBalance());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("thresholdType",wallet.getThresholdType());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("thresholdValue",wallet.getThresholdValue());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("dateTime",wallet.getDateTime());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("capacity",wallet.getCapacity());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("capacityUnit",wallet.getCapacityUnit());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("productType",wallet.getProductType());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("walletType",wallet.getWalletType());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("allowedNegative",wallet.getAllowedNegative());
                    ((HashMap)((ArrayList)response.getSdtData().get(CONTENT)).get(((ArrayList)response.getSdtData().get(CONTENT)).size()-1)).put("walletUUID",wallet.getWalletUUID());

                }
                response.getSdtData().put(TOTAL_PAGES,((Long)response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);

                if((Long)response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                    response.getSdtData().replace(TOTAL_PAGES,(Long)response.getSdtData().get(TOTAL_PAGES) - 1);

                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Page of Wallet for SDT got successfully. Returning it to controller");

            }catch(Exception e){
                throw new ApplicationException("An Error occurred in getting page of Wallet for SDT, "+convertToJSON(request),e);

            }finally{
                util.clearThreadContextForLogging();
                query = null;
                root = null;
                clauses = null;
                criteriaBuilder = null;
                util = null;
                walletUsers=null;
                user=null;
                walletRequest=null;
            }

            return response;
        }

        /******************************************* Wallet Sdt End ***********************************************/
        /*******************************************Feign Call Functions Strat**************************************************/
        public WalletRequestModel walletRequestModel(WalletRequestModel walletRequest){
            try{
                WalletRequestModel walletUsers = apsServiceProxy.getUserName(walletRequest);

                if( walletUsers == null){
                    LOGGER.error("No Username  found in APS!");
                }else {
                    return walletUsers;
                }
            }catch (Exception e){
                LOGGER.error("An unexpected Error Occurred while fetching username from APS!",e);
            }
            return null;
        }

        public GetAssetUsersResponse getAssetUsersByAssetIds(List<String> assetIds) throws ApplicationException {
            try{
                LOGGER.info("Sending request to APS to get Assets Users name by Asset ids.");
                GetAssetUsersResponse response = apsServiceProxy.getAssetUsersByAssetIds(assetIds);
                if(response != null){
                    return response;
                }
            }catch (Exception e){
                throw new ApplicationException("An Error Occurred while fetching users name for assets from APS.",e);
            }

            return null;
        }
        /*****************************************Feign Call Functions End**************************************/

        /***************************************SDT Call Functions Start***************************************/
//        @HasRead
        public GetPaginatedDataForSDTResponse getPaginatedAssetsforSDT(GetPaginatedDataForSDTRequest request) throws IOException,AccessDeniedException{

            if(!privilegeHandler.hasRead())
                throw new AccessDeniedException();

            Long count=0l;
            Util util = new Util();
            GetPaginatedDataForSDTResponse response=new GetPaginatedDataForSDTResponse();
            CriteriaBuilder criteriaBuilder = null;
            CriteriaQuery query = null;
            Root root = null;
            List<Predicate> clauses = null;
            try{
                util.setThreadContextForLogging(scim2Util);
                LOGGER.info("Inside Service function of getting Asset Groups, "+convertToJSON(request));
                criteriaBuilder = entityManager.getCriteriaBuilder();
                query = criteriaBuilder.createQuery(Long.class);

                root = query.from(AssetGroup.class);

                clauses = new ArrayList<>();
                clauses.add(criteriaBuilder.equal(root.get("tenantUUID"),request.getTenantUUID()));
                clauses.add(criteriaBuilder.isNotNull(root.get("uuid")));
                clauses.add(criteriaBuilder.isNull(root.get("deletefromGroupUUID")));

                clauses = addInspectionFilters(criteriaBuilder,root,clauses,request.getFilters(),request.getSearchQuery());

                response.getSdtData().put(TOTAL_ELEMENTS,(Long)entityManager.createQuery(query.select(criteriaBuilder.count(root)).where(clauses.toArray(new Predicate[]{}))).getSingleResult());

                List<AssetGroup> assetGroups = entityManager.createQuery(query.select(root).where(clauses.toArray( new Predicate[]{}))
                        .orderBy(
                                (javax.persistence.criteria.Order) CriteriaBuilder.class.getDeclaredMethod(request.getSortDirection(), Expression.class)
                                        .invoke(criteriaBuilder,root.get(request.getSortField()))
                        ))
                        .setFirstResult(request.getLimit() * request.getOffset())
                        .setMaxResults(request.getLimit())
                        .getResultList();
                response.getSdtData().put(CONTENT,new ArrayList<>());
                for(AssetGroup assetGroup:assetGroups){
                    ((ArrayList)response.getSdtData().get(CONTENT)).add(new HashMap<>());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("groupName", assetGroup.getGroupName());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("totalMembers", assetGroup.getTotalMembers());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("assetCategory", assetGroup.getAssetCategory());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("createdByUserName", assetGroup.getCreatedByUserName());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("createdAt", assetGroup.getCreatedAt());
                    ((HashMap) ((ArrayList) response.getSdtData().get(CONTENT)).get(((ArrayList) response.getSdtData().get(CONTENT)).size() - 1)).put("uuid", assetGroup.getUuid());

                }
                response.getSdtData().put(TOTAL_PAGES,((Long)response.getSdtData().get(TOTAL_ELEMENTS) / request.getLimit()) + 1);
                if((Long)response.getSdtData().get(TOTAL_ELEMENTS) == request.getLimit())
                    response.getSdtData().replace(TOTAL_PAGES,(Long)response.getSdtData().get(TOTAL_PAGES) - 1);
                response.setResponseIdentifier(SUCCESS);
                LOGGER.info("Page of Assets Group for SDT got successfully. Returning it to controller");
            }
            catch (Exception e){
                LOGGER.error("Error in getting page of Asset Groups, "+convertToJSON(request),e);
                response.setResponseIdentifier("Failure");
                e = null;
            }finally{
                util.clearThreadContextForLogging();
                query = null;
                root = null;
                clauses = null;
                criteriaBuilder = null;
                util = null;
            }
            return  response;
        }

        public List<Predicate> addInspectionFilters(CriteriaBuilder criteriaBuilder, Root issues, List<Predicate> clauses, List<HashMap<String,Object>> filters,String searchQuery){
            Predicate searchClause = null;
            for(HashMap<String,Object> filter : filters) {
                if (filter.get(COMPARISON_TYPE).equals(SDT_EQUALS) && filter.get(SDT_VALUE1).toString() != "") {

                    if (filter.get(SDT_VALUE1).toString().contains(SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(SDT_VALUE1).toString().split(SDT_SEPARATOR);
                        Predicate clause = null;

                        clause = criteriaBuilder.or(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[0].trim()), criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[1].trim()));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.or(clause, criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filterValues[i].trim()));


                        clauses.add(clause);

                    } else{
                        if(filter.get(SDT_TYPE).toString().equals("date")) {
                            Date endDate =  (Date) filter.get(SDT_VALUE1);
                            Calendar c = Calendar.getInstance();
                            c.setTime(endDate);
                            c.add(Calendar.DATE, 1);
                            endDate = c.getTime();

                            //decrementing date by one
                            Date startDate =(Date) filter.get(SDT_VALUE1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(startDate);
                            int daysToDecrement = -1;
                            cal.add(Calendar.DATE, daysToDecrement);
                            startDate = cal.getTime();
                            clauses.add(criteriaBuilder.greaterThan(issues.<Date>get((String) filter.get(SDT_FIELD)), startDate));
                            clauses.add(criteriaBuilder.lessThan(issues.<Date>get((String) filter.get(SDT_FIELD)), endDate));

                        }
                        else if(filter.get(SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), filter.get(SDT_VALUE1).toString().trim()));

                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else {
                            try{
                                clauses.add(criteriaBuilder.equal(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));

                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }

                    }


                }

                if (filter.get(COMPARISON_TYPE).equals(SDT_NOT_EQUALS) && filter.get(SDT_VALUE1).toString() != "") {
                    if (filter.get(SDT_VALUE1).toString().contains(SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(SDT_VALUE1).toString().split(SDT_SEPARATOR);
                        Predicate clause = criteriaBuilder.and(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[0].trim()), criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[1].trim()));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.and(clause, criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filterValues[i].trim()));

                        clauses.add(clause);
                    } else{
                        if(filter.get(SDT_TYPE).toString().equals("date"))
                            clauses.add(criteriaBuilder.notEqual(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(SDT_VALUE1)));
                        else if(filter.get(SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), filter.get(SDT_VALUE1).toString().trim()));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else{
                            try{
                                clauses.add(criteriaBuilder.notEqual(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }
                    }


                }

                if (filter.get(COMPARISON_TYPE).equals(SDT_LIKE) && filter.get(SDT_VALUE1).toString() != "") {
                    if (filter.get(SDT_VALUE1).toString().contains(SDT_SEPARATOR)) {
                        // Multiple values
                        String[] filterValues = filter.get(SDT_VALUE1).toString().split(SDT_SEPARATOR);
                        Predicate clause = criteriaBuilder.or(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[0].trim() + "%"), criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[1].trim() + "%"));
                        for (int i = 2; i < filterValues.length; i++)
                            clause = criteriaBuilder.or(clause, criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filterValues[i].trim() + "%"));

                        clauses.add(clause);
                    } else{
                        if(filter.get(SDT_TYPE).toString().equals("date"))
                            clauses.add(criteriaBuilder.like(issues.<Date>get((String) filter.get(SDT_FIELD)).as(String.class),"%" + filter.get(SDT_VALUE1).toString().trim() + "%"));
                        else if(filter.get(SDT_TYPE).toString().equals("string")){
                            try{
                                clauses.add(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)), "%" + filter.get(SDT_VALUE1).toString().trim() + "%"));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }else {
                            try{
                                clauses.add(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + Integer.valueOf(filter.get(SDT_VALUE1).toString().trim()) + "%"));
                            }catch (Exception e){
                                LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                            }
                        }
                    }


                }

                if (filter.get(COMPARISON_TYPE).equals(SDT_GREATER_THAN) && filter.get(SDT_VALUE1).toString() != "") {
                    if(filter.get(SDT_TYPE).toString().equals("date")){
                        try{
                            clauses.add(criteriaBuilder.greaterThan(issues.<Date>get((String) filter.get(SDT_FIELD)),(Date)filter.get(SDT_VALUE1)));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.greaterThan(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }


                }

                if (filter.get(COMPARISON_TYPE).equals(SDT_LESS_THAN) && filter.get(SDT_VALUE1).toString() != "") {
                    if(filter.get(SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.lessThan(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date) filter.get(SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.lessThan(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if (filter.get(COMPARISON_TYPE).equals(SDT_BETWEEN) && filter.get(SDT_VALUE1).toString() != "") {
                    if(filter.get(SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.between(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date) filter.get(SDT_VALUE1), (Date) filter.get(SDT_VALUE2)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try {
                            clauses.add(criteriaBuilder.between(issues.<Date>get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim()), Integer.valueOf(filter.get(SDT_VALUE2).toString().trim())));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if(filter.get(COMPARISON_TYPE).equals(SDT_GREATER_THAN_EQUAL_TO) && filter.get(SDT_VALUE1).toString() != "") {
                    if (filter.get(SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.greaterThanOrEqualTo(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.greaterThanOrEqualTo(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }

                if(filter.get(COMPARISON_TYPE).equals(SDT_LESS_THAN_EQUAL_TO) && filter.get(SDT_VALUE1).toString() != "") {
                    if (filter.get(SDT_TYPE).toString().equals("date")) {
                        try {
                            clauses.add(criteriaBuilder.lessThanOrEqualTo(issues.<Date>get((String) filter.get(SDT_FIELD)), (Date)filter.get(SDT_VALUE1)));
                        } catch (Exception e) {
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }else{
                        try{
                            clauses.add(criteriaBuilder.lessThanOrEqualTo(issues.get((String) filter.get(SDT_FIELD)), Integer.valueOf(filter.get(SDT_VALUE1).toString().trim())));
                        }catch (Exception e){
                            LOGGER.error("An Error Occurred While Adding clause for filter " + e);
                        }
                    }
                }
                // For Search, Adding Like clauses for all columns if searchQuery contains data.
                if(searchQuery != null){
                    if(searchQuery != ""){
                        if(searchClause != null){
                            searchClause = criteriaBuilder.or(searchClause,criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + searchQuery.toString() + "%"));
                        }else{
                            searchClause = criteriaBuilder.or(criteriaBuilder.like(issues.get((String) filter.get(SDT_FIELD)).as(String.class), "%" + searchQuery.toString() + "%"));
                        }
                    }
                }
            }
            if(searchClause !=null){
                clauses.add(searchClause);
            }
            return clauses;
        }
        /***************************************SDT Call Functions End*****************************************/

    /******************************************** S3 Functions ********************************************/
    //get file IS_UC_19
    /*
     * This function gets a file from s3. Url of file is passed to the function
     */
    public GetFileResponse getFile(String url) {
        LOGGER.debug("Inside service function of getting file from s3");
        GetFileResponse response = new GetFileResponse();
        try {
            String[] parts = url.split("/");
            String key = "";
            for (int i = 3; i < parts.length; i++) {
                key += parts[i];
                if (i != parts.length - 1) {
                    key += "/";
                }
            }
//            String key=parts[parts.length-1];
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
            S3Object s3Object = this.s3client.getObject(getObjectRequest);

            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

            response.setContent(bytes);
            response.setContentLength(bytes.length);
            response.setFileName(fileName);
            response.setResponseIdentifier("Success");
            LOGGER.info("File got successfully. Returning to controller");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting file from s3",e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    }


