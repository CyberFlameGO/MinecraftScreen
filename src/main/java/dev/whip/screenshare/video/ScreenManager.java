package dev.whip.screenshare.video;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ScreenManager {
    private final int WIDTH;
    private final int HEIGHT;
    private final int FPS;
    private final Location screenStart;
    private final FrameCache cache;

    private final ScreenPlayer player;
    private final FrameEncoder encoder;

    private final ScreenThread thread;

    public ScreenManager(int WIDTH, int HEIGHT, int FPS, Location screenStart, ScreenPlayer player, FrameEncoder encoder) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.FPS = FPS;
        this.screenStart = screenStart;
        this.cache = new FrameCache();

        player.setManager(this);
        encoder.setManager(this);
        encoder.load();

        this.player = player;
        this.encoder = encoder;

        this.thread = new ScreenThread(this);
    }

    public void start(){
        thread.start();
    }

    public void stop(){
        thread.setRunning(false);
        //TODO kill all the fake entities via a stop method
    }

    public void addWatcher(Player user){
        player.addWatcher(user);
    }

    public void encode(){
        encoder.encode();
    }

    public void render(){
        player.render();
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getFPS() {
        return FPS;
    }

    public FrameCache getCache() {
        return cache;
    }

    public Location getScreenStart() {
        return screenStart;
    }
}
