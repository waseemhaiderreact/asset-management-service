package com.sharklabs.ams.feign;
import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.events.wallet.WalletUser;
import com.sharklabs.ams.response.GetAssetUsersResponse;
import com.sharklabs.ams.response.GetUserDetailForWallet;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="asset-personnel",url="${aps.service.fetch.url}")

public interface ApsServiceProxy {
    @RequestMapping(method = RequestMethod.POST,value="/usernames")
    public @ResponseBody
    WalletRequestModel getUserName(@RequestBody  WalletRequestModel walletRequestModel);

    @PostMapping("/asset/users")
    public @ResponseBody
    GetAssetUsersResponse getAssetUsersByAssetIds(@RequestParam("assetIds") List<String> assetIds);

    @PostMapping("/user/detail/wallet")
    @ResponseBody
    GetUserDetailForWallet getUserDetail(@RequestBody WalletRequestModel request);

    @GetMapping("/get/assignees")
    @ResponseBody
    HashMap<String,String > getAssignees(@RequestParam("uuids") List<String> uuids);
}
