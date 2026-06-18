# AGENTS.md — Android_dev_Exes

Kotlin Android course project. Each exercise is a self-contained app. Current
exercise: **HW1** — an endless 3-lane obstacle racing game.

See [README.md](README.md) for the feature list and [ARCHITECTURE.md](ARCHITECTURE.md)
for the design (movement model, game loop, crash handling). Read those before
changing game logic — don't re-derive the design.

## Project shape

- Single Gradle module `:app`, namespace `com.example.hw1` (root project name is `HW1`).
- All app logic lives in one Activity: [app/src/main/java/com/example/hw1/MainActivity.kt](app/src/main/java/com/example/hw1/MainActivity.kt).
- UI via **View Binding** (`ActivityMainBinding`) — `buildFeatures.viewBinding = true`.
  Use binding, not `findViewById`.
- Layout/resources under [app/src/main/res](app/src/main/res); manifest at
  [app/src/main/AndroidManifest.xml](app/src/main/AndroidManifest.xml).

## Build & run

```powershell
./gradlew assembleDebug      # build the debug APK
./gradlew installDebug       # install on a connected device/emulator (API 24+)
./gradlew lint               # Android lint
```

- Toolchain: AGP **8.3.2**, Kotlin Android, **Java 17** (`sourceCompatibility`/`jvmTarget`).
- SDK: `minSdk 24`, `compileSdk`/`targetSdk 34`.

## Critical pitfalls (don't skip)

- **Gradle JDK is pinned** in [gradle.properties](gradle.properties) via
  `org.gradle.java.home` to a full **Adoptium JDK 17** (not a JRE — AGP's
  `JdkImageTransform` needs `jlink.exe`). If a build fails with a JDK/jlink
  error, the pinned path is wrong for this machine — fix that path, don't
  change the Java version.
- `org.gradle.vfs.watch=false` is intentional (avoids file-handle locks on
  Windows). Leave it off.

## Game-logic constraints (HW1 — hard requirements)

- **No `Canvas` / `SurfaceView`.** No `translationX` / `translationY` for
  movement. "Movement" is done by toggling each `ImageView` cell's
  `visibility` every tick. Preserve this model when editing the game loop.
- Game state is a `Boolean[rows][cols]` grid; the car row is a separate strip
  of 3 cells where only the active lane is `VISIBLE`. See ARCHITECTURE.md.
- The `running` flag must gate every tick re-schedule; the loop stops in
  `onPause()` and while the Game Over overlay is visible.

## Conventions

- All code, comments, commit messages, and docs in **English**.
- Record notable changes in [CHANGELOG/](CHANGELOG/CHANGELOG_README.md): one
  file per date (`YYYY-MM-DD.md`), sections per the changelog README.
