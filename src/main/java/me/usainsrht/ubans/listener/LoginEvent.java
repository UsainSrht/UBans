package me.usainsrht.ubans.listener;

import me.usainsrht.ubans.Punishment;
import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        Punishment punishment = UBans.getInstance().getPunishmentManager().getBan(e.getUniqueId());
        if (punishment != null) {

            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, MessageUtil.getBannedScreen(punishment));
        }
    }
}
