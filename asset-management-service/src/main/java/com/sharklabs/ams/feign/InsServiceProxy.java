package com.sharklabs.ams.feign;
import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.response.GetAssetUsersResponse;
import com.sharklabs.ams.response.GetUserDetailForWallet;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="inspection",url="${is.service.fetch.url}")

public interface InsServiceProxy {

    @GetMapping("/get/issues")
    @ResponseBody
    HashMap<String,String > getIssues(@RequestParam("uuids") List<String> uuids, @RequestParam("type") String type);
}
