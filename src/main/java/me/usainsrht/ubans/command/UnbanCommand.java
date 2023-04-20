package me.usainsrht.ubans.command;

import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class UnbanCommand extends Command {
    public UnbanCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!sender.hasPermission("ubans.command.unban")) {
            String noPerm = UBans.getInstance().getConfig().getString("messages.no_permission");
            MessageUtil.send(sender, MessageUtil.parseColor(noPerm));
            return false;
        }
        if (args.length == 0) {
            String usage = UBans.getInstance().getConfig().getString("messages.unban.usage");
            MessageUtil.send(sender, MessageUtil.parseColor(usage));
            return false;
        }
        UUID uuid = UUIDUtil.getUUID(args[0]);
        UBans.getInstance().getPunishmentManager().unban(uuid, UUIDUtil.getUUID(sender.getName()));
        return true;
    }
}
