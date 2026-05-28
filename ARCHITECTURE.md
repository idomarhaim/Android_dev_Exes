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
- No persistence, scoring, sound, or accelerometer input (out of scope for
  HW1).
