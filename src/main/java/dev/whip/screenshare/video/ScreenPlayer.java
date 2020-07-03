package dev.whip.screenshare.video;

import org.bukkit.entity.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ScreenPlayer {
    protected ScreenManager manager;
    protected CopyOnWriteArrayList<Player> players;

    public void setManager(ScreenManager manager) {
        this.manager = manager;
        this.players = new CopyOnWriteArrayList<>();
    }

    public void addWatcher(Player player){
        initWatcher(player);
        players.add(player);
    }

    public abstract void render();

    public abstract void initWatcher(Player player);
}
