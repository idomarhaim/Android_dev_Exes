package com.example.hw2.highscores;

/**
 * Renders the top-ten high scores. Tapping a row notifies [onClick] so the map
 * fragment can re-centre on that score's location.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u001aB\u0019\u0012\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u001c\u0010\u000e\u001a\u00020\u00062\n\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\rH\u0016J\u001c\u0010\u0011\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\rH\u0016J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u000bH\u0007J\u0016\u0010\u0017\u001a\u00020\u00062\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0019H\u0007R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/hw2/highscores/ScoresAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/hw2/highscores/ScoresAdapter$ScoreViewHolder;", "onClick", "Lkotlin/Function1;", "Lcom/example/hw2/data/HighScore;", "", "(Lkotlin/jvm/functions/Function1;)V", "items", "", "selectedId", "", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setSelected", "id", "submit", "scores", "", "ScoreViewHolder", "app_debug"})
public final class ScoresAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.hw2.highscores.ScoresAdapter.ScoreViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.hw2.data.HighScore, kotlin.Unit> onClick = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.hw2.data.HighScore> items = null;
    private long selectedId = -1L;
    
    public ScoresAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.hw2.data.HighScore, kotlin.Unit> onClick) {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
    public final void submit(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.hw2.data.HighScore> scores) {
    }
    
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
    public final void setSelected(long id) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.hw2.highscores.ScoresAdapter.ScoreViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.hw2.highscores.ScoresAdapter.ScoreViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/hw2/highscores/ScoresAdapter$ScoreViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/hw2/databinding/ItemHighScoreBinding;", "(Lcom/example/hw2/highscores/ScoresAdapter;Lcom/example/hw2/databinding/ItemHighScoreBinding;)V", "bind", "", "item", "Lcom/example/hw2/data/HighScore;", "rank", "", "selected", "", "app_debug"})
    public final class ScoreViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.hw2.databinding.ItemHighScoreBinding binding = null;
        
        public ScoreViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.hw2.databinding.ItemHighScoreBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.hw2.data.HighScore item, int rank, boolean selected) {
        }
    }
}