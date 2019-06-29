package com.sharklabs.ams.api;

import com.sharklabs.ams.events.asset.AssetModel;
import com.sharklabs.ams.events.asset.AssetSourceBean;
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

    @Async
    public void sendAsset(AssetModel assets) {
        try {
            assetSourceBean.sendAssetUuid(assets);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while sending assetNames to asset management service", e);
        }
    }
}
