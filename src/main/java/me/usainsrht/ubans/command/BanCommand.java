package me.usainsrht.ubans.command;

import me.usainsrht.ubans.PunishmentManager;
import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.DurationUtil;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BanCommand extends Command {

    public BanCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, String[] args) {
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
        long duration = 0;
        String reason = "";
        if (args.length > 1) {
            duration = DurationUtil.getDurationAsMilliseconds(args[1]);
            if (duration < 1) {
                String usage = UBans.getInstance().getConfig().getString("messages.invalid_duration");
                MessageUtil.send(sender, MessageUtil.parseColor(usage));
                return false;
            }
            if (args.length > 2) {
                reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            }
        }
        UUID staff = UUID.fromString("0");
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