package dev.whip.screenshare.video;

import dev.whip.screenshare.video.threads.EncodeThread;
import dev.whip.screenshare.video.threads.ScreenThread;
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

    private final ScreenThread screenThread;
    private final EncodeThread encodeThread;

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

        this.screenThread = new ScreenThread(this);
        this.encodeThread = new EncodeThread(this);
    }

    public void start(){
        encodeThread.start();
        screenThread.start();
    }

    public void stop(){
        screenThread.setRunning(false);
        screenThread.stop();
        encodeThread.stop();
        //TODO kill all the fake entities via a stop method
    }

    public void addWatcher(Player user){
        player.addWatcher(user);
    }

    public void render(){
        player.render();
    }

    public void encode() {
        encoder.encode();
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
