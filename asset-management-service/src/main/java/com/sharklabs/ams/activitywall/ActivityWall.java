package com.sharklabs.ams.activitywall;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.message.Message;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_activity_wall")
public class ActivityWall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id",referencedColumnName = "id")
    private Asset asset;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "activityWall",fetch = FetchType.EAGER)
    private Set<Message> messages =new HashSet<>();

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

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
