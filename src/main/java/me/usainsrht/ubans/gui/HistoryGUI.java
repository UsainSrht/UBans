package me.usainsrht.ubans.gui;

import me.usainsrht.ubans.Punishment;
import me.usainsrht.ubans.SQLCommands;
import me.usainsrht.ubans.TemporaryPunishment;
import me.usainsrht.ubans.UBans;
import me.usainsrht.ubans.util.DurationUtil;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.List;

public class HistoryGUI {
    private UUID uuid;
    private String name;
    private int page;
    private Inventory inventory;

    public HistoryGUI(UUID uuid, int page) {
        this.page = page;
        this.uuid = uuid;
        this.name = UUIDUtil.getName(uuid);
        String title = "&cHistory " + name + " Page " + page;
        title = MessageUtil.parseColor(title);
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(MessageUtil.parseColor("&e->"));
        nextPage.setItemMeta(nextMeta);
        inventory.setItem(53, nextPage);
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = nextPage.getItemMeta();
        previousMeta.setDisplayName(MessageUtil.parseColor("&e<-"));
        nextPage.setItemMeta(previousMeta);
        inventory.setItem(45, previousPage);
        List<Punishment> activePunishments = UBans.getInstance().getPunishmentManager().getPunishments(SQLCommands.SELECT_UUID_PUNISHMENTS, uuid.toString());
        List<Punishment> punishments = UBans.getInstance().getPunishmentManager().getPunishments(SQLCommands.SELECT_UUID_HISTORY, uuid.toString());
        int i = 0;
        for (Punishment punishment : activePunishments) {
            ItemStack item = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(MessageUtil.parseColor("&4&lACTIVE &cPunishment " + (punishments.size() - i)));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(MessageUtil.parseColor("&7type: &6" + punishment.getType()));
            lore.add(MessageUtil.parseColor("&7reason: &6" + punishment.getReason()));
            lore.add(MessageUtil.parseColor("&7staff: &6" + UUIDUtil.getName(punishment.getStaff())));
            lore.add(MessageUtil.parseColor("&7date: &6" + new Date(punishment.getStart())));
            if (punishment.isTemp()) {
                TemporaryPunishment temporaryPunishment = (TemporaryPunishment)punishment;
                lore.add(MessageUtil.parseColor("&7duration: &6" + DurationUtil.getDurationAsString(temporaryPunishment.getDuration())));
                lore.add(MessageUtil.parseColor("&7end: &6" + new Date(temporaryPunishment.getEnd())));
            }
            lore.add("");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            i++;
        }
        for (Punishment punishment : punishments) {
            ItemStack item = new ItemStack(Material.OBSIDIAN);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(MessageUtil.parseColor("&cPunishment " + (punishments.size() - i)));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(MessageUtil.parseColor("&7type: &6" + punishment.getType()));
            lore.add(MessageUtil.parseColor("&7reason: &6" + punishment.getReason()));
            lore.add(MessageUtil.parseColor("&7staff: &6" + UUIDUtil.getName(punishment.getStaff())));
            lore.add(MessageUtil.parseColor("&7date: &6" + new Date(punishment.getStart())));
            if (punishment.isTemp()) {
                TemporaryPunishment temporaryPunishment = (TemporaryPunishment)punishment;
                lore.add(MessageUtil.parseColor("&7duration: &6" + DurationUtil.getDurationAsString(temporaryPunishment.getDuration())));
                lore.add(MessageUtil.parseColor("&7end: &6" + new Date(temporaryPunishment.getEnd())));
                lore.add(MessageUtil.parseColor("&7duration left: &6" + DurationUtil.getDurationAsString(temporaryPunishment.getEnd() - System.currentTimeMillis())));
            }
            lore.add("");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            i++;
        }
        this.inventory = inventory;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getPage() {
        return page;
    }

}
