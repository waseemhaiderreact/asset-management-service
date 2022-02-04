package com.sharklabs.ams.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="work-orders",url="${wos.service.fetch.url}")

public interface WosServiceProxy {

    @GetMapping("/get/workorders")
    @ResponseBody
    HashMap<String,String > getWorkOrders(@RequestParam("uuids") List<String> uuids, @RequestParam("type") String type);

    @GetMapping("/get/costs")
    @ResponseBody
    HashMap<String,String> getAssetMaintenanceCost(@RequestParam("uuids") List<String> uuids);
}
