package me.usainsrht.ubans.command;

import me.usainsrht.ubans.PunishmentManager;
import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.DurationUtil;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BanCommand extends Command {

    public BanCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!sender.hasPermission("ubans.command.ban")) {
            String noPerm = UBans.getInstance().getConfig().getString("messages.no_permission");
            MessageUtil.send(sender, MessageUtil.parseColor(noPerm));
            return false;
        }
        if (args.length == 0) {
            String usage = UBans.getInstance().getConfig().getString("messages.ban.usage");
            MessageUtil.send(sender, MessageUtil.parseColor(usage));
            return false;
        }
        UUID uuid = UUIDUtil.getUUID(args[0]);
        OfflinePlayer targetPlayer = UUIDUtil.getOfflinePlayer(uuid);
        if (targetPlayer.isOnline()) {
            if (((Permissible)targetPlayer).hasPermission("ubans.command.ban")) {
                String unbanable = UBans.getInstance().getConfig().getString("messages.unbanable");
                MessageUtil.send(sender, MessageUtil.parseColor(unbanable));
                return false;
            }
        }
        long duration = 0;
        String reason = "";
        if (args.length > 1) {
            duration = DurationUtil.getDurationAsMilliseconds(args[1]);
            if (duration > 0) {
                if (args.length > 2) {
                    //duration and reason are both provided
                    reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                }
            }
            else {
                //duration invalid, count all args as reason
                reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            }
        }
        UUID staff = UUID.fromString("00000000-0000-0000-0000-000000000000");
        if (sender instanceof Player) {
            staff = UUIDUtil.getUUID(sender.getName());
        }
        if (duration > 0) {
            UBans.getInstance().getPunishmentManager().tempBan(uuid, staff, reason, duration);
        }
        else {
            UBans.getInstance().getPunishmentManager().ban(uuid, staff, reason);
        }
        return true;
    }
}