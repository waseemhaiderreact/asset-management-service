package com.sharklabs.ams.events.userOrganization;

import com.sharklabs.ams.AssignmentRecord.AssignmentRecord;

public class AssignmentRecordModel {
    private String action;
    private AssignmentRecord assignmentRecord;

    public AssignmentRecordModel(){
        super();
    }

    public AssignmentRecordModel(String action, AssignmentRecord assignmentRecord) {
        super();
        this.action = action;
        this.assignmentRecord = assignmentRecord;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public AssignmentRecord getAssignmentRecord() {
        return assignmentRecord;
    }

    public void setAssignmentRecord(AssignmentRecord assignmentRecord) {
        this.assignmentRecord = assignmentRecord;
    }
}
