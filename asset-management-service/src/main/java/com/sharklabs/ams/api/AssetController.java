package com.sharklabs.ams.api;

import com.sharklabs.ams.exception.EmptyEntityTableException;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.serviceentry.ServiceEntry;
import com.sharklabs.ams.servicetask.ServiceTask;
import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;


@Controller
@CrossOrigin
@RequestMapping("/assets")
public class AssetController {
    @Autowired
    AssetService assetService;

    //create a new vehicle
    @RequestMapping(method = RequestMethod.POST,value="/vehicles")
    public @ResponseBody
    ResponseEntity createVehicle(@RequestBody Vehicle vehicle) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.createVehicle(vehicle))
                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get a vehicle by AssetNumber
    @RequestMapping(method = RequestMethod.GET,value="/vehicles/{assetNumber}")
    public @ResponseBody
    ResponseEntity getVehicle(@PathVariable("assetNumber") String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getVehicle(assetNumber))
                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //update a vehicle
    @RequestMapping(method = RequestMethod.PUT,value="/vehicles")
    public @ResponseBody
    ResponseEntity updateVehicle(@RequestBody Vehicle vehicle) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.updateVehicle(vehicle))
                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //Delete a vehicle by assetNumber
    @RequestMapping(method = RequestMethod.DELETE,value="/vehicles/{assetNumber}")
    public @ResponseBody
    ResponseEntity updateVehicle(@PathVariable("assetNumber") String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.deleteVehicle(assetNumber))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get List of vehicles
    @RequestMapping(method = RequestMethod.GET,value="/vehicles",params = {"offset","limit"})
    public @ResponseBody
    ResponseEntity getVehicles(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getVehicles(offset,limit))
                .map(resp -> new ResponseEntity<Page<Vehicle>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get list of vehicles given the asset numbers
    @RequestMapping(method = RequestMethod.POST,value="/vehicles/getdetails")
    public @ResponseBody
    ResponseEntity getVehicles(@RequestBody List<String> assetNumbers) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getVehiclesGivenAssetNumbers(assetNumbers))
                .map(resp -> new ResponseEntity<List<Vehicle>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //re-index vehicles
    @RequestMapping(method = RequestMethod.POST,value="/vehicles/re-index")
    public @ResponseBody
    ResponseEntity reIndexVehicles() throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.reIndexVehicles())
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get vehicle by driverNumber
//    @RequestMapping(method=RequestMethod.GET,value="/vehicles",params = {"driverNumber"})
//    public @ResponseBody
//    ResponseEntity getVehicleByDriverNumber(@RequestParam String driverNumber) throws EmptyEntityTableException {
//        return Optional.ofNullable(assetService.getVehicleByDriverNumber(driverNumber))
//                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
//                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
//    }

    //save an inspection report
    @RequestMapping(method = RequestMethod.POST,value="/inspections",params = {"assetNumber"})
    public @ResponseBody
    ResponseEntity createIssue(@RequestBody InspectionReport inspectionReport, @RequestParam String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.saveInspectionReport(inspectionReport,assetNumber))
                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get list of inspection reports of a vehicle
    @RequestMapping(method = RequestMethod.GET,value="/inspections",params = {"assetNumber"})
    public @ResponseBody
    ResponseEntity getInspectionReports(@RequestParam String assetNumber) throws EmptyEntityTableException {
            return Optional.ofNullable(assetService.getInspectionReports(assetNumber))
                    .map(resp -> new ResponseEntity<Iterable<InspectionReport>>(resp, HttpStatus.OK))
                    .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
    }

    //get all inspection reports
    @RequestMapping(method = RequestMethod.GET,value="/inspections")
    public @ResponseBody
    ResponseEntity getAllInspectionReports() throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getAllInspectionReports())
                .map(resp -> new ResponseEntity<Iterable<InspectionReport>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
    }

    //re-index inspection reports
    @RequestMapping(method = RequestMethod.POST,value="/inspections/re-index")
    public @ResponseBody
    ResponseEntity reIndexInspections() throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.reIndexInspections())
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Inspection Report exists",0L));
    }

    //save an inspection report template
    @RequestMapping(method = RequestMethod.POST,value="/inspectiontemplates",params = {"assetNumber"})
    public @ResponseBody
    ResponseEntity createIssue(@RequestBody InspectionReportTemplate inspectionReportTemplate, @RequestParam String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.saveInspectionReportTemplate(inspectionReportTemplate,assetNumber))
                .map(resp -> new ResponseEntity<Vehicle>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }

    //get list of inspection report templates of a vehicle
    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params = {"assetNumber"})
    public @ResponseBody
    ResponseEntity getInspectionReportTemplates(@RequestParam String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getInspectionReportTemplates(assetNumber))
                .map(resp -> new ResponseEntity<Iterable<InspectionReportTemplate>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
    }

    //get list of inspection report templates
    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates",params = {"offset","limit"})
    public @ResponseBody
    ResponseEntity getInspectionReportTemplates(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getAllInspectionReportTemplates(offset,limit))
                .map(resp -> new ResponseEntity<Page<InspectionReportTemplate>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
    }

    //get issues of a vehicle
    @RequestMapping(method = RequestMethod.GET,value="/issues",params = {"assetNumber"})
    public @ResponseBody
    ResponseEntity getIssuesByAssetNumber(@RequestParam String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getIssuesOfVehicle(assetNumber))
                .map(resp -> new ResponseEntity<Iterable<IssueReporting>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Issues exists", 0L));
    }

    //add a service task
    @RequestMapping(method = RequestMethod.POST,value="/servicetasks")
    public @ResponseBody
    ResponseEntity addServiceTask(@RequestBody ServiceTask serviceTask) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.addServiceTask(serviceTask))
                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }

    //update a service task
    @RequestMapping(method = RequestMethod.PUT,value="/servicetasks")
    public @ResponseBody
    ResponseEntity updateServiceTask(@RequestBody ServiceTask serviceTask) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.updateServiceTask(serviceTask))
                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }

    //delete a service task
    @RequestMapping(method = RequestMethod.DELETE,value="/servicetasks")
    public @ResponseBody
    ResponseEntity deleteServiceTask(@RequestParam Long id) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.deleteServiceTask(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }

    //get a service task by id
    @RequestMapping(method = RequestMethod.GET,value="/servicetasks",params = {"id"})
    public @ResponseBody
    ResponseEntity getServiceTask(@RequestParam Long id) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getServiceTask(id))
                .map(resp -> new ResponseEntity<ServiceTask>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }

    //get list of service tasks
    @RequestMapping(method = RequestMethod.GET,value="/servicetasks",params = {"page","size"})
    public @ResponseBody
    ResponseEntity getServiceTasks(@RequestParam int page,@RequestParam int size) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getServiceTasks(page,size))
                .map(resp -> new ResponseEntity<Page<ServiceTask>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }

    //add a service entry
    @RequestMapping(method = RequestMethod.POST,value="/serviceentries")
    public @ResponseBody
    ResponseEntity addServiceEntries(@RequestBody ServiceEntry serviceEntry) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.addServiceEntry(serviceEntry))
                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
    }

    //update a service entry
    @RequestMapping(method = RequestMethod.PUT,value="/serviceentries")
    public @ResponseBody
    ResponseEntity updateServiceEntries(@RequestBody ServiceEntry serviceEntry) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.updateServiceEntry(serviceEntry))
                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
    }

    //delete a service entry
    @RequestMapping(method = RequestMethod.DELETE,value="/serviceentries",params = {"id"})
    public @ResponseBody
    ResponseEntity addServiceEntries(@RequestParam Long id) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.deleteServiceEntry(id))
                .map(resp -> new ResponseEntity<DefaultResponse>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
    }

    //get a service entry by id
    @RequestMapping(method = RequestMethod.GET,value="/serviceentries",params = {"id"})
    public @ResponseBody
    ResponseEntity getServiceEntryById(@RequestParam Long id) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getServiceEntry(id))
                .map(resp -> new ResponseEntity<ServiceEntry>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Entry exists", 0L));
    }

    //get list of service entries
    @RequestMapping(method = RequestMethod.GET,value="/serviceentries",params = {"page","size"})
    public @ResponseBody
    ResponseEntity getServiceEntries(@RequestParam int page,@RequestParam int size) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getServiceEntries(page,size))
                .map(resp -> new ResponseEntity<Page<ServiceEntry>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Service Task exists", 0L));
    }
}
