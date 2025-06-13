package com.example.vulnsdk.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScanHistoryResults {
    @SerializedName("history")
    private List<ScanHistoryResponse.ScanHistoryItem> history;

    // Default constructor for Gson
    public ScanHistoryResults() {}

    public List<ScanHistoryResponse.ScanHistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<ScanHistoryResponse.ScanHistoryItem> history) {
        this.history = history;
    }
}
