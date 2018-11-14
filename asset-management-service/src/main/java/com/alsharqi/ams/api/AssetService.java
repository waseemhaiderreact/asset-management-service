package com.alsharqi.ams.api;


import com.alsharqi.ams.vehicle.Vehicle;
import com.alsharqi.ams.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AssetService {
    @Autowired
    private VehicleRepository vehicleRepository;

    //create a new vehicle
    Vehicle createVehicle(Vehicle vehicle){
        try{
            vehicleRepository.save(vehicle);
            String assetNumber="AS-18";
            Long myId=1000L+vehicle.getId();
            String formatted = String.format("%06d",myId);
            vehicle.setAssetNumber(assetNumber+formatted);
            vehicle.setCreatedAt(new Date());
            vehicleRepository.save(vehicle);
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
            return vehicle;
        }catch(Exception e){
            return null;
        }
    }

    //delete a vehicle by assetNumber
    Integer deleteVehicle(String assetNumber){
        return vehicleRepository.deleteByAssetNumber(assetNumber);
    }

    //get a vehicle by assetNumber
    Page<Vehicle> getVehicles(int offset, int limit){
        return vehicleRepository.findByIdNotNull(new PageRequest(offset, limit));
    }
}

