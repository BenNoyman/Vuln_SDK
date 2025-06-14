# VulnSDK2

[![](https://jitpack.io/v/BenNoyman/Vuln_SDK.svg)](https://jitpack.io/#BenNoyman/Vuln_SDK)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A powerful Android SDK for vulnerability scanning and security monitoring. VulnSDK2 provides automated vulnerability detection, network monitoring, and security analysis capabilities for Android applications with seamless integration to the VulnPlatform backend.

## ✅ **INTEGRATION STATUS: VERIFIED & WORKING**

✅ Authentication integration tested and working  
✅ Code scanning functionality verified  
✅ Network communication established  
✅ CORS and security configurations validated  
✅ Complete SDK-Platform integration confirmed  

## 🚀 Features

* **Vulnerability Scanning**: Automated detection of common security vulnerabilities
* **Network Monitoring**: Real-time monitoring of network requests and responses  
* **Security Analysis**: Comprehensive security analysis and reporting
* **Self-Hosted**: Keep your security data on your own infrastructure
* **Real-time Dashboard**: Visualize security findings through the companion VulnPlatform web interface
* **Minimal Setup**: Single line initialization
* **Thread-Safe**: Concurrent scanning without blocking your UI
* **Authentication**: Built-in user authentication and token management
* **Scan History**: Complete history of vulnerability scans with detailed results

## 📦 Installation

### Step 1: Add JitPack repository

Add JitPack to your project's `settings.gradle.kts` (or root `build.gradle`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Add this line
    }
}
```

### Step 2: Add the dependency

Add to your app-level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.BenNoyman:Vuln_SDK:1.0.0")
}
```

### Step 3: ⚠️ **CRITICAL: Configure Android Permissions & Network Security**

#### Add Required Permissions
Add these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### Enable Cleartext Traffic (for HTTP connections)
Add these attributes to your `<application>` tag in `AndroidManifest.xml`:

```xml
<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

#### Create Network Security Configuration
Create `app/src/main/res/xml/network_security_config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">127.0.0.1</domain>
    </domain-config>
</network-security-config>
```

## 🛠️ Setup

### VulnPlatform Backend Prerequisites

Before using the SDK, ensure your VulnPlatform backend is running:

```bash
# Start VulnPlatform (in VulnPlatform directory)
docker-compose up -d

# Verify it's running
curl http://localhost:5000/api/auth/register
```

The SDK is pre-configured to connect to:
- **Emulator**: `http://10.0.2.2:5000/api/` (recommended for development)
- **Physical Device**: Update `ApiClient.BASE_URL` to your computer's IP

### Basic Initialization

Initialize the SDK in your `Activity`:

```java
import com.example.vulnsdk.controller.VulnerabilitiesController;
import com.example.vulnsdk.model.AuthResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize VulnSDK2
        VulnerabilitiesController controller = new VulnerabilitiesController();
        
        // Register or login
        controller.register("username", "password123", new VulnerabilitiesController.Callback<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse response) {
                Log.d(TAG, "Registration successful! Token: " + response.getToken());
                // Token will be 4 characters (e.g., "M6VB")
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Registration failed: " + error);
            }
        });
    }
}
```

### Advanced Configuration

```java
VulnerabilitiesController controller = new VulnerabilitiesController();

// Configure with custom settings
controller.setBaseUrl("https://your-vulnplatform.com/api/");
controller.setAccessToken("your-4-char-token");
controller.setConnectionTimeout(30); // seconds
controller.setReadTimeout(30); // seconds

// Initialize
controller.initialize(this);
```

## 📝 Usage

### Authentication

```java
VulnerabilitiesController controller = new VulnerabilitiesController();

// Register new user
controller.register("username", "password", new AuthCallback() {
    @Override
    public void onSuccess(AuthResponse response) {
        String accessToken = response.getAccessToken();
        // Save token for future use
    }
    
    @Override
    public void onError(String error) {
        Log.e("Auth", "Registration failed: " + error);
    }
});

// Login existing user
controller.login("username", "password", new AuthCallback() {
    @Override
    public void onSuccess(AuthResponse response) {
        String accessToken = response.getAccessToken();
        boolean isAdmin = response.isAdmin();
    }
    
    @Override
    public void onError(String error) {
        Log.e("Auth", "Login failed: " + error);
    }
});
```

### Vulnerability Scanning

```java
// Scan code for vulnerabilities
String codeToScan = "SELECT * FROM users WHERE id = " + userInput;
String language = "java";

controller.scanCode(codeToScan, language, new ScanCallback() {
    @Override
    public void onSuccess(ScanResponse response) {
        String scanId = response.getScanId();
        List<Vulnerability> findings = response.getResults().getFindings();
        
        for (Vulnerability vuln : findings) {
            Log.d("Scan", "Found: " + vuln.getType() + " - " + vuln.getDescription());
        }
    }
    
    @Override
    public void onError(String error) {
        Log.e("Scan", "Scan failed: " + error);
    }
});
```

### Scan History Management

```java
// Get scan history
controller.getScanHistory(new ScanHistoryCallback() {
    @Override
    public void onSuccess(ScanHistoryResponse response) {
        List<ScanHistoryResults> scans = response.getScans();
        for (ScanHistoryResults scan : scans) {
            Log.d("History", "Scan " + scan.getScanId() + ": " + scan.getVulnerabilitiesCount() + " findings");
        }
    }
    
    @Override
    public void onError(String error) {
        Log.e("History", "Failed to get history: " + error);
    }
});

// Delete specific scan
controller.deleteScan("scan-id-here", new DeleteCallback() {
    @Override
    public void onSuccess(DeleteResponse response) {
        Log.d("Delete", "Scan deleted successfully");
    }
    
    @Override
    public void onError(String error) {
        Log.e("Delete", "Failed to delete scan: " + error);
    }
});
```

### Automatic Monitoring

The SDK automatically monitors:

**🔍 Vulnerability Detection**
* SQL Injection attempts
* XSS vulnerabilities  
* Code injection patterns
* Insecure data handling

**🌐 Network Security**
* API communication security
* Certificate validation
* Secure transmission protocols

**📱 Application Security**
* Runtime security violations
* Authentication flow security
* Token management

## 🔧 VulnPlatform Integration

Your VulnPlatform backend should be running and accessible. The SDK communicates with these endpoints:

* `POST /api/auth/register` - User registration
* `POST /api/auth/login` - User authentication  
* `POST /api/scan` - Submit code for vulnerability scanning
* `GET /api/scans` - Retrieve scan history
* `DELETE /api/scans/<id>` - Delete specific scan

### Sample Backend Response

```json
{
  "scan_id": "64f8a1b2c3d4e5f6g7h8i9j0",
  "results": {
    "total_findings": 3,
    "high_severity": 1,
    "medium_severity": 2,
    "low_severity": 0,
    "findings": [
      {
        "type": "SQL_INJECTION",
        "severity": "HIGH",
        "description": "Potential SQL injection detected",
        "line": 42,
        "code": "SELECT * FROM users WHERE id = " + userInput
      }
    ]
  }
}
```

## 🎯 Best Practices

1. **Initialize Early**: Call SDK initialization in your `Application.onCreate()`
2. **Secure Tokens**: Store access tokens securely using Android Keystore
3. **Handle Errors**: Always implement error callbacks for network operations
4. **Monitor Performance**: SDK operations are asynchronous and non-blocking
5. **Regular Updates**: Keep the SDK updated for latest security features
6. **Test Integration**: Verify VulnPlatform connectivity before production

## 🐛 Troubleshooting

### ⚠️ **VERIFIED SOLUTIONS (2025-06-13)**

The following issues have been identified and resolved:

### 1. Network Connection Issues ✅ FIXED

**Problem**: SDK can't connect to VulnPlatform  
**Solution**: Ensure all Android network configurations are applied:

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

### 2. CORS Errors ✅ FIXED

**Problem**: Cross-origin requests blocked  
**Solution**: VulnPlatform backend now allows SDK connections. Ensure you're using the latest version.

### 3. Authentication Token Issues ✅ WORKING

**Verified**: 4-character tokens work correctly (e.g., "M6VB")
```java
// Example successful registration
controller.register("username", "password123", callback);
// Returns: access_token: "M6VB"
```

### 4. Development Environment Setup

**Important**: Use Android Emulator (not physical device) for development:
- SDK configured for: `http://10.0.2.2:5000/api/`
- This maps to `localhost:5000` on your development machine

**For Physical Device**: Update `ApiClient.BASE_URL` to your computer's IP:
```java
// In ApiClient.java, change:
private static final String BASE_URL = "http://YOUR_COMPUTER_IP:5000/api/";
```

### 5. Backend Connection Verification

**Before running your app**, verify VulnPlatform is running:
```bash
# In VulnPlatform directory
docker-compose up -d
docker-compose ps  # Should show all containers as "Up"

# Test endpoint
curl -X POST http://localhost:5000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

### 6. Debug Logging

Enable HTTP logging to see network requests:
```java
// The SDK automatically includes HTTP logging
// Check Android Logcat for "okhttp.OkHttpClient" messages
```

**Expected Successful Log**:
```
I/okhttp.OkHttpClient: <-- 201 CREATED http://10.0.2.2:5000/api/auth/register (96ms)
I/okhttp.OkHttpClient: {
I/okhttp.OkHttpClient:   "access_token": "M6VB",
I/okhttp.OkHttpClient:   "message": "User registered successfully"
I/okhttp.OkHttpClient: }
```

## 📋 Requirements

* **Android API 26+** (Android 8.0)
* **Java 8+** or **Kotlin** compatible
* **Internet permission** in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🏗️ Architecture

```
VulnSDK2/
├── controller/
│   └── VulnerabilitiesController.java    # Main SDK controller
├── model/
│   ├── AuthResponse.java                 # Authentication responses
│   ├── ScanResponse.java                 # Scan result models
│   ├── Vulnerability.java                # Vulnerability data model
│   └── ...                              # Other data models
├── network/
│   ├── ApiClient.java                    # HTTP client configuration
│   └── VulnerabilityService.java         # API service interface
└── utils/
    └── TokenGenerator.java               # Utility classes
```

## 🔗 Related Projects

* **[VulnPlatform](https://github.com/BenNoyman/VulnPlatform)**: Backend & Frontend Dashboard
* **Documentation**: Complete API documentation available

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes and test thoroughly
4. Commit your changes: `git commit -m 'Add new feature'`
5. Push to the branch: `git push origin feature-name`
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

* **Issues**: [GitHub Issues](https://github.com/BenNoyman/Vuln_SDK/issues)
* **Documentation**: [Wiki](https://github.com/BenNoyman/Vuln_SDK/wiki)
* **Security**: For security issues, please contact privately

---

**Made with ❤️ for Android developers who prioritize security**

## 🎯 Quick Start Example

```java
public class MainActivity extends AppCompatActivity {
    private VulnerabilitiesController vulnController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize SDK
        vulnController = new VulnerabilitiesController();
        vulnController.initialize(this, "https://your-platform.com/api/", "AB12");
        
        // Example scan
        String suspiciousCode = "SELECT * FROM users WHERE name = '" + userInput + "'";
        vulnController.scanCode(suspiciousCode, "java", new ScanCallback() {
            @Override
            public void onSuccess(ScanResponse response) {
                Toast.makeText(MainActivity.this, 
                    "Found " + response.getResults().getTotalFindings() + " vulnerabilities", 
                    Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error) {
                Log.e("VulnSDK", "Scan error: " + error);
            }
        });
    }
}
``` 