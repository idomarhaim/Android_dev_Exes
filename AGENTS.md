# AGENTS.md — Android_dev_Exes

Kotlin Android course project. Each exercise is a self-contained app. Current
exercise: **HW2** — an endless obstacle racing game (built on HW1) with a menu,
tilt/button control modes, coins, an odometer, a crash sound, and a high-scores
screen (table + map) backed by Room.

See [README.md](README.md) for the feature list and [ARCHITECTURE.md](ARCHITECTURE.md)
for the design (movement model, game loop, crash handling). Read those before
changing game logic — don't re-derive the design.

## Project shape

- Single Gradle module `:app`, namespace `com.example.hw2` (root project name
  is still `HW1`).
- Screens (Activities): `MenuActivity` (launcher), `GameActivity` (the game),
  `highscores/HighScoresActivity` (hosts `ScoresTableFragment` +
  `ScoresMapFragment`, coordinated by `HighScoresViewModel`).
- Game **state/logic** lives in the pure-Kotlin `game/GameEngine` (no Android
  deps, JVM-unit-tested); `GameActivity` only renders it.
- Persistence in `data/` (Room): `HighScore`, `HighScoreDao`, `AppDatabase`,
  `HighScoreRepository`.
- UI via **View Binding** (`buildFeatures.viewBinding = true`). Use binding,
  not `findViewById`.
- Layout/resources under [app/src/main/res](app/src/main/res); manifest at
  [app/src/main/AndroidManifest.xml](app/src/main/AndroidManifest.xml).
- The high-scores **map** needs a Google Maps key: `MAPS_API_KEY` in
  `local.properties` (manifest placeholder, falls back to a stub so it builds).

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

## Game-logic constraints (hard requirements)

- **No `Canvas` / `SurfaceView`.** No `translationX` / `translationY` for
  movement. "Movement" is done by toggling each `ImageView` cell's
  `visibility` every tick. Preserve this model when editing the game loop.
- Game state is owned by `game/GameEngine` (`Boolean[rows][cols]` grids for
  obstacles and coins; `rows=16`, `cols=5`). `GameActivity` reads the engine
  and renders; keep logic in the engine so the unit tests stay meaningful.
- The car row is a separate strip of `cols` cells where only the active lane
  is `VISIBLE`. See ARCHITECTURE.md.
- The `running` flag must gate every tick re-schedule; the loop stops in
  `onPause()` and while the Game Over overlay is visible.

## Conventions

- All code, comments, commit messages, and docs in **English**.
- Record notable changes in [CHANGELOG/](CHANGELOG/CHANGELOG_README.md): one
  file per date (`YYYY-MM-DD.md`), sections per the changelog README.
