package com.sharklabs.ams.AssetImage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.asset.Asset;

import javax.persistence.*;

@Entity(name = "t_asset_images")
public class AssetImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    private String assetUUID;

    private byte[] content;

    private String message;

    private boolean voiceFlag;

    private boolean imageFlag;

    private boolean messageFlag;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public byte[] getContent() {    return content;   }

    public void setContent(byte[] content) {     this.content = content;   }

    public String getMessage() {    return message;   }

    public void setMessage(String message) {      this.message = message;    }

    public boolean isVoiceFlag() {        return voiceFlag;    }

    public void setVoiceFlag(boolean voiceFlag) {        this.voiceFlag = voiceFlag;    }

    public boolean isImageFlag() {        return imageFlag;    }

    public void setImageFlag(boolean imageFlag) {        this.imageFlag = imageFlag;    }

    public boolean isMessageFlag() {        return messageFlag;    }

    public void setMessageFlag(boolean messageFlag) {        this.messageFlag = messageFlag;    }
}
