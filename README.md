# Minecraft Java Launcher for Android

A feature-rich Minecraft Java Edition launcher for Android devices, built with Kotlin and Jetpack Compose.

## ✨ Features

- 🎮 **Game Launcher**: Launch Minecraft Java Edition on Android
- 🔐 **Account Management**: Multiple authentication methods (Offline, Launcher, Microsoft OAuth)
- 📦 **Version Management**: Install and manage multiple Minecraft versions
- ⚙️ **Java Configuration**: Customizable JVM arguments and memory allocation
- 🎨 **Modern UI**: Built with Jetpack Compose and Material Design 3
- 🌓 **Dark Mode**: Full dark mode support
- 🔒 **Secure Storage**: Encrypted token and account storage

## 🏗️ Project Structure

```
app/src/main/kotlin/com/minecraft/launcher/
├── domain/
│   ├── model/          # Data models (MinecraftAccount, GameProfile, etc.)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic (AuthUseCase, VersionUseCase)
├── data/
│   ├── local/
│   │   ├── database/   # Room database configuration
│   │   ├── entity/     # Database entities
│   │   ├── dao/        # Data access objects
│   │   └── preferences/# EncryptedSharedPreferences
│   ├── remote/
│   │   ├── api/        # Retrofit API clients
│   │   └── dto/        # Data transfer objects
│   └── repository/     # Repository implementations
├── presentation/
│   ├── ui/
│   │   ├── screens/    # Compose screens
│   │   └── theme/      # Theme configuration
│   └── viewmodel/      # ViewModels (MVVM)
└── di/                 # Dependency injection (Koin)
```

## 🔐 Authentication System

### Supported Login Methods
1. **Offline Mode** - Local player with generated UUID
2. **Launcher Account** - Username/password authentication
3. **Microsoft OAuth** - Microsoft account integration (in development)

### Key Features
- Secure token storage with encryption
- Automatic token refresh
- Multi-account support
- Account switching
- Logout with token invalidation

See [AUTHENTICATION.md](AUTHENTICATION.md) for detailed documentation.

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin 1.9.10 |
| UI Framework | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture |
| Database | Room + EncryptedSharedPreferences |
| Networking | Retrofit + OkHttp |
| DI | Koin 3.4.0 |
| Async | Coroutines |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

## 🎯 Screens

### Home Screen
- Account status and login button
- Version selection
- Quick statistics (installed versions, total size, playtime)
- Launch button with error handling

### Authentication Screen
- Current account display
- Account switching
- Multiple login methods
- Token management

### Profiles Screen (Version Management)
- Installed versions list
- Available versions list
- Install/Uninstall functionality
- Download progress tracking

### Settings Screen
- Memory allocation slider (512-4096 MB)
- Game directory configuration
- VM arguments customization
- Java version management
- Save/Reset options

## 📋 Requirements

- Android 7.0 (API 24) or higher
- Minimum 2GB RAM recommended
- Java 11 or higher for development
- Android Studio Giraffe or later

## 🚀 Getting Started

### Prerequisites
```bash
# Install Android SDK 34
# Install Kotlin 1.9.10
# Install Android Studio Giraffe or later
```

### Clone and Build
```bash
# Clone repository
git clone https://github.com/leomar00/minecraft-java-launcher-android.git
cd minecraft-java-launcher-android

# Build project
./gradlew build

# Run on device/emulator
./gradlew installDebug

# Run tests
./gradlew test
```

## 🔧 Configuration

### API Configuration
All API clients are configured in `AppModule.kt`:

```kotlin
// Minecraft Auth API
Retrofit.Builder()
    .baseUrl("https://authserver.mojang.com/")
    .client(okHttpClient)
    .build()
    .create(MinecraftAuthApi::class.java)
```

### Game Directory
Default location: `.minecraft` in app's external files directory

Customizable via Settings screen

### Database
Room database with automatic schema versioning

## 📦 Dependencies

```gradle
// Core
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1

// Compose & UI
androidx.compose.ui:ui:1.5.4
androidx.compose.material3:material3:1.1.1

// Security
androidx.security:security-crypto:1.1.0-alpha06

// Network
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.okhttp3:okhttp:4.11.0

// Database
androidx.room:room-runtime:2.6.0

// DI
io.insert-koin:koin-android:3.4.0

// Async
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
```

## 🔐 Security

- ✅ HTTPS-only connections
- ✅ Encrypted token storage
- ✅ Secure password handling
- ✅ No cleartext credentials in code
- ✅ Certificate validation
- ✅ Input validation and sanitization

## 📊 Project Status

### Completed ✅
- [x] UI Framework setup
- [x] Database persistence
- [x] Dependency injection
- [x] Authentication system
- [x] Account management
- [x] Settings screen

### In Progress 🔄
- [ ] Version downloading
- [ ] Game launching
- [ ] Microsoft OAuth integration

### Planned 📋
- [ ] Mod/Plugin support
- [ ] Crash handling
- [ ] Game logs viewer
- [ ] Performance optimization
- [ ] Unit tests
- [ ] Integration tests

## 📝 Usage

### Offline Login
```kotlin
// In your Compose UI
val authViewModel: AuthViewModel = koinViewModel()

Button(
    onClick = { authViewModel.loginOffline("Steve") }
) {
    Text("Login Offline")
}
```

### Online Login
```kotlin
Button(
    onClick = { authViewModel.loginWithUsername("user@email.com", "password") }
) {
    Text("Login")
}
```

### Observe Authentication State
```kotlin
val authState by authViewModel.authState.collectAsState()

when (authState) {
    is AuthViewModel.AuthState.Success -> {
        // User logged in
    }
    is AuthViewModel.AuthState.Error -> {
        // Show error
    }
    is AuthViewModel.AuthState.Loading -> {
        // Show loading
    }
    else -> {}
}
```

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

MIT License - See LICENSE file for details

## ⚠️ Disclaimer

This project is not affiliated with Minecraft or Microsoft. Minecraft is a trademark of Microsoft Corporation.

## 📚 Additional Documentation

- [DEVELOPMENT.md](DEVELOPMENT.md) - Development roadmap and status
- [AUTHENTICATION.md](AUTHENTICATION.md) - Authentication system details
- [Android Docs](https://developer.android.com)
- [Minecraft Wiki](https://wiki.vg)

## 🆘 Troubleshooting

### Build Issues
- Clear build cache: `./gradlew clean`
- Update Gradle: `./gradlew wrapper --gradle-version=8.2`
- Sync dependencies: `./gradlew build --refresh-dependencies`

### Runtime Issues
- Check API endpoints are accessible
- Verify internet connection
- Check app permissions
- Review Logcat for detailed errors

## 📞 Support

For issues and feature requests, please open a GitHub issue with:
- Device/Emulator info
- Android version
- Steps to reproduce
- Expected vs actual behavior

---

**Made with ❤️ by leomar00**
