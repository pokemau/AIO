# Agent Guidelines for AIO Android Project

This document provides essential information for AI coding agents working on the AIO Android project.

## Project Overview

- **Type**: Android mobile application
- **Language**: Kotlin 2.0.21
- **Build System**: Gradle 8.13 with Kotlin DSL
- **UI Framework**: Jetpack Compose with Material 3
- **Package**: pokemau.aio
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Java Version**: 11

## Build & Run Commands

### Building
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew build                  # Build all variants and run checks
./gradlew clean                  # Clean build artifacts
```

### Testing
```bash
./gradlew test                   # Run all unit tests
./gradlew testDebugUnitTest      # Run debug unit tests only
./gradlew connectedAndroidTest   # Run instrumented tests (requires device/emulator)
./gradlew connectedDebugAndroidTest  # Run debug instrumented tests

# Run a single unit test class
./gradlew test --tests pokemau.aio.ExampleUnitTest

# Run a specific test method
./gradlew test --tests pokemau.aio.ExampleUnitTest.addition_isCorrect
```

### Linting & Code Quality
```bash
./gradlew lint                   # Run lint checks
./gradlew lintDebug              # Run lint on debug variant
./gradlew lintRelease            # Run lint on release variant
```

### Installation
```bash
./gradlew installDebug           # Install debug APK on connected device
./gradlew installRelease         # Install release APK on connected device
```

## Code Style Guidelines

### File Organization
- Package structure: `pokemau.aio` (root), with feature-based subpackages
- UI theme files in: `pokemau.aio.ui.theme`
- Test files mirror main source structure

### Import Ordering
1. Android framework imports (android.*)
2. AndroidX imports (androidx.*)
3. Third-party libraries
4. Project imports (pokemau.aio.*)
5. Blank line between groups
6. Organize alphabetically within groups

Example:
```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import pokemau.aio.ui.theme.AIOTheme
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `MainActivity`, `ExampleUnitTest`)
- **Functions**: camelCase (e.g., `onCreate`, `addition_isCorrect`)
- **Composables**: PascalCase (e.g., `Greeting`, `AIOTheme`)
- **Properties/Variables**: camelCase (e.g., `innerPadding`, `colorScheme`)
- **Constants**: PascalCase for Color/Theme values (e.g., `Purple80`, `DarkColorScheme`)
- **Test Methods**: snake_case descriptive names (e.g., `addition_isCorrect`, `useAppContext`)

### Kotlin Code Style
- Follow official Kotlin code style (configured in gradle.properties)
- Use `val` over `var` whenever possible
- Prefer expression bodies for simple functions
- Use named parameters for better readability
- Apply trailing commas in multi-line declarations

### Type Handling
- Leverage Kotlin's type inference where clear
- Explicitly declare types for public APIs
- Use nullable types (`?`) appropriately
- Prefer `lateinit` for non-null properties initialized later
- Use safe calls (`?.`) and Elvis operator (`?:`) for null safety

### Jetpack Compose Specifics
- Composable functions use PascalCase and `@Composable` annotation
- Keep composables small and focused
- Use `Modifier` parameter (typically last parameter with default value)
- Preview composables with `@Preview` annotation
- Hoist state when possible for better reusability

Example:
```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AIOTheme {
        Greeting("Android")
    }
}
```

### Error Handling
- Use Kotlin's exception handling (try-catch-finally)
- Leverage `Result` type for recoverable errors
- Document exceptions with KDoc `@throws` tag
- Prefer sealed classes for representing states with errors

### Documentation
- Use KDoc for public APIs and complex logic
- Keep comments concise and meaningful
- Document non-obvious behavior
- Include `@param`, `@return`, `@throws` tags for functions

### Testing Conventions
- Unit tests in `app/src/test/java/`
- Instrumented tests in `app/src/androidTest/java/`
- Test class names: `<ClassUnderTest>Test` (e.g., `ExampleUnitTest`)
- Use JUnit 4 framework with `@Test` annotations
- Use `@RunWith(AndroidJUnit4::class)` for instrumented tests
- Assert with `org.junit.Assert.*` methods
- Use descriptive test method names with underscores

## Dependencies Management
- Dependencies managed in `gradle/libs.versions.toml`
- Use version catalogs for consistent versioning
- Add dependencies via `implementation()`, `testImplementation()`, `androidTestImplementation()`

## Common Tasks for Agents

### Adding a New Screen/Feature
1. Create Kotlin file in appropriate package under `app/src/main/java/pokemau/aio/`
2. Follow existing package structure (e.g., `ui.<feature>`)
3. Create composable functions with proper modifiers
4. Add navigation if needed
5. Write unit tests for business logic
6. Write UI tests for composables

### Modifying Build Configuration
- Edit `app/build.gradle.kts` for app-level config
- Edit `gradle/libs.versions.toml` for dependency versions
- Edit root `build.gradle.kts` for project-wide plugins

### Running Before Committing
```bash
./gradlew clean build test lint
```

## Notes for Agents
- Always preserve existing code style and formatting
- Run tests after making changes
- Use Android Studio conventions for resource naming
- Follow Material 3 design guidelines for UI components
- Ensure backwards compatibility with minSdk 24
- Test on both light and dark themes when modifying UI
