# Android_dev_Exes

Exercises for an Android development course (Kotlin).

## 📦 Exercises

### 🏁 HW1 — Endless 3-Lane Obstacle Racing Game

A minimal endless racing game built without `Canvas`. Per the lecturer's
guidance, movement is **not** done via `translationX` / `translationY` either —
instead the road is a fixed **matrix of `ImageView` cells** and "movement" is
achieved by toggling each cell's `visibility` every tick, so the picture
appears to jump from one cell to the next.

**Features**

- 🛣️ Three-lane road rendered as a 12 × 3 grid of `ImageView` cells
- 🚗 Player car shown by toggling the active lane cell in the bottom row
- ⬅️➡️ On-screen arrow buttons move the car between the three lanes
- 🧱 Obstacles fall one row per tick at a constant pace
- 💥 Crash notification — `Toast` message + device vibration
- ❤️❤️❤️ Three lives shown as hearts in the top-right corner
- 🛑 **Game Over screen** with a **Play Again** button after losing all 3 lives
- ⏸️ Game is fully paused while the Game Over overlay is showing
  (no background ticks, no phantom crashes)

## 🛠️ Tech

- Language: **Kotlin**
- Min SDK: **24**, Target SDK: **34**, Java **17**
- Build system: **Gradle (Kotlin DSL)** with Android Gradle Plugin **8.3.2**
- Gradle JDK: **Eclipse Adoptium JDK 17** (pinned via `org.gradle.java.home`)
- UI: `FrameLayout` root, runtime-built `LinearLayout` grid, View Binding

## ▶️ How to Build & Run

1. Open the project root in **Android Studio**.
2. Make sure the Gradle JDK is set to **JDK 17** (Settings → Build Tools →
   Gradle → Gradle JDK).
3. Let Gradle sync.
4. Connect a device or start an emulator (API 24+).
5. Press **Run ▶** to install and launch the app.

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
gradle.properties             # pins Gradle JDK to Adoptium JDK 17
```

See [ARCHITECTURE.md](ARCHITECTURE.md) for design details and
[CHANGELOG/](CHANGELOG/CHANGELOG_README.md) for the change history.
