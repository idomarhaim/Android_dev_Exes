# 🏗️ Architecture — HW1

## 🎯 Goal

Implement an endless 3-lane obstacle racing game as specified by the HW1
assignment, with the explicit constraint **"don't work with `Canvas` — change
position using x / y on screen"**.

## 🧩 High-level Design

The game is a single-`Activity` app. All game objects are standard Android
`View`s that live inside a `FrameLayout` ("the road"). Movement is achieved by
mutating each view's `translationX` / `translationY` from a `Handler`-driven
game loop on the main thread.

```
ConstraintLayout (root, grass background)
├── FrameLayout (gameArea, road background)        ← 85% width, full height
│   ├── View laneLine1 (yellow vertical divider, positioned via translationX)
│   ├── View laneLine2 (yellow vertical divider, positioned via translationX)
│   ├── ImageView car  (player, positioned via translationX/Y)
│   └── ImageView obstacle * N (added dynamically, scroll via translationY)
├── LinearLayout heartsContainer (3 heart ImageViews)
├── ImageButton btnLeft
└── ImageButton btnRight
```

## ⏱️ Game Loop

Two periodic tasks run on the main `Looper` via `Handler.postDelayed`:

| Task         | Period             | Responsibility                          |
| ------------ | ------------------ | --------------------------------------- |
| `tickRunnable`  | `tickMs` (~30 ms)  | Advance each obstacle's `translationY`, detect collisions, recycle off-screen obstacles. |
| `spawnRunnable` | `spawnIntervalMs` (~900 ms) | Create a new obstacle in a random lane at the top of the road. |

Both loops are started in `onResume()` and stopped in `onPause()` to avoid
draining the battery / leaking views.

## 📐 Geometry

Lane geometry is computed once the `gameArea` is laid out (via a
`OnGlobalLayoutListener`):

- `laneWidthPx = gameArea.width / 3`
- `laneCenterX(lane) = laneWidthPx * (lane + 0.5)`
- The two lane dividers are positioned at `laneWidthPx` and `2 * laneWidthPx`.
- The car's resting Y is anchored near the bottom:
  `carBaseY = gameArea.height - carHeight - 24dp`.

## 💥 Collision

Collision is checked per tick for every live obstacle:

1. The obstacle must be in the same lane index as the car.
2. The obstacle's vertical rectangle must overlap the car's vertical rectangle.

When a collision occurs:

- The obstacle is removed from the game.
- The device vibrates for ~250 ms (`Vibrator` / `VibratorManager` API).
- A `Toast` with `"Crash!"` is shown.
- Lives are decremented and the hearts row is updated.
- When lives reach 0, the game shows a `"Game Over — restarting"` toast,
  clears all obstacles, restores 3 hearts, recenters the car, and keeps
  playing — making the game endless.

## 🔌 Permissions

`android.permission.VIBRATE` is required for the crash vibration and is
declared in [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml).

## 🚫 Non-goals

- No `Canvas` / `SurfaceView` rendering (per assignment).
- No persistence, scoring, sound, or accelerometer input (out of scope for
  HW1).
