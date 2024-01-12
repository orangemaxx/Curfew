package ml.maxht.curfew;



import ml.maxht.curfew.Commands.TimeCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Curfew extends JavaPlugin implements Listener {

    Boolean runTitle10 = false;
    Boolean runTitle5 = false;
    Boolean barexists = false;

    BossBar bar;

    public FileConfiguration config = getConfig();

    TimeCommand timeCommand = new TimeCommand();

    @Override
    public void onEnable() {

        Integer bstatsId = 20673;
        Metrics metrics = new Metrics(this, bstatsId);
        ConfigSetup();
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("time").setExecutor(timeCommand);

        CountdownTasks();
        getLogger().info("Plugin enabled! Current time is: " + GetTime());
    }

    @Override
    public void onDisable() {
        for (Player target : getServer().getOnlinePlayers()) {
            if (bar != null && barexists){bar.removePlayer(target);}
            continue;
        }
        if (bar != null){bar.removeAll();}
        getLogger().info("Curfew Offline");
    }

    public void ConfigSetup() {
        config.addDefault("starttime", 2100);
        config.addDefault("endtime", 730);
        config.addDefault("curfewmessage", "Sorry! The Server is currently closed please join again later!");

        config.options().copyDefaults(true);

        saveConfig();
    }


    public Integer GetTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer time = Integer.valueOf(format.format(date).replace(":", ""));
        return time;
    }



    public void CountdownTasks() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Close Server
                if (GetTime() == config.getInt("starttime")) {
                    for (Player target : getServer().getOnlinePlayers()) {
                        target.kickPlayer(config.getString("curfewmessage"));
                        continue;
                    }
                }

                // Start countdown
                if (GetTime() == config.getInt("starttime") - 10 && !runTitle10) {
                    Title10();
                    runTitle10 = true;
                }

                if (GetTime() >= config.getInt("starttime") - 10 && GetTime() <= config.getInt("starttime") && !barexists) {
                    bar = Bukkit.createBossBar(ChatColor.RED + "Server Closing", BarColor.RED, BarStyle.SOLID);
                    bar.setVisible(true);
                    barexists = true;
                }

                if (GetTime() == config.getInt("starttime") - 5 && !runTitle5) {
                    Title5();
                    runTitle5 = true;
                }

                if (GetTime() == config.getInt("endtime")) {
                    runTitle10 = false;
                    runTitle5 = false;
                }

                if (barexists) {
                    Integer timeLeft = config.getInt("starttime") - GetTime();
                    AddToBar();
                    if (timeLeft > 0) {
                        bar.setProgress(timeLeft.doubleValue() / 10D);
                    } else if (timeLeft <= 0) {
                        bar.removeAll();
                        barexists = false;
                        runTitle10 = false;
                        runTitle5 = false;
                    }

                }

            }
        }, 0L, 20L);
    }

    void AddToBar() {
        for (Player target : getServer().getOnlinePlayers()){
            if (!bar.getPlayers().contains(target)){bar.addPlayer(target);}
            continue;
        }
    }

    void Title5() {
        for (Player target : getServer().getOnlinePlayers()) {
            target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 5 minutes", 3, 100, 3);
            continue;
        }
    }

    void Title10(){
        for (Player target : getServer().getOnlinePlayers()) {
            target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 10 minutes", 3, 100, 3);
            continue;
        }
    }



    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent e) {
        if (GetTime() >= config.getInt("starttime") || GetTime() <= config.getInt("endtime")){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The Server is currently closed. \n" + ChatColor.GREEN + "Please try again later. \n " + ChatColor.RESET + "Please contact a server admin if this is a mistake :).");
        }

    }
}