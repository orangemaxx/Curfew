package hewin.max.curfew;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Curfew extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();
    Boolean runTitle10 = false;
    Boolean runTitle5 = false;
    Boolean bossbar = false;
    Boolean barexists = false;
    BossBar bar;

    @Override
    public void onEnable() {
        ConfigSetup();

        getServer().getPluginManager().registerEvents(this, this);

        Runnable();
    }

    // Listeners
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        if (GetTime() >= config.getInt("starttime") || GetTime() < config.getInt("endtime")){
            for (Player target : getServer().getOnlinePlayers()) {
                if (!target.isOp()){
                    target.kickPlayer(config.getString("curfewmessage"));
                }
            }
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
        config.addDefault("endtime", 730);
        config.addDefault("curfewmessage", "Sorry! The Server is currently closed please join again later!");

        config.options().copyDefaults(true);

        saveConfig();
    }

    public void Runnable() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Close Server
                if (GetTime() == config.getInt("starttime")) {
                    for (Player target : getServer().getOnlinePlayers()) {
                        target.kickPlayer(config.getString("curfewmessage"));
                    }
                }

                // Start countdown
                if (GetTime() == config.getInt("starttime") - 10 && !runTitle10) {
                    bossbar = true;
                    for (Player target : getServer().getOnlinePlayers()) {
                        target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 10 minutes", 3, 100, 3);
                    }
                    runTitle10 = true;
                }

                if (GetTime() >= config.getInt("starttime") - 10 && GetTime() <= config.getInt("starttime")) {
                    bossbar = true;
                }

                if (GetTime() == config.getInt("starttime") - 5 && !runTitle5) {
                    for (Player target : getServer().getOnlinePlayers()) {
                        target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 5 minutes", 3, 100, 3);
                    }
                    runTitle5 = true;
                }

                if (GetTime() == config.getInt("endtime")) {
                    runTitle10 = false;
                    runTitle5 = false;
                }

                if (bossbar) {
                    if (!barexists){
                        bar = Bukkit.createBossBar(ChatColor.RED + "Server Closing", BarColor.RED, BarStyle.SOLID);
                        bar.setVisible(true);
                        barexists=true;
                    } else if (!bar.isVisible()) {
                        bar.setVisible(true);
                    }
                    Integer timeLeft = config.getInt("starttime") - GetTime();
                    for (Player target : getServer().getOnlinePlayers()){
                        bar.addPlayer(target);
                    }
                    if (timeLeft > 0) {
                        bar.setProgress(timeLeft.doubleValue() / 10D);
                    } else if (timeLeft <= 0) {
                        bar.setVisible(false);
                        bossbar = false;
                    }

                }

            }
        }, 0L, 20L);
    }
}
