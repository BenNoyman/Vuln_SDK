package com.example.vulnsdk.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScanHistoryResponse {
    @SerializedName("scans")
    private List<ScanHistoryItem> scans;

    // Default constructor for Gson
    public ScanHistoryResponse() {}

    public List<ScanHistoryItem> getScans() {
        return scans;
    }

    public void setScans(List<ScanHistoryItem> scans) {
        this.scans = scans;
    }

    public static class ScanHistoryItem {
        @SerializedName("_id")
        private String _id;
        
        @SerializedName("language")
        private String language;
        
        @SerializedName("timestamp")
        private String timestamp;
        
        @SerializedName("results")
        private ScanResults results;

        // Default constructor for Gson
        public ScanHistoryItem() {}

        public String getId() { return _id; }
        public void setId(String id) { this._id = id; }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public ScanResults getResults() { return results; }
        public void setResults(ScanResults results) { this.results = results; }
    }
}
