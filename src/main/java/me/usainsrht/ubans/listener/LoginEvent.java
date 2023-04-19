package me.usainsrht.ubans.listener;

import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        if (UBans.getInstance().getPunishmentManager().isBanned(e.getUniqueId())) {
            String message = UBans.getInstance().getConfig().getString("messages.ban_screen");
            message = MessageUtil.parseColor(message);
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
        }
    }
}
