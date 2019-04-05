package com.sharklabs.ams.api;


import com.sharklabs.ams.exception.EmptyEntityTableException;
import com.sharklabs.ams.request.*;
import com.sharklabs.ams.response.*;
import com.sharklabs.ams.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@CrossOrigin
@RequestMapping("/assets")
public class AssetController {

    private static final Logger LOGGER = LogManager.getLogger(AssetController.class);

    @Autowired
    AssetService assetService;

    /*******************************************Category Functions*******************************************/
    //add a category AMS_UC_01
    @RequestMapping(method = RequestMethod.POST,value="/categories")
    public @ResponseBody
    ResponseEntity addCategory(@RequestBody AddCategoryRequest addCategoryRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to add category");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.addCategory(addCategoryRequest))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Category exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //delete a category AMS_UC_05
    @RequestMapping(method = RequestMethod.DELETE,value="/categories",params = {"id"})
    public @ResponseBody
    ResponseEntity deleteCategory(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to delete category. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.deleteCategory(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Category exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get a category AMS_UC_02
    @RequestMapping(method = RequestMethod.GET,value="/categories",params = {"id"})
    public @ResponseBody
    ResponseEntity getCategory(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get category. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getCategory(id))
                .map(resp -> new ResponseEntity<GetCategoryResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Category exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get all categories AMS_UC_03
    @RequestMapping(method = RequestMethod.GET,value="/categories",params = {"tenantuuid"})
    public @ResponseBody
    ResponseEntity getAllCategories(@RequestParam String tenantuuid) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get all categories.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.GetAllCategories(tenantuuid))
                .map(resp -> new ResponseEntity<GetCategoriesResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Category exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //edit categories AMS_UC_04
    @RequestMapping(method = RequestMethod.PUT,value="/categories")
    public @ResponseBody
    ResponseEntity editCategory(@RequestBody EditCategoryRequest editCategoryRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to edit category.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.editCategory(editCategoryRequest))
                .map(resp -> new ResponseEntity<EditCategoryResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Category exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*******************************************END Category Functions*******************************************/

    /*******************************************Field Template Functions*****************************************/
    //add a field template AMS_UC_06
    @RequestMapping(method = RequestMethod.POST,value="/fieldtemplate")
    public @ResponseBody
    ResponseEntity addFieldTemplate(@RequestBody AddFieldTemplateRequest addFieldTemplateRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to add field template");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.addFieldTemplate(addFieldTemplateRequest))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Field Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //delete a field template AMS_UC_09
    @RequestMapping(method = RequestMethod.DELETE,value="/fieldtemplate",params={"id"})
    public @ResponseBody
    ResponseEntity deleteFieldTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to delete field template. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.deleteFieldTemplate(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Field Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get field template by uuid AMS_UC_07
    @RequestMapping(method = RequestMethod.GET,value="/fieldtemplate",params={"id"})
    public @ResponseBody
    ResponseEntity getFieldTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get field template. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getFieldTemplate(id))
                .map(resp -> new ResponseEntity<GetFieldTemplateResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Field Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //edit field template AMS_UC_08
    @RequestMapping(method = RequestMethod.PUT,value="/fieldtemplate")
    public @ResponseBody
    ResponseEntity editFieldTemplate(@RequestBody EditFieldTemplateRequest editFieldTemplateRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to edit field template.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.editFieldTemplate(editFieldTemplateRequest))
                .map(resp -> new ResponseEntity<EditFieldTemplateResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Field Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*******************************************END Field Template Functions*****************************************/

    /*******************************************Asset Functions**********************************************/
    //add asset AMS_UC_10
    @RequestMapping(method = RequestMethod.POST,value="")
    public @ResponseBody
    ResponseEntity addAsset(@RequestBody AddAssetRequest addAssetRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to add asset.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.addAsset(addAssetRequest))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //edit asset AMS_UC_11
    @RequestMapping(method = RequestMethod.PUT,value="")
    public @ResponseBody
    ResponseEntity editAsset(@RequestBody EditAssetRequest editAssetRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to edit asset.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.editAsset(editAssetRequest))
                .map(resp -> new ResponseEntity<EditAssetResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //delete asset AMS_UC_12
    @RequestMapping(method = RequestMethod.DELETE,value="",params={"id"})
    public @ResponseBody
    ResponseEntity deleteAsset(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to delete asset. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.deleteAsset(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get asset AMS_UC_13
    @RequestMapping(method = RequestMethod.GET,value="",params={"id"})
    public @ResponseBody
    ResponseEntity getAsset(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get asset. UUID: "+id);
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getAsset(id))
                .map(resp -> new ResponseEntity<GetAssetResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get assets AMS_UC_14
    @RequestMapping(method = RequestMethod.GET,value="")
    public @ResponseBody
    ResponseEntity getAssets() throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get assets.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getAssets())
                .map(resp -> new ResponseEntity<GetAssetsResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get paginated assets AMS_UC_22
    @RequestMapping(method = RequestMethod.GET,value="",params = {"offset","limit"})
    public @ResponseBody
    ResponseEntity getPaginatedAssets(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get page of assets.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getPaginatedAssets(offset, limit))
                .map(resp -> new ResponseEntity<GetPaginatedAssetsResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get name of types of assets by uuids AMS_UC_23
    @RequestMapping(method = RequestMethod.POST,value="/inspections/listview")
    public @ResponseBody
    ResponseEntity getNameAndTypeOfAssetsByUUIDS(@RequestBody GetNameAndTypeOfAssetsByUUIDSRequest request) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get name and type of assets by uuids.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getNameAndTypeOfAssetsByUUIDS(request))
                .map(resp -> new ResponseEntity<GetNameAndTypeOfAssetsByUUIDSResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*******************************************END Asset Functions**********************************************/

    /******************************************* Inspection Template Functions***********************************/
    //post inspection template AMS_UC_15
    @RequestMapping(method = RequestMethod.POST,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity addInspectionTemplate(@RequestBody PostInspectionTemplateRequest postInspectionTemplateRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to post inspection template of a category.");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.postInspectionTemplate(postInspectionTemplateRequest))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get inspection template by uuid AMS_UC_16
    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params={"id"})
    public @ResponseBody
    ResponseEntity getInspectionTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to get inspection template");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.getInspectionTemplate(id))
                .map(resp -> new ResponseEntity<GetInspectionTemplateResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //edit inspection template by uuid AMS_UC_17
    @RequestMapping(method = RequestMethod.PUT,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity editInspectionTemplate(@RequestBody EditInspectionTemplateRequest editInspectionTemplateRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to edit inspection template");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.editInspectionTemplate(editInspectionTemplateRequest))
                .map(resp -> new ResponseEntity<EditInspectionTemplateResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //delete inspection template by uuid AMS_UC_18
    @RequestMapping(method = RequestMethod.DELETE,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity deleteInspectionTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to delete inspection template");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.deleteInspectionTemplate(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /******************************************* END Inspection Template Functions***********************************/

    /******************************************* Activity Wall Functions********************************************/
    //add message to activity wall AMS_UC_19
    @RequestMapping(method = RequestMethod.POST,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity addMessage(@RequestBody AddMessageRequest addMessageRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to add message");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.addMessage(addMessageRequest))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //edit message to activity wall AMS_UC_20
    @RequestMapping(method = RequestMethod.PUT,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity editMessage(@RequestBody EditMessageRequest editMessageRequest) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to edit message");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.editMessage(editMessageRequest))
                .map(resp -> new ResponseEntity<EditMessageResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //delete message to activity wall AMS_UC_21
    @RequestMapping(method = RequestMethod.DELETE,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity deleteMessage(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to delete message");
        ResponseEntity responseEntity=Optional.ofNullable(assetService.deleteMessage(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*******************************************END Activity Wall Functions********************************************/

    /******************************************* File Functions ******************************************************/
    //upload file to s3 AMS_UC_24
    @RequestMapping(method = RequestMethod.POST,value="/files")
    public @ResponseBody
    ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging();
        LOGGER.info("Request received in controller to upload file to s3");
        UploadFileResponse response=assetService.uploadFile(file);
        ResponseEntity responseEntity=null;
        if(response.getResponseIdentifier().equals("Success")){
            responseEntity=new ResponseEntity<UploadFileResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseIdentifier().equals("Failure")){
            responseEntity=new ResponseEntity<UploadFileResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*******************************************END File Functions ******************************************************/
    //create a new vehicle
//    @RequestMapping(method = RequestMethod.POST,value="/vehicles")
//    public @ResponseBody
//    ResponseEntity createVehicle(@RequestBody Vehicle vehicle) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.createVehicle(vehicle))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get a vehicle by AssetNumber
//    @RequestMapping(method = RequestMethod.GET,value="/vehicles/{assetNumber}")
//    public @ResponseBody
//    ResponseEntity getVehicle(@PathVariable("assetNumber") String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getVehicle(assetNumber))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //update a vehicle
//    @RequestMapping(method = RequestMethod.PUT,value="/vehicles")
//    public @ResponseBody
//    ResponseEntity updateVehicle(@RequestBody Vehicle vehicle) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.updateVehicle(vehicle))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //Delete a vehicle by assetNumber
//    @RequestMapping(method = RequestMethod.DELETE,value="/vehicles/{assetNumber}")
//    public @ResponseBody
//    ResponseEntity updateVehicle(@PathVariable("assetNumber") String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.deleteVehicle(assetNumber))
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get Paginated List of vehicles
//    @RequestMapping(method = RequestMethod.GET,value="/vehicles",params = {"offset","limit"})
//    public @ResponseBody
//    ResponseEntity getVehicles(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getVehicles(offset,limit))
//                .map(resp -> new ResponseEntity<Page<Vehicle>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get List of vehicles
//    @RequestMapping(method = RequestMethod.GET,value="/vehicles")
//    public @ResponseBody
//    ResponseEntity getAllVehicles() throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getAllVehicles())
//                .map(resp -> new ResponseEntity<Iterable<Vehicle>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get list of vehicles given the asset numbers
//    @RequestMapping(method = RequestMethod.POST,value="/vehicles/getdetails")
//    public @ResponseBody
//    ResponseEntity getVehicles(@RequestBody List<String> assetNumbers) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getVehiclesGivenAssetNumbers(assetNumbers))
//                .map(resp -> new ResponseEntity<List<Vehicle>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //re-index vehicles
//    @RequestMapping(method = RequestMethod.POST,value="/vehicles/re-index")
//    public @ResponseBody
//    ResponseEntity reIndexVehicles() throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.reIndexVehicles())
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get vehicle by driverNumber
////    @RequestMapping(method=RequestMethod.GET,value="/vehicles",params = {"driverNumber"})
////    public @ResponseBody
////    ResponseEntity getVehicleByDriverNumber(@RequestParam String driverNumber) throws EmptyEntityTableException {
////        return Optional.ofNullable(assetService.getVehicleByDriverNumber(driverNumber))
////                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
////                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
////    }
//
//    //save an inspection report
//    @RequestMapping(method = RequestMethod.POST,value="/inspections",params = {"assetNumber"})
//    public @ResponseBody
//    ResponseEntity createIssue(@RequestBody InspectionReport inspectionReport, @RequestParam String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.saveInspectionReport(inspectionReport,assetNumber))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get list of inspection reports of a vehicle
//    @RequestMapping(method = RequestMethod.GET,value="/inspections",params = {"assetNumber"})
//    public @ResponseBody
//    ResponseEntity getInspectionReports(@RequestParam String assetNumber) throws EmptyEntityTableException {
//            return Optional.ofNullable(assetService.getInspectionReports(assetNumber))
//                    .map(resp -> new ResponseEntity<Iterable<InspectionReport>>(resp, HttpStatus.OK))
//                    .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
//    }
//
//    //get all inspection reports
//    @RequestMapping(method = RequestMethod.GET,value="/inspections")
//    public @ResponseBody
//    ResponseEntity getAllInspectionReports() throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getAllInspectionReports())
//                .map(resp -> new ResponseEntity<Iterable<InspectionReport>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
//    }
//
//    //re-index inspection reports
//    @RequestMapping(method = RequestMethod.POST,value="/inspections/re-index")
//    public @ResponseBody
//    ResponseEntity reIndexInspections() throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.reIndexInspections())
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Report exists",0L));
//    }
//
//    //save an inspection report template
//    @RequestMapping(method = RequestMethod.POST,value="/inspectiontemplates",params = {"assetNumber"})
//    public @ResponseBody
//    ResponseEntity createIssue(@RequestBody InspectionReportTemplate inspectionReportTemplate, @RequestParam String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.saveInspectionReportTemplate(inspectionReportTemplate,assetNumber))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }
//
//    //get list of inspection report templates of a vehicle
//    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params = {"assetNumber"})
//    public @ResponseBody
//    ResponseEntity getInspectionReportTemplates(@RequestParam String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getInspectionReportTemplates(assetNumber))
//                .map(resp -> new ResponseEntity<Iterable<InspectionReportTemplate>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
//    }
//
//    //get list of inspection report templates
//    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params = {"offset","limit"})
//    public @ResponseBody
//    ResponseEntity getInspectionReportTemplates(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getAllInspectionReportTemplates(offset,limit))
//                .map(resp -> new ResponseEntity<Page<InspectionReportTemplate>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
//    }
//
//    //get issues of a vehicle
//    @RequestMapping(method = RequestMethod.GET,value="/issues",params = {"assetNumber"})
//    public @ResponseBody
//    ResponseEntity getIssuesByAssetNumber(@RequestParam String assetNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getIssuesOfVehicle(assetNumber))
//                .map(resp -> new ResponseEntity<Iterable<IssueReporting>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Issues exists", 0L));
//    }
//
//    //get paginated issues
//    @RequestMapping(method = RequestMethod.GET,value="/issues",params = {"page","size"})
//    public @ResponseBody
//    ResponseEntity getPaginatedIssues(@RequestParam int page,int size) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getPaginatedIssues(page,size))
//                .map(resp -> new ResponseEntity<Page<IssueReporting>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Issues exists", 0L));
//    }
//
//    //get all issues
//    @RequestMapping(method = RequestMethod.GET,value="/issues")
//    public @ResponseBody
//    ResponseEntity getAllIssues() throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getAllIssues())
//                .map(resp -> new ResponseEntity<Iterable<IssueReporting>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Issues exists", 0L));
//    }
//
//    //add a service task
//    @RequestMapping(method = RequestMethod.POST,value="/servicetasks")
//    public @ResponseBody
//    ResponseEntity addServiceTask(@RequestBody ServiceTask serviceTask) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.addServiceTask(serviceTask))
//                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //update a service task
//    @RequestMapping(method = RequestMethod.PUT,value="/servicetasks")
//    public @ResponseBody
//    ResponseEntity updateServiceTask(@RequestBody ServiceTask serviceTask) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.updateServiceTask(serviceTask))
//                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //delete a service task
//    @RequestMapping(method = RequestMethod.DELETE,value="/servicetasks")
//    public @ResponseBody
//    ResponseEntity deleteServiceTask(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.deleteServiceTask(id))
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //get a service task by id
//    @RequestMapping(method = RequestMethod.GET,value="/servicetasks",params = {"id"})
//    public @ResponseBody
//    ResponseEntity getServiceTask(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getServiceTask(id))
//                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //get list of service tasks
//    @RequestMapping(method = RequestMethod.GET,value="/servicetasks",params = {"page","size"})
//    public @ResponseBody
//    ResponseEntity getServiceTasks(@RequestParam int page,@RequestParam int size) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getServiceTasks(page,size))
//                .map(resp -> new ResponseEntity<Page<ServiceTask>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //add a service entry
//    @RequestMapping(method = RequestMethod.POST,value="/serviceentries")
//    public @ResponseBody
//    ResponseEntity addServiceEntries(@RequestBody ServiceEntry serviceEntry) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.addServiceEntry(serviceEntry))
//                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
//    }
//
//    //update a service entry
//    @RequestMapping(method = RequestMethod.PUT,value="/serviceentries")
//    public @ResponseBody
//    ResponseEntity updateServiceEntries(@RequestBody ServiceEntry serviceEntry) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.updateServiceEntry(serviceEntry))
//                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
//    }
//
//    //delete a service entry
//    @RequestMapping(method = RequestMethod.DELETE,value="/serviceentries",params = {"id"})
//    public @ResponseBody
//    ResponseEntity deleteServiceEntries(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.deleteServiceEntry(id))
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
//    }
//
//    //get a service entry by id
//    @RequestMapping(method = RequestMethod.GET,value="/serviceentries",params = {"id"})
//    public @ResponseBody
//    ResponseEntity getServiceEntryById(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getServiceEntry(id))
//                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
//    }
//
//    //get list of service entries
//    @RequestMapping(method = RequestMethod.GET,value="/serviceentries",params = {"page","size"})
//    public @ResponseBody
//    ResponseEntity getServiceEntries(@RequestParam int page,@RequestParam int size) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getServiceEntries(page,size))
//                .map(resp -> new ResponseEntity<Page<ServiceEntry>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
//    }
//
//    //add a work order
//    @RequestMapping(method = RequestMethod.POST,value="/workorder")
//    public @ResponseBody
//    ResponseEntity addWorkOrder(@RequestBody WorkOrder workOrder) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.addWorkOrder(workOrder))
//                .map(resp -> new ResponseEntity<WorkOrder>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Work Order exists", 0L));
//    }
//
//    //update a work order
//    @RequestMapping(method = RequestMethod.PUT,value="/workorder")
//    public @ResponseBody
//    ResponseEntity updateWorkOrder(@RequestBody WorkOrder workOrder) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.updateWorkOrder(workOrder))
//                .map(resp -> new ResponseEntity<WorkOrder>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Work Order exists", 0L));
//    }
//
//    //delete a work order
//    @RequestMapping(method = RequestMethod.DELETE,value="/workorder",params = {"id"})
//    public @ResponseBody
//    ResponseEntity deleteWorkOrder(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.deleteWorkOrder(id))
//                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Work Order exists", 0L));
//    }
//
//    //get a work order by id
//    @RequestMapping(method = RequestMethod.GET,value="/workorder",params = {"id"})
//    public @ResponseBody
//    ResponseEntity getWorkOrderById(@RequestParam Long id) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getWorkOrder(id))
//                .map(resp -> new ResponseEntity<WorkOrder>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Work Order exists", 0L));
//    }
//
//    //get list of work orders
//    @RequestMapping(method = RequestMethod.GET,value="/workorder",params = {"page","size"})
//    public @ResponseBody
//    ResponseEntity getWorkOrders(@RequestParam int page,@RequestParam int size) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getWorkOrders(page,size))
//                .map(resp -> new ResponseEntity<Page<WorkOrder>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Work Order exists", 0L));
//    }
//
//    //get asset from s3
//    @RequestMapping(method = RequestMethod.GET,value = "",params = {"url"})
//    public @ResponseBody
//    ResponseEntity getFile(@RequestParam String url) throws EmptyEntityTableException, IOException {
//        return Optional.ofNullable(assetService.getFileFroms3(url))
//                .map(resp -> new ResponseEntity<ResponseEntity<byte[]>>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("File not found", 0L));
//    }
}
