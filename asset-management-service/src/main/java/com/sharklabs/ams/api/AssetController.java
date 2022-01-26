package com.sharklabs.ams.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharklabs.ams.events.asset.AssetsNameModel;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModelResponse;
import com.sharklabs.ams.exception.EmptyEntityTableException;
import com.sharklabs.ams.request.*;
import com.sharklabs.ams.response.*;
import com.sharklabs.ams.security.SCIM2Util;
import com.sharklabs.ams.util.AccessDeniedException;
import com.sharklabs.ams.util.Constant;
import com.sharklabs.ams.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shark.commons.util.ApplicationException;

import javax.persistence.Access;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import com.sun.xml.internal.bind.v2.TODO;
//import org.springframework.security.access.prepost.PreAuthorize;


@Controller
@CrossOrigin
@RequestMapping("/assets")
public class AssetController {

    private static final Logger LOGGER = LogManager.getLogger(AssetController.class);

    @Autowired
    AssetService assetService;
    
    @Autowired
    SCIM2Util scim2Util;

    /*******************************************Category Functions*******************************************/

    //add a category AMS_UC_01
    @RequestMapping(method = RequestMethod.POST,value="/categories")
    @Caching(evict = {
            @CacheEvict(value = "categories",allEntries = true)
    })
    public @ResponseBody
    ResponseEntity addCategory(@RequestBody AddCategoryRequest addCategoryRequest) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add category");
            DefaultResponse response=assetService.addCategory(addCategoryRequest);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Category, details: "+new ObjectMapper().writeValueAsString(addCategoryRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for adding Category, details: "+new ObjectMapper().writeValueAsString(addCategoryRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            addCategoryRequest = null;
        }
        return responseEntity;
    }

    //delete a category AMS_UC_05
    @RequestMapping(method = RequestMethod.DELETE,value="/categories",params = {"id"})
    @Caching(evict = {
            @CacheEvict(value = "categories",allEntries = true)
    })
    public @ResponseBody
    ResponseEntity deleteCategory(@RequestParam String id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete category. UUID: "+id);
            DefaultResponse response=assetService.deleteCategory(id);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for deleting Category, details: id:"+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for deleting Category, details: id:"+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get a category AMS_UC_02
    @RequestMapping(method = RequestMethod.GET,value="/categories",params = {"id"})
    public @ResponseBody
    ResponseEntity getCategory(@RequestParam String id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get category. UUID: "+id);
            GetCategoryResponse response=assetService.getCategory(id);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetCategoryResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetCategoryResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Category, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Category, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get all categories AMS_UC_03
    @RequestMapping(method = RequestMethod.GET,value="/categories",params = {"tenantuuid"})
    public @ResponseBody
    ResponseEntity getAllCategories(@RequestParam String tenantuuid) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get all categories.");
            GetCategoriesResponse response=assetService.GetAllCategories(tenantuuid);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetCategoriesResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetCategoriesResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting all Categories, details: tenantUUID: "+tenantuuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting all Categories, details: tenantUUID: "+tenantuuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //edit categories AMS_UC_04
    @RequestMapping(method = RequestMethod.PUT,value="/categories")
    @Caching(evict = {
            @CacheEvict(value = "categories",allEntries = true)
    })
    public @ResponseBody
    ResponseEntity editCategory(@RequestBody EditCategoryRequest editCategoryRequest) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to edit category.");
            EditCategoryResponse response=assetService.editCategory(editCategoryRequest);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<EditCategoryResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<EditCategoryResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for editing Category, details: "+new ObjectMapper().writeValueAsString(editCategoryRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for editing Category, details: "+new ObjectMapper().writeValueAsString(editCategoryRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            editCategoryRequest = null;
        }
        return responseEntity;
    }

    //purpose of controller to delete category field by Category Name, Tenant Uuid, Field Name
    @DeleteMapping("/categories/fields")
    public @ResponseBody ResponseEntity deleteCategoryFields(@RequestParam String categoryName, @RequestParam String tenantUuid, @RequestParam String fieldName){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in delete category field controller. Category Name: " + categoryName + " tenant uuid: " + tenantUuid + " field Name " + fieldName);
            responseEntity = new ResponseEntity<DefaultResponse>(assetService.deleteCategoryFields(categoryName,tenantUuid,fieldName),HttpStatus.OK);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from delete category field controller. Category Name: " + categoryName + " tenant uuid: " + tenantUuid + " field Name " + fieldName);
            util.clearThreadContextForLogging();
        }
        return responseEntity;
    }

    //get Assets and Assets group by category uuid
    @GetMapping("/categories/asset/asset-group")
    public @ResponseBody
    ResponseEntity getAssetGroupsAndAssetsByCategoryUUID(@RequestParam String categoryUUID, @RequestParam String tenantUUID) {
        Util util = new Util();
        ResponseEntity response = null;
        try {
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of get Asset groups and Assets by category uuid: " + categoryUUID);
            response = new ResponseEntity<>(assetService.getAssetGroupsAndAssetsByCategoryUUID(categoryUUID,tenantUUID), HttpStatus.OK);
        }catch (AccessDeniedException ade){
            LOGGER.error("Access is denied  for getting Asset groups and Assets by category UUID.",ade);
            response = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch (Exception e){
            LOGGER.error("An Error occurred while getting Asset groups and Assets by categoru UUID.",e);
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally {
            LOGGER.info("Returning from controller of getting Asset groups and Assets by catgeory uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    //Web App get Categories list by tenant uuid
    @GetMapping("/categories/list")
    @Cacheable( value = "categories")
    public @ResponseBody
    CategoriesListResponse getCategoriesListByTenantUUID(@RequestParam("tenantUUID") String tenantUUID){
        Util util = new Util();
        CategoriesListResponse response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in get categories list by tenant uuid: " + tenantUUID);
            response = assetService.getCategoriesListByTenantUUID(tenantUUID);
        }catch (AccessDeniedException ae){
            response = new CategoriesListResponse(null,Constant.FAILURE);
        }catch (Exception e){
            response = new CategoriesListResponse(null,Constant.FAILURE);
        }finally {
            LOGGER.info("Returning from controller of get categories list by tenant uuid.");
        }
        return response;
    }

    @GetMapping("/categories/fields")
    public @ResponseBody
    ResponseEntity getCategoriesFieldsListByUUID(@RequestParam("uuid") String uuid){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in get categories fields list by tenant uuid: " + uuid);
            responseEntity = new ResponseEntity<CategoriesFieldsListResponse>(assetService.getCategoriesFieldsListByTenantUUID(uuid),HttpStatus.OK);
        }catch (AccessDeniedException ae){
            responseEntity = new ResponseEntity<String>(ae.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.OK);
        }finally {
            LOGGER.info("Returning from controller of get categories fields list by tenant uuid.");
        }
        return responseEntity;
    }

    /*******************************************END Category Functions*******************************************/

    /*******************************************Field Template Functions*****************************************/

    //add a field template AMS_UC_06
    @RequestMapping(method = RequestMethod.POST,value="/fieldtemplate")
    public @ResponseBody
    ResponseEntity addFieldTemplate(@RequestBody AddFieldTemplateRequest addFieldTemplateRequest) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add field template");
            DefaultResponse response=assetService.addFieldTemplate(addFieldTemplateRequest);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Field Template, details: "+new ObjectMapper().writeValueAsString(addFieldTemplateRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for adding Field Template, details: "+new ObjectMapper().writeValueAsString(addFieldTemplateRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            addFieldTemplateRequest = null;
        }
        return responseEntity;
    }

    //delete a field template AMS_UC_09
    @RequestMapping(method = RequestMethod.DELETE,value="/fieldtemplate",params={"id"})
    public @ResponseBody
    ResponseEntity deleteFieldTemplate(@RequestParam String id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete field template. UUID: "+id);
            DefaultResponse response=assetService.deleteFieldTemplate(id);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for delete Field Template, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for delete Field Template, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get field template by uuid AMS_UC_07
    @RequestMapping(method = RequestMethod.GET,value="/fieldtemplate",params={"id"})
    public @ResponseBody
    ResponseEntity getFieldTemplate(@RequestParam String id){
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get field template. UUID: "+id);
            GetFieldTemplateResponse response=assetService.getFieldTemplate(id);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetFieldTemplateResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetFieldTemplateResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Field Template, details: id:"+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Field Template, details: id:"+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //edit field template AMS_UC_08
    @RequestMapping(method = RequestMethod.PUT,value="/fieldtemplate")
    public @ResponseBody
    ResponseEntity editFieldTemplate(@RequestBody EditFieldTemplateRequest editFieldTemplateRequest) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to edit field template.");
            EditFieldTemplateResponse response=assetService.editFieldTemplate(editFieldTemplateRequest);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<EditFieldTemplateResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<EditFieldTemplateResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for editing Field Template, details: "+new ObjectMapper().writeValueAsString(editFieldTemplateRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for editing Field Template, details: "+new ObjectMapper().writeValueAsString(editFieldTemplateRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            editFieldTemplateRequest = null;
        }
        return responseEntity;
    }

    /*******************************************END Field Template Functions*****************************************/

    /*******************************************Import Template Functions*****************************************/

    @PostMapping("/import/template")
    public @ResponseBody
    ResponseEntity addAssetImportTemplate(@RequestBody ImportTemplateRequest request) {
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request receive in add Asset import template controller.");
            response = new ResponseEntity<DefaultResponse>(assetService.addAssetImportTemplate(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            response = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of add Asset import template.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    @PostMapping("/import/templates")
    public @ResponseBody
    ResponseEntity getPaginatedAssetImportTemplatesForSDT(@RequestBody GetPaginatedDataForSDTRequest request){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in get paginated Asset import templates for Sdt.");
            responseEntity = new ResponseEntity<GetPaginatedDataForSDTResponse>(assetService.getPaginatedAssetImportTemplatesForSDT(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of get paginated Asset import templates for sdt.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @GetMapping("/import/templates")
    public @ResponseBody
    ResponseEntity getListOfImportTemplateByUserUUIDAndTenantUUID(@RequestParam String userUUID, @RequestParam String tenantUUID){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in get list of import template by user uuid: " + userUUID + " and tenant uuid: " + tenantUUID);
            responseEntity = new ResponseEntity<ImportTemplateListResponse>(assetService.getListOfImportTemplateByUserUUIDAndTenantUUID(userUUID,tenantUUID),HttpStatus.OK);
        } catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        } catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of get list of import template by user uuid and tenant uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @PostMapping("/download/template")
    public @ResponseBody
    ResponseEntity downloadAssetImportTemplate(@RequestBody DownloadCSVTemplateRequest request) {
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request receive in download Asset import template controller.");
            response = new ResponseEntity<GetFileResponse>(assetService.downloadAssetImportTemplate(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            response = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of download Asset import template.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    @PostMapping("/import/csv")
    public @ResponseBody
    ResponseEntity importBulkAssetsByCSV(@RequestParam("file") MultipartFile file,
                                         @RequestParam("tenantUUID") String tenantUUID,
                                         @RequestParam("userUUID") String userUUID,
                                         @RequestParam("userName") String userName,
                                         @RequestParam("importType") String importType) {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of import bulk Asset by csv.");
            ImportBulkAssetRequest request = new ImportBulkAssetRequest(tenantUUID,userUUID,userName,importType);
            responseEntity = new ResponseEntity<ImportBulkAssetResponse>(assetService.importBulkAssetsByCSV(file,request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
            ade = null;
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally {
            LOGGER.info("Returning from controller of import bulk Asset by csv.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @GetMapping("/download/failures")
    public @ResponseBody
    ResponseEntity downloadFailureImports(@RequestParam("importUUID") String importUUID) {
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request receive in download failure import controller.");
            response = new ResponseEntity<GetFileResponse>(assetService.downloadFailureImports(importUUID),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            response = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of download failure import template.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }

    @PostMapping("/import/last")
    public @ResponseBody
    ResponseEntity getPaginatedLastAssetImports(@RequestBody GetPaginatedDataForSDTRequest request){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in get paginated last Asset imports for Sdt.");
            responseEntity = new ResponseEntity<GetPaginatedDataForSDTResponse>(assetService.getPaginatedLastImports(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of get paginated last Asset imports for Sdt.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @PostMapping("/export/detail")
    public @ResponseBody
    ResponseEntity exportAssetDetailInBulk(@RequestBody ExportAssetInBulkRequest request){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in export Asset detail in bulk.");
            responseEntity = new ResponseEntity<GetFileResponse>(assetService.exportAssetDetailInBulk(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of export Asset detail in bulk.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    /*******************************************Import Template Functions*****************************************/

    /*******************************************Asset Functions**********************************************/
    //add asset AMS_UC_10
    @RequestMapping(method = RequestMethod.POST,value="")
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)


    })
    public @ResponseBody
    ResponseEntity addAsset(@RequestBody AddAssetRequest addAssetRequest/*, OAuth2Authentication oAuth2Authentication*/) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add asset.");
            DefaultResponse response=assetService.addAsset(addAssetRequest/*,oAuth2Authentication*/);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for creating Asset, details: "+new ObjectMapper().writeValueAsString(addAssetRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("an unknown Error occurred for creating Asset, details: "+new ObjectMapper().writeValueAsString(addAssetRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            addAssetRequest = null;
        }
        return responseEntity;
    }

    //edit asset AMS_UC_11
    @RequestMapping(method = RequestMethod.PUT,value="")
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity editAsset(@RequestBody EditAssetRequest editAssetRequest) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to edit asset.");
            EditAssetResponse response=assetService.editAsset(editAssetRequest);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<EditAssetResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<EditAssetResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for updating Asset, details: "+new ObjectMapper().writeValueAsString(editAssetRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for updating Asset, details: "+new ObjectMapper().writeValueAsString(editAssetRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR );
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            editAssetRequest = null;
        }
        return responseEntity;
    }

    //delete asset AMS_UC_12
    @RequestMapping(method = RequestMethod.DELETE,value="",params={"id"})
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity deleteAsset(@RequestParam String id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete asset. UUID: "+id);
            DefaultResponse response=assetService.deleteAsset(id);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for deleting Asset, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for deleting Asset, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //delete asset Images AMS_UC_12
    @RequestMapping(method = RequestMethod.DELETE,value="/image",params={"id"})
    public @ResponseBody
    ResponseEntity deleteAssetImages(@RequestParam Long id) {
        Util util = new Util();
        LOGGER.info("Request received in controller to delete asset. UUID: "+id);
        util.setThreadContextForLogging(scim2Util);
        ResponseEntity responseEntity=null;
        DefaultResponse response=assetService.deleteAssetImages(id);
        if(response.getResponseCode().equals("200")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseCode().equals("500")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    @DeleteMapping("/archive-delete")
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity archiveOrDeleteAssetByUuid(@RequestParam String uuid, @RequestParam String type){
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of archive or delete Asset by uuid: " + uuid);
            response = new ResponseEntity<DefaultResponse>(assetService.archiveOrDeleteAssetByUuid(uuid,type),HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("An Error occurred while archive or delete Asset by uuid.",e);
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returing from controller of archive or delete Asset by uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }



    //get asset AMS_UC_13
    @RequestMapping(method = RequestMethod.GET,value="",params={"id"})
    public @ResponseBody
    ResponseEntity getAsset(@RequestParam String id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get asset. UUID: "+id);
            GetAssetResponse response=assetService.getAsset(id);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetAssetResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetAssetResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Asset, details: id:"+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Asset, details: id:"+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get asset with provided desired detail
    @PostMapping(value="/detail",consumes = "application/json")
    public @ResponseBody
    ResponseEntity getAssetwithDetail(@RequestBody AssetDetailRequest assetDetail) throws IOException{

        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get asset with detail. UUID: "+assetDetail.getUuid());
            responseEntity= new ResponseEntity<GetAssetDetailResponse>(assetService.getAssetDetail(assetDetail,assetDetail.getUuid()),HttpStatus.OK);
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Asset with detail, details: "+new ObjectMapper().writeValueAsString(assetDetail),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Asset with detail, details: "+new ObjectMapper().writeValueAsString(assetDetail),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            assetDetail = null;
        }

        return responseEntity;
    }

    //get assets AMS_UC_14
    @RequestMapping(method = RequestMethod.GET,value="",params = {"tenantuuid"})
    public @ResponseBody
    ResponseEntity getAssets(@RequestParam String tenantuuid) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get assets.");
            GetAssetsResponse response=assetService.getAssets(tenantuuid);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetAssetsResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetAssetsResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for geting Assets, details: tenantUUID"+tenantuuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for geting Assets, details: tenantUUID"+tenantuuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //Web App Api
    @GetMapping("/asset-groups/name")
    @Cacheable(value = "assetAndAssetGroup")
    public @ResponseBody
    AssetsNameAndUUIDResponse getAssetsAndAssetGroupsNameAndUUIDByTenantUUID(@RequestParam String tenantUUID, @RequestParam String accessKey, @RequestParam(required = false) String assetUUID){
        try{
            LOGGER.info("Request received in get Assets and Asset groups name and uuid by tenant uuid: " + tenantUUID);
            return assetService.getAssetsAndAssetGroupsNameAndUUIDByTenantUUID(tenantUUID,accessKey,assetUUID);
        }catch (AccessDeniedException ae){
            return new AssetsNameAndUUIDResponse(null,null,null,Constant.FAILURE);
        }catch (Exception e){
            return new AssetsNameAndUUIDResponse(null,null,null,Constant.FAILURE);
        }
    }

    /* Written By Kumail Ahmed Khan */
    @PostMapping ("/asset-groups")
    @Cacheable(value = "assetGroupByAsset")
    public @ResponseBody
    AssetGroupByAssetResponse getAssetGroupsByAssets(@RequestBody AssetGroupByAssetUUIDsRequest request){
        try{
            LOGGER.info("Request received in Controller to get Asset Group By AssetUUIDs: "+convertToJSON(request));
            return assetService.getAssetGroupsByAssets(request);
        }catch (AccessDeniedException ae){
            return new AssetGroupByAssetResponse();
        }catch (Exception e){
            return new AssetGroupByAssetResponse();
        }
    }
    //get Asset Name and AssetgroupName  by
    @PostMapping ("/asset/asset-group/uuid")
    @Cacheable(value = "assetAndAssetGroupByUUID")
    public @ResponseBody
    AssetAndAssetGroupResponse getAssetAndAssetGroup(@RequestBody AssetAndAssetGroupRequest request){
        try{
            LOGGER.info("Request received in Controller to get Asset Group By AssetUUIDs: "+convertToJSON(request));
            return assetService.getAssetAndAssetGroup(request);
        }catch (AccessDeniedException ae){
            return new AssetAndAssetGroupResponse();
        }catch (Exception e){
            return new AssetAndAssetGroupResponse();
        }
    }

    @PostMapping ("/asset/asset-group/useruuid")
    public @ResponseBody
    AssetAndAssetGroupResponse getAssetAndAssetGroupUseruuid(@RequestBody AssetAndAssetGroupRequest request){
        try{
            LOGGER.info("Request received in Controller to get Asset Group And Asset By UserUUIDs: "+convertToJSON(request));
            return assetService.getAssetAndAssetGroupUseruuid(request);
        }catch (AccessDeniedException ae){
            return new AssetAndAssetGroupResponse();
        }catch (Exception e){
            return new AssetAndAssetGroupResponse();
        }
    }
    @PostMapping ("/asset/asset-group/tenantuuid")
    public @ResponseBody
    AssetAndAssetGroupResponse getAssetAndAssetGroupTenantuuid(@RequestBody AssetAndAssetGroupRequest request){
        try{
            LOGGER.info("Request received in Controller to get Asset Group And Asset By TenantUUIDs: "+convertToJSON(request));
            return assetService.getAssetAndAssetGroupTenantuuid(request);
        }catch (AccessDeniedException ae){
            return new AssetAndAssetGroupResponse();
        }catch (Exception e){
            return new AssetAndAssetGroupResponse();
        }
    }

    //get asset basic detail by tenant AMS_UC_31
    @RequestMapping(method = RequestMethod.GET,value="/basicinfo",params = {"tenantuuid"})
    public @ResponseBody
    ResponseEntity getBasicAssetDetailByTenant(@RequestParam String tenantuuid) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get basic asset detail by tenant uuid. TenantUUID: "+tenantuuid);
            GetBasicAssetDetailByTenantResponse response=assetService.getBasicAssetDetailByTenant(tenantuuid);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetBasicAssetDetailByTenantResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetBasicAssetDetailByTenantResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Basic Asset Detail by tenant, details: tenantUUID"+tenantuuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Basic Asset Detail by tenant, details: tenantUUID"+tenantuuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get paginated assets AMS_UC_22
    @RequestMapping(method = RequestMethod.GET,value="",params = {"offset","limit","tenantuuid"})
    public @ResponseBody
    ResponseEntity getPaginatedAssets(@RequestParam int offset,@RequestParam int limit,@RequestParam String tenantuuid) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get page of assets.");
            GetPaginatedAssetsResponse response=assetService.getPaginatedAssets(offset, limit,tenantuuid);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedAssetsResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedAssetsResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Assets, details: offset: "+offset+", limit: "+limit+", tenantUUID: "+tenantuuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Assets, details: offset: "+offset+", limit: "+limit+", tenantUUID: "+tenantuuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get paginated assets for SDT
    @PostMapping("/sdt")
    public @ResponseBody
    ResponseEntity getPaginatedAssetsForSDT(@RequestBody GetPaginatedDataForSDTRequest request) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get page of assets for SDT.");
            GetPaginatedAssetsResponse response=assetService.getPaginatedAssetsForSDT(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedAssetsResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedAssetsResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Assets for sDT, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Assets for sDT, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    @GetMapping("/bulk")
    @ResponseBody
    public ResponseEntity getPaginatedBulkOrSingleAssets(@RequestParam String tenantuuid, @RequestParam int offset, @RequestParam int limit, @RequestParam boolean isBulk){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered controller of get paginated bulk or single assets. ");

            responseEntity = new ResponseEntity<GetPaginatedAssetsResponse>(assetService.getPaginatedBulkOrSingleAssets(tenantuuid,offset,limit,isBulk),HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("An Error occurred while getting page of bulk or single assets. ",e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of getting page of bulk or single assets.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get name of types of assets by uuids AMS_UC_23
    @RequestMapping(method = RequestMethod.POST,value="/inspections/listview")
    public @ResponseBody
    ResponseEntity getNameAndTypeOfAssetsByUUIDS(@RequestBody GetNameAndTypeOfAssetsByUUIDSRequest request) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get name and type of assets by uuids.");
            GetNameAndTypeOfAssetsByUUIDSResponse response=assetService.getNameAndTypeOfAssetsByUUIDS(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetNameAndTypeOfAssetsByUUIDSResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetNameAndTypeOfAssetsByUUIDSResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Name and Type of Assets, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Name and Type of Assets, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST,value="/inspections/assetName")
    public @ResponseBody
    ResponseEntity getAssetNameByUUIDS(@RequestBody GetNameAndTypeOfAssetsByUUIDSRequest request) throws IOException {
      //  Util util = new Util();
        ResponseEntity responseEntity=null;

        try {
            //util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get name and type of assets by uuids.");
            AssetsNameModel assetsNameModel=assetService.getNameByUUIDS(request);
            responseEntity = new ResponseEntity<AssetsNameModel>(assetsNameModel,HttpStatus.OK);

        } catch (Exception e){
            LOGGER.error("An Error occurred while fetching Asset Name",e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of fetching assets Name");
          //  util.clearThreadContextForLogging();
            //util = null;
        }

       // util.clearThreadContextForLogging();
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST,value="/inspections/assetCategory", params={"tenantUUID"})
    public @ResponseBody
    ResponseEntity getAssetCategoryByUUIDS(@RequestParam("tenantUUID") String tenantUUID) throws IOException {
        //  Util util = new Util();
        ResponseEntity responseEntity=null;
        try {
            //util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get asset Category by uuids.");
            AssetBasicDetailModelResponse assetsNameModel=assetService.fetchAssetNameAndType(tenantUUID);
            responseEntity = new ResponseEntity<AssetBasicDetailModelResponse>(assetsNameModel,HttpStatus.OK);

        } catch (Exception e){
            LOGGER.error("An Error occurred while fetching Asset Category",e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of fetching assets Category");
            //  util.clearThreadContextForLogging();
            //util = null;
        }
        // util.clearThreadContextForLogging();
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET,value="/",params = {"'tenantuuid'"})
    public @ResponseBody
    ResponseEntity getNameAndUUIDOfAssetsByTenantUUID(@RequestParam String tenantuuid) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get name and type of assets by tenantuuid.");
            GetNameAndUUIDOfAssetResponse response= null;
//            try {
                response = assetService.getNameAndUUIDOfAssetByTenantUUID(tenantuuid);
                responseEntity=new ResponseEntity<>(response,HttpStatus.OK);
//            } catch (ApplicationException e) {
//                LOGGER.error(e.getMessage(),e);
//            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Name and UUID of Asset, details: tenantUUID"+tenantuuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(ApplicationException ae){
            LOGGER.error("An known Error occurred for getting Name and UUID of Asset, details: tenantUUID"+tenantuuid,ae);
            responseEntity = new ResponseEntity<String>(ae.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            ae = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Name and UUID of Asset, details: tenantUUID"+tenantuuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET,value = "",params = {"name"})
    public @ResponseBody
    ResponseEntity getAssetUUIDsByName(@RequestParam String name) throws EmptyEntityTableException{
        Util util=new Util();
        ResponseEntity responseEntity;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get asset uuids by name. name: "+name);


            GetAssetUUIDsByNameResponse response=assetService.getAssetUUIDsByName(name);
            if(response.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(response,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for get Asset UUID by name, details: name: "+name,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for get Asset UUID by name, details: name: "+name,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }

        return responseEntity;
    }

    // Update all Asset Fields of an Asset
    @PutMapping("/fields")
    public @ResponseBody
    ResponseEntity updateAllAssetFields(@RequestBody UpdateAssetFieldsRequest updateAssetFieldsRequest) throws JsonProcessingException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered controller of updating all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()));

            responseEntity = new ResponseEntity<DefaultResponse>(assetService.updateAssetFields(updateAssetFieldsRequest),HttpStatus.OK);
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for updating Asset Fields, details: "+new ObjectMapper().writeValueAsString(updateAssetFieldsRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An Error occurred while updating all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally{
            LOGGER.info("Returning from controller of updating all assetFields of asset uuid: "+updateAssetFieldsRequest.getAssetUUID()+" object: " + new ObjectMapper().writeValueAsString(updateAssetFieldsRequest.getAssetFields()));
            util.clearThreadContextForLogging();
            util = null;
            updateAssetFieldsRequest = null;
        }

        return responseEntity;
    }

    @PostMapping("/fields")
    public @ResponseBody
    ResponseEntity getAssetFields(@RequestBody List<String> assetUUIds) throws JsonProcessingException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered controller of getting asset fields ");
            responseEntity = new ResponseEntity<GetAssetFeildResponse>(assetService.getAssetFields(assetUUIds),HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("An Error occurred while get asset fields");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }finally {
            LOGGER.info("Returning from controller of getting asset fields");
            util.clearThreadContextForLogging();
            util = null;
        }

        return responseEntity;
    }

    @PostMapping("/user")
    public @ResponseBody
    ResponseEntity getAssetNameAndUUIDForAps(@RequestParam("assetUUIDS") List<String> assetUUIDS){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            responseEntity = new ResponseEntity<GetUserAssetsResponse>(assetService.getAssetNameAndUUIDForAps(assetUUIDS),HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("An Error Occurred while getting Assets name and uuid for aps.",e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of get Asset name and uuid for Aps.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @GetMapping("/detail")
    public @ResponseBody
    ResponseEntity getAssetNameAndNumberAndCategoryByAssetUUID(@RequestParam String uuid){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered controller of get Asset name and number and category by Asset uuid: "+uuid);
            responseEntity = new ResponseEntity<AssetNameAndNumberResponse>(assetService.getAssetNameAndNumberAndCategoryByAssetUUID(uuid),HttpStatus.OK);
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Asset name and number and category by Asset uuid: "+uuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset name and number and category by Asset uuid: "+uuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally{
            LOGGER.info("Returning from controller of get Asset name and number and category by Asset uuid: "+uuid);
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @GetMapping("/images")
    public @ResponseBody
    ResponseEntity getAssetImageByAssetUUID(@RequestParam String uuid){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered controller of get Asset image by Asset uuid: "+uuid);
            responseEntity = new ResponseEntity<GetAssetImageResponse>(assetService.getAssetImageByAssetUUID(uuid),HttpStatus.OK);
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Asset image by Asset uuid: "+uuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting Asset image by Asset uuid: "+uuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally{
            LOGGER.info("Returning from controller of get Asset images by Asset uuid: "+uuid);
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @PostMapping("/export/excel")
    public @ResponseBody
    ResponseEntity exportExcelSample(@RequestBody ExportSampleExcelRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Inside service function of export excel sample file. Details: " + convertToJSON(request));
            responseEntity = new ResponseEntity<ExportSampleExcelResponse>(assetService.exportExcelSample(request),HttpStatus.OK);
        }catch (AccessDeniedException ae){
            responseEntity = new ResponseEntity<String>(ae.getMessage(),HttpStatus.FORBIDDEN);
            ae = null;
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of export Excel Sample.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @PostMapping("/import/excel")
    public @ResponseBody
    ResponseEntity importExcelFile(@RequestParam("file") MultipartFile file, @RequestParam String category) {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of import excel file");
            responseEntity = new ResponseEntity<ImportExcelResponse>(assetService.importExcelSample(file,category),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
            ade = null;
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally {
            LOGGER.info("Returning from controller of import excel file.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @PostMapping("/export/details")
    public @ResponseBody
    ResponseEntity exportAssetDetails(@RequestBody ExportAssetDetailRequest request){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of export Asset details: " + convertToJSON(request));
            responseEntity = new ResponseEntity<ExportSampleExcelResponse>(assetService.exportAssetDetails(request),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of export Asset Details.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    /*******************************************END Asset Functions**********************************************/

    /*******************************************Asset Mapper Functions**********************************************/

    //purpose of this function is map Assets Data to plain Table for SDT functionality
    // its one time function to map data of an organization
    @PutMapping("/map")
    public @ResponseBody
    ResponseEntity mapAssetsDataToCookedTableByType(@RequestParam String organizationId, @RequestParam String type){
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to map Assets data to Cooked Table by type: " + type);
            responseEntity = new ResponseEntity<DefaultResponse>(assetService.mapAssetsDataToCookedTableByType(organizationId,type),HttpStatus.OK);
        }catch (AccessDeniedException ade){
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of mapping Assets Data to Cooked Table.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    /*******************************************END Asset Mapper Functions**********************************************/

    /******************************************* Consumption Functions *******************************************/

    //post consumption units of asset AMS_UC_25
    @RequestMapping(method = RequestMethod.POST,value="/consumption")
    public @ResponseBody
    ResponseEntity postConsumptionUnit(@RequestBody AddConsumptionUnitsRequest request/*, OAuth2Authentication oAuth2Authentication*/) throws ApplicationException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;

        try{
            util.setThreadContextForLogging(scim2Util);

            LOGGER.info("Request received in controller to add consumption units of asset. Asset UUID: "+request.getAssetUUID());
            DefaultResponse response=assetService.addConsumptionUnits(request);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Consumption, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An Error occurred while  to add Consumption in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller to add Consumption in asset service");
            util=null;
            request = null;
        }
        return responseEntity;
    }

    @RequestMapping(method=RequestMethod.GET,value="/consumption",params={"uuid","offset","limit"})
    public @ResponseBody
    ResponseEntity getPaginatedConsumptionsByAsset(@RequestParam String uuid, @RequestParam int offset, @RequestParam int limit) throws EmptyEntityTableException{
        Util util = new Util();
        ResponseEntity responseEntity;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get consumption units of asset. Asset UUID: "+uuid+" with offset: "+offset+" and limit: "+limit);

            GetPaginatedConsumptionsResponse response=assetService.getPaginatedConsumptionsByAsset(uuid, offset,limit);
            if(response.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(response,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Consumption by Asset, details: assetUUID"+uuid+", offset: "+offset+", limit: "+limit,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Consumption by Asset, details: assetUUID"+uuid+", offset: "+offset+", limit: "+limit,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //delete consumption units of asset AMS_UC_26
    @RequestMapping(method = RequestMethod.DELETE,value="/consumption",params = {"uuid"})
    public @ResponseBody
    ResponseEntity deleteConsumptionUnit(@RequestParam String uuid) throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete consumption unit by uuid. UUID: "+uuid);
            DefaultResponse response=assetService.deleteConsumptionUnits(uuid);
            if(response.getResponseCode().equals("200")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseCode().equals("500")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for delete Consumption, details: consumptionUuid"+uuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for delete Consumption, details: consumptionUuid"+uuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get paginated consumptions AMS_UC_29
    @RequestMapping(method = RequestMethod.POST,value="/consumption/filter")
    public @ResponseBody
    ResponseEntity getPaginatedConsumptions(@RequestBody GetPaginatedConsumptionsRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get paginated consumptions. TenantUUID: "+request.getTenantUUID()+" Offset: "+request.getOffset()+" Limit: "+request.getLimit()+"  AssetID: "+request.getAssetUUID()+" Start Date: "+request.getStartDate()+" End Date: "+request.getEndDate());
            GetPaginatedConsumptionsResponse response=assetService.getPaginatedConsumptions(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedConsumptionsResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedConsumptionsResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for get paginated Consumption, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occcurred for get paginated Consumption, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    //get paginated consumptions by asset uuids AMS_UC_31
    @RequestMapping(method = RequestMethod.POST,value="/consumption/filter/assets")
    public @ResponseBody
    ResponseEntity getPaginatedConsumptionsByAssets(@RequestBody GetPaginatedConsumptionsByAssetsRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get paginated consumptions by asset uuids. Offset: "+request.getOffset()+" Limit: "+request.getLimit());
            GetPaginatedConsumptionsByAssetsResponse response=assetService.getPaginatedConsumptionsByAssets(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedConsumptionsByAssetsResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedConsumptionsByAssetsResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Consumptions by Asset, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Consumptions by Asset, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    //get consumption by id AMS_UC_39
    @RequestMapping(method = RequestMethod.GET,value="/consumption",params = {"id"})
    public @ResponseBody
    ResponseEntity getConsumptionById(@RequestParam Long id) {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get consumption by id. ID: "+id);
            GetConsumptionByIdResponse response=assetService.getConsumptionById(id);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetConsumptionByIdResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetConsumptionByIdResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Consumption by id, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Consumption by id, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get paginated consumptions for SDT
    @PostMapping("/consumptions/sdt")
    public @ResponseBody
    ResponseEntity getPaginatedConsumptionsForSDT(@RequestBody GetPaginatedDataForSDTRequest request) throws IOException,ApplicationException{
        Util util = new Util();
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to get paginated consumptions for SDT,details:: "+convertToJSON(request));
        ResponseEntity responseEntity=null;
        GetPaginatedDataForSDTResponse response=assetService.getPaginatedConsumptionsForSDT(request);
        if(response.getResponseIdentifier().equals("Success")){
            responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseIdentifier().equals("Failure")){
            responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //Edit consumption Images by qasim...
    @RequestMapping(method=RequestMethod.PUT, value="/consumption")
    public @ResponseBody
    ResponseEntity editConsumption(@RequestBody EditConsumptionRequest request) throws ApplicationException,IOException{
        Util util=new Util();
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to edit consumption");
        ResponseEntity responseEntity;
        DefaultResponse defaultResponse=assetService.editConsumption(request);

        responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);


        util.clearThreadContextForLogging();
        util = null;
        request = null;
        return responseEntity;
    }

    //Delete consumption Images by qasim...
    @RequestMapping(method = RequestMethod.DELETE,value="/consumption/image",params={"id"})
    public @ResponseBody
    ResponseEntity deleteConsumptionImages(@RequestParam Long id) {
        Util util = new Util();
        LOGGER.info("Request received in controller to delete asset. UUID: "+id);
        util.setThreadContextForLogging(scim2Util);
        ResponseEntity responseEntity=null;
        DefaultResponse response=assetService.deleteConsumptionImages(id);
        if(response.getResponseCode().equals("200")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseCode().equals("500")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /******************************************* END Consumption Functions *******************************************/

    /******************************************* Usages Functions ****************************************************/
    //add usage

    @RequestMapping(method = RequestMethod.POST, value="/usages")
    public @ResponseBody
    ResponseEntity addUsages(@RequestBody AddUsageRequest request) throws IOException{
        Util util=new Util();
        ResponseEntity responseEntity;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add usage");
            DefaultResponse defaultResponse=assetService.addUsage(request);
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Usage, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occcurred for adding Usage, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    //finish trip.
    @RequestMapping(method=RequestMethod.PUT, value="/usages")
    public @ResponseBody
    ResponseEntity editUsage(@RequestBody EditUsageRequest request) throws IOException{
        Util util=new Util();
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to edit usage");
        ResponseEntity responseEntity;
        DefaultResponse defaultResponse=assetService.editUsage(request);
        if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
            responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
        }
        else{
            responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get usages by asset AMS_UC_27
    @RequestMapping(method = RequestMethod.GET,value="/usages",params = {"assetuuid","offset","limit"})
    public @ResponseBody
    ResponseEntity getPaginatedUsagesByAsset(@RequestParam String assetuuid,@RequestParam int offset,@RequestParam int limit)  {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get usages by asset. AssetUUID: "+assetuuid+" Offset: "+offset+" Limit: "+limit);
            GetPaginatedUsagesByAssetResponse response=assetService.getPaginatedUsagesByAsset(assetuuid,offset,limit);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesByAssetResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesByAssetResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated usages by asset, details: assetUuid: "+assetuuid+", offset: "+offset+", limit: "+limit,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated usages by asset, details: assetUuid: "+assetuuid+", offset: "+offset+", limit: "+limit,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //get paginated usages AMS_UC_28
    @RequestMapping(method = RequestMethod.POST,value = "/usages/filter")
    public @ResponseBody
    ResponseEntity getPaginatedUsages(@RequestBody GetPaginatedUsagesRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get paginated usages. TenantUUID: "+request.getTenantUUID()+" Offset: "+request.getOffset()+" Limit: "+request.getLimit()+"  AssetUUID: "+request.getAssetUUID()+" Start Date: "+request.getStartDate()+" End Date: "+request.getEndDate());
            GetPaginatedUsagesResponse response=assetService.getPaginatedUsages(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Usages, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Usages, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    //get paginated usages for SDT
    @PostMapping("/usages/sdt")
    public @ResponseBody
    ResponseEntity getPaginatedUsagesForSDT(@RequestBody GetPaginatedDataForSDTRequest request) throws IOException,ApplicationException{
        Util util = new Util();
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to get paginated usages for SDT,details:: "+convertToJSON(request));
        ResponseEntity responseEntity=null;
        GetPaginatedDataForSDTResponse response=assetService.getPaginatedUsagesForSDT(request);
        if(response.getResponseIdentifier().equals("Success")){
            responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseIdentifier().equals("Failure")){
            responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    //get paginated usages by asset uuids AMS_UC_33
    @RequestMapping(method = RequestMethod.POST,value = "/usages/filter/assets")
    public @ResponseBody
    ResponseEntity getPaginatedUsagesByAssetsAndType(@RequestBody GetPaginatedUsagesByAssetsAndCategoryRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get paginated usages by asset uuids. Offset: "+request.getOffset()+" Limit: "+request.getLimit());
            GetPaginatedUsagesByAssetsAndCategoryResponse response=assetService.getPaginatedUsagesByAssetsAndType(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesByAssetsAndCategoryResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedUsagesByAssetsAndCategoryResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Usages by asset and type, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Usages by asset and type, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    //get usage by id
    @RequestMapping(method = RequestMethod.GET,value = "/usages", params = {"id"})
    public @ResponseBody
    ResponseEntity getusageById(@RequestParam Long id){
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get usage by id. Id: "+id);
            GetUsageByIdResponse response=assetService.getUsageById(id);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetUsageByIdResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetUsageByIdResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Usage, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Usage, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    /******************************************* END Usages Functions ****************************************************/

    /******************************************* Inspection Template Functions***********************************/
    //post inspection template AMS_UC_15
    @RequestMapping(method = RequestMethod.POST,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity addInspectionTemplate(@RequestBody PostInspectionTemplateRequest postInspectionTemplateRequest) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to post inspection template of a category.");
            responseEntity=Optional.ofNullable(assetService.postInspectionTemplate(postInspectionTemplateRequest))
                    .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for posting Inspection Template, details: "+new ObjectMapper().writeValueAsString(postInspectionTemplateRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for posting Inspection Template, details: "+new ObjectMapper().writeValueAsString(postInspectionTemplateRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            postInspectionTemplateRequest = null;
        }
        return responseEntity;
    }

    //get inspection template by uuid AMS_UC_16
    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params={"id"})
    public @ResponseBody
    ResponseEntity getInspectionTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get inspection template");
            responseEntity=Optional.ofNullable(assetService.getInspectionTemplate(id))
                    .map(resp -> new ResponseEntity<GetInspectionTemplateResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Inspection Template, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting Inspection Template, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    //edit inspection template by uuid AMS_UC_17
    @RequestMapping(method = RequestMethod.PUT,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity editInspectionTemplate(@RequestBody EditInspectionTemplateRequest editInspectionTemplateRequest) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to edit inspection template");
            responseEntity=Optional.ofNullable(assetService.editInspectionTemplate(editInspectionTemplateRequest))
                    .map(resp -> new ResponseEntity<EditInspectionTemplateResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for editing Inspection Template, details: "+new ObjectMapper().writeValueAsString(editInspectionTemplateRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for editing Inspection Template, details: "+new ObjectMapper().writeValueAsString(editInspectionTemplateRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            editInspectionTemplateRequest = null;
        }
        return responseEntity;
    }

    //delete inspection template by uuid AMS_UC_18
    @RequestMapping(method = RequestMethod.DELETE,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity deleteInspectionTemplate(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete inspection template");
            responseEntity=Optional.ofNullable(assetService.deleteInspectionTemplate(id))
                    .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Inspection Template exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for deleting Inspection Template, details: id: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for deleting Inspection Template, details: id: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    /******************************************* END Inspection Template Functions***********************************/

    /******************************************* Activity Wall Functions********************************************/
    //add message to activity wall AMS_UC_19
    @RequestMapping(method = RequestMethod.POST,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity addMessage(@RequestBody AddMessageRequest addMessageRequest) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add message");
            responseEntity=Optional.ofNullable(assetService.addMessage(addMessageRequest))
                    .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Message, details: "+new ObjectMapper().writeValueAsString(addMessageRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for adding Message, details: "+new ObjectMapper().writeValueAsString(addMessageRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            addMessageRequest = null;
        }
        return responseEntity;
    }

    //edit message to activity wall AMS_UC_20
    @RequestMapping(method = RequestMethod.PUT,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity editMessage(@RequestBody EditMessageRequest editMessageRequest) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to edit message");
            responseEntity=Optional.ofNullable(assetService.editMessage(editMessageRequest))
                    .map(resp -> new ResponseEntity<EditMessageResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for editing Message, details: "+new ObjectMapper().writeValueAsString(editMessageRequest),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for editing Message, details: "+new ObjectMapper().writeValueAsString(editMessageRequest),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            editMessageRequest = null;
        }
        return responseEntity;
    }

    //delete message to activity wall AMS_UC_21
    @RequestMapping(method = RequestMethod.DELETE,value="/activitywall/messages")
    public @ResponseBody
    ResponseEntity deleteMessage(@RequestParam String id) throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete message");
            responseEntity=Optional.ofNullable(assetService.deleteMessage(id))
                    .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Message exists",0L));
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for deleting Message, details: messageUuid: "+id,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for deleting Message, details: messageUuid: "+id,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/activitywall/reply")
    public @ResponseBody
    ResponseEntity addReplyToMessage(@RequestBody AddReplyRequest request) throws IOException{

        Util util=new Util();
        ResponseEntity responseEntity = null;
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to add reply to a message");
        try {
            DefaultResponse response=assetService.addReplyToMessage(request);
            responseEntity = new ResponseEntity<>(response,HttpStatus.OK);

        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for adding Reply Message, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.setThreadContextForLogging(scim2Util);
            util = null;
            request = null;
        }

        return responseEntity;
    }

    /*******************************************END Activity Wall Functions********************************************/

    /******************************************* File Functions ******************************************************/
    //upload file to s3 AMS_UC_24
    @RequestMapping(method = RequestMethod.POST,value="/files")
    public @ResponseBody
    ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to upload file to s3");
            UploadFileResponse response=assetService.uploadFile(file);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<UploadFileResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<UploadFileResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for uploading File, details: "+new ObjectMapper().writeValueAsString(file),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for uploading File, details: "+new ObjectMapper().writeValueAsString(file),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            file = null;
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.DELETE,value="/files", params = {"filename"})
    public @ResponseBody
    ResponseEntity deleteFile(@RequestParam("filename") String filename) throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{

            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete file from s3");
            DefaultResponse response=assetService.removeFile(filename);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for removing File, details: fileName: "+new ObjectMapper().writeValueAsString(filename),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for removing File, details: fileName: "+new ObjectMapper().writeValueAsString(filename),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @GetMapping("/codes")
    @ResponseBody
    public List generateCodes(@RequestParam int type,@RequestParam int quantity){
        Util util = new Util();
        List<String> codes = new ArrayList<>();

        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered function to generate codes, details: class: "+type+", quantity: "+quantity);

            String code = "01.0000389.00016"+String.valueOf(type);
            String serialNumber = "000169740";


            for(int i = 0 ; i < quantity ; i++){
                codes.add(code+"."+String.valueOf(Integer.parseInt(serialNumber))+i);
            }

            LOGGER.info("Successfully generated codes");

        }catch(Exception e){
            LOGGER.error("Error in generating codes oc class: "+type+", quantity: "+quantity,e);
        }

        return codes;
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

    /*************************************************************************************************************************************/
                                                            /* Wriiten By Kumail Khan*/

    /*************************************************************************************************************************************/
    /* Wriiten By Qasim Ishtiaq*/


    //get file IS_UC_19
    @RequestMapping(method = RequestMethod.GET,value="/files", params = {"url"})
    public @ResponseBody
    ResponseEntity getFile(@RequestParam String url)  throws EmptyEntityTableException {
        Util util = new Util();
        util.setThreadContextForLogging(scim2Util);
        LOGGER.info("Request received in controller to get file from s3: URl: "+url);
        ResponseEntity responseEntity=null;
        GetFileResponse response=assetService.getFile(url);
        if(response.getResponseIdentifier().equals("Success")){
            responseEntity=new ResponseEntity<GetFileResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseIdentifier().equals("Failure")){
            responseEntity=new ResponseEntity<GetFileResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }

    /*************************************************************************************************************************************/
    /* Wriiten By Qasim Ishtiaq*/
                                    /* Written By Nouman Afzaal*/
    /*********************************ASSET GROUP SDT FUNCTION START********************************/

    //get page of assetsgroups for SDT
    @PostMapping("/assets/group/sdt")
    public @ResponseBody
    ResponseEntity getPaginatedAssetsGroup(@RequestBody GetPaginatedDataForSDTRequest request) throws IOException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get Assets Groups SDT");
            GetPaginatedDataForSDTResponse response=assetService.getPaginatedAssetsforSDT(request);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<GetPaginatedDataForSDTResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Asset Groups for SDT, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unnkown Error occurred for getting paginated Asset Groups for SDT, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    /*********************************ASSET GROUP SDT FUNCTION END**********************************/

    /*********************************ASSET GROUP FUNCTION START ***********************************/

    @RequestMapping(method = RequestMethod.POST,value="/assets/group")
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity addAssetGroup(@RequestBody AddAssetGroupRequest request)  throws EmptyEntityTableException,IOException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to add asset group");
            responseEntity=Optional.ofNullable(assetService.addAssetGroup(request))
                    .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Asset exists",0L));

        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for add Asset Group, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for add Asset Group, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }
        return responseEntity;
    }

    @PutMapping("/assets/group")
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity editAssetGroup(@RequestBody EditGroupAssetsRequest request) throws IOException {
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of edit asset group. Details: " + convertToJSON(request));
            response = new ResponseEntity<DefaultResponse>(assetService.updateAssetGroup(request),HttpStatus.OK);
        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for update Asset Group, details: "+new ObjectMapper().writeValueAsString(request),ade);
            response = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch (Exception e){
            LOGGER.error("An Error Occurred while updating asset group.",e);
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Returning from controller of edit asset group.");
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.GET,value="/assets/group",params = {"uuid"})
    public @ResponseBody
    ResponseEntity getAssetGroup(@RequestParam String uuid)  throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get asset group");

            GetAssetGroupResponse response=assetService.getAssetGroupByUUID(uuid);
            if(response.getResponseIdentifier().contentEquals("Success")){
                responseEntity =  new ResponseEntity<>(response,HttpStatus.OK);
            }else
                responseEntity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting Asset Group, details: assetGroupUuid: "+uuid,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unnown Error occurred for getting Asset Group, details: assetGroupUuid: "+uuid,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }

        return responseEntity;
    }

    // archive/delete asset groups
    @RequestMapping(method = RequestMethod.DELETE,value="/assets/group",params = {"id","type"})
    @Caching(evict = {
            @CacheEvict(value= "assetAndAssetGroup",allEntries= true),
            @CacheEvict(value= "assetGroupByAsset",allEntries= true),
            @CacheEvict(value= "assetAndAssetGroupByUUID",allEntries= true)
    })
    public @ResponseBody
    ResponseEntity deleteAssetGroup(@RequestParam String id,@RequestParam String type)  throws EmptyEntityTableException {
        Util util = new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to delete an inspection template");
            DefaultResponse response=assetService.deleteAssetGroup(id,type);
            if(response.getResponseIdentifier().equals("Success")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
            }
            else if(response.getResponseIdentifier().equals("Failure")){
                responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for delete Asset Group by uuid and type, details: uuid: "+id+", type: "+type,ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for delete Asset Group by uuid and type, details: uuid: "+id+", type: "+type,e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/assets/group/filter")
    public @ResponseBody
    ResponseEntity getPaginatedAssetGroups(@RequestBody GetPaginatedAssetGroupsRequest request) throws EmptyEntityTableException,IOException {
        Util util=new Util();
        ResponseEntity responseEntity = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller to get paginated asset groups");

            GetPaginatedAssetGroupsResponse response=assetService.getPaginatedAssetGroups(request);
            if(response.getResponseIdentifier().contentEquals("Success")){
                responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
            }else
                responseEntity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch(AccessDeniedException ade){
            LOGGER.error("Access is Denied for getting paginated Asset Group, details: "+new ObjectMapper().writeValueAsString(request),ade);
            responseEntity = new ResponseEntity<String>(ade.getMessage(),HttpStatus.UNAUTHORIZED);
            ade = null;
        }catch(Exception e){
            LOGGER.error("An unknown Error occurred for getting paginated Asset Group, details: "+new ObjectMapper().writeValueAsString(request),e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            util.clearThreadContextForLogging();
            util = null;
            request = null;
        }

        return responseEntity;
    }

//    @GetMapping("/group")
//    public @ResponseBody
//    ResponseEntity getAssetGroupsNameAndUUIDByTenantUUID(@RequestParam String tenantUUID){
//        Util util = new Util();
//        ResponseEntity responseEntity = null;
//        try{
//            util.setThreadContextForLogging(scim2Util);
//            LOGGER.info("Request received in get Asset groups name and uuid by tenant uuid: " + tenantUUID);
//            responseEntity = new ResponseEntity<AssetGroupsNameAndUUIDResponse>(assetService.getAssetGroupsNameAndUUIDByTenantUUID(tenantUUID),HttpStatus.OK);
//        }catch (AccessDeniedException ae){
//            responseEntity = new ResponseEntity<String>(ae.getMessage(),HttpStatus.FORBIDDEN);
//        }catch (Exception e){
//            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//        }finally {
//            LOGGER.info("Returning from get Asset groups name and uuid by tenant uuid.");
//            util.clearThreadContextForLogging();
//            util = null;
//        }
//        return responseEntity;
//    }

    /*********************************ASSET GROUP FUNCTION END ***********************************/
    /*******************************************************Start Wallet Function**********************************************************/
    @PutMapping("/wallets")
    public  ResponseEntity EditWallet(@RequestBody EditWalletRequest wallet) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            DefaultResponse defaultResponse=assetService.EditWallet(wallet);
            LOGGER.info("Entered Controller of Editing Wallet In Asset Controller");
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Editing Wallet in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Editing wallet in asset service");
            util=null;
            wallet=null;
        }
        return  responseEntity;
    }
    @PostMapping("/wallets")
    public  ResponseEntity createWallet(@RequestBody CreateWalletRequest wallet) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            DefaultResponse defaultResponse=assetService.createWallet(wallet);
            LOGGER.info("Entered Controller of creating Wallet In Asset Controller");
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while create Wallet in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of creating wallet in asset service");
            util=null;
            wallet=null;
        }
        return  responseEntity;
    }

    //archive or delete wallet by UUID qasim.....
    @DeleteMapping("wallet/archive-delete")
    public @ResponseBody
    DefaultResponse archiveOrDeleteWalletByUUID(@RequestParam String uuid, @RequestParam String type){
        Util util = new Util();
        DefaultResponse response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Request received in controller of archive or delete Wallet by uuid: " + uuid);
             response = assetService.archiveOrDeleteWalletByUUID(uuid,type);
        }catch (Exception e){
            LOGGER.error("An Error occurred while archive or delete Wallet by uuid.",e);
            response = new DefaultResponse("failure","Failed","F500");
        }finally {
            LOGGER.info("Returing from controller of archive or delete Wallet by uuid.");
            util.clearThreadContextForLogging();
            util = null;
        }
        return response;
    }
    @PostMapping("/wallet/spent")
    public  ResponseEntity addSpend(@RequestBody AddSpendRequest spendRequest) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered Controller of Add Spend In Asset Controller");
            DefaultResponse defaultResponse=assetService.addSpent(spendRequest);
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Add Spend in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Add Spend in asset service");
            util=null;
            spendRequest=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/spent")
    public ResponseEntity getSpent(@RequestParam String walletUUID,@RequestParam int offset,@RequestParam int limit){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            GetSpentResponse getSpentResponse=assetService.getSpent(walletUUID,offset,limit);
            LOGGER.info("Entered Controller of Get Spent In Asset Controller");
            if(getSpentResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getSpentResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getSpentResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Get Spent in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Get Spent in asset service");
            util=null;

        }
        return  responseEntity;
    }
    @PostMapping("/wallet/purchase")
    public  ResponseEntity addPurchase(@RequestBody AddPurchaseRequest purchaseRequest) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered Controller of Add Purchase In Asset Controller");
            DefaultResponse defaultResponse=assetService.addPurchase(purchaseRequest);
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Add Purchase in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Add Purchase in asset service");
            util=null;
            purchaseRequest=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/purchase")
    public  ResponseEntity getPurchase(@RequestParam String UserUUID,@RequestParam String TransactionType) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            getPurchasesResponse getPurchasesResponse=assetService.getPurchase(UserUUID,TransactionType);
            LOGGER.info("Entered Controller of Get Purchase In Asset Controller");
            if(getPurchasesResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getPurchasesResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getPurchasesResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Get Purchase in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Get Purchase in asset service");
            util=null;
            UserUUID=null;
            TransactionType=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallets")
    public  ResponseEntity getWalletByUserUUIDandOrgUUID(@RequestParam String userUUID,@RequestParam String orgUUID) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            getWalletByUserUUIDAndOrgUUIDResponse getWalletByUserUUIDAndOrgUUIDResponse=assetService.getWalletsByUserUUIDandOrgUUID(userUUID,orgUUID);
            LOGGER.info("Entered Controller of get Wallet By UserUUID and OrgUUID In Asset Controller");
            if(getWalletByUserUUIDAndOrgUUIDResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getWalletByUserUUIDAndOrgUUIDResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getWalletByUserUUIDAndOrgUUIDResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get Wallet By UserUUID and OrgUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get Wallet By UserUUID and OrgUUID in asset service");
            util=null;
            userUUID=null;
            orgUUID=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/single")
    public  ResponseEntity getWalletByUserUUIDandOrgUUIDandwalletUUID(@RequestParam String userUUID,@RequestParam String orgUUID,@RequestParam String walletType) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            getWalletByUserUUIDAndOrgUUIDResponse getWalletByUserUUIDAndOrgUUIDResponse=assetService.getWalletsByUserUUIDandOrgUUIDandWalletType(userUUID,orgUUID,walletType);
            LOGGER.info("Entered Controller of get Wallet By UserUUID and OrgUUID In Asset Controller");
            if(getWalletByUserUUIDAndOrgUUIDResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getWalletByUserUUIDAndOrgUUIDResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getWalletByUserUUIDAndOrgUUIDResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get Wallet By UserUUID and OrgUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get Wallet By UserUUID and OrgUUID in asset service");
            util=null;
            userUUID=null;
            orgUUID=null;
        }
        return  responseEntity;
    }
    @PostMapping("/wallet/request")
    public  ResponseEntity addRequest(@RequestBody AddRequestInWallet addRequestInWallet) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            DefaultResponse defaultResponse=assetService.addRequest(addRequestInWallet);
            LOGGER.info("Entered Controller of Add Request in Wallet In Asset Controller");
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Add Request in Wallet in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Add Request in Wallet in asset service");
            util=null;
            addRequestInWallet=null;
        }
        return  responseEntity;
    }
    @PostMapping("/wallet/request/approve/ignore")
    public  ResponseEntity RequestApproveOrIgnore(@RequestBody RequestApproveOrIgnore requestApproveOrIgnore) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            DefaultResponse defaultResponse=assetService.requestApproveOrIgnore(requestApproveOrIgnore);
            LOGGER.info("Entered Controller of Request Approve or Ignore in Wallet In Asset Controller");
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Request Approve or Ignore in Wallet in Asset Controller",e);

            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Request Approve or Ignore in Wallet in asset service");
            util=null;
            requestApproveOrIgnore=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/request")
    public  ResponseEntity getAllRequestByWalletUUID(@RequestParam String walletUUID,@RequestParam Boolean approve,@RequestParam Boolean ignore) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            GetRequestResponse getRequestResponse=assetService.getAllRequestByWalletUUID(walletUUID,approve,ignore);
            LOGGER.info("Entered Controller of get All Requests by WalletUUID In Asset Controller");
            if(getRequestResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get All Requests by WalletUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get All Requests by WalletUUID in asset service");
            util=null;
            walletUUID=null;
            ignore=null;
            approve=null;

        }
        return  responseEntity;
    }
    @GetMapping("/wallet/requests")
    public  ResponseEntity getAllRequestByWalletUUIDAndWalletType(@RequestParam String walletUUID,@RequestParam String walletType,@RequestParam String orgUUID) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            GetRequestResponse getRequestResponse=assetService.getAllRequestByWalletUUID(walletUUID,walletType,orgUUID);
            LOGGER.info("Entered Controller of get All Requests by WalletUUID In Asset Controller");
            if(getRequestResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get All Requests by WalletUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get All Requests by WalletUUID in asset service");
            util=null;
            walletUUID=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/requests/user")
    public  ResponseEntity getAllRequestUserDataByWalletUUIDAndWalletType(@RequestParam String walletUUID,@RequestParam Boolean approve,@RequestParam Boolean ignore,@RequestParam Boolean withdraw) {
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered Controller of get All Requests by WalletUUID In Asset Controller");
            GetRequestUserDataResponse getRequestResponse=assetService.getAllRequestWithUserDataByWalletUUID(walletUUID,approve,ignore,withdraw);
            if(getRequestResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get All Requests by WalletUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get All Requests by WalletUUID in asset service");
            util=null;
            walletUUID=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/wallets/organization")
    public ResponseEntity getAllWalletsByWalletTypeAndOrgUUID(@RequestParam String walletType,@RequestParam String orgUUID){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            getWalletResponse getRequestResponse=assetService.getAllWalletsByWalletTypeAndOrgUUID(walletType,orgUUID);
            LOGGER.info("Entered Controller of get Wallet By walletType and OrgUUID In Asset Controller");
            if(getRequestResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get Wallet By walletType and OrgUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get Wallet By walletType and OrgUUID in asset service");
            util=null;
        }
        return  responseEntity;
    }

    @GetMapping ("/wallet/user")
    public ResponseEntity getAllUserByWalletsTypeAndOrgUUIDAndProductType(@RequestParam String walletType,@RequestParam String orgUUID,@RequestParam String productType,@RequestParam String userUUID){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            getWalletsUserResponse getRequestResponse=assetService.getAllUserByWalletsTypeAndOrgUUIDAndProductType(walletType,orgUUID,productType,userUUID);
            LOGGER.info("Entered Controller of get User By walletType and OrgUUID In Asset Controller");
            if(getRequestResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getRequestResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while get USer By walletType and OrgUUID in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of get User By walletType and OrgUUID in asset service");
            util=null;
        }
        return  responseEntity;
    }
    @PostMapping("/wallet/request/withdraw")
    public ResponseEntity withdrawRequest(@RequestBody WithDrawnRequest withDrawnRequest){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            LOGGER.info("Entered Controller of withDraw request In Asset Controller");
            DefaultResponse defaultResponse=assetService.withDrawnRequest(withDrawnRequest);
            if(defaultResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(defaultResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Add Purchase in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Add Purchase in asset service");
            util=null;
            withDrawnRequest=null;
        }
        return  responseEntity;
    }
    @GetMapping("/wallet/transaction")
    public ResponseEntity getTransaction(@RequestParam String walletUUID,@RequestParam int offset,@RequestParam int limit){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            GetTransactionResponse getTransactionResponse=assetService.getTransaction(walletUUID,offset,limit);
            LOGGER.info("Entered Controller of Get Transaction In Asset Controller");
            if(getTransactionResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getTransactionResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getTransactionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Get Transaction in Asset Controller");
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Get Transaction in asset service");
            util=null;

        }
        return  responseEntity;
    }
    @GetMapping("/wallet/get/wallet")
    public ResponseEntity GetWalletByWalletUUID(@RequestParam String walletUUID){
        Util util =new Util();
        ResponseEntity responseEntity=null;
        try{
            util.setThreadContextForLogging(scim2Util);
            GetWalletObjectResponse getWalletObjectResponse=assetService.getWallet(walletUUID);
            LOGGER.info("Entered Controller of Get wallet By WalletUUID In Asset Controller");
            if(getWalletObjectResponse.getResponseIdentifier().contentEquals("Success")){
                responseEntity=new ResponseEntity<>(getWalletObjectResponse,HttpStatus.OK);
            }
            else{
                responseEntity=new ResponseEntity<>(getWalletObjectResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(ApplicationException e){
            LOGGER.error("An Error occurred while Get Wallet By WalletUUID in Asset Controller",e);
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            util.clearThreadContextForLogging();
            LOGGER.info("Returning from controller of Get Wallet by WalletUUID in asset service");
            util=null;

        }
        return  responseEntity;
    }
    @PostMapping("/wallet/sdt")
    @ResponseBody
    public ResponseEntity getPaginatedWalletForSDT(@RequestBody GetPaginatedDataForSDTRequest request) throws IOException{
        Util util = new Util();
        ResponseEntity response = null;
        try{
            util.setThreadContextForLogging(scim2Util);
           LOGGER.info("In controller to get paginated wallet for SDT details: "+convertToJSON(request));
            response = new ResponseEntity<GetPaginatedDataForSDTResponse>(assetService.getPaginatetWalletforSDT(request),HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("An Error occurred while getting paginated wallet for SDT details: "+convertToJSON(request),e);
            response = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            e = null;
        }finally{
            LOGGER.info("Returning from controller of getting paginated wallet for SDT");
            util.clearThreadContextForLogging();
            util = null;
        }

        return response;
    }
    //Delete Asset Attachment by qasim...
    @RequestMapping(method = RequestMethod.DELETE,value="/attachment",params={"id"})
    public @ResponseBody
    ResponseEntity deleteAssetAttachment(@RequestParam Long id) {
        Util util = new Util();
        LOGGER.info("Request received in controller to delete asset Attachment. UUID: "+id);
        util.setThreadContextForLogging(scim2Util);
        ResponseEntity responseEntity=null;
        DefaultResponse response=assetService.deleteAssetAttachment(id);
        if(response.getResponseCode().equals("200")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.OK);
        }
        else if(response.getResponseCode().equals("500")){
            responseEntity=new ResponseEntity<DefaultResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        util.clearThreadContextForLogging();
        return responseEntity;
    }
    public String convertToJSON (Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
