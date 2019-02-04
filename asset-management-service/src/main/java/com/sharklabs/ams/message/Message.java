package com.sharklabs.ams.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.activitywall.ActivityWall;

import javax.persistence.*;

@Entity(name = "t_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String userUUID;

    private boolean imageFlag;

    private String imageUrl;

    private boolean messageFlag;

    private String message;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_wall_id",referencedColumnName = "id")
    private ActivityWall activityWall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ActivityWall getActivityWall() {
        return activityWall;
    }

    public void setActivityWall(ActivityWall activityWall) {
        this.activityWall = activityWall;
    }

    public boolean isImageFlag() {
        return imageFlag;
    }

    public void setImageFlag(boolean imageFlag) {
        this.imageFlag = imageFlag;
    }

    public boolean isMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(boolean messageFlag) {
        this.messageFlag = messageFlag;
    }
}
