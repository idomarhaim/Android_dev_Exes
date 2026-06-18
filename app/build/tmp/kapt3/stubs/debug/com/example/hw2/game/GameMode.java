package com.example.hw2.game;

/**
 * How the player controls the car and how fast the road scrolls.
 *
 * The menu offers three options (HW2 spec):
 * - two on-screen buttons, slow pace,
 * - two on-screen buttons, fast pace,
 * - tilt sensor (accelerometer).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u0000 \u000e2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u000eB\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r\u00a8\u0006\u000f"}, d2 = {"Lcom/example/hw2/game/GameMode;", "", "tickMs", "", "usesSensor", "", "(Ljava/lang/String;IJZ)V", "getTickMs", "()J", "getUsesSensor", "()Z", "BUTTONS_SLOW", "BUTTONS_FAST", "SENSOR", "Companion", "app_debug"})
public enum GameMode {
    /*public static final*/ BUTTONS_SLOW /* = new BUTTONS_SLOW(0L, false) */,
    /*public static final*/ BUTTONS_FAST /* = new BUTTONS_FAST(0L, false) */,
    /*public static final*/ SENSOR /* = new SENSOR(0L, false) */;
    private final long tickMs = 0L;
    private final boolean usesSensor = false;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_KEY = "com.example.hw2.GAME_MODE";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.hw2.game.GameMode.Companion Companion = null;
    
    GameMode(long tickMs, boolean usesSensor) {
    }
    
    public final long getTickMs() {
        return 0L;
    }
    
    public final boolean getUsesSensor() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.hw2.game.GameMode> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/hw2/game/GameMode$Companion;", "", "()V", "EXTRA_KEY", "", "fromName", "Lcom/example/hw2/game/GameMode;", "name", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.hw2.game.GameMode fromName(@org.jetbrains.annotations.Nullable()
        java.lang.String name) {
            return null;
        }
    }
}