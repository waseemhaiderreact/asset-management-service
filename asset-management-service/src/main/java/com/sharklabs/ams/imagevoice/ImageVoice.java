package com.sharklabs.ams.imagevoice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "t_image_voices")
public class ImageVoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String contentUrl;

    private byte[] content;

    private String consumptionUUID; // consumption it is attached with if this imagevoice is of a consumption.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public byte[] getContent() {    return content;   }

    public void setContent(byte[] content) {    this.content = content;   }

    public String getConsumptionUUID() {
        return consumptionUUID;
    }

    public void setConsumptionUUID(String consumptionUUID) {
        this.consumptionUUID = consumptionUUID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
