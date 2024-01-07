package hewin.max.curfew.Events;

import hewin.max.curfew.Curfew;
import org.bukkit.Server;

public class RegisterEvents {

    Server getServer = new Curfew().getServer();

    public void RegisterEvents() {
        getServer.getPluginManager().registerEvents(new JoinListener(), new Curfew());
    }

}
