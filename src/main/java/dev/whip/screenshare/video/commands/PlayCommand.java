package dev.whip.screenshare.video.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.ScreenManager;
import dev.whip.screenshare.video.encoders.FileEncoder;
import dev.whip.screenshare.video.players.ProtocolPlayer;
import org.bukkit.entity.Player;

@CommandAlias("screen")
public class PlayCommand extends BaseCommand {
    @Default
    @CommandCompletion("128 72 60 video2.mp4")
    public void onPlay(Player player, int width, int height, int fps, String filename){
        ScreenManager manager = new ScreenManager(width, height, fps, player.getLocation(),
                new ProtocolPlayer(ScreenShare.getInstance().getProtocolManager()), new FileEncoder(filename));

        manager.addWatcher(player);

        manager.start();
    }
}
