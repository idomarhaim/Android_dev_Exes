package com.example.hw2.game;

/**
 * Pure-JVM unit tests for [GameEngine]. The collision/coin scenarios use a
 * single-lane road with a 100% spawn chance, which makes the grid fully
 * deterministic without needing to seed the RNG.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\b\u0010\n\u001a\u00020\u0004H\u0007J\b\u0010\u000b\u001a\u00020\u0004H\u0007\u00a8\u0006\f"}, d2 = {"Lcom/example/hw2/game/GameEngineTest;", "", "()V", "coins are collected and counted in the score", "", "initial state is centred, full lives, zero score", "moveCar is clamped to the road", "obstacle reaching the car costs a life", "reset restores a fresh game", "setLane clamps out-of-range lanes", "three crashes end the game", "tick advances the odometer", "app_debugUnitTest"})
public final class GameEngineTest {
    
    public GameEngineTest() {
        super();
    }
}