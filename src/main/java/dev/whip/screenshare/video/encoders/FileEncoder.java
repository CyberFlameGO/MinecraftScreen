package dev.whip.screenshare.video.encoders;

import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.FrameEncoder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class FileEncoder extends FrameEncoder {
    private final Java2DFrameConverter converter = new Java2DFrameConverter();

    private FFmpegFrameGrabber grabber;

    public FileEncoder() {

    }

    @Override
    public void load(){
        grabber = new FFmpegFrameGrabber(new File(ScreenShare.getInstance().getDataFolder(), "video.mp4"));
        grabber.setFrameRate(manager.getFPS());
        grabber.setImageHeight(manager.getHEIGHT());
        grabber.setImageWidth(manager.getWIDTH());

        try {
            grabber.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void encode() {
        try {
            if (grabber.getFrameNumber() >= grabber.getLengthInFrames()){
                manager.stop();
                return;
            }

            Frame newframe = grabber.grabImage();

            BufferedImage frame = converter.getBufferedImage(newframe);

            ArrayList<String> newFrame = new ArrayList<>(frame.getHeight());;

            // Cycles through the image pixels
            for (int y = 0; y < frame.getHeight(); y++) {
                BaseComponent[] pixels = new BaseComponent[frame.getWidth()];

                for (int x = 0; x < frame.getWidth(); x++) {
                    int color = frame.getRGB(x, y);

                    BaseComponent pixel = new TextComponent("█");
                    pixel.setColor(ChatColor.of(new Color(color)));
                    pixels[x] = pixel;
                }
                newFrame.add(ComponentSerializer.toString(pixels));
            }

            manager.getCache().addFrame(newFrame);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
