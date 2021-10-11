package com.sharklabs.ams.api;


import com.sharklabs.ams.events.asset.AssetModel;
import com.sharklabs.ams.events.asset.AssetSourceBean;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModelResponse;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailSourceBean;
import com.sharklabs.ams.events.wallet.WalletNotificationModel;
import com.sharklabs.ams.events.wallet.WalletNotificationSourceBean;
import com.sharklabs.ams.events.wallet.WalletRequestModel;
import com.sharklabs.ams.events.wallet.WalletRequestSourceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class KafkaAsyncService {

    private static final Logger LOGGER = LogManager.getLogger(KafkaAsyncService.class);

    @Autowired
    AssetSourceBean assetSourceBean;

    @Autowired
    AssetBasicDetailSourceBean assetBasicDetailSourceBean;

    @Autowired
    WalletNotificationSourceBean walletNotificationSourceBean;
    @Autowired
    WalletRequestSourceBean walletRequestSourceBean;


    /* Written By Kumail Khan*/
    @Async
    public void sendWalletNotification(WalletNotificationModel walletNotificationModel){
        try{
            LOGGER.info("Adding in sending Wallet Notification to Asset Management service");
            walletNotificationSourceBean.walletNotification(walletNotificationModel);
        }catch(Exception e){
            LOGGER.error("Error while sending Wallet Notification to Asset Personal service", e);
        }
    }
    @Async
    public void sendWalletUserUUIDs(WalletRequestModel walletRequestModel){
        try{
            LOGGER.info("Adding in sending Wallet Request to Asset Management service");
            walletRequestSourceBean.walletRequest(walletRequestModel);
        }catch(Exception e){
            LOGGER.error("Error while sending Wallet Request to Asset Personal service", e);
        }
    }




    @Async
    public void sendBasicDetail(AssetBasicDetailModelResponse assets) {
        try {
            assetBasicDetailSourceBean.sendBasicAssetDetail(assets);
        } catch (Exception e) {
            LOGGER.error("Error while sending Basic Asset Detail to inspection service", e);
        }
    }

    @Async
    public void sendAsset(AssetModel assets) {
        try {
            assetSourceBean.sendAssetUuid(assets);
        } catch (Exception e) {
            LOGGER.error("Error while sending assetNames to asset management service", e);
        }
    }
}
