package com.sharklabs.ams.api;

import com.sharklabs.ams.exception.EmptyEntityTableException;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.issuesreporting.IssueReporting;
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
    @RequestMapping(method = RequestMethod.GET,value="/inspectiontemplates")
    public @ResponseBody
    ResponseEntity getInspectionReportTemplates(@RequestParam String assetNumber) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getInspectionReportTemplates(assetNumber))
                .map(resp -> new ResponseEntity<Iterable<InspectionReportTemplate>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists", 0L));
    }
}
