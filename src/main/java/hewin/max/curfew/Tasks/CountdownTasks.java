package hewin.max.curfew.Tasks;

import github.scarsz.discordsrv.util.DiscordUtil;
import hewin.max.curfew.Curfew;
import hewin.max.curfew.Utils.Config;
import hewin.max.curfew.Utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class CountdownTasks {

    Curfew curfew = new Curfew();
    Time time = new Time();
    FileConfiguration config = new Curfew().getConfig();

    public BossBar bar;
    Boolean runTitle10 = false;
    Boolean runTitle5 = false;
    Boolean barexists = false;

    public void CountdownTasks() {
        BukkitScheduler scheduler = curfew.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(new Curfew(), new Runnable() {
            @Override
            public void run() {
                // Close Server
                if (time.GetTime() == config.getInt("starttime")) {
                    for (Player target : curfew.getServer().getOnlinePlayers()) {
                        target.kickPlayer(config.getString("curfewmessage"));
                    }
                }

                // Start countdown
                if (time.GetTime() == config.getInt("starttime") - 10 && !runTitle10) {
                    for (Player target : curfew.getServer().getOnlinePlayers()) {
                        target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 10 minutes", 3, 100, 3);
                    }
                    runTitle10 = true;
                }

                if (time.GetTime() >= config.getInt("starttime") - 10 && time.GetTime() <= config.getInt("starttime") && !barexists) {
                    bar = Bukkit.createBossBar(ChatColor.RED + "Server Closing", BarColor.RED, BarStyle.SOLID);
                    bar.setVisible(true);
                    barexists = true;
                }

                if (time.GetTime() == config.getInt("starttime") - 5 && !runTitle5) {
                    for (Player target : curfew.getServer().getOnlinePlayers()) {
                        target.sendTitle(ChatColor.RED + "SERVER CLOSING", ChatColor.GREEN + "The server will close in 5 minutes", 3, 100, 3);
                    }
                    runTitle5 = true;
                }

                if (time.GetTime() == config.getInt("endtime")) {
                    runTitle10 = false;
                    runTitle5 = false;
                }

                if (barexists) {
                    Integer timeLeft = config.getInt("starttime") - time.GetTime();
                    for (Player target : curfew.getServer().getOnlinePlayers()){
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

}
