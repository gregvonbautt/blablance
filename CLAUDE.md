# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Blablance is an Android app for tracking and reflecting on the balance between user-defined positive and negative activities. The app uses a fragment-based architecture with bottom navigation and follows MVVM pattern with ViewModels and LiveData.

## Architecture

- **MainActivity**: Single activity hosting a bottom navigation with three main fragments
- **Fragment Structure**: Home, Stats, and Settings fragments with corresponding ViewModels
- **Navigation**: Uses Android Navigation Component with bottom navigation
- **UI Pattern**: MVVM with ViewBinding enabled
- **Package Structure**: `dev.gbautin.blablance` with UI components organized by feature in `ui/` subdirectories

## Development Commands

### Build and Run
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew installDebug           # Install debug APK on connected device
./gradlew build                  # Full build with tests
```

### Testing
```bash
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests on device/emulator
```

### Code Quality
```bash
./gradlew lint                   # Run Android lint checks
./gradlew lintDebug             # Run lint for debug variant only
```

## Key Configuration

- **Target SDK**: 35 (Android 15)
- **Min SDK**: 24 (Android 7.0)
- **Kotlin**: 2.0.21
- **ViewBinding**: Enabled
- **Package**: `dev.gbautin.blablance`

## Dependencies

Uses standard Android Jetpack components:
- Navigation Component for fragment navigation
- Lifecycle (ViewModel, LiveData) for MVVM
- Material Design Components for UI
- ViewBinding for view references