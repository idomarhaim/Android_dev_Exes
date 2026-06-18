# 🏗️ Architecture — HW1

## 🎯 Goal

Implement an endless 3-lane obstacle racing game as specified by the HW1
assignment, with two explicit constraints from the lecturer:

1. **No `Canvas`** — don't render manually.
2. **No `translationX` / `translationY`** either — movement must be done as a
   sequence of image transitions (toggling visibility of pre-placed cells).

## 🧩 High-level Design

The game is a single-`Activity` app. The screen is a fixed grid of standard
`ImageView`s. "Movement" is implemented by mutating each cell's `visibility`
state every tick, so the picture appears to jump between cells.

```
FrameLayout (root, grass background)
├── FrameLayout (gameArea, road background)
│   └── LinearLayout (vertical, full screen) — built at runtime
│       ├── LinearLayout row 0       ┐
│       │   ├── ImageView cell[0][0] │
│       │   ├── ImageView cell[0][1] │  rows × cols obstacle cells
│       │   └── ImageView cell[0][2] │  (ic_obstacle, INVISIBLE by default)
│       ├── ...                      │
│       ├── LinearLayout row N-1     ┘
│       └── LinearLayout carRow
│           ├── ImageView carCell 0  ┐
│           ├── ImageView carCell 1  │  one ic_car per lane;
│           └── ImageView carCell 2  ┘  only the active lane is VISIBLE
├── LinearLayout heartsContainer (3 heart ImageViews)
├── ImageButton btnLeft
├── ImageButton btnRight
└── FrameLayout gameOverOverlay
    └── LinearLayout (centered)
        ├── TextView  "Game Over"
        └── Button    "Play Again"
```

All cells share `layout_weight=1`, so every row has the same height and every
column has the same width, regardless of screen size.

## ⏱️ Game Loop

A single periodic task drives the game on the main `Looper` via
`Handler.postDelayed`:

| Task           | Period       | Responsibility                                         |
| -------------- | ------------ | ------------------------------------------------------ |
| `tickRunnable` | `tickMs`     | Detect collision, shift the obstacle grid down by one row, spawn a new obstacle at the top, re-render visibilities. |

Defaults: `rows = 12`, `cols = 3`, `tickMs = 260 ms`, `spawnChance = 0.55`.

A `running` flag guards both the entry and the re-scheduling of
`tickRunnable`, so once `stopLoop()` is called no further ticks fire even if
one was already in flight. The loop is also stopped in `onPause()` and
restarted in `onResume()` (unless the Game Over overlay is showing).

## 🔁 Movement Model

State is a `Boolean[rows][cols]` array:

```
true  → cell is showing the obstacle image
false → cell is INVISIBLE
```

Each tick:

1. **Collision check** — if `state[rows-1][carLane] == true`, the obstacle in
   the bottom row in the car's lane hit the car.
2. **Shift** — copy each row's state down by one (`state[r] = state[r-1]`).
3. **Spawn** — clear row 0 and, with probability `spawnChance`, set a random
   lane in row 0 to `true`.
4. **Render** — walk every cell and set `visibility = VISIBLE / INVISIBLE`
   from the boolean state.

The car row is a separate strip of 3 `ImageView`s; left/right buttons just
flip which one is `VISIBLE`. No coordinates are ever computed.

## 💥 Crash Handling

When a collision is detected:

- The device vibrates for ~250 ms (`VibratorManager` on API 31+, `Vibrator`
  on older releases).
- A `Toast` with `"Crash!"` is shown. A single `crashToast` reference is
  kept and `cancel()`ed before showing the next one, so successive crashes
  don't queue stale toasts.
- Lives are decremented and the hearts row is updated.
- When lives reach `0`, `showGameOver()` stops the loop, clears the grid,
  cancels any pending crash toast, and reveals the Game Over overlay.

## 🛑 Game Over / Play Again

- `gameOverOverlay` is a full-screen, semi-transparent `FrameLayout` over
  the rest of the UI. It is `gone` at start.
- While the overlay is `VISIBLE`, the loop is fully stopped — no background
  ticks, no further vibrations or toasts.
- Tapping **Play Again** hides the overlay, calls `resetGame()` (clear
  grid, restore 3 hearts, center the car), and restarts the loop.

## 🔌 Permissions

`android.permission.VIBRATE` is required for the crash vibration and is
declared in [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml).

## 🚫 Non-goals

- No `Canvas` / `SurfaceView` rendering (per assignment).
- No `translationX` / `translationY` for movement (per lecturer guidance).

---

# 🏗️ Architecture — HW2

HW2 keeps the HW1 movement model verbatim (visibility toggling) and grows it
into a multi-screen app. Package renamed `com.example.hw1` → `com.example.hw2`.

## 🧭 Screens

```
MenuActivity (launcher)
├── Buttons–Slow / Buttons–Fast / Tilt Sensor → GameActivity (mode via Intent)
└── High Scores → HighScoresActivity
                  ├── ScoresMapFragment   (SupportMapFragment subclass)
                  └── ScoresTableFragment (RecyclerView)
                      └── both share HighScoresViewModel (activity scope)
```

## 🧠 GameEngine (pure logic, unit-tested)

Game state moved out of the Activity into `game/GameEngine` — no Android deps,
so it runs under plain JUnit. It owns two `Boolean[rows][cols]` grids
(obstacles + coins, `rows=16`, `cols=5`), the car lane, lives, distance and
coin count. `tick()` advances the odometer, resolves whatever reached the car
row (obstacle → crash, coin → pickup), shifts the grids down and spawns a new
top row, returning a `TickResult` (`crashed`, `coinCollected`, `gameOver`).
`GameActivity` renders the engine by toggling `ImageView` visibility — still
no Canvas, no translation.

- **Score** = `distance + coinCount × COIN_BONUS`.

## 🎮 Control modes

`GameMode` (enum) carries a `tickMs` and a `usesSensor` flag:

- `BUTTONS_SLOW` / `BUTTONS_FAST` — on-screen arrows; slow/fast differ in pace.
- `SENSOR` — accelerometer: x-axis past a threshold (with a cooldown) steps the
  car a lane; **bonus** — the y-axis scales the scroll speed (`effectiveTickMs`).

## 🔊 Crash sound

A `ToneGenerator` plays a short alert tone on crash (and a beep on coin
pickup) — chosen over a raw audio asset so there's no binary to ship. Released
in `onDestroy`. Vibration + toast from HW1 are retained.

## 💾 Persistence (Room)

`data/`: `HighScore` entity (score, distance, coins, lat/lng, `hasLocation`,
timestamp), `HighScoreDao` (`topTen()` = `ORDER BY score DESC LIMIT 10` as a
`Flow`, plus `insert`), `AppDatabase` (singleton), `HighScoreRepository`. On
game over `GameActivity` grabs the last known location
(`FusedLocationProviderClient`, `ACCESS_FINE_LOCATION` requested at runtime)
and inserts the score via `lifecycleScope`.

## 🗺️ High scores screen

`HighScoresViewModel` (AndroidViewModel) exposes `scores` (LiveData from the
Room Flow) and a `selected` score. `ScoresTableFragment` lists them; a row tap
calls `viewModel.select(...)`. `ScoresMapFragment` (a `SupportMapFragment`)
drops a marker per located score and animates the camera to the selected one.
The map needs a Google Maps key (`MAPS_API_KEY` in `local.properties`, exposed
as a manifest placeholder; falls back to a stub so the build never breaks).
