package dev.whip.screenshare.video.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.ScreenManager;
import dev.whip.screenshare.video.encoders.FileEncoder;
import dev.whip.screenshare.video.players.ProtocolPlayer;
import org.bukkit.entity.Player;

@CommandAlias("screen")
public class PlayCommand extends BaseCommand {
    @Default
    public void onPlay(Player player, int fps){
        ScreenManager manager = new ScreenManager(128, 72, fps, player.getLocation(),
                new ProtocolPlayer(ScreenShare.getInstance().getProtocolManager()), new FileEncoder());

        manager.addWatcher(player);

        manager.start();
    }
}