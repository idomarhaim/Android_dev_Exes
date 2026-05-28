# Android_dev_Exes

Exercises for an Android development course (Kotlin).

## 📦 Exercises

### 🏁 HW1 — Endless 3-Lane Obstacle Racing Game

A minimal endless racing game built without `Canvas`, using only standard
`View`s positioned via `translationX` / `translationY`.

**Features**

- 🛣️ Three-lane road
- 🚗 Player car that moves left and right via on-screen arrow buttons
- 🧱 Obstacles that scroll from the top of the screen at a constant speed
- 💥 Crash notification — `Toast` message + device vibration
- ❤️❤️❤️ Three lives shown as hearts in the top-right corner
- ♾️ Endless game — after losing all 3 lives, the lives reset and the game
  continues

## 🛠️ Tech

- Language: **Kotlin**
- Min SDK: **24**, Target SDK: **34**
- Build system: **Gradle (Kotlin DSL)** with Android Gradle Plugin 8.5.x
- UI: `ConstraintLayout` + `FrameLayout` (game area), View Binding
- No external game engine — positions are updated on a `Handler` game loop

## ▶️ How to Build & Run

1. Open the project root in **Android Studio** (Giraffe / Koala or newer).
2. Let Gradle sync — it will download the configured AGP / Kotlin versions.
3. Connect a device or start an emulator (API 24+).
4. Press **Run ▶** to install and launch the app.

> The first sync will also generate the Gradle wrapper if missing. If your
> Android Studio does not generate it automatically, run
> `gradle wrapper --gradle-version 8.7` once in the project root.

## 📁 Project Layout

```
app/
├── build.gradle.kts
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/example/hw1/MainActivity.kt
    └── res/
        ├── drawable/         # vector assets (car, obstacle, hearts, arrows)
        ├── layout/activity_main.xml
        └── values/           # strings, colors, themes
build.gradle.kts              # root build file
settings.gradle.kts           # module declarations
gradle.properties
```

See [ARCHITECTURE.md](ARCHITECTURE.md) for more details.
