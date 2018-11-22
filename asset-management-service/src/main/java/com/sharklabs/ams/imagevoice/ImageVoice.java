package com.sharklabs.ams.imagevoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.issuesreporting.IssueReporting;

import javax.persistence.*;

@Entity(name = "t_images_recordings")
public class ImageVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] voiceContent;

    @Lob
    private byte[] imageContent;

    private String message;

    private int voiceFlag;

    private int imageFlag;

    private int messageFlag;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id",referencedColumnName = "id")
    private IssueReporting issue;

    public ImageVoice() {
        this.voiceFlag = 0;
        this.imageFlag = 0;
        this.messageFlag = 0;
    }

    public byte[] getVoiceContent() {
        return voiceContent;
    }

    public void setVoiceContent(byte[] voiceContent) {
        this.voiceContent = voiceContent;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public int getVoiceFlag() {
        return voiceFlag;
    }

    public void setVoiceFlag(int voiceFlag) {
        this.voiceFlag = voiceFlag;
    }

    public int getImageFlag() {
        return imageFlag;
    }

    public void setImageFlag(int imageFlag) {
        this.imageFlag = imageFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IssueReporting getIssue() {
        return issue;
    }

    public void setIssue(IssueReporting issue) {
        this.issue = issue;
    }
}
