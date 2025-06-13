package com.example.vulnsdk.controller;

import android.util.Log;

import com.example.vulnsdk.model.*;
import com.example.vulnsdk.network.ApiClient;
import com.example.vulnsdk.network.VulnerabilityService;
import com.example.vulnsdk.utils.TokenGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Example usage of VulnerabilitiesController SDK:
 *
 * // Initialize the controller
 * VulnerabilitiesController controller = new VulnerabilitiesController();
 *
 * // Register a new user
 * controller.register("username", "password", new VulnerabilitiesController.Callback<AuthResponse>() {
 *     @Override
 *     public void onSuccess(AuthResponse response) {
 *         // Handle successful registration
 *     }
 *     @Override
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 *
 * // Scan code
 * controller.scanCode(token, code, language, new VulnerabilitiesController.Callback<ScanResponse>() {
 *     @Override
 *     public void onSuccess(ScanResponse response) {
 *         // Handle scan results
 *     }
 *     @Override
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 *
 * // Fetch scan history
 * controller.getScanHistory(token, new VulnerabilitiesController.Callback<ScanHistoryResponse>() {
 *     @Override
 *     public void onSuccess(ScanHistoryResponse response) {
 *         // Handle scan history
 *     }
 *     @Override
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 *
 * // Fetch scan details
 * controller.getScanDetails(token, scanId, new VulnerabilitiesController.Callback<ScanResponse>() {
 *     @Override
 *     public void onSuccess(ScanResponse response) {
 *         // Handle scan details
 *     }
 *     @Override
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 *
 * // Delete a scan
 * controller.deleteScan(token, scanId, new VulnerabilitiesController.Callback<DeleteResponse>() {
 *     @Override
 *     public void onSuccess(DeleteResponse response) {
 *         // Handle successful deletion
 *     }
 *     @Override
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 */

public class VulnerabilitiesController {
    private final VulnerabilityService service;

    public interface Callback<T> {
        void onSuccess(T response);
        void onError(String error);
    }

    public VulnerabilitiesController() {
        service = ApiClient.getService();
    }

    /**
     * Registers a new user with the given username and password.
     * @param username The username for registration.
     * @param password The password for registration.
     * @param callback Callback to handle success or error.
     */
    public void register(String username, String password, Callback<AuthResponse> callback) {
        if (callback == null) return;

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        
        service.register(credentials).enqueue(new retrofit2.Callback<AuthResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    callback.onSuccess(authResponse);
                } else {
                    String error = "Registration failed: " + response.message();
                    Log.e("VulnerabilitiesController", error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AuthResponse> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("VulnerabilitiesController", error);
                callback.onError(error);
            }
        });
    }

    /**
     * Scans the provided code for vulnerabilities.
     * @param token The authentication token.
     * @param code The source code to scan.
     * @param language The programming language of the code.
     * @param callback Callback to handle scan results or error.
     */
    public void scanCode(String token, String code, String language, Callback<ScanResponse> callback) {
        if (callback == null) {
            Log.e("VulnerabilitiesController", "Error: Callback is null");
            return;
        }
    
        Map<String, String> scanRequest = new HashMap<>();
        scanRequest.put("code", code);
        scanRequest.put("language", language);
        String scanToken = TokenGenerator.generateToken();
        scanRequest.put("token", scanToken);
    
        Log.i("VulnerabilitiesController", "Scanning code...");
        Log.i("VulnerabilitiesController", "Language: " + language);
        Log.i("VulnerabilitiesController", "Scan Token: " + scanToken);
    
        service.scanCode(token, scanRequest, false).enqueue(new retrofit2.Callback<ScanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ScanResponse> call, retrofit2.Response<ScanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScanResponse scanResponse = response.body();
                    ScanResults results = scanResponse.getResults();

                    callback.onSuccess(scanResponse);
                } else {
                    String error = "Scan failed: " + response.message();
                    Log.e("VulnerabilitiesController", error);
                    callback.onError(error);
                }
            }
    
            @Override
            public void onFailure(retrofit2.Call<ScanResponse> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("VulnerabilitiesController", error);
                callback.onError(error);
            }
        });
    }
    
    /**
     * Retrieves the scan history for the authenticated user.
     * @param token The authentication token.
     * @param callback Callback to handle scan history or error.
     */
    public void getScanHistory(String token, Callback<ScanHistoryResponse> callback) {
        if (callback == null) {
            Log.e("VulnerabilitiesController", "Error: Callback is null");
            return;
        }
        if (token == null || token.isEmpty()) {
            Log.e("VulnerabilitiesController", "Error: Token is missing");
            callback.onError("Token is missing");
            return;
        }
    
        Log.i("VulnerabilitiesController", "Fetching scan history...");
        service.getScanHistory(token, false).enqueue(new retrofit2.Callback<ScanHistoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ScanHistoryResponse> call, retrofit2.Response<ScanHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScanHistoryResponse historyResponse = response.body();
                    List<ScanHistoryResponse.ScanHistoryItem> scans = historyResponse.getScans();
    
                    Log.i("VulnerabilitiesController", "\nScan History:");
                    Log.i("VulnerabilitiesController", "-------------");
    
                    if (scans != null && !scans.isEmpty()) {
                        for (ScanHistoryResponse.ScanHistoryItem scan : scans) {
                            Log.i("VulnerabilitiesController", "Scan ID: " + scan.getId());
                            Log.i("VulnerabilitiesController", "Language: " + scan.getLanguage());
                            Log.i("VulnerabilitiesController", "Timestamp: " + scan.getTimestamp());
    
                            if (scan.getResults() != null && scan.getResults().getFindings() != null) {
                                Log.i("VulnerabilitiesController", "Findings count: " + scan.getResults().getFindings().size());
                            }
    
                            Log.i("VulnerabilitiesController", "-------------");
                        }
                    } else {
                        Log.i("VulnerabilitiesController", "No scan history available.");
                    }
    
                    callback.onSuccess(historyResponse);
                } else {
                    String error = "Failed to get scan history: " + response.message();
                    Log.e("VulnerabilitiesController", error);
                    callback.onError(error);
                }
            }
    
            @Override
            public void onFailure(retrofit2.Call<ScanHistoryResponse> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("VulnerabilitiesController", error);
                callback.onError(error);
            }
        });
    }
    
    /**
     * Retrieves the details of a specific scan by scan ID.
     * @param token The authentication token.
     * @param scanId The ID of the scan to retrieve.
     * @param callback Callback to handle scan details or error.
     */
    public void getScanDetails(String token, String scanId, Callback<ScanResponse> callback) {
        if (callback == null) {
            Log.e("VulnerabilitiesController", "Error: Callback is null");
            return;
        }
        if (token == null || token.isEmpty()) {
            Log.e("VulnerabilitiesController", "Error: Token is missing");
            callback.onError("Token is missing");
            return;
        }
    
        Log.i("VulnerabilitiesController", "Fetching scan details for ID: " + scanId);
    
        service.getScanDetails(token, scanId, false).enqueue(new retrofit2.Callback<ScanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ScanResponse> call, retrofit2.Response<ScanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScanResponse scanResponse = response.body();
                    ScanResults results = scanResponse.getResults();

                    callback.onSuccess(scanResponse);
                } else {
                    String error = "Failed to get scan details: " + response.message();
                    Log.e("VulnerabilitiesController", error);
                    callback.onError(error);
                }
            }
    
            @Override
            public void onFailure(retrofit2.Call<ScanResponse> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("VulnerabilitiesController", error);
                callback.onError(error);
            }
        });
    }
    
    /**
     * Deletes a scan by its scan ID.
     * @param token The authentication token.
     * @param scanId The ID of the scan to delete.
     * @param callback Callback to handle deletion result or error.
     */
    public void deleteScan(String token, String scanId, Callback<DeleteResponse> callback) {
        if (callback == null) {
            Log.e("VulnerabilitiesController", "Error: Callback is null");
            return;
        }
        if (token == null || token.isEmpty()) {
            Log.e("VulnerabilitiesController", "Error: Token is missing");
            callback.onError("Token is missing");
            return;
        }

        Log.i("VulnerabilitiesController", "Deleting scan with ID: " + scanId);
        
        service.deleteScan(token, scanId).enqueue(new retrofit2.Callback<DeleteResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DeleteResponse> call, retrofit2.Response<DeleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("VulnerabilitiesController", "Scan deleted successfully!");
                    callback.onSuccess(response.body());
                } else {
                    String error = "Failed to delete scan: " + response.message();
                    Log.e("VulnerabilitiesController", error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DeleteResponse> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("VulnerabilitiesController", error);
                callback.onError(error);
            }
        });
    }
} 