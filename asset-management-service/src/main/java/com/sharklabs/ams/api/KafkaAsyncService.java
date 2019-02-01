package com.sharklabs.ams.api;

//import com.sharklabs.ams.events.inspectionreport.InspectionReportModel;
import com.sharklabs.ams.events.inspectionreport.InspectionReportSourceBean;
//import com.sharklabs.ams.events.vehicle.VehicleModel;
import com.sharklabs.ams.events.vehicle.VehicleSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class KafkaAsyncService {

    @Autowired
    private VehicleSourceBean vehicleSourceBean;
    @Autowired
    private InspectionReportSourceBean inspectionReportSourceBean;

//    @Async
//    void sendVehicle(Vehicle vehicle,String action){
//        try {
//            VehicleModel vehicleModel = new VehicleModel(action,vehicle);
//            vehicleSourceBean.sendVehicle(vehicleModel);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Async
//    void sendInspectionReport(InspectionReport inspectionReport, String action){
//        try{
//            InspectionReportModel inspectionReportModel=new InspectionReportModel(action,inspectionReport);
//            inspectionReportSourceBean.sendInspectionReport(inspectionReportModel);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

}
