package com.sharklabs.ams.events.asset;

import com.sharklabs.ams.asset.AssetMapper;
import com.sharklabs.ams.asset.AssetMapperRepository;
import com.sharklabs.ams.events.CustomChannels;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@EnableBinding(CustomChannels.class)
public class AssetIssuesHandler {

    private static final Logger LOGGER = Logger.getLogger(AssetIssuesHandler.class);

    @Autowired
    AssetMapperRepository assetMapperRepository;

    @StreamListener("inBoundAssetIssues")
    public void assetIssues(AssetIssuesModel assetIssuesModel){
        try{
            LOGGER.info("Inside asset Issues kafka listener.");
            AssetMapper assetMapper = null;
            switch (assetIssuesModel.getAction()){
                case "OPEN":
                    LOGGER.info("Adding open Issue to Asset Cooked Table.");
                    assetMapper = assetMapperRepository.findByUuid(assetIssuesModel.getAssetUUID());
                    Integer count = Integer.valueOf(assetMapper.getOpenIssues()) + 1;
                    assetMapper.setOpenIssues((count).toString());
                    assetMapperRepository.save(assetMapper);
                    assetMapper = null;
                    break;
                case "ASSIGN":
                    LOGGER.info("Assigning Issues to Asset Cooked Table.");
                    List<String> uuids = Arrays.asList(assetIssuesModel.getAssetUUID().split(","));
                    List<AssetMapper> assetMappers = assetMapperRepository.findAllByUuid(uuids);
                    if(assetMappers.size() > 1){
                        HashMap<String,Integer> counts = new HashMap<>();
                        for(String s: uuids){
                            Integer occurrence = counts.get(s);
                            counts.put(s,(occurrence == null) ? 1 : occurrence + 1);
                        }
                        assetMappers.forEach(assetMapper1 -> {
                            Integer assignedIssues = Integer.valueOf(assetMapper1.getAssignedIssues()) + counts.get(assetMapper1.getUuid());
                            assetMapper1.setAssignedIssues(assignedIssues.toString());
                            Integer openIssues = Integer.valueOf(assetMapper1.getOpenIssues()) > 0 ? Integer.valueOf(assetMapper1.getOpenIssues()) - counts.get(assetMapper1.getUuid()) : 0;
                            assetMapper1.setOpenIssues(openIssues.toString());
                        });
                        assetMapperRepository.save(assetMappers);
                    }else{
                        assetMappers.forEach(assetMapper1 -> {
                            Integer assignedIssues = Integer.valueOf(assetMapper1.getAssignedIssues()) + 1;
                            assetMapper1.setAssignedIssues(assignedIssues.toString());
                            Integer openIssues = Integer.valueOf(assetMapper1.getOpenIssues()) > 0 ? Integer.valueOf(assetMapper1.getOpenIssues()) - 1 : 0;
                            assetMapper1.setOpenIssues(openIssues.toString());
                        });
                        assetMapperRepository.save(assetMappers);
                    }

                    break;
            }
        }catch (Exception e){
            LOGGER.error("An Error occurred after receive call in AMS.");
        }
    }
}
