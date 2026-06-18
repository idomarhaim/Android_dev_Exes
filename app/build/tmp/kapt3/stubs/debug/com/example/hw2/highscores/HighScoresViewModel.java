package com.example.hw2.highscores;

/**
 * Shared between [ScoresTableFragment] and [ScoresMapFragment] (activity
 * scope) so that tapping a row in the table moves the map.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0007R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0019\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u0014"}, d2 = {"Lcom/example/hw2/highscores/HighScoresViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_selected", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/hw2/data/HighScore;", "repository", "Lcom/example/hw2/data/HighScoreRepository;", "scores", "Landroidx/lifecycle/LiveData;", "", "getScores", "()Landroidx/lifecycle/LiveData;", "selected", "getSelected", "select", "", "score", "app_debug"})
public final class HighScoresViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.hw2.data.HighScoreRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.hw2.data.HighScore>> scores = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.hw2.data.HighScore> _selected = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.hw2.data.HighScore> selected = null;
    
    public HighScoresViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.hw2.data.HighScore>> getScores() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.hw2.data.HighScore> getSelected() {
        return null;
    }
    
    public final void select(@org.jetbrains.annotations.NotNull()
    com.example.hw2.data.HighScore score) {
    }
}