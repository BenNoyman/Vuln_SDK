package com.example.vulnsdk;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vulnsdk.controller.VulnerabilitiesController;
import com.example.vulnsdk.model.AuthResponse;
import com.example.vulnsdk.model.ScanResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private VulnerabilitiesController vulnerabilitiesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the VulnSDK controller
        vulnerabilitiesController = new VulnerabilitiesController();
        
        // Example usage of the VulnSDK
        demonstrateVulnSDK();
    }

    private void demonstrateVulnSDK() {
        Log.d(TAG, "VulnSDK initialized successfully!");
        Toast.makeText(this, "VulnSDK is ready to use!", Toast.LENGTH_SHORT).show();
        
        // Example: Register a user (uncomment to test with a real backend)
        /*
        vulnerabilitiesController.register("testuser", "testpass", new VulnerabilitiesController.Callback<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse response) {
                Log.d(TAG, "Registration successful: " + response.getToken());
                
                // Example: Scan some code
                String codeToScan = "print('Hello World')";
                vulnerabilitiesController.scanCode(response.getToken(), codeToScan, "python", 
                    new VulnerabilitiesController.Callback<ScanResponse>() {
                        @Override
                        public void onSuccess(ScanResponse scanResponse) {
                            Log.d(TAG, "Scan successful: " + scanResponse.getScanId());
                        }
                        
                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "Scan error: " + error);
                        }
                    });
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Registration error: " + error);
            }
        });
        */
    }
}