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

### 🏎️ HW2 — Menu, Sensors, Coins & High Scores

A large expansion of HW1 (package renamed `com.example.hw1` →
`com.example.hw2`). The visibility-toggling movement model is unchanged.

**Features**

- 📋 **Menu screen** (launcher) — three modes: Buttons–Slow, Buttons–Fast,
  Tilt Sensor — plus a link to the High Scores screen
- 🛣️ **Wider + longer road**: 5 lanes × 16 rows
- 🪙 **Coins** that spawn on the road and add a bonus to the score
- 📏 **Odometer** (distance) + coins counter in the HUD
- 📱 **Tilt to steer** via the accelerometer; **bonus**: tilt forward/back for speed
- 🔊 **Crash sound** (`ToneGenerator`) in addition to vibration
- 🏆 **High Scores screen** — two fragments: a **top-ten table** (Room) and a
  **map** of where each score was achieved; tapping a row re-centres the map
- 💾 Score (`distance + coins × bonus`) + device location persisted in **Room**
- 🖼️ Custom **app icon**

> The map needs a Google Maps API key. Put `MAPS_API_KEY=...` in
> `local.properties`; without it the app still builds and the map is blank.

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
    ├── java/com/example/hw2/
    │   ├── MenuActivity.kt          # launcher: mode picker + High Scores
    │   ├── GameActivity.kt          # the game screen (renders GameEngine)
    │   ├── game/                    # GameEngine (pure logic), GameMode
    │   ├── data/                    # Room: HighScore, DAO, DB, repository
    │   └── highscores/             # HighScores activity + table/map fragments
    └── res/
        ├── drawable/         # vector assets (car, obstacle, coin, hearts, …)
        ├── layout/           # menu, game, high-scores, fragment, item
        └── values/           # strings, colors, themes
app/src/test/java/com/example/hw2/game/GameEngineTest.kt   # JVM unit tests
build.gradle.kts              # root build file
settings.gradle.kts           # module declarations
gradle.properties             # pins Gradle JDK to Adoptium JDK 17
```

See [ARCHITECTURE.md](ARCHITECTURE.md) for design details and
[CHANGELOG/](CHANGELOG/CHANGELOG_README.md) for the change history.
