package com.example.hw2.game;

/**
 * Pure game logic for the endless racing game — no Android dependencies, so it
 * can be unit-tested on the JVM.
 *
 * Movement model (per assignment / lecturer guidance): the road is a fixed
 * [rows] x [cols] matrix of cells. This class only owns the *state* of those
 * cells (which hold an obstacle / a coin) and the car lane; the Activity
 * renders the state by toggling `ImageView` visibility. No coordinates, no
 * `Canvas`, no `translationX/Y`.
 *
 * Each [tick]:
 * 1. advances the odometer,
 * 2. resolves whatever reached the car's row (obstacle => crash, coin => pick up),
 * 3. shifts every row one step down,
 * 4. spawns new obstacles/coins in the top row.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\u0010\u0018\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 12\u00020\u0001:\u00011BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u0003J\u000e\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0003J\u0016\u0010&\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u0003J\u0006\u0010\'\u001a\u00020$J\u0006\u0010(\u001a\u00020\u0003J\u000e\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020\u0003J\u001b\u0010+\u001a\u00020$2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0002\u00a2\u0006\u0002\u0010-J\b\u0010.\u001a\u00020$H\u0002J\u0006\u0010/\u001a\u000200R\u001e\u0010\r\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0010\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u001e\u0010\u0017\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000fR\u0011\u0010\u0019\u001a\u00020\u001a8F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u001bR\u001e\u0010\u001c\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u000fR\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u000f\u00a8\u00062"}, d2 = {"Lcom/example/hw2/game/GameEngine;", "", "rows", "", "cols", "maxLives", "obstacleSpawnChance", "", "coinSpawnChance", "random", "Lkotlin/random/Random;", "(IIIFFLkotlin/random/Random;)V", "<set-?>", "carLane", "getCarLane", "()I", "coinCount", "getCoinCount", "coins", "", "", "[[Z", "getCols", "distance", "getDistance", "isGameOver", "", "()Z", "lives", "getLives", "obstacles", "getRows", "coinAt", "row", "col", "moveCar", "", "direction", "obstacleAt", "reset", "score", "setLane", "lane", "shiftDown", "grid", "([[Z)V", "spawnTopRow", "tick", "Lcom/example/hw2/game/TickResult;", "Companion", "app_debug"})
public final class GameEngine {
    private final int rows = 0;
    private final int cols = 0;
    private final int maxLives = 0;
    private final float obstacleSpawnChance = 0.0F;
    private final float coinSpawnChance = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.random.Random random = null;
    @org.jetbrains.annotations.NotNull()
    private final boolean[][] obstacles = null;
    @org.jetbrains.annotations.NotNull()
    private final boolean[][] coins = null;
    private int carLane;
    private int lives;
    private int distance = 0;
    private int coinCount = 0;
    
    /**
     * Points awarded per coin in the combined score.
     */
    public static final int COIN_BONUS = 25;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.hw2.game.GameEngine.Companion Companion = null;
    
    public GameEngine(int rows, int cols, int maxLives, float obstacleSpawnChance, float coinSpawnChance, @org.jetbrains.annotations.NotNull()
    kotlin.random.Random random) {
        super();
    }
    
    public final int getRows() {
        return 0;
    }
    
    public final int getCols() {
        return 0;
    }
    
    public final int getCarLane() {
        return 0;
    }
    
    public final int getLives() {
        return 0;
    }
    
    public final int getDistance() {
        return 0;
    }
    
    public final int getCoinCount() {
        return 0;
    }
    
    public final boolean isGameOver() {
        return false;
    }
    
    public final boolean obstacleAt(int row, int col) {
        return false;
    }
    
    public final boolean coinAt(int row, int col) {
        return false;
    }
    
    /**
     * Combined score = distance travelled plus a bonus per collected coin.
     */
    public final int score() {
        return 0;
    }
    
    /**
     * Move the car one lane left (-1) or right (+1); clamped to the road.
     */
    public final void moveCar(int direction) {
    }
    
    /**
     * Place the car directly on [lane] (used by the tilt sensor); clamped.
     */
    public final void setLane(int lane) {
    }
    
    /**
     * Advance the game by one step and report what happened to the car.
     * When the game is already over this is a no-op.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.hw2.game.TickResult tick() {
        return null;
    }
    
    /**
     * Restore the engine to a fresh game.
     */
    public final void reset() {
    }
    
    private final void shiftDown(boolean[][] grid) {
    }
    
    private final void spawnTopRow() {
    }
    
    public GameEngine() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/hw2/game/GameEngine$Companion;", "", "()V", "COIN_BONUS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}