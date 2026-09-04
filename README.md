# Minecraft Java Launcher for Android

A feature-rich Minecraft Java Edition launcher for Android devices, built with Kotlin and Jetpack Compose.

## Features

- 🎮 **Game Launcher**: Launch Minecraft Java Edition on Android
- 🔐 **Account Management**: Microsoft/Launcher authentication support
- 📦 **Version Management**: Install and manage multiple Minecraft versions
- ⚙️ **Java Configuration**: Customizable JVM arguments and memory allocation
- 🎨 **Modern UI**: Built with Jetpack Compose and Material Design 3
- 🌓 **Dark Mode**: Full dark mode support

## Project Structure

```
minecraft-java-launcher-android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/com/minecraft/launcher/
│   │       │   ├── presentation/
│   │       │   │   └── ui/
│   │       │   │       ├── screens/        # UI Screens
│   │       │   │       └── theme/          # Theme Configuration
│   │       │   ├── domain/                 # Business Logic
│   │       │   └── data/                   # Data & Networking
│   │       └── res/
│   │           ├── values/                 # Colors, Strings, Themes
│   │           └── ...
│   ├── build.gradle.kts
│   └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Screens

### Home Screen
- Account status and login button
- Version selection
- Quick statistics (installed versions, total size, playtime)
- Launch button with progress indicator

### Profiles Screen
- List of installed Minecraft versions
- Add new version button
- Empty state with install prompt

### Settings Screen
- Game directory configuration
- Java memory allocation slider
- Java version management
- Custom VM arguments
- Save and reset options

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose & Material Design 3
- **Architecture**: MVVM (to be implemented)
- **Networking**: Retrofit + OkHttp
- **Database**: Room
- **Dependency Injection**: Koin
- **Async**: Coroutines
- **Parsing**: Gson

## Requirements

- Android 7.0 (API 24) or higher
- Minimum 2GB RAM recommended
- Java 11 or higher for development

## Building

### Prerequisites
- Android Studio Giraffe or later
- Android SDK 34
- Kotlin 1.9.10

### Steps

1. Clone the repository:
```bash
git clone https://github.com/leomar00/minecraft-java-launcher-android.git
cd minecraft-java-launcher-android
```

2. Open the project in Android Studio

3. Build the project:
```bash
./gradlew build
```

4. Run on emulator or device:
```bash
./gradlew installDebug
```

## Next Steps

- [ ] Implement authentication system
- [ ] Add version management API
- [ ] Implement game launching mechanism
- [ ] Add JVM runtime support
- [ ] Implement game crash handler
- [ ] Add mod/plugin support
- [ ] Performance optimization
- [ ] Unit and integration tests

## Contributing

Contributions are welcome! Please feel free to submit pull requests.

## License

MIT License - see LICENSE file for details

## Resources

- [Android Development Guide](https://developer.android.com)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Minecraft API Documentation](https://wiki.vg)
- [PojavLauncher (Reference Implementation)](https://github.com/PojavLauncherTeam/PojavLauncher)

## Disclaimer

This project is not affiliated with Minecraft or Microsoft. Minecraft is a trademark of Microsoft Corporation.
