package me.yoast.moba.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.MobaPlayer.Team;
import me.yoast.moba.ui.InventoryUI;
import net.md_5.bungee.api.ChatColor;

public class ClickItemListener implements Listener{
	
	private List<MobaPlayer> mobaPlayers = new ArrayList<MobaPlayer>();
	
	public ClickItemListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
	    Action action = event.getAction();
	    ItemStack item = player.getItemInHand();
		if(item.getType() == Material.GOLD_SWORD) {
			player.openInventory(InventoryUI.classSelector(player));
		}
		MobaPlayer mobaPlayer = null;
		if(item.getType() == Material.WOOL) {
			if(item.getDurability() == 14) {
				mobaPlayer = new MobaPlayer(player, Team.RED); 
				Bukkit.broadcastMessage(player.getDisplayName() + " has joined" + ChatColor.RED + " red team");
			}
			else {
				mobaPlayer = new MobaPlayer(player, Team.BLUE);
				Bukkit.broadcastMessage(player.getDisplayName() + " has joined" + ChatColor.BLUE + " blue team");
			}
			//Bukkit.broadcastMessage(mobaPlayer.getTeam().toString());
			this.mobaPlayers.add(mobaPlayer);	
			//Bukkit.broadcastMessage(this.mobaPlayers.get(0).getTeam().toString());
			
			
		}
		
	}
	
	public List<MobaPlayer> getMobaPlayers(){
		//Bukkit.broadcastMessage(this.mobaPlayers.toString());
		return this.mobaPlayers;
	}

}
