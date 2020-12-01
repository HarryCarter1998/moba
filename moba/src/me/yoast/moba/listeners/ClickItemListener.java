package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.MobaPlayer.Team;
import me.yoast.moba.ui.ClassSelector;
import net.md_5.bungee.api.ChatColor;

public class ClickItemListener implements Listener{
	
	
	private Main plugin;
	
	public ClickItemListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
	    ItemStack item = player.getItemInHand();
		if(item.getType() == Material.GOLD_SWORD) {
			new ClassSelector(player, this.plugin);
		}
		MobaPlayer mobaPlayer = this.plugin.getMobaPlayer(player);
		if(item.getType() == Material.WOOL) {
			removeIfAlreadyOnTeam(player, item);
			if(item.getDurability() == 14) {
				mobaPlayer.setTeam(Team.RED);
				Bukkit.broadcastMessage(player.getDisplayName() + " has joined" + ChatColor.RED + " red team");
			}
			else {
				mobaPlayer.setTeam(Team.BLUE);
				Bukkit.broadcastMessage(player.getDisplayName() + " has joined" + ChatColor.BLUE + " blue team");
			}
			
			ItemStack changedItem = player.getItemInHand();
			addGlow(changedItem);
		}
		
	}
	
	
	public void addGlow(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}
	
	public void replaceWool(Player player, ItemStack item) {
		ItemStack goldenSword = new ItemStack(Material.GOLD_SWORD,1);
		ItemStack redWool = new ItemStack(Material.WOOL,1);
		redWool.setDurability((short) 14);
		ItemStack blueWool = new ItemStack(Material.WOOL,1);
		blueWool.setDurability((short) 11);
		player.getInventory().clear();
		player.getInventory().addItem(goldenSword);
		player.getInventory().addItem(redWool);
		player.getInventory().addItem(blueWool);
		if(item.getDurability() == 14) {
			addGlow(redWool);
		}else {
			addGlow(blueWool);
		}
	}
	
	public void removeIfAlreadyOnTeam(Player player, ItemStack item) {
		MobaPlayer mobaPlayer = this.plugin.getMobaPlayer(player);
		if(mobaPlayer != null) {
			replaceWool(player, item);
		}
	}


}
