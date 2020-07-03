package dev.whip.screenshare.video;

import java.util.LinkedList;
import java.util.List;

public class FrameCache {
    private final LinkedList<List<String>> frames;

    public FrameCache(){
        frames = new LinkedList<>();
    }

    public List<String> getFrame(){
        return frames.pollFirst();
    }

    public void addFrame(List<String> frame){
        frames.add(frame);
    }

    public void clear(){
        frames.clear();
    }
}
