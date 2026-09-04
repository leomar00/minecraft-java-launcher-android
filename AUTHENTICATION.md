# Minecraft Java Launcher for Android - Authentication Implementation

## Authentication System Overview

The authentication system supports multiple login methods:
1. **Offline Mode** - Local player authentication
2. **Username/Password** - Minecraft launcher authentication
3. **Microsoft OAuth** - Microsoft account integration (placeholder)

## Architecture

### API Layer
- **MinecraftAuthApi**: Retrofit client for Minecraft launcher authentication
- **MicrosoftAuthApi**: Retrofit client for Microsoft OAuth (future)
- **AccountPreferences**: Encrypted storage for tokens and account data

### Data Layer
- **AccountRepositoryImpl**: Implements authentication logic
- **AccountEntity & AccountDao**: Room database persistence
- **AccountPreferences**: Secure token storage using EncryptedSharedPreferences

### Domain Layer
- **AuthUseCase**: Business logic for authentication
- **AccountRepository**: Interface for account operations
- **MinecraftAccount**: Data model for user accounts

### Presentation Layer
- **AuthViewModel**: State management for authentication
- **AuthenticationScreen**: Main authentication UI
- **LoginDialog & OfflineLoginDialog**: Login UI components

## Authentication Flow

### Offline Login
```
User enters username
    ↓
Validation check
    ↓
Create offline account (UUID generated locally)
    ↓
Store in database
    ↓
Save to encrypted preferences
    ↓
Success
```

### Username/Password Login
```
User enters username and password
    ↓
Validation check
    ↓
Send to Minecraft API (authserver.mojang.com)
    ↓
Receive access token and profile
    ↓
Store in database
    ↓
Save to encrypted preferences
    ↓
Success
```

### Token Refresh
```
Access token expires
    ↓
Use refresh token
    ↓
Request new token from API
    ↓
Update database and preferences
    ↓
Continue using new token
```

### Logout
```
User initiates logout
    ↓
Invalidate token on server (if online)
    ↓
Delete from database
    ↓
Clear from preferences
    ↓
Success
```

## Security Features

### Encrypted Storage
- Tokens stored using EncryptedSharedPreferences
- Master key generated using AES256_GCM
- Value encryption using AES256_GCM

### Token Management
- Access tokens stored securely
- Refresh tokens stored for long-term access
- Automatic token expiration handling

### Network Security
- HTTPS-only connections
- Certificate pinning support (optional)
- Timeout protection (30 seconds)

## API Endpoints

### Minecraft Authentication API
**Base URL**: `https://authserver.mojang.com/`

- **POST /authenticate** - Authenticate with username and password
- **POST /refresh** - Refresh access token
- **GET /profile/minecraft** - Get player profile
- **POST /invalidate** - Invalidate token

### Microsoft Authentication API
**Base URL**: `https://login.live.com/oauth20_` (placeholder)

- **POST /token** - Get access token from authorization code
- **POST /token** - Refresh access token

## Usage Examples

### Offline Login
```kotlin
val viewModel: AuthViewModel = koinViewModel()

// Trigger offline login
viewModel.loginOffline("Steve")

// Observe auth state
viewModel.authState.collectLatest { state ->
    when (state) {
        AuthViewModel.AuthState.Success -> {
            // User logged in
        }
        AuthViewModel.AuthState.Error -> {
            // Show error
        }
        else -> {}
    }
}
```

### Username/Password Login
```kotlin
viewModel.loginWithUsername("player@email.com", "password")
```

### Account Switching
```kotlin
viewModel.switchAccount(account)
```

### Logout
```kotlin
viewModel.logout(accountId)
```

## Database Schema

### accounts table
```sql
CREATE TABLE accounts (
    id TEXT PRIMARY KEY,
    username TEXT NOT NULL,
    uuid TEXT NOT NULL,
    accessToken TEXT NOT NULL,
    refreshToken TEXT,
    email TEXT,
    profileName TEXT NOT NULL,
    skinUrl TEXT,
    capeUrl TEXT,
    createdAt INTEGER NOT NULL,
    isOfflineMode INTEGER NOT NULL
);
```

## Dependencies Added

```gradle
// Security & Encryption
implementation("androidx.security:security-crypto:1.1.0-alpha06")

// Networking
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
```

## Configuration

### Retrofit Clients
The following Retrofit clients are configured in Koin:
- MinecraftAuthApi - Base URL: `https://authserver.mojang.com/`
- MicrosoftAuthApi - Base URL: `https://login.live.com/oauth20_`
- MinecraftVersionApi - Base URL: `https://launcher.mojang.com/v1/objects/`

### Network Configuration
- Connection Timeout: 30 seconds
- Read Timeout: 30 seconds
- Write Timeout: 30 seconds
- Logging: Basic (request/response lines)

## Error Handling

### AuthException
Handled through Result sealed class:
- `Result.Success<T>` - Operation succeeded
- `Result.Error(exception, message)` - Operation failed
- `Result.Loading` - Operation in progress

### Common Errors
- Empty username/password
- Network connection failure
- Invalid credentials
- Token expiration
- Account not found

## Testing

### Offline Mode Testing
1. Launch app
2. Click "Sign In"
3. Select "Offline Mode"
4. Enter any username
5. Account created with locally generated UUID

### Username/Password Testing
1. Use valid Minecraft account credentials
2. App contacts authserver.mojang.com
3. Receives access token and profile
4. Account persisted locally

## Future Enhancements

1. **Microsoft OAuth Flow**
   - Exchange authorization code for token
   - Authenticate with Minecraft using Microsoft token
   - Automatic token refresh

2. **Multi-Account Management**
   - Switch between accounts seamlessly
   - Remove unused accounts
   - View account details

3. **Biometric Authentication**
   - Fingerprint/Face unlock
   - Quick account access

4. **Account Recovery**
   - Recover deleted accounts
   - Email verification
   - Security questions

## Security Considerations

1. **Never hardcode credentials**
2. **Always use HTTPS**
3. **Encrypt sensitive data**
4. **Validate user input**
5. **Handle errors gracefully**
6. **Keep tokens secure**
7. **Implement certificate pinning** (optional)
8. **Add rate limiting** (future)

## Troubleshooting

### "Failed to authenticate"
- Check internet connection
- Verify username and password
- Check if account exists

### "Token expired"
- App automatically attempts refresh
- If refresh fails, user must re-login

### "Network error"
- Check connectivity
- Verify API endpoints are accessible
- Check firewall/proxy settings

## References

- [Minecraft Launcher Documentation](https://wiki.vg/Authentication)
- [Android Security Best Practices](https://developer.android.com/training/articles/security-tips)
- [EncryptedSharedPreferences](https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences)
