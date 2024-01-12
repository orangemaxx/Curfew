package ml.maxht.curfew.Commands;

import ml.maxht.curfew.Curfew;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class TimeCommand implements CommandExecutor {
    Curfew curfew;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.curfew = curfew;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer time = Integer.valueOf(format.format(date).replace(":", ""));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("The current time is: " + time);
        }
        if (!(sender instanceof Player)) {
            curfew.getLogger().info("The current time is: " + time);
        }
        return true;
    }

}
