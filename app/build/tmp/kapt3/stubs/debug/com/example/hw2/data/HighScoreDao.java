package com.example.hw2.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bH\'\u00a8\u0006\r"}, d2 = {"Lcom/example/hw2/data/HighScoreDao;", "", "clear", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "", "score", "Lcom/example/hw2/data/HighScore;", "(Lcom/example/hw2/data/HighScore;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "topTen", "Lkotlinx/coroutines/flow/Flow;", "", "app_debug"})
@androidx.room.Dao()
public abstract interface HighScoreDao {
    
    /**
     * Top ten scores since the app was installed, highest first.
     */
    @androidx.room.Query(value = "SELECT * FROM high_scores ORDER BY score DESC, timestamp DESC LIMIT 10")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.hw2.data.HighScore>> topTen();
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.hw2.data.HighScore score, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "DELETE FROM high_scores")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}