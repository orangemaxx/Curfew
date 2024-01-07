package hewin.max.curfew.Utils;

import hewin.max.curfew.Curfew;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    Curfew curfew = new Curfew();

    public FileConfiguration config = curfew.getConfig();

    public void ConfigSetup() {
        config.addDefault("starttime", 2130);
        config.addDefault("endtime", 730);
        config.addDefault("curfewmessage", "Sorry! The Server is currently closed please join again later!");

        config.options().copyDefaults(true);

        curfew.saveConfig();
    }

}
