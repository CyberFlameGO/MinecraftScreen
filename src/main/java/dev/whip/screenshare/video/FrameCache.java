package dev.whip.screenshare.video;

import net.minecraft.server.v1_16_R1.IChatBaseComponent;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class FrameCache {
    private final LinkedBlockingQueue<List<IChatBaseComponent>> frames;

    public FrameCache(){
        frames = new LinkedBlockingQueue<>(900);
    }

    public List<IChatBaseComponent> getFrame(){
        try {
            return frames.take();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Frame grab error");
    }

    public void addFrame(List<IChatBaseComponent> frame){
        try {
            frames.put(frame);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void clear(){
        frames.clear();
    }

    public int getRemainingCapacity(){
        return frames.remainingCapacity();
    }
}
