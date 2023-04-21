package me.usainsrht.ubans.command;

import me.usainsrht.ubans.Punishment;
import me.usainsrht.ubans.PunishmentManager;
import me.usainsrht.ubans.SQLCommands;
import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.gui.HistoryGUI;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HistoryCommand extends Command {

    public HistoryCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!sender.hasPermission("ubans.command.history")) {
            String noPerm = UBans.getInstance().getConfig().getString("messages.no_permission");
            MessageUtil.send(sender, MessageUtil.parseColor(noPerm));
            return false;
        }
        if (args.length == 0) {
            String usage = UBans.getInstance().getConfig().getString("messages.history.usage");
            MessageUtil.send(sender, MessageUtil.parseColor(usage));
            return false;
        }
        UUID uuid = UUIDUtil.getUUID(args[0]);
        //dediced to skip this check when i added active punishments to the history gui
        /*PunishmentManager pm = UBans.getInstance().getPunishmentManager();
        List<Punishment> punishments = pm.getPunishments(SQLCommands.SELECT_UUID_HISTORY, uuid.toString());
        if (punishments.isEmpty()) {
            String noRecords = UBans.getInstance().getConfig().getString("messages.history.no_history");
            noRecords = noRecords.replace("<player>", UUIDUtil.getName(uuid));
            MessageUtil.send(sender, MessageUtil.parseColor(noRecords));
            return false;
        }*/
        if (sender instanceof Player) {
            ((Player)sender).openInventory((new HistoryGUI(uuid, 1)).getInventory());
        }
        else {
            sender.sendMessage("&cYou must be a player to see GUI!");
        }
        return true;
    }
}
