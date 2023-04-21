package me.usainsrht.ubans.gui;

import me.usainsrht.ubans.*;
import me.usainsrht.ubans.util.DurationUtil;
import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HistoryGUI {
    private UUID uuid;
    private String name;
    private int page;
    private Inventory inventory;
    private boolean hasNextPage;

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
        int limit = 45;
        int offset = ((page - 1) * 45);
        PunishmentManager pm = UBans.getInstance().getPunishmentManager();
        List<Punishment> activePunishments = pm.getPunishments(SQLCommands.SELECT_UUID_PUNISHMENTS_WITH_LIMIT_AND_OFFSET, uuid.toString(), limit, offset);
        limit -= activePunishments.size();
        offset -= activePunishments.size();
        if (offset < 0) offset = 0;
        List<Punishment> punishments = pm.getPunishments(SQLCommands.SELECT_UUID_HISTORY_WITH_LIMIT_AND_OFFSET, uuid.toString(), limit, offset);
        if (pm.getPunishmentsCount(uuid) + pm.getHistoryCount(uuid) > page * 45) {
            hasNextPage = true;
        }
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
                lore.add(MessageUtil.parseColor("&7duration left: &6" + DurationUtil.getDurationAsString(temporaryPunishment.getEnd() - System.currentTimeMillis())));
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

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public boolean hasPreviousPage() {
        return page > 1;
    }

    @Override
    public String toString() {
        return uuid + "," + page;
    }

    public static HistoryGUI getFromString(String string) {
        String[] strings = string.split(",");
        return new HistoryGUI(UUID.fromString(strings[0]), Integer.parseInt(strings[1]));
    }

    public static void open(UUID uuid, UUID target, int page) {
        HistoryGUI historyGUI = new HistoryGUI(target, page);
        Player player = Bukkit.getPlayer(uuid);
        player.setMetadata("ubans-historygui", new FixedMetadataValue(UBans.getInstance(), historyGUI.toString()));
        player.openInventory(historyGUI.getInventory());
    }

    public static void nextPage(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        HistoryGUI historyGUI = HistoryGUI.getFromString(player.getMetadata("ubans-historygui").get(0).asString());
        if (!historyGUI.hasNextPage()) return;
        int page = historyGUI.getPage();
        open(uuid, historyGUI.getUuid(), page + 1);
    }

    public static void previousPage(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        HistoryGUI historyGUI = HistoryGUI.getFromString(player.getMetadata("ubans-historygui").get(0).asString());
        if (!historyGUI.hasPreviousPage()) return;
        int page = historyGUI.getPage();
        open(uuid, historyGUI.getUuid(), page - 1);
    }

}
