package dev.whip.screenshare.video.threads;

import dev.whip.screenshare.video.ScreenManager;

public class EncodeThread extends Thread{
    private final ScreenManager manager;

    public EncodeThread(ScreenManager manager){
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.encode();
    }
}
