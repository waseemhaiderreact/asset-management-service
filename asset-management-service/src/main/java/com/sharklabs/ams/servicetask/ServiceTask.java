package com.sharklabs.ams.servicetask;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.serviceentry.ServiceEntry;
import com.sharklabs.ams.workorderlineitems.WorkOrderLineItems;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_service_task")
public class ServiceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private String labels;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},mappedBy = "subTasks",fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<ServiceTask> tasks = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinTable(name = "t_service_subtasks",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "subtask_id")
    )
    private Set<ServiceTask> subTasks=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinTable(name = "t_service_entries_tasks",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "entry_id") })
    @JsonIgnore
    private Set<ServiceEntry> serviceEntries = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "serviceTask", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private WorkOrderLineItems workOrderLineItems;

    public void addSubtask(ServiceTask serviceTask){
        this.subTasks.add(serviceTask);
    }

    public void addTask(ServiceTask task){
        this.tasks.add(task);
    }

    public void addServiceEntry(ServiceEntry serviceEntry){
        this.serviceEntries.add(serviceEntry);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Set<ServiceTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<ServiceTask> tasks) {
        this.tasks = tasks;
    }

    public Set<ServiceTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<ServiceTask> subTasks) {
        this.subTasks = subTasks;
    }

    public Set<ServiceEntry> getServiceEntries() {
        return serviceEntries;
    }

    public void setServiceEntries(Set<ServiceEntry> serviceEntries) {
        this.serviceEntries = serviceEntries;
    }

    public WorkOrderLineItems getWorkOrderLineItems() {
        return workOrderLineItems;
    }

    public void setWorkOrderLineItems(WorkOrderLineItems workOrderLineItems) {
        this.workOrderLineItems = workOrderLineItems;
    }
}
