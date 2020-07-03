package dev.whip.screenshare.video.encoders;

import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.FrameEncoder;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

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

    private long time;
    private void start(){
        time = System.currentTimeMillis();
    }
    private void stop(String name){
        System.out.println(name + ": " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
    }

    @Override
    public void encode() {
        while (true) {
            start();
            try {
                BufferedImage frame = converter.getBufferedImage(grabber.grabImage());
                stop("Converter");

                if (frame == null){
                    System.out.println("Encoding finished");
                    try {
                        grabber.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ArrayList<IChatBaseComponent> newFrame = new ArrayList<>(frame.getHeight());

                //pixel   = rgbArray[offset + (y-startY)*scansize + (x-startX)];

                stop("list alloc");

                final int width = frame.getWidth();
                int[] rgbArray = frame.getRGB(0, 0, width, frame.getHeight(), null, 0, width);

                for (int y = 0; y < frame.getHeight(); y++){
                    StringBuilder builder = new StringBuilder("{\"extra\":[");
                    for (int x = 0; x < width; x++) {
                        builder.append("{\"color\":\"#");
                        builder.append(Integer.toHexString(rgbArray[y * width + x]).substring(2));
                        builder.append("\",\"text\":\"█\"},");
                    }

                    builder.deleteCharAt(builder.length() - 1);
                    builder.append("],\"text\":\"\"}");

                    newFrame.add(IChatBaseComponent.ChatSerializer.jsonToComponent(builder.toString()));
                }

                stop("encode");

                /*
                // Cycles through the image pixels
                for (int y = 0; y < frame.getHeight(); y++) {
                    StringBuilder builder = new StringBuilder("{\"extra\":[");

                    for (int x = 0; x < frame.getWidth(); x++) {
                        builder.append("{\"color\":\"#");
                        builder.append(Integer.toHexString(frame.getRGB(x, y)).substring(2));
                        builder.append("\",\"text\":\"█\"},");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append("],\"text\":\"\"}");

                    newFrame.add(IChatBaseComponent.ChatSerializer.jsonToComponent(builder.toString()));
                }

                 */

                manager.getCache().addFrame(newFrame);
                System.out.println("Remaining Cache Capacity: " + manager.getCache().getRemainingCapacity());
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        }
    }
}
