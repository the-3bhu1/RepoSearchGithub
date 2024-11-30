package com.example.githubreposearchapp2.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RepoSearchResponse {
    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("items")
    private List<Repository> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Repository> getItems() {
        return items;
    }

    public void setItems(List<Repository> items) {
        this.items = items;
    }
}