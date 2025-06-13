package com.example.vulnsdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanResults {
    @SerializedName("findings")
    private List<Vulnerability.Finding> findings;

    @SerializedName("language")
    private String language;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("total_findings")
    private int totalFindings;

    // Default constructor for Gson
    public ScanResults() {}

    public List<Vulnerability.Finding> getFindings() {
        return findings;
    }

    public void setFindings(List<Vulnerability.Finding> findings) {
        this.findings = findings;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotalFindings() {
        return totalFindings;
    }

    public void setTotalFindings(int totalFindings) {
        this.totalFindings = totalFindings;
    }
}
