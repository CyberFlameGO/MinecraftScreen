package dev.whip.screenshare;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.whip.screenshare.video.commands.PlayCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ScreenShare extends JavaPlugin {
    private static ScreenShare screenShare;

    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        screenShare = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new PlayCommand());
    }

    @Override
    public void onDisable() {

    }

    public static ScreenShare getInstance(){
        return screenShare;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
