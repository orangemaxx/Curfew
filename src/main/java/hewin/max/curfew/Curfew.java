package hewin.max.curfew;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Curfew extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        ConfigSetup();

        if (config.getBoolean("enabled")) {getServer().getPluginManager().registerEvents(this, this);}
    }

    // Listeners
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        if (GetTime() >= config.getInt("starttime") || GetTime() < config.getInt("endtime")) {
            e.getPlayer().kickPlayer(config.getString("curfewmessage"));
        }
    }

    public Integer GetTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        Integer time = Integer.valueOf(format.format(date).replace(":", ""));
        return time;
    }

    public void ConfigSetup() {
        config.addDefault("enabled", true);
        config.addDefault("starttime", 2130);
        config.addDefault("endtime", 0700);
        config.addDefault("curfewmessage", "Sorry! The Server is currently closed please join again later!");

        config.options().copyDefaults(true);

        saveConfig();
    }

    
}
