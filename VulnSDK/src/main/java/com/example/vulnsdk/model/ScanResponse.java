package com.example.vulnsdk.model;

import com.google.gson.annotations.SerializedName;

public class ScanResponse {
    @SerializedName("scan_id")
    private String scanId;

    @SerializedName("results")
    private ScanResults results;

    // Default constructor for Gson
    public ScanResponse() {}

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public ScanResults getResults() {
        return results;
    }

    public void setResults(ScanResults results) {
        this.results = results;
    }
}
