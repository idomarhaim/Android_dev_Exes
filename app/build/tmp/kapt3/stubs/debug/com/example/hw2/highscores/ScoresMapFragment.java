package com.example.hw2.highscores;

/**
 * Bottom fragment of the high-scores screen: a map with a marker for every
 * score that has a recorded location. Selecting a row in
 * [ScoresTableFragment] re-centres the camera here.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005H\u0016J\u001a\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0016\u0010\u0017\u001a\u00020\r2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0019H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\u00a8\u0006\u001b"}, d2 = {"Lcom/example/hw2/highscores/ScoresMapFragment;", "Lcom/google/android/gms/maps/SupportMapFragment;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "()V", "map", "Lcom/google/android/gms/maps/GoogleMap;", "viewModel", "Lcom/example/hw2/highscores/HighScoresViewModel;", "getViewModel", "()Lcom/example/hw2/highscores/HighScoresViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "focusOn", "", "score", "Lcom/example/hw2/data/HighScore;", "onMapReady", "googleMap", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "renderMarkers", "scores", "", "Companion", "app_debug"})
public final class ScoresMapFragment extends com.google.android.gms.maps.SupportMapFragment implements com.google.android.gms.maps.OnMapReadyCallback {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.GoogleMap map;
    private static final int MAP_PADDING_PX = 96;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.hw2.highscores.ScoresMapFragment.Companion Companion = null;
    
    public ScoresMapFragment() {
        super();
    }
    
    private final com.example.hw2.highscores.HighScoresViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void renderMarkers(java.util.List<com.example.hw2.data.HighScore> scores) {
    }
    
    private final void focusOn(com.example.hw2.data.HighScore score) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/hw2/highscores/ScoresMapFragment$Companion;", "", "()V", "MAP_PADDING_PX", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}