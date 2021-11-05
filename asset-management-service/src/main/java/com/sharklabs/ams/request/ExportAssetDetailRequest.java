package com.sharklabs.ams.request;

import com.sharklabs.ams.asset.AssetExcelData;
import com.sharklabs.ams.model.assignment.Assignment;
import com.sharklabs.ams.model.assignment.AssignmentHistory;
import com.sharklabs.ams.model.consumption.Consumption;
import com.sharklabs.ams.model.issue.Issue;
import com.sharklabs.ams.model.usage.Usage;
import com.sharklabs.ams.model.workorder.WorkOrder;

import java.util.List;

public class ExportAssetDetailRequest {

    private AssetExcelData assetExcelData;

    private List<Assignment> assignments;

    private List<AssignmentHistory> assignmentHistories;

    private List<Issue> issues;

    private List<WorkOrder> workOrders;

    private List<Consumption> consumptions;

    private List<Usage> usages;

    public AssetExcelData getAssetExcelData() {
        return assetExcelData;
    }

    public void setAssetExcelData(AssetExcelData assetExcelData) {
        this.assetExcelData = assetExcelData;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<AssignmentHistory> getAssignmentHistories() {
        return assignmentHistories;
    }

    public void setAssignmentHistories(List<AssignmentHistory> assignmentHistories) {
        this.assignmentHistories = assignmentHistories;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    public List<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }
}
