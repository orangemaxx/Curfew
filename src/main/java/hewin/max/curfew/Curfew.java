package hewin.max.curfew;



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

    Boolean runTitle10 = false;
    Boolean runTitle5 = false;
    Boolean barexists = false;

    BossBar bar;

    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {

        Integer bstatsId = 20673;
        Metrics metrics = new Metrics(this, bstatsId);
        ConfigSetup();
        getServer().getPluginManager().registerEvents(this, this);
        
        CountdownTasks();
        System.out.println("Plugin enabled! Current time is: " + GetTime());
    }

    public void ConfigSetup() {
        config.addDefault("starttime", 2130);
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
        BukkitScheduler scheduler = new Curfew().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(new Curfew(), new Runnable() {
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
                    for (Player target : getServer().getOnlinePlayers()) {
                        target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 10 minutes", 3, 100, 3);
                    }
                    runTitle10 = true;
                }

                if (GetTime() >= config.getInt("starttime") - 10 && GetTime() <= config.getInt("starttime") && !barexists) {
                    bar = Bukkit.createBossBar(ChatColor.RED + "Server Closing", BarColor.RED, BarStyle.SOLID);
                    bar.setVisible(true);
                    barexists = true;
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

                if (barexists) {
                    Integer timeLeft = config.getInt("starttime") - GetTime();
                    for (Player target : getServer().getOnlinePlayers()){
                        if (!bar.getPlayers().contains(target)){bar.addPlayer(target);}
                        continue;
                    }
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


    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        if (GetTime() >= config.getInt("starttime") || GetTime() <= config.getInt("endtime")){
            for (Player target : getServer().getOnlinePlayers()) {
                if (!target.isOp()){
                    target.kickPlayer(config.getString("curfewmessage"));
                }
            }
        }
    }
}