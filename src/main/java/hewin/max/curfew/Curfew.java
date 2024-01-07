package hewin.max.curfew;

import github.scarsz.discordsrv.DiscordSRV;
import hewin.max.curfew.DiscordSRV.DiscordSrvListener;
import hewin.max.curfew.Events.JoinListener;
import hewin.max.curfew.Events.RegisterEvents;
import hewin.max.curfew.Tasks.CountdownTasks;
import hewin.max.curfew.Utils.Config;
import hewin.max.curfew.Utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

public final class Curfew extends JavaPlugin {

    CountdownTasks countdownTasks = new CountdownTasks();
    Config configClass = new Config();
    RegisterEvents events = new RegisterEvents();
    Time time = new Time();
    public final DiscordSrvListener discordSrvListener = new DiscordSrvListener();

    BossBar bar = countdownTasks.bar;

    @Override
    public void onEnable() {

        Integer bstatsId = 20673;
        Metrics metrics = new Metrics(this, bstatsId);
        configClass.ConfigSetup();

        events.RegisterEvents();
        countdownTasks.CountdownTasks();
        System.out.println("Plugin enabled! Current time is: " + time.GetTime());
    }

    @Override
    public void onDisable(){
        bar.removeAll();
    }
}