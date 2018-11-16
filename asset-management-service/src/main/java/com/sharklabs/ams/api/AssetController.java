package com.sharklabs.ams.api;

import com.sharklabs.ams.exception.EmptyEntityTableException;
import com.sharklabs.ams.response.DefaultResponse;
import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(method = RequestMethod.GET,value="/vehicles")
    public @ResponseBody
    ResponseEntity getVehicles(@RequestParam int offset,@RequestParam int limit) throws EmptyEntityTableException {
        return Optional.ofNullable(assetService.getVehicles(offset,limit))
                .map(resp -> new ResponseEntity<Page<Vehicle>>(resp, HttpStatus.OK))
                .orElseThrow(() -> new EmptyEntityTableException("No Vehicle exists",0L));
    }
}
