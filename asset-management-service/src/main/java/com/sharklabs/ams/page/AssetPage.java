package com.sharklabs.ams.page;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetModelForTableView;

import java.util.List;

public class AssetPage {
    private List<AssetModelForTableView> content;
    private Boolean last;
    private long totalPages;
    private long totalElements;
    private int size;
    private int number;
    private String sort;
    private Boolean first;
    private int numberOfElements;

    public List<AssetModelForTableView> getContent() {
        return content;
    }

    public void setContent(List<AssetModelForTableView> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
