package me.yoast.moba.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;

public class SetupCommand implements CommandExecutor{
	
	private Main plugin;
	private List<Player> players;
	ItemStack goldenSword = new ItemStack(Material.GOLD_SWORD,1);
	ItemStack redWool = new ItemStack(Material.WOOL,1);
	ItemStack blueWool = new ItemStack(Material.WOOL,1);
	
	
	public SetupCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("setup").setExecutor(this);
		redWool.setDurability((short) 14);
		blueWool.setDurability((short) 11);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		this.players = new ArrayList<>(Bukkit.getOnlinePlayers());
		for(Player player : players) {
			player.getInventory().clear();
			player.getInventory().addItem(goldenSword);
			player.getInventory().addItem(redWool);
			player.getInventory().addItem(blueWool);
			this.plugin.addMobaPlayer(new MobaPlayer(player));
			((CraftPlayer) player).setGameMode(GameMode.ADVENTURE);
		}
		
		return false;
	}

}
