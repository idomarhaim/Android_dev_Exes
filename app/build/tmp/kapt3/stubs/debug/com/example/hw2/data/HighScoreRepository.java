package com.example.hw2.data;

/**
 * Thin wrapper over [HighScoreDao] so callers don't touch Room directly.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0010"}, d2 = {"Lcom/example/hw2/data/HighScoreRepository;", "", "dao", "Lcom/example/hw2/data/HighScoreDao;", "(Lcom/example/hw2/data/HighScoreDao;)V", "topTen", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/hw2/data/HighScore;", "getTopTen", "()Lkotlinx/coroutines/flow/Flow;", "add", "", "score", "(Lcom/example/hw2/data/HighScore;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class HighScoreRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.hw2.data.HighScoreDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.example.hw2.data.HighScore>> topTen = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.hw2.data.HighScoreRepository.Companion Companion = null;
    
    public HighScoreRepository(@org.jetbrains.annotations.NotNull()
    com.example.hw2.data.HighScoreDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.hw2.data.HighScore>> getTopTen() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object add(@org.jetbrains.annotations.NotNull()
    com.example.hw2.data.HighScore score, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/hw2/data/HighScoreRepository$Companion;", "", "()V", "from", "Lcom/example/hw2/data/HighScoreRepository;", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.hw2.data.HighScoreRepository from(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}