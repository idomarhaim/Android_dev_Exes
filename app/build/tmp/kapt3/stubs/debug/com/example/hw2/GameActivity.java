package com.example.hw2;

/**
 * HW2 — the game screen. Reuses the HW1 visibility-toggling grid model
 * (no Canvas, no translationX/Y) and adds: a 5-lane / longer road, coins,
 * an odometer, a crash sound, tilt-sensor control, and persistence of the
 * final score (with device location) into Room.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ba\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 f2\u00020\u00012\u00020\u0002:\u0001fB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u00104\u001a\u000205H\u0002J\b\u00106\u001a\u000207H\u0002J\b\u00108\u001a\u000205H\u0002J\b\u00109\u001a\u000205H\u0002J\b\u0010:\u001a\u00020)H\u0002J\u001a\u0010;\u001a\u0002052\b\u0010<\u001a\u0004\u0018\u00010\u00052\u0006\u0010=\u001a\u00020\u0012H\u0016J\u0012\u0010>\u001a\u0002052\b\u0010?\u001a\u0004\u0018\u00010@H\u0014J\b\u0010A\u001a\u000205H\u0014J\u0010\u0010B\u001a\u0002052\u0006\u0010C\u001a\u00020\u0012H\u0002J\b\u0010D\u001a\u000205H\u0014J\b\u0010E\u001a\u000205H\u0002J\b\u0010F\u001a\u000205H\u0014J\u0010\u0010G\u001a\u0002052\u0006\u0010H\u001a\u00020IH\u0016J\b\u0010J\u001a\u000205H\u0002J9\u0010K\u001a\u0002052\u0006\u0010L\u001a\u00020\u00122\u0006\u0010M\u001a\u00020\u00122\u0006\u0010N\u001a\u00020\u00122\b\u0010O\u001a\u0004\u0018\u00010P2\b\u0010Q\u001a\u0004\u0018\u00010PH\u0002\u00a2\u0006\u0002\u0010RJ\u0018\u0010S\u001a\u0002052\u0006\u0010T\u001a\u00020\u00122\u0006\u0010U\u001a\u00020\u0012H\u0002J\b\u0010V\u001a\u000205H\u0002J\b\u0010W\u001a\u000205H\u0002J(\u0010X\u001a\u0002052\u0006\u0010Y\u001a\u00020Z2\u0006\u0010L\u001a\u00020\u00122\u0006\u0010M\u001a\u00020\u00122\u0006\u0010N\u001a\u00020\u0012H\u0002J\b\u0010[\u001a\u000205H\u0002J\b\u0010\\\u001a\u000205H\u0002J\b\u0010]\u001a\u000205H\u0002J\b\u0010^\u001a\u000205H\u0002J\b\u0010_\u001a\u000205H\u0002J\b\u0010`\u001a\u000205H\u0002J\u0010\u0010a\u001a\u0002052\u0006\u0010b\u001a\u00020\u0012H\u0002J\b\u0010c\u001a\u000205H\u0002J\b\u0010d\u001a\u000205H\u0002J\b\u0010e\u001a\u000205H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082.\u00a2\u0006\u0004\n\u0002\u0010\u000bR\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\tX\u0082.\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u001c\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\tX\u0082.\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R!\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\n0\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0012X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020\u0012X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u000103X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006g"}, d2 = {"Lcom/example/hw2/GameActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/hardware/SensorEventListener;", "()V", "accelerometer", "Landroid/hardware/Sensor;", "binding", "Lcom/example/hw2/databinding/ActivityGameBinding;", "carCells", "", "Landroid/widget/ImageView;", "[Landroid/widget/ImageView;", "cellRes", "", "[[I", "cells", "[[Landroid/widget/ImageView;", "cols", "", "crashToast", "Landroid/widget/Toast;", "engine", "Lcom/example/hw2/game/GameEngine;", "hearts", "", "getHearts", "()Ljava/util/List;", "hearts$delegate", "Lkotlin/Lazy;", "locationPermission", "Landroidx/activity/result/ActivityResultLauncher;", "", "mainHandler", "Landroid/os/Handler;", "maxLives", "mode", "Lcom/example/hw2/game/GameMode;", "repository", "Lcom/example/hw2/data/HighScoreRepository;", "rows", "running", "", "scoreSaved", "sensorManager", "Landroid/hardware/SensorManager;", "speedFactor", "", "tickRunnable", "Ljava/lang/Runnable;", "tiltArmed", "toneGenerator", "Landroid/media/ToneGenerator;", "buildGrid", "", "effectiveTickMs", "", "ensureLocationPermission", "handleCrash", "hasLocationPermission", "onAccuracyChanged", "sensor", "accuracy", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onMove", "direction", "onPause", "onPlayAgain", "onResume", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "onTick", "persist", "score", "distance", "coins", "lat", "", "lng", "(IIILjava/lang/Double;Ljava/lang/Double;)V", "playTone", "tone", "durationMs", "registerSensor", "renderGrid", "requestCurrentLocation", "client", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "saveScore", "setupControlsForMode", "showGameOver", "startLoop", "stopLoop", "unregisterSensor", "updateCarCell", "previousLane", "updateHearts", "updateHud", "vibrate", "Companion", "app_debug"})
public final class GameActivity extends androidx.appcompat.app.AppCompatActivity implements android.hardware.SensorEventListener {
    private com.example.hw2.databinding.ActivityGameBinding binding;
    private com.example.hw2.game.GameMode mode;
    private com.example.hw2.game.GameEngine engine;
    private com.example.hw2.data.HighScoreRepository repository;
    private final int rows = 16;
    private final int cols = 5;
    private final int maxLives = 3;
    private boolean running = false;
    private boolean scoreSaved = false;
    private android.widget.ImageView[][] cells;
    private int[][] cellRes;
    private android.widget.ImageView[] carCells;
    @org.jetbrains.annotations.Nullable()
    private android.widget.Toast crashToast;
    @org.jetbrains.annotations.Nullable()
    private android.media.ToneGenerator toneGenerator;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.SensorManager sensorManager;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor accelerometer;
    private boolean tiltArmed = true;
    private float speedFactor = 1.0F;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy hearts$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler mainHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable tickRunnable = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> locationPermission = null;
    private static final float TILT_THRESHOLD = 3.0F;
    private static final float TILT_RELEASE = 1.5F;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.hw2.GameActivity.Companion Companion = null;
    
    public GameActivity() {
        super();
    }
    
    private final java.util.List<android.widget.ImageView> getHearts() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void setupControlsForMode() {
    }
    
    private final void registerSensor() {
    }
    
    private final void unregisterSensor() {
    }
    
    private final long effectiveTickMs() {
        return 0L;
    }
    
    private final void buildGrid() {
    }
    
    private final void startLoop() {
    }
    
    private final void stopLoop() {
    }
    
    private final void onTick() {
    }
    
    private final void renderGrid() {
    }
    
    private final void updateHud() {
    }
    
    private final void onMove(int direction) {
    }
    
    private final void updateCarCell(int previousLane) {
    }
    
    @java.lang.Override()
    public void onSensorChanged(@org.jetbrains.annotations.NotNull()
    android.hardware.SensorEvent event) {
    }
    
    @java.lang.Override()
    public void onAccuracyChanged(@org.jetbrains.annotations.Nullable()
    android.hardware.Sensor sensor, int accuracy) {
    }
    
    private final void handleCrash() {
    }
    
    private final void updateHearts() {
    }
    
    private final void playTone(int tone, int durationMs) {
    }
    
    private final void showGameOver() {
    }
    
    private final void onPlayAgain() {
    }
    
    private final void ensureLocationPermission() {
    }
    
    private final boolean hasLocationPermission() {
        return false;
    }
    
    private final void saveScore() {
    }
    
    private final void requestCurrentLocation(com.google.android.gms.location.FusedLocationProviderClient client, int score, int distance, int coins) {
    }
    
    private final void persist(int score, int distance, int coins, java.lang.Double lat, java.lang.Double lng) {
    }
    
    private final void vibrate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/hw2/GameActivity$Companion;", "", "()V", "TILT_RELEASE", "", "TILT_THRESHOLD", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}