# Minecraft Java Launcher for Android - Development Log

## Completed Phases

### Phase 1: Project Setup & UI Framework ✅
- [x] Created basic Android project structure
- [x] Set up Jetpack Compose with Material Design 3
- [x] Implemented theme system (Minecraft-themed colors)
- [x] Created three main screens: Home, Profiles, Settings
- [x] Added resource files (strings, colors, themes)

### Phase 2: Domain Layer (Business Logic) ✅
- [x] Created data models:
  - MinecraftAccount (user authentication)
  - MinecraftVersion (version management)
  - GameProfile (player profiles)
  - Result sealed class (error handling)
- [x] Defined repository interfaces:
  - AccountRepository
  - VersionRepository
  - GameRepository
- [x] Implemented use cases:
  - AuthUseCase
  - VersionUseCase
  - GameUseCase

### Phase 3: Data Layer & Persistence ✅
- [x] Set up Room database:
  - AccountEntity & AccountDao
  - GameProfileEntity & GameProfileDao
- [x] Implemented repository implementations:
  - AccountRepositoryImpl (offline login support)
  - VersionRepositoryImpl (version management)
  - GameRepositoryImpl (profile management)
- [x] Created Retrofit API client for Minecraft versions
- [x] Added data transfer objects (DTOs)

### Phase 4: Dependency Injection & ViewModels ✅
- [x] Configured Koin for dependency injection
- [x] Created ViewModels:
  - AuthViewModel (auth state management)
  - HomeViewModel (home screen logic)
  - VersionsViewModel (version management)
  - SettingsViewModel (settings management)
- [x] Initialized MinecraftLauncherApp with Koin

### Phase 5: UI Integration with State Management ✅
- [x] Integrated HomeScreen with HomeViewModel
- [x] Integrated ProfilesScreen with VersionsViewModel
- [x] Integrated SettingsScreen with SettingsViewModel
- [x] Added login dialog with AuthViewModel
- [x] Implemented error handling UI
- [x] Added loading states
- [x] Version installation and management UI

## Next Steps

### Phase 6: Authentication Implementation (TODO)
- [ ] Implement Microsoft OAuth flow
- [ ] Implement Minecraft launcher authentication
- [ ] Add token refresh mechanism
- [ ] Implement profile management API

### Phase 7: Version Management (TODO)
- [ ] Implement version manifest fetching from launcher.mojang.com
- [ ] Add version download with progress tracking
- [ ] Implement version installation
- [ ] Add library management

### Phase 8: Game Launching (TODO)
- [ ] Integrate JVM runtime (e.g., PojavLauncher approach)
- [ ] Implement game process launching
- [ ] Add game log capture
- [ ] Implement crash handling

### Phase 9: Advanced Features (TODO)
- [ ] Mod/Plugin support
- [ ] Java version management
- [ ] RAM allocation optimization
- [ ] Game directory management
- [ ] Profile creation and editing

### Phase 10: Testing & Optimization (TODO)
- [ ] Unit tests for ViewModels
- [ ] Integration tests for repositories
- [ ] UI tests
- [ ] Performance optimization
- [ ] Memory profiling

## Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **DI**: Koin
- **Async**: Coroutines
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)

## Project Structure

```
app/src/main/kotlin/com/minecraft/launcher/
├── domain/
│   ├── model/          # Data models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic
├── data/
│   ├── local/          # Room database
│   ├── remote/         # Retrofit APIs
│   └── repository/     # Repository implementations
├── presentation/
│   ├── ui/
│   │   ├── screens/    # Compose screens
│   │   └── theme/      # Theme configuration
│   └── viewmodel/      # ViewModels
└── di/                 # Dependency injection
```

## Current Status

✅ **Complete**: Project structure, UI framework, domain layer, data persistence, DI setup
🔄 **In Progress**: UI screen integration
⏳ **Planned**: Authentication, version management, game launching

## Key Features Implemented

1. **Home Screen**
   - Account status display
   - Version selection
   - Quick statistics (versions, size, playtime)
   - Launch button with error handling
   - Login dialog (offline mode)

2. **Profiles Screen** (Version Management)
   - Tab view (Installed/Available)
   - Version listing
   - Install/Uninstall functionality
   - Progress tracking

3. **Settings Screen**
   - Memory allocation slider (512-4096 MB)
   - Game directory configuration
   - VM arguments customization
   - Save/Reset options

## Running the Project

```bash
# Clone the repository
git clone https://github.com/leomar00/minecraft-java-launcher-android.git

# Open in Android Studio
cd minecraft-java-launcher-android

# Build
./gradlew build

# Install on device/emulator
./gradlew installDebug
```

## Contributing

Contributions are welcome! Please feel free to submit pull requests.

## License

MIT License

## Disclaimer

This project is not affiliated with Minecraft or Microsoft. Minecraft is a trademark of Microsoft Corporation.
