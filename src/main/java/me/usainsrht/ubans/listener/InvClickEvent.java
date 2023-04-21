package me.usainsrht.ubans.listener;

import me.usainsrht.ubans.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getTitle().startsWith(MessageUtil.parseColor("&cHistory"))) {
            e.setCancelled(true);
            if (e.getSlot() == 53) {
                e.getWhoClicked().sendMessage("next page");
            }
            else if (e.getSlot() == 45) {
                e.getWhoClicked().sendMessage("previous page");
            }
        }
    }
}
