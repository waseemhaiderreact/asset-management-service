package com.sharklabs.ams.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;

@Entity(name = "t_image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id",referencedColumnName = "id")
    private IssueReporting issue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public IssueReporting getIssue() {
        return issue;
    }

    public void setIssue(IssueReporting issue) {
        this.issue = issue;
    }
}
