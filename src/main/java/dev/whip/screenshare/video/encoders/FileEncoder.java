package dev.whip.screenshare.video.encoders;

import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.FrameEncoder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class FileEncoder extends FrameEncoder {
    private final String filename;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private FFmpegFrameGrabber grabber;

    public FileEncoder(String filename) {
        this.filename = filename;
    }

    @Override
    public void load(){
        grabber = new FFmpegFrameGrabber(new File(ScreenShare.getInstance().getDataFolder(), filename));
        grabber.setFrameRate(manager.getFPS());
        grabber.setImageHeight(manager.getHEIGHT());
        grabber.setImageWidth(manager.getWIDTH());

        try {
            grabber.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    long time = 0;
    private void start(){
        time = System.currentTimeMillis();
    }

    public void time(String name){
        System.out.println(name + ": " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
    }

    @Override
    public void encode() {
        try {
            start();
            if (grabber.getFrameNumber() >= grabber.getLengthInFrames()){
                manager.stop();
                return;
            }

            BufferedImage frame = converter.getBufferedImage(grabber.grabImage());
            ArrayList<String> newFrame = new ArrayList<>(frame.getHeight());;

            // Cycles through the image pixels
            for (int y = 0; y < frame.getHeight(); y++) {
                StringBuilder builder = new StringBuilder("{\"extra\":[");

                for (int x = 0; x < frame.getWidth(); x++) {
                    int color = frame.getRGB(x, y);

                    builder.append("{\"color\":\"#");
                    builder.append(Integer.toHexString(color).substring(2));
                    builder.append("\",\"text\":\"█\"},");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("],\"text\":\"\"}");

                newFrame.add(builder.toString());
                time("frame encoded");
            }

            manager.getCache().addFrame(newFrame);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
