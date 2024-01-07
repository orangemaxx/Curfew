package hewin.max.curfew.Events;

import hewin.max.curfew.Curfew;
import hewin.max.curfew.Utils.Config;
import hewin.max.curfew.Utils.Time;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    Curfew curfew = new Curfew();
    Integer getTime = new Time().GetTime();
    Config configClass = new Config();

    FileConfiguration config = configClass.config;
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        if (getTime >= config.getInt("starttime") || getTime <= config.getInt("endtime")){
            for (Player target : curfew.getServer().getOnlinePlayers()) {
                if (!target.isOp()){
                    target.kickPlayer(config.getString("curfewmessage"));
                }
            }
        }
    }

}
