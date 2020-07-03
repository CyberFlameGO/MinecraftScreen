package dev.whip.screenshare.video;

public abstract class FrameEncoder {
    protected ScreenManager manager;

    public abstract void load();

    public abstract void encode();

    public void setManager(ScreenManager manager) {
        this.manager = manager;
    }
}
