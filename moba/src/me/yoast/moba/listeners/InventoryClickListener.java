package me.yoast.moba.listeners;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.jline.console.Operation;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;
import net.minecraft.server.v1_8_R3.AttributeModifier;
import net.minecraft.server.v1_8_R3.GenericAttributes;

public class InventoryClickListener implements Listener{
	
	private Main plugin;
	
	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getInventory().getTitle();
		if(title.equals(Utils.chat("&f&lClasses"))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			e.getWhoClicked().closeInventory();
			setClassItems((Player) e.getWhoClicked(), e.getSlot(), this.plugin);
		}
		if(title.equals(Utils.chat("&f&lShop"))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			transaction(e);
			
		}
	}
	
	public void setClassItems(Player player, int slot, Main plugin) {
		List<String> xmlItems = XMLReader.getItems(slot);
		List<ItemStack> items = new ArrayList<ItemStack>();
		List<ItemStack> armor = new ArrayList<ItemStack>();
		for(int i=0; i<xmlItems.size(); i++) {
			String itemString = xmlItems.get(i);
			ItemStack item = new ItemStack(Material.getMaterial(itemString), 1);
			ItemMeta itemMeta = item.getItemMeta();
		    itemMeta.spigot().setUnbreakable(true);
		    item.setItemMeta(itemMeta);
			if(isArmor(item)) {
				armor.add(item);
			} else {
				items.add(item);
			}
		}
		this.plugin.getMobaPlayer(player).setClassItems(items);
		this.plugin.getMobaPlayer(player).setClassArmor(armor);
	}
	
	public boolean isArmor(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
            return true;
            }

        return false;
    }
	
	public ItemStack getSword(Player player) {
		ItemStack[] items = player.getInventory().getContents();
		List<ItemStack> swords = new ArrayList<ItemStack>();
		for(ItemStack item : items) {
			if(item != null) {
				if (item.getType().toString().endsWith("SWORD")) {
					swords.add(item);
				}
			}
		}
		return swords.get(0);
	}
	
	
	public void transaction(InventoryClickEvent e) {
		Player player = ((Player) e.getWhoClicked());
		MobaPlayer mobaPlayer = this.plugin.getMobaPlayer(player);
		int upgrade = e.getSlot();
		if(!subtractFunds(upgrade, mobaPlayer)) {
			Bukkit.broadcastMessage("Insufficient funds");
			return;
		}
		DecimalFormat df = new DecimalFormat("###.##");
		if(upgrade == 0) {
			double newDamage = Double.parseDouble(df.format(mobaPlayer.getDamage()+0.05));
			mobaPlayer.setDamage(newDamage);
			ItemStack sword = getSword((Player) e.getWhoClicked());
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setLore(Arrays.asList("+" + (int)((Double.parseDouble(df.format(newDamage - 1)))*100) + "% damage"));
			sword.setItemMeta(swordMeta);
			Bukkit.broadcastMessage(ChatColor.GREEN + "Damage Upgraded");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
		}
		if(upgrade == 1) {
			
			double newDefence = Double.parseDouble(df.format(mobaPlayer.getDefence()+0.05));
			mobaPlayer.setDefence(newDefence);
			ItemStack[] armor = player.getInventory().getArmorContents();
			for(ItemStack piece : armor) {
				ItemMeta pieceMeta = piece.getItemMeta();
				pieceMeta.setLore(Arrays.asList("+" + (int)((Double.parseDouble(df.format(newDefence - 1)))*100) + "% defence"));
				piece.setItemMeta(pieceMeta);
			}
			Bukkit.broadcastMessage(ChatColor.GREEN + "Defence Upgraded");
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
		}
		if(upgrade == 2) {
			
			double newSpeed = Double.parseDouble(df.format(mobaPlayer.getSpeed()+0.01));
			mobaPlayer.setSpeed(newSpeed);
			player.setWalkSpeed((float) mobaPlayer.getSpeed());
			Bukkit.broadcastMessage(ChatColor.GREEN + "Speed Upgraded");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
		}
	}

	private boolean subtractFunds(int upgrade, MobaPlayer mobaPlayer) {
		List<String[]> shopArray = XMLReader.getShop();
		int cost = Integer.parseInt(shopArray.get(upgrade)[3]);
		int playerGold = mobaPlayer.getGold();
		if (cost > playerGold) {
			return false;
		}
		else {
			mobaPlayer.setGold(playerGold - cost);
			return true;
		}
	}
}
