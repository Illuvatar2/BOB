package de.idiotischer.bob.game;

public class ServerGameManager {

   public GameState state = GameState.INGAME;

    public GameState getState() {
        return state;
    }

    public void tickTime() {
        if(state == GameState.PAUSED || state == GameState.WAITING) return;
    }
}
