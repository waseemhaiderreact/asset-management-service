package com.sharklabs.ams.workorderlineitems;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.servicetask.ServiceTask;
import com.sharklabs.ams.workorder.WorkOrder;

import javax.persistence.*;

@Entity(name="t_work_order_line_items")
public class WorkOrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float laborCost;
    private Float partsCost;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "work_order_id",referencedColumnName = "id")
    private WorkOrder workOrder;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id",referencedColumnName = "id")
    private IssueReporting issueReporting;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "service_task_id",referencedColumnName = "id")
    private ServiceTask serviceTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(Float laborCost) {
        this.laborCost = laborCost;
    }

    public Float getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(Float partsCost) {
        this.partsCost = partsCost;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public IssueReporting getIssueReporting() {
        return issueReporting;
    }

    public void setIssueReporting(IssueReporting issueReporting) {
        this.issueReporting = issueReporting;
    }

    public ServiceTask getServiceTask() {
        return serviceTask;
    }

    public void setServiceTask(ServiceTask serviceTask) {
        this.serviceTask = serviceTask;
    }
}
