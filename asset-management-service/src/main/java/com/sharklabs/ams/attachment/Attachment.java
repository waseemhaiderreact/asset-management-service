package com.sharklabs.ams.attachment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.asset.Asset;

import javax.persistence.*;

@Entity(name = "t_attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String contentUrl;

    private String fileName;

    private String assetUUID;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "asset_id",referencedColumnName = "id")
//    private Asset asset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

//    public Asset getAsset() {
//        return asset;
//    }
//
//    public void setAsset(Asset asset) {
//        this.asset = asset;
//    }


    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }
}
