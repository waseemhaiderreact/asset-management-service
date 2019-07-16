package com.sharklabs.ams.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.AssetImage.AssetImageRepository;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.activitywall.ActivityWallRepository;
import com.sharklabs.ams.asset.*;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.assetfield.AssetFieldRepository;
import com.sharklabs.ams.attachment.Attachment;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.category.CategoryRepository;
import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.consumption.ConsumptionRepository;
import com.sharklabs.ams.field.Field;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import com.sharklabs.ams.fieldtemplate.FieldTemplateRepository;
import com.sharklabs.ams.fieldtemplate.FieldTemplateResponse;
import com.sharklabs.ams.imagevoice.ImageVoiceRepository;
import com.sharklabs.ams.inspectionitem.InspectionItem;
import com.sharklabs.ams.inspectionitemcategory.InspectionItemCategory;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplateRepository;
import com.sharklabs.ams.message.Message;
import com.sharklabs.ams.message.MessageRepository;
import com.sharklabs.ams.page.AssetPage;
import com.sharklabs.ams.request.*;
import com.sharklabs.ams.response.*;
import com.sharklabs.ams.usage.Usage;
import com.sharklabs.ams.usage.UsageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class AssetService {
    private static final Logger LOGGER = LogManager.getLogger(AssetService.class);

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
    DefaultResponse addCategory(AddCategoryRequest addCategoryRequest) {
        try {
            LOGGER.debug("Inside add category service function");
            addCategoryRequest.getCategory().setUuid(UUID.randomUUID().toString());
            //if children exists
            if (addCategoryRequest.getCategory().getFieldTemplate() != null) {
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
            if (addCategoryRequest.getCategory().getInspectionTemplate() != null) {
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
            //if children exists
            for (Asset asset : addCategoryRequest.getCategory().getAssets()) {
                asset.setCategory(addCategoryRequest.getCategory());
                asset.setUuid(UUID.randomUUID().toString());
                ActivityWall activityWall = new ActivityWall();
                activityWall.setAsset(asset);
                asset.setActivityWall(activityWall);
                activityWall.setUuid(UUID.randomUUID().toString());
                for (AssetField assetField : asset.getAssetFields()) {
                    assetField.setUuid(UUID.randomUUID().toString());
                    assetField.setAsset(asset);
                }
            }
            categoryRepository.save(addCategoryRequest.getCategory());
            LOGGER.info("Category Added Successfully");
            return new DefaultResponse("Success", "Category added succssfully", "200", addCategoryRequest.getCategory().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding category", e);
            return new DefaultResponse("Failure", "Error in add category: " + e.getMessage(), "500");
        }
    }

    //delete category (AMS_UC_05)
    /*
        This function deletes a category from db and all it's children(if a category is deleted
        then it's assets,field template, inspection template will also be deleted)
     */
    DefaultResponse deleteCategory(String id) {
        LOGGER.debug("Inside Service function of deleting category");
        try {
            Category category = categoryRepository.findCategoryByUuid(id);
            categoryRepository.deleteById(category.getId());
            LOGGER.info("Category deleted Successfully");
            return new DefaultResponse("Success", "Category deleted Successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting category", e);
            return new DefaultResponse("Failure", "Error in deleting category: " + e.getMessage(), "500");
        }
    }

    //get a category (AMS_UC_02)
    /*
        This function gets a category from db. We pass it the uuid of category and in response we get a category
     */
    GetCategoryResponse getCategory(String id) {
        LOGGER.debug("Inside Service function of get category");
        GetCategoryResponse response = new GetCategoryResponse();
        try {
            response.setCategory(categoryRepository.findCategoryByUuid(id));
            response.setResponseIdentifier("Success");
            response.getCategory().setAssets(null);
            LOGGER.info("Received Category From database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting category from db. Category UUID: " + id, e);
            return response;
        }
    }

    //get all categories (AMS_UC_03)
    /*
        This function just returns all the categories (List of categories) from db
     */
    GetCategoriesResponse GetAllCategories(String tenantUUID) {
        LOGGER.debug("Inside Service function of get all categories");
        GetCategoriesResponse response = new GetCategoriesResponse();
        try {
            response.setCategories(categoryRepository.findByTenantUUID(tenantUUID));
            for (Category category : response.getCategories()) {
                category.setAssets(null);
            }
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Categories From database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db.", e);
            return response;
        }
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
    EditCategoryResponse editCategory(EditCategoryRequest editCategoryRequest) {
        LOGGER.debug("Inside Service function of edit category");
        EditCategoryResponse response = new EditCategoryResponse();
        try {
            Category category = categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid());
            //if id of category is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if (editCategoryRequest.getCategory().getId() == null) {
                editCategoryRequest.getCategory().setId(category.getId());
                editCategoryRequest.getCategory().setFieldTemplate(category.getFieldTemplate());
                editCategoryRequest.getCategory().setAssets(category.getAssets());
                editCategoryRequest.getCategory().setInspectionTemplate(category.getInspectionTemplate());
            }
            //setting parent of children if exists
            if (editCategoryRequest.getCategory().getInspectionTemplate() != null) {
                editCategoryRequest.getCategory().getInspectionTemplate().setCategory(editCategoryRequest.getCategory());
                for (InspectionItemCategory inspectionItemCategory : editCategoryRequest.getCategory().getInspectionTemplate().getInspectionItemCategories()) {
                    inspectionItemCategory.setInspectionTemplate(editCategoryRequest.getCategory().getInspectionTemplate());
                    for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                        inspectionItem.setInspectionItemCategory(inspectionItemCategory);
                    }
                }
            }
            if (editCategoryRequest.getCategory().getFieldTemplate() != null) {
                editCategoryRequest.getCategory().getFieldTemplate().setCategory(editCategoryRequest.getCategory());
                for (Field field : editCategoryRequest.getCategory().getFieldTemplate().getFields()) {
                    field.setFieldTemplate(editCategoryRequest.getCategory().getFieldTemplate());
                }
            }

            for (Asset asset : editCategoryRequest.getCategory().getAssets()) {
                asset.setCategory(editCategoryRequest.getCategory());
                for (AssetField assetField : asset.getAssetFields()) {
                    assetField.setAsset(asset);
                }
            }

            categoryRepository.save(editCategoryRequest.getCategory());
            response.setCategory(categoryRepository.findCategoryByUuid(editCategoryRequest.getCategory().getUuid()));
            response.setResponseIdentifier("Success");
            LOGGER.info("Category Updated Successfully. Sending updated Category to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while updating category or getting updated category from db. ", e);
            return response;
        }
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
    DefaultResponse addFieldTemplate(AddFieldTemplateRequest addFieldTemplateRequest) {
        LOGGER.debug("Inside Service function of add field template");
        try {
            //get category from db
            Category category = null;
            if (addFieldTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(addFieldTemplateRequest.getCategoryId());
                addFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(addFieldTemplateRequest.getFieldTemplate());
            } else {
                LOGGER.error("category uuid is not given for field template");
                return new DefaultResponse("Failure", "Category id not present in request object", "500");
            }
            //setting uuid
            addFieldTemplateRequest.getFieldTemplate().setUuid(UUID.randomUUID().toString());
            //setting parent of all children and also setting uuid
            for (Field field : addFieldTemplateRequest.getFieldTemplate().getFields()) {
                field.setFieldTemplate(addFieldTemplateRequest.getFieldTemplate());
                field.setUuid(UUID.randomUUID().toString());
            }
            //saving in db
            categoryRepository.save(category);
            LOGGER.info("Field Template Added Successfully");
            return new DefaultResponse("Success", "Field Template Added Successfully", "200", addFieldTemplateRequest.getFieldTemplate().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding field template ", e);
            return new DefaultResponse("Failure", "Error in adding field template. Error: " + e.getMessage(), "500");
        }
    }

    //delete field template AMS_UC_09
    /*
    This function deletes a field template
    UUID of field template is passed to this function
    First, we find the field template with that id
    Then, the parent of field template is set to null because we don't need to delete it's parent(Category)
    Then, field template is deleted
     */
    DefaultResponse deleteFieldTemplate(String id) {
        LOGGER.debug("Inside Service function of deleting field template");
        try {
            FieldTemplate fieldTemplate = fieldTemplateRepository.findByUuid(id);
            //setting parent of field template to null to not delete the parent alongwith the children
            fieldTemplate.setCategory(null);
            fieldTemplateRepository.save(fieldTemplate);
            //deleting
            fieldTemplateRepository.deleteById(fieldTemplate.getId());
            LOGGER.info("Field Template deleted Successfully");
            return new DefaultResponse("Success", "Field Template deleted Successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting field template", e);
            return new DefaultResponse("Failure", "Error in deleting field template: " + e.getMessage(), "500");
        }
    }

    //get field template AMS_UC_07
    /*
    This function gets a field template by uuid
    UUID is passed to this function and field template is returned with that UUID
     */
    GetFieldTemplateResponse getFieldTemplate(String id) {
        LOGGER.debug("Inside Service function of get field template");
        GetFieldTemplateResponse response = new GetFieldTemplateResponse();
        try {
            response.setFieldTemplate(fieldTemplateRepository.findByUuid(id));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Field Template From database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting field template from db. Field Template UUID: " + id, e);
            return response;
        }
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
    EditFieldTemplateResponse editFieldTemplate(EditFieldTemplateRequest editFieldTemplateRequest) {
        LOGGER.debug("Inside Service function of edit field template");
        EditFieldTemplateResponse response = new EditFieldTemplateResponse();
        try {
            //get category from db
            Category category = null;
            if (editFieldTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(editFieldTemplateRequest.getCategoryId());
                editFieldTemplateRequest.getFieldTemplate().setCategory(category);
                category.setFieldTemplate(editFieldTemplateRequest.getFieldTemplate());
            } else {
                LOGGER.error("category uuid is not given for field template");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //if id of field template is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if (editFieldTemplateRequest.getFieldTemplate().getId() == null) {
                FieldTemplate fieldTemplate = fieldTemplateRepository.findByUuid(editFieldTemplateRequest.getFieldTemplate().getUuid());
                editFieldTemplateRequest.getFieldTemplate().setId(fieldTemplate.getId());
                editFieldTemplateRequest.getFieldTemplate().setFields(fieldTemplate.getFields());
            }
            //setting parent of all children and also setting uuid
            for (Field field : editFieldTemplateRequest.getFieldTemplate().getFields()) {
                field.setFieldTemplate(editFieldTemplateRequest.getFieldTemplate());
            }
            //saving in db
            categoryRepository.save(category);
            response.setResponseIdentifier("Success");
            response.setFieldTemplate(category.getFieldTemplate());
            LOGGER.info("Field Template Edited Successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing field template from db. Field Template UUID: " + editFieldTemplateRequest.getFieldTemplate().getUuid(), e);
            return response;
        }
    }

    /*******************************************END Field Template Functions*************************************/


    /******************************************* Asset Functions ************************************************/
    //add asset (AMS_UC_10)
    /*
    This function adds a new asset
    Category uuid and asset object is passed to this function
    First, we get the category with that uuid and set it as a parent of asset
    The, uuid is assigned to asset and it's children. Also, parent of children is set so that children are saved automatically when parent is saved
     */
    DefaultResponse addAsset(AddAssetRequest addAssetRequest) {
        LOGGER.debug("Inside Service function of add asset");
        try {
            //get category from db
            Category category = null;
            if (addAssetRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(addAssetRequest.getCategoryId());
                addAssetRequest.getAsset().setCategory(category);
                category.addAsset(addAssetRequest.getAsset());
            } else {
                LOGGER.error("category uuid is not given for asset");
                return new DefaultResponse("Failure", "Category id not present in request object", "500");
            }
            //setting uuid
            addAssetRequest.getAsset().setUuid(UUID.randomUUID().toString());
            //setting parent of children and also setting uuid
            for (AssetField assetField : addAssetRequest.getAsset().getAssetFields()) {
                assetField.setAsset(addAssetRequest.getAsset());
                assetField.setUuid(UUID.randomUUID().toString());
            }
            for (AssetImage assetImage : addAssetRequest.getAsset().getAssetImages()) {
                assetImage.setAsset(addAssetRequest.getAsset());
            }
            for(Attachment attachment: addAssetRequest.getAsset().getAttachments()){
                attachment.setAsset(addAssetRequest.getAsset());
            }
            //creating activity wall for that asset and also setting uuid
            ActivityWall activityWall = new ActivityWall();
            activityWall.setUuid(UUID.randomUUID().toString());
            activityWall.setAsset(addAssetRequest.getAsset());
            addAssetRequest.getAsset().setActivityWall(activityWall);
            //saving in db
            categoryRepository.save(category);

            //set asset number
            Asset savedAsset = assetRepository.findAssetByUuid(addAssetRequest.getAsset().getUuid());
            savedAsset.setAssetNumber(this.genrateAssetNumber(savedAsset.getId()));
            assetRepository.save(savedAsset);

            LOGGER.info("Asset Added Successfully");
            return new DefaultResponse("Success", "Asset Added Successfully", "200", addAssetRequest.getAsset().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding asset ", e);
            return new DefaultResponse("Failure", "Error in adding asset. Error: " + e.getMessage(), "500");
        }
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
    EditAssetResponse editAsset(EditAssetRequest editAssetRequest) {
        LOGGER.debug("Inside Service function of edit asset");
        EditAssetResponse response = new EditAssetResponse();
        try {
            //get category from db
            Category category = null;
            if (editAssetRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(editAssetRequest.getCategoryId());
                editAssetRequest.getAsset().setCategory(category);
                //this code is for test library. This condition wont be true when request is sent by FE
                if (editAssetRequest.getAsset().getId() == null) {
                    for (Asset asset : category.getAssets()) {
                        if (asset.getUuid().equals(editAssetRequest.getAsset().getUuid())) {
                            editAssetRequest.getAsset().setId(asset.getId());
                        }
                    }
                }
            } else {
                LOGGER.error("category uuid is not given for asset");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //setting parent of children
            for (AssetField assetField : editAssetRequest.getAsset().getAssetFields()) {
                assetField.setAsset(editAssetRequest.getAsset());
            }
            if (editAssetRequest.getAsset().getActivityWall() != null) {
                editAssetRequest.getAsset().getActivityWall().setAsset(editAssetRequest.getAsset());
                for (Message message : editAssetRequest.getAsset().getActivityWall().getMessages()) {
                    message.setActivityWall(editAssetRequest.getAsset().getActivityWall());
                }
            }
            //saving in db
            assetRepository.save(editAssetRequest.getAsset());

            response.setResponseIdentifier("Success");
            response.setAsset(editAssetRequest.getAsset());
            LOGGER.info("Asset Edited Successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while editing asset from db. Asset UUID: " + editAssetRequest.getAsset().getUuid(), e);
            return response;
        }
    }

    //delete asset AMS_UC_12
    /*
    This function deletes an asset
    asset uuid is passed to this function
    First, we get the asset with that uuid
    then, we set it's parent to null and save it because we don't want to delete parent alongwith the children
    then, asset is deleted by id
     */
    DefaultResponse deleteAsset(String id) {
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
            LOGGER.error("Error while deleting asset", e);
            return new DefaultResponse("Failure", "Error in deleting asset: " + e.getMessage(), "500");

        }
    }

    //get asset AMS_UC_13
    /*
    This function gets an asset by uuid
    uuid of asset is passed to this function
    We get the asset with that uuid from db and return it
     */
    GetAssetResponse getAsset(String id) {
        LOGGER.debug("Inside Service function of get asset");
        GetAssetResponse response = new GetAssetResponse();
        try {
            //find asset with that uuid
            Asset asset = assetRepository.findAssetByUuid(id);
            //map it to a new object
            AssetResponse assetResponse = new AssetResponse();
            assetResponse.setAsset(asset);
            response.setAsset(assetResponse);
            //set field template of asset
            FieldTemplateResponse fieldTemplate = new FieldTemplateResponse();
            if (asset.getCategory().getFieldTemplate() != null) {
                fieldTemplate.setFieldTemplate(asset.getCategory().getFieldTemplate());
                Collections.sort(fieldTemplate.getFields());
            }
            response.setFieldTemplate(fieldTemplate);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Asset From database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting asset from db. Asset UUID: " + id, e);
            return response;
        }
    }

    // Get Asset with provided Asset Detail
    /* The function retrieves selective Asset Detail
     * @Parmm - Asset Uuid
     * @Param - Requested Asset Detail
     */
    GetAssetDetailResponse getAssetDetail(AssetDetailRequest assetDetailRequest,String uuid){
        GetAssetDetailResponse response = new GetAssetDetailResponse();
        AssetDetailResponse assetDetailResponse = new AssetDetailResponse();
        AssetDetail assetDetail = null;
        try {

            //Retrieve Basic Detail of Asset
            assetDetail = assetRepository.getBasicAssetDetailByUuid(uuid);
            assetDetailResponse.setAssetDetail(assetDetail);

            // Additions as requested by AssetDetail

            if(assetDetailRequest.isActivityWall())
                assetDetailResponse.setActivityWall(activityWallRepository.findActivityWallByAssetUuid(uuid));

            if(assetDetailRequest.isAssetFields())
                assetDetailResponse.setAssetField(assetFieldRepository.findAllByAssetUuid(uuid));

            if(assetDetailRequest.isAssetImages())
                assetDetailResponse.setAssetImage(assetImageRepository.findAllByAssetUuid(uuid));

            if(assetDetailRequest.isAttachments())
                assetDetailResponse.setAssetImage(assetImageRepository.findAllByAssetUuid(uuid));

            if(assetDetailRequest.isConsumptions())
                assetDetailResponse.setConsumption(consumptionRepository.findByAssetUUID(uuid));

            if(assetDetailRequest.isUsages())
                assetDetailResponse.setUsage(usageRepository.findByAssetUUID(uuid));

            if(assetDetailRequest.isCategory())
                assetDetailResponse.setCategory(categoryRepository.findByAssetsUuid(uuid));

            response.setAssetDetail(assetDetailResponse);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Asset Detail From database. Sending it to controller");
        }catch(Exception e){
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting asset detail from db. Asset UUID: " + uuid, e);
        }finally{
            assetDetail = null;
            assetDetailResponse = null;
        }
        return response;
    }

    //get assets AMS_UC_14
    /*
    This function gets all assets
    It simply fetches all assets from db and return it
     */
    GetAssetsResponse getAssets(String tenantUUID) {
        LOGGER.debug("Inside Service function of get assets");
        GetAssetsResponse response = new GetAssetsResponse();
        try {
            List<Asset> assets = assetRepository.findByTenantUUID(tenantUUID);
            List<AssetModel> assetModels = new ArrayList<>();
            for (Asset asset : assets) {
                AssetModel assetModel = new AssetModel();
                assetModel.setAsset(asset);
                assetModels.add(assetModel);
            }
            response.setAssets(assetModels);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets From database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting all categories from db.", e);
            return response;
        }
    }

    //get asset basic detail by tenant AMS_UC_31
    GetBasicAssetDetailByTenantResponse getBasicAssetDetailByTenant(String tenantUUID){
        LOGGER.debug("Inside service function of getting basic details of asset by tenant. Tenant UUID: "+tenantUUID);
        GetBasicAssetDetailByTenantResponse response=new GetBasicAssetDetailByTenantResponse();
        try{
            JdbcTemplate jt;
            jt = new JdbcTemplate(this.dataSource());

            String sql = "select a.id as id,a.uuid as uuid,a.asset_number as asset_number,a.name as asset_name,a.primary_usage_unit as primary_usage_unit,a.secondary_usage_unit as secondary_usage_unit,a.consumption_unit as consumption_unit,a.consumption_points as consumption_points,c.uuid as category_uuid " +
                        "from t_asset a inner join t_category c on a.category_id=c.id " +
                        "where a.tenantuuid=?";
            List<Map<String, Object>> assetsResponse = jt.queryForList(sql, tenantUUID);
            List<GetNameAndTypeOfAssetResponse> assets=new ArrayList<>();
            for(Map<String,Object> assetResponse: assetsResponse) {
                GetNameAndTypeOfAssetResponse asset = new GetNameAndTypeOfAssetResponse();
                asset.setName(String.valueOf(assetResponse.get("asset_name")));
                asset.setCategoryUUID(String.valueOf(assetResponse.get("category_uuid")));
                asset.setUuid(String.valueOf(assetResponse.get("uuid")));
                asset.setAssetNumber(String.valueOf(assetResponse.get("asset_number")));
                asset.setConsumptionUnit(String.valueOf(assetResponse.get("consumption_unit")));
                asset.setPrimaryUsageUnit(String.valueOf(assetResponse.get("primary_usage_unit")));
                asset.setSecondaryUsageUnit(String.valueOf(assetResponse.get("secondary_usage_unit")));
                asset.setConsumptionPoints((int)assetResponse.get("consumption_points"));
                assets.add(asset);
            }

            response.setAssets(assets);
            response.setResponseIdentifier("Success");
            LOGGER.info("Basic detail of asset got successfully");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            LOGGER.error("Error while getting basic detail of asset by tenant. TenantUUID: "+tenantUUID);
            return response;
        }
    }

    //get page of assets AMS_UC_22
    /*
     * This function gets limit and offset and returns page of assets
     */
    GetPaginatedAssetsResponse getPaginatedAssets(int offset, int limit, String tenantuuid) {
        LOGGER.debug("Inside service function of get page of assets");
        GetPaginatedAssetsResponse response = new GetPaginatedAssetsResponse();
        try {
            JdbcTemplate jt;
            jt = new JdbcTemplate(this.dataSource());
            int lowerLimit = offset * limit;
            int upperLimit = limit;
            AssetPage assets = new AssetPage();
            //get total count of assets
            String sql = "select count(*) as count from t_asset a where a.tenantuuid=?";
            Map<String, Object> totalAssetsResponse = jt.queryForMap(sql, tenantuuid);
            assets.setTotalElements((Long) (totalAssetsResponse.get("count")));
            //getting page of assets
            sql = "select a.id as id, a.asset_number as assetNumber, a.description as description, a.inventory as inventory, " +
                    " a.manufacture as manufacture, a.model_number as modelNumber, a.name as name, a.purchase_date as purchaseDate, a.tenantuuid as tenantUUID, a.uuid as uuid, " +
                    " a.warranty as warranty, a.primary_usage_unit as primaryUsageUnit, a.secondary_usage_unit as secondaryUsageUnit, a.consumption_unit as consumptionUnit " +
                    " from t_asset a where a.tenantuuid=? " +
                    "limit ?,?";
            List<Map<String, Object>> assetsResponse = jt.queryForList(sql, tenantuuid, lowerLimit, upperLimit);
            List<AssetModelForTableView> assetModelList = new ArrayList<>();
            for (Map<String, Object> assetResponse : assetsResponse) {
                AssetModelForTableView assetModel = new AssetModelForTableView();
                assetModel.setId((Long) assetResponse.get("id"));
                assetModel.setAssetNumber(String.valueOf(assetResponse.get("assetNumber")));
                assetModel.setConsumptionUnit(String.valueOf(assetResponse.get("consumptionUnit")));
                assetModel.setDescription(String.valueOf(assetResponse.get("description")));
                assetModel.setInventory(String.valueOf(assetResponse.get("inventory")));
                assetModel.setManufacture(String.valueOf(assetResponse.get("manufacture")));
                assetModel.setModelNumber(String.valueOf(assetResponse.get("modelNumber")));
                assetModel.setName(String.valueOf(assetResponse.get("name")));
                assetModel.setPrimaryUsageUnit(String.valueOf(assetResponse.get("primaryUsageUnit")));
                assetModel.setSecondaryUsageUnit(String.valueOf(assetResponse.get("secondaryUsageUnit")));
                assetModel.setTenantUUID(String.valueOf(assetResponse.get("tenantUUID")));
                assetModel.setUuid(String.valueOf(assetResponse.get("uuid")));
                assetModel.setWarranty(String.valueOf(assetResponse.get("warranty")));
                assetModel.setPurchaseDate((Date) assetResponse.get("purchaseDate"));
                assetModel.setExpiryDate((Date) assetResponse.get("expiryDate"));

                assetModelList.add(assetModel);
            }
            assets.setContent(assetModelList);
            response.setAssets(assets);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets from database. Returning to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while getting page of assets", e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get name and type of assets AMS_UC_23
    /*
     * This will be used by Inspection Table View FE Screen
     * This function gets a list of uuids. First, it finds assets by those uuids from db and then creates a hashmap of assets
     * inwhich a name and type of asset is stored against uuid of asset and returned
     */
    GetNameAndTypeOfAssetsByUUIDSResponse getNameAndTypeOfAssetsByUUIDS(GetNameAndTypeOfAssetsByUUIDSRequest request) {
        LOGGER.debug("Inside service function of get get name and type of assets by uuids");
        GetNameAndTypeOfAssetsByUUIDSResponse response = new GetNameAndTypeOfAssetsByUUIDSResponse();
        try {
            JdbcTemplate jt;
            jt = new JdbcTemplate(this.dataSource());
            HashMap<String, GetNameAndTypeOfAssetResponse> assetsHashmap = new HashMap<>();
            for (String uuid : request.getUuids()) {
                String sql = "select a.id as id,a.name as asset_name,a.primary_usage_unit as primary_usage_unit,a.secondary_usage_unit as secondary_usage_unit,a.consumption_unit as consumption_unit,a.consumption_points as consumption_points,c.name as category_name,a.asset_number,a.uuid " +
                        "from t_asset a inner join t_category c on a.category_id=c.id " +
                        "where a.uuid=?";
                Map<String, Object> assetResponse = jt.queryForMap(sql, uuid);
                sql = "select i.image_url as image " +
                        "from t_asset_images i " +
                        "where i.asset_id=? " +
                        "limit 1";
                GetNameAndTypeOfAssetResponse asset = new GetNameAndTypeOfAssetResponse();
                Map<String, Object> imageResponse = null;
                try {
                    imageResponse = jt.queryForMap(sql, Long.valueOf(String.valueOf(assetResponse.get("id"))));
                    asset.setImageUrl(String.valueOf(imageResponse.get("image")));
                } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                    LOGGER.debug("There are no images of this asset.Asset UUID: " + uuid);
                }

                asset.setName(String.valueOf(assetResponse.get("asset_name")));
                asset.setType(String.valueOf(assetResponse.get("category_name")));
                asset.setAssetNumber(String.valueOf(assetResponse.get("asset_number")));
                asset.setConsumptionUnit(String.valueOf(assetResponse.get("consumption_unit")));
                asset.setPrimaryUsageUnit(String.valueOf(assetResponse.get("primary_usage_unit")));
                asset.setSecondaryUsageUnit(String.valueOf(assetResponse.get("secondary_usage_unit")));
                asset.setConsumptionPoints((int)assetResponse.get("consumption_points"));
                assetsHashmap.put(String.valueOf(assetResponse.get("uuid")), asset);
            }

            response.setAssets(assetsHashmap);
            response.setResponseIdentifier("Success");
            LOGGER.info("Received assets from database. Returning to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while getting assets by uuids", e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    /******************************************* END Asset Functions ************************************************/

    /******************************************* Consumption Functions **********************************************/
    //this functions adds a consumption unit of an asset e.g fuel entries of vehicles AMS_UC_25
    /*
     * Request object contains asset uuid of which this entry is to be made. We find the asset by uuid and add consumption entry in it's array
     * and save the updated object
     */
    DefaultResponse addConsumptionUnits(AddConsumptionUnitsRequest request) {
        LOGGER.debug("Inside service function of adding consumption units of asset. AssetUUID: " + request.getAssetUUID());
        try {
            //find asset by uuid
            Asset asset = assetRepository.findAssetByUuid(request.getAssetUUID());
            //add consumption unit in the array of consumptions of asset
            asset.addConsumption(request.getConsumption());
            request.getConsumption().setAsset(asset);
            request.getConsumption().setAssetUUID(request.getAssetUUID());
            request.getConsumption().setCreatedAt(new Date());
            request.getConsumption().setUuid(UUID.randomUUID().toString());

            Usage usage=new Usage();
            usage.setCreatedAt(new Date());
            usage.setAsset(asset);
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

            assetRepository.save(asset);

            //save attachments
            imageVoiceRepository.save(request.getImageVoices());

            return new DefaultResponse("Success", "Consumption Unit Added Successfully", "200", request.getConsumption().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding consumption unit of asset. AssetUUID: " + request.getAssetUUID(), e);
            return new DefaultResponse("Failure", "Error while adding consumption unit of asset. Error Message: " + e.getMessage(), "500");
        }
    }

    //get paginated consumptions
    GetPaginatedConsumptionsResponse getPaginatedConsumptionsByAsset(String uuid, int offset, int limit) {
        LOGGER.debug("Inside service to get consumption units of asset. Asset UUID: " + uuid + " with offset: " + offset + " and limit: " + limit);

        GetPaginatedConsumptionsResponse response = new GetPaginatedConsumptionsResponse();
        try {
            Asset asset = assetRepository.findAssetByUuid(uuid);
            Page<Consumption> consumptions = consumptionRepository.findByAssetOrderByCreatedAt(asset, new PageRequest(offset, limit));
            response.setConsumptions(consumptions);
            response.setResponseIdentifier("Success");

        } catch (Exception e) {
            response.setResponseIdentifier("Failure");
        }
        return response;
    }

    //this function deletes a consumption unit by uuid AMS_UC_26
    DefaultResponse deleteConsumptionUnits(String uuid) {
        LOGGER.debug("Inside service function of deleting consumption unit by uuid. UUID: " + uuid);
        try {
            //find consumption by uuid
            Consumption consumption = consumptionRepository.findConsumptionByUuid(uuid);
            //delete by id
            consumptionRepository.deleteById(consumption.getId());

            return new DefaultResponse("Success", "Consumption unit deleted successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting consumption unit by uuid: UUID: " + uuid, e);
            return new DefaultResponse("Failure", "Error while deleting consumption unit by uuid. Error Message: " + e.getMessage(), "500");
        }
    }

    //get paginated consumptions AMS_UC_29
    GetPaginatedConsumptionsResponse getPaginatedConsumptions(GetPaginatedConsumptionsRequest request){
        LOGGER.debug("Inside service function of getting paginated consumptions.  TenantUUID: "+request.getTenantUUID()+" Offset: "+request.getOffset()+" Limit: "+request.getLimit()+"  AssetID: "+request.getAssetUUID()+" Start Date: "+request.getStartDate()+" End Date: "+request.getEndDate());
        GetPaginatedConsumptionsResponse response=new GetPaginatedConsumptionsResponse();
        try{
            Page<Consumption> consumptions=consumptionRepository.filterConsumptions(request.getAssetUUID(),request.getTenantUUID(),request.getStartDate(),request.getEndDate(),new PageRequest(request.getOffset(),request.getLimit()));

            response.setConsumptions(consumptions);
            response.setResponseIdentifier("Success");
            LOGGER.info("Page of consumptions got successfully.");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting paginated consumptions",e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get paginated consumptions by asset uuids AMS_UC_31
    GetPaginatedConsumptionsByAssetsResponse getPaginatedConsumptionsByAssets(GetPaginatedConsumptionsByAssetsRequest request){
        LOGGER.debug("Inside service function of getting paginated consumptions by asset uuids. Offset: "+request.getOffset()+ "Limit: "+request.getLimit());
        GetPaginatedConsumptionsByAssetsResponse response=new GetPaginatedConsumptionsByAssetsResponse();
        try{
            Page<Consumption> consumptions=consumptionRepository.findByAssetUUIDInOrderByIdDesc(request.getAssetUUIDS(),new PageRequest(request.getOffset(),request.getLimit()));

            LOGGER.info("Page of consumptions by asset uuids got successfully.");
            response.setConsumptions(consumptions);
            response.setResponseIdentifier("Success");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting paginated consumptions by asset uuids. Offset: "+request.getOffset()+ " Limit: "+request.getLimit());
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get consumption by id AMS_UC_39
    GetConsumptionByIdResponse getConsumptionById(Long id){
        LOGGER.debug("Inside service function of getting consumption by id. ID: "+id);
        GetConsumptionByIdResponse response=new GetConsumptionByIdResponse();
        try{
            //find consumption by id
            Consumption consumption=consumptionRepository.findOne(id);

            response.setConsumption(consumption);
            response.setResponseIdentifier("Success");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting consumption by id",e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    /******************************************* END Consumption Functions **********************************************/

    /******************************************* Usages Functions ****************************************************/

    //add usage
    DefaultResponse addUsage(AddUsageRequest request){
        LOGGER.debug("Inside service function to add usage in asset. AssetUUID: " + request.getUsage().getAssetUUID());
        DefaultResponse response=new DefaultResponse();
        try{
            Asset asset=assetRepository.findAssetByUuid(request.getUsage().getAssetUUID());
            request.getUsage().setAsset(asset);
            request.getUsage().setCreatedAt(new Date());
            asset.addUsage(request.getUsage());
            assetRepository.save(asset);
            response.setResponseIdentifier("Success");
            response.setDescription("Usage added successfully");
            response.setResponseCode("200");
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Error while saving usage",e);
            response.setResponseIdentifier("Failure");
            response.setResponseCode("500");
            return response;
        }

    }

    //edit usage

    DefaultResponse editUsage(EditUsageRequest request){
        LOGGER.debug("Inside service function to edit usage in asset. AssetUUID: " + request.getUsage().getAssetUUID());
        DefaultResponse response=new DefaultResponse();
        try{
            usageRepository.save(request.getUsage());
            response.setResponseIdentifier("Success");
            response.setDescription("Usage edited successfully");
            response.setResponseCode("200");
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Error while editing usage",e);
            response.setResponseIdentifier("Failure");
            response.setResponseCode("500");
            return response;
        }

    }

    //get usages by asset AMS_UC_27
    /*
     * this function will get a page of usages by asset uuid. asset uuid, offset and limit are passed to this function
     * this function finds the usages by asset and returns them
     */
    GetPaginatedUsagesByAssetResponse getPaginatedUsagesByAsset(String assetUUID,int offset,int limit){
        LOGGER.debug("Inside service function of getting paginated usages by asset. Asset UUID: "+assetUUID+" Offset: "+offset+"Limit: "+limit);
        GetPaginatedUsagesByAssetResponse response=new GetPaginatedUsagesByAssetResponse();
        try{
            Page<Usage> usages=usageRepository.findByAssetUUIDOrderByCreatedAt(assetUUID,new PageRequest(offset,limit));
            response.setUsages(usages);
            response.setResponseIdentifier("Success");
            LOGGER.info("Paginated Usages by asset got successfully.");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting paginated usages by asset. Asset UUID: "+assetUUID,e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get paginated usages AMS_UC_28
    /*
     * this function is used to get usages by tenant or filter usages by asset uuid and date
     */
    GetPaginatedUsagesResponse getPaginatedUsages(GetPaginatedUsagesRequest request){
        LOGGER.debug("Inside service function of getting paginated usages. TenantUUID: "+request.getTenantUUID()+" Offset: "+request.getOffset()+" Limit: "+request.getLimit()+"  AssetUUID: "+request.getAssetUUID()+" Start Date: "+request.getStartDate()+" End Date: "+request.getEndDate());
        GetPaginatedUsagesResponse response=new GetPaginatedUsagesResponse();
        try{
            Page<Usage> usages=usageRepository.filterUsages(request.getAssetUUID(),request.getTenantUUID(),request.getStartDate(),request.getEndDate(),new PageRequest(request.getOffset(),request.getLimit()));

            response.setUsages(usages);
            response.setResponseIdentifier("Success");
            LOGGER.info("Usages got successfully");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting paginated usages",e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get paginated usages by asset uuids AMS_UC_33
    GetPaginatedUsagesByAssetsAndCategoryResponse getPaginatedUsagesByAssetsAndType(GetPaginatedUsagesByAssetsAndCategoryRequest request){
        LOGGER.debug("Inside service function of getting paginated usages by asset uuids. Offset: "+request.getOffset()+ "Limit: "+request.getLimit());
        GetPaginatedUsagesByAssetsAndCategoryResponse response=new GetPaginatedUsagesByAssetsAndCategoryResponse();
        try{
            Page<Usage> usages=usageRepository.findByAssetUUIDInAndCategoryOrderByIdDesc(request.getAssetUUIDS(),request.getCategory(),new PageRequest(request.getOffset(),request.getLimit()));
            response.setUsages(usages);
            response.setResponseIdentifier("Success");
            LOGGER.info("Page of usages by asset uuids got successfully");
            return response;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting paginated usages by asset uuids. Offset: "+request.getOffset()+" Limit: "+request.getLimit()+" and category: "+request.getCategory());
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //get usage by id
    GetUsageByIdResponse getUsageById(Long id){
        LOGGER.debug("Inside service function of getting usage by id.Id: "+id);
        GetUsageByIdResponse response=new GetUsageByIdResponse();
        try{
            Usage usage=usageRepository.findOne(id);
            response.setUsage(usage);
            response.setResponseIdentifier("Success");
            LOGGER.info("Usage by id got successfully");
            return response;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error while getting usage by id",e);
            response.setResponseIdentifier("Failure");
            return response;
        }
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
    DefaultResponse postInspectionTemplate(PostInspectionTemplateRequest postInspectionTemplateRequest) {
        LOGGER.debug("Inside Service function of post inspection template");
        try {
            //get category from db
            Category category = null;
            if (postInspectionTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(postInspectionTemplateRequest.getCategoryId());
                postInspectionTemplateRequest.getInspectionTemplate().setCategory(category);
                category.setInspectionTemplate(postInspectionTemplateRequest.getInspectionTemplate());
            } else {
                LOGGER.error("category uuid is not given for inspection template");
                return new DefaultResponse("Failure", "Category id not present in request object", "500");
            }
            //setting uuid
            postInspectionTemplateRequest.getInspectionTemplate().setUuid(UUID.randomUUID().toString());
            //setting parent of children and also setting uuid
            for (InspectionItemCategory inspectionItemCategory : postInspectionTemplateRequest.getInspectionTemplate().getInspectionItemCategories()) {
                inspectionItemCategory.setInspectionTemplate(postInspectionTemplateRequest.getInspectionTemplate());
                inspectionItemCategory.setUuid(UUID.randomUUID().toString());
                for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                    inspectionItem.setInspectionItemCategory(inspectionItemCategory);
                    inspectionItem.setUuid(UUID.randomUUID().toString());
                }
            }
            //saving in db
            categoryRepository.save(category);
            LOGGER.info("Inspection Template Added Successfully");
            return new DefaultResponse("Success", "Inspection Template Added Successfully", "200", postInspectionTemplateRequest.getInspectionTemplate().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding inspection template", e);
            return new DefaultResponse("Failure", "Error while adding inspection template. Reason: " + e.getMessage(), "500");
        }
    }

    //Get Inspection Template AMS_UC_16
    /*
    This function gets an inspection template by uuid
    uuid of inspection template is passed to this function
    then, we get inspection template with that uuid from db and return it
     */
    GetInspectionTemplateResponse getInspectionTemplate(String id) {
        LOGGER.debug("Inside Service function of get inspection template");
        GetInspectionTemplateResponse response = new GetInspectionTemplateResponse();
        try {
            response.setInspectionTemplate(inspectionTemplateRepository.findInspectionTemplateByUuid(id));
            response.setResponseIdentifier("Success");
            LOGGER.info("Received Inspection Template from database. Sending it to controller");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while getting inspection template", e);
            response.setResponseIdentifier("Failure");
            return response;
        }
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
    EditInspectionTemplateResponse editInspectionTemplate(EditInspectionTemplateRequest editInspectionTemplateRequest) {
        LOGGER.debug("Inside Service function of edit inspection template");
        EditInspectionTemplateResponse response = new EditInspectionTemplateResponse();
        try {
            //get category from db
            Category category = null;
            if (editInspectionTemplateRequest.getCategoryId() != null) {
                category = categoryRepository.findCategoryByUuid(editInspectionTemplateRequest.getCategoryId());
                editInspectionTemplateRequest.getInspectionTemplate().setCategory(category);
                category.setInspectionTemplate(editInspectionTemplateRequest.getInspectionTemplate());
            } else {
                LOGGER.error("category uuid is not given for inspection template");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //if id of inspection template is null (This will be null when we execute the test library otherwise by frontend, id will be passed).
            //This if is for Test Library only
            if (editInspectionTemplateRequest.getInspectionTemplate().getId() == null) {
                InspectionTemplate inspectionTemplate = inspectionTemplateRepository.findInspectionTemplateByUuid(editInspectionTemplateRequest.getInspectionTemplate().getUuid());
                editInspectionTemplateRequest.getInspectionTemplate().setId(inspectionTemplate.getId());
                editInspectionTemplateRequest.getInspectionTemplate().setInspectionItemCategories(inspectionTemplate.getInspectionItemCategories());
            }
            //setting parent of all children
            for (InspectionItemCategory inspectionItemCategory : editInspectionTemplateRequest.getInspectionTemplate().getInspectionItemCategories()) {
                inspectionItemCategory.setInspectionTemplate(editInspectionTemplateRequest.getInspectionTemplate());
                for (InspectionItem inspectionItem : inspectionItemCategory.getInspectionItems()) {
                    inspectionItem.setInspectionItemCategory(inspectionItemCategory);
                }
            }
            //saving in db
            categoryRepository.save(category);
            response.setInspectionTemplate(editInspectionTemplateRequest.getInspectionTemplate());
            response.setResponseIdentifier("Success");
            LOGGER.info("Inspection Template Edited Successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while editing inspection template", e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //delete inspection template AMS_UC_18
    /*
    This function will delete a inspection template
    uuid of inspection template is passed
    First, we get the inspection template with that uuid
    Then, we set it's parent to null because we don't want to delete it's parent alongwith the children
    Then, we delete inspection template by id
     */
    DefaultResponse deleteInspectionTemplate(String id) {
        LOGGER.debug("Inside service function of deleting inspection template");
        try {
            InspectionTemplate inspectionTemplate = inspectionTemplateRepository.findInspectionTemplateByUuid(id);
            //setting parent of inspection template to null to not delete the parent alongwith the children
            inspectionTemplate.setCategory(null);
            inspectionTemplateRepository.save(inspectionTemplate);
            //deleting
            inspectionTemplateRepository.deleteById(inspectionTemplate.getId());
            LOGGER.info("Inspection Template deleted Successfully");
            return new DefaultResponse("Success", "Inspection Template deleted Successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting inspection template", e);
            return new DefaultResponse("Failure", "Error while deleting inspection template. Reason: " + e.getMessage(), "500");
        }
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
    DefaultResponse addMessage(AddMessageRequest addMessageRequest) {
        LOGGER.debug("Inside service function of adding message");
        try {
            Asset asset = null;
            //if activity wall uuid is passed in the request then set parent of message
            if (addMessageRequest.getAssetId() != null) {
                asset = assetRepository.findAssetByUuid(addMessageRequest.getAssetId());
                asset.getActivityWall().addMessage(addMessageRequest.getMessage());
                addMessageRequest.getMessage().setActivityWall(asset.getActivityWall());
            } else {
                LOGGER.error("Asset uuid is not passed in the request");
                return new DefaultResponse("Failure", "Asset uuid is not passed in the request", "500");
            }
            //setting uuid
            addMessageRequest.getMessage().setUuid(UUID.randomUUID().toString());
            //saving it in db
            activityWallRepository.save(asset.getActivityWall());
            LOGGER.info("Message Added Successfully");
            return new DefaultResponse("Success", "Message Added Successfully", "200", addMessageRequest.getMessage().getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while adding message", e);
            return new DefaultResponse("Failure", "Error while adding message. Reason " + e.getMessage(), "500");
        }
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
    EditMessageResponse editMessage(EditMessageRequest editMessageRequest) {
        LOGGER.debug("Inside service function of adding message");
        EditMessageResponse response = new EditMessageResponse();
        try {
            Asset asset = null;
            //if activity wall uuid is passed in the request then set parent of message
            if (editMessageRequest.getAssetId() != null) {
                asset = assetRepository.findAssetByUuid(editMessageRequest.getAssetId());
                editMessageRequest.getMessage().setActivityWall(asset.getActivityWall());
                //This if will be executed for test library
                if (editMessageRequest.getMessage().getId() == null) {
                    for (Message message : asset.getActivityWall().getMessages()) {
                        if (message.getUuid().equals(editMessageRequest.getMessage().getUuid())) {
                            editMessageRequest.getMessage().setId(message.getId());
                        }
                    }
                }
            } else {
                LOGGER.error("Asset uuid is not passed in the request");
                response.setResponseIdentifier("Failure");
                return response;
            }
            //saving it in db
            messageRepository.save(editMessageRequest.getMessage());
            LOGGER.info("Message Edited Successfully");
            response.setMessage(editMessageRequest.getMessage());
            response.setResponseIdentifier("Success");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while editing message", e);
            response.setResponseIdentifier("Failure");
            return response;
        }
    }

    //delete message from activity wall AMS_UC_21
    /*
    This function will delete message of an activity wall
    message uuid is passed to this function
    First, we get that message from db
    Then, we set the parent of message to null because we don't want to delete the parent alongwith the children
    we save this change and then delete the message
     */
    DefaultResponse deleteMessage(String id) {
        LOGGER.debug("Inside Service function of delete message");
        try {
            //get message by uuid
            Message message = messageRepository.findMessageByUuid(id);
            //setting parent of message to null so that it does not delete the parent along with it
            message.setActivityWall(null);
            //saving the change
            messageRepository.save(message);
            //now deleting it
            messageRepository.deleteById(message.getId());
            LOGGER.info("Message deleted Successfully");
            return new DefaultResponse("Success", "Message deleted Successfully", "200");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while deleting message", e);
            return new DefaultResponse("Failure", "Ërror while deleting message", "500");
        }
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
    public UploadFileResponse uploadFile(MultipartFile file) {
        UploadFileResponse response = new UploadFileResponse();
        LOGGER.debug("inside service function of uploading file to s3");
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            String fileName = generateFileName(file.getOriginalFilename());
            String fileUrl = s3EnpointUrl + "/asset-images/" + fileName;
            this.s3client.putObject(new PutObjectRequest(this.bucket + "/asset-images", fileName, convFile));
            response.setResponseIdentifier("Success");
            response.setFileUrl(fileUrl);
            LOGGER.info("File uploaded Successfully");
            convFile.delete();
            return response;
        } catch (Exception e) {
            LOGGER.error("Error while uploading file to s3", e);
            e.printStackTrace();
            response.setResponseIdentifier("Failure");
            convFile.delete();
            return response;
        }
    }

    /******************************************** END s3 Functions *********************************************************/

    /******************************************* Class Functions *****************************************************/
    //generate asset number
    private String genrateAssetNumber(Long id) {
        String assetNumber = "AMS-ASSET-";
        Long myId = 1000L + id;
        String formatted = String.format("%06d", myId);
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
    public void updateUsageUnits(Usage usage) {
        try {
            Asset asset = assetRepository.findAssetByUuid(usage.getAssetUUID());
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
                usage.setAsset(asset);
                usage.setCreatedAt(new Date());
                usage.setTenantUUID(asset.getTenantUUID());
                assetRepository.save(asset);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while updating usage units of asset", e);
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

}

