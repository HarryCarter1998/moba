package me.yoast.moba.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import me.yoast.moba.Main;
import me.yoast.moba.listeners.ClickItemListener;
import me.yoast.moba.mobs.EntityTypes;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.MobaPlayer.Team;
import me.yoast.moba.mobs.Tower;
import me.yoast.moba.utils.WaveSeperator;
import net.md_5.bungee.api.ChatColor;


public class SpawnMobsCommand implements CommandExecutor{
	
	private Main plugin;
	private List<MobaPlayer> mobaPlayers;
	private CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
	
	public SpawnMobsCommand(Main plugin, ClickItemListener clickItemListener) {
		this.plugin = plugin;
		this.mobaPlayers = clickItemListener.getMobaPlayers();
		plugin.getCommand("start").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(this.mobaPlayers.size()<1) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "No players are ready, pick a team before starting");
			return false;
		}
		spawnPlayers();
		spawnMobs();
		
		return false;
	}
	
	public void spawnMobs(){
		Tower towerRed1 = new Tower(Tower.Team.RED, world, this.plugin);
		EntityTypes.spawnEntity(towerRed1, new Location(world, -59.5, 7, -419.5));
		Tower towerRed2 = new Tower(Tower.Team.RED, world, this.plugin);
		EntityTypes.spawnEntity(towerRed2, new Location(world, -49.5, 7, -419.5));
		Tower towerBlue1 = new Tower(Tower.Team.BLUE, world, this.plugin);
		EntityTypes.spawnEntity(towerBlue1, new Location(world, -72.5, 7, -419.5));
		Tower towerBlue2 = new Tower(Tower.Team.BLUE, world, this.plugin);
		EntityTypes.spawnEntity(towerBlue2, new Location(world, -83.5, 7, -419.5));
		
		new WaveSeperator(this.plugin, 10).runTaskTimer(this.plugin, 0, 200);
	}
	
	public void spawnPlayers() {
		
		Location redSpawn = new Location(world, -60, 4, -420);
		Location blueSpawn = new Location(world, -84, 4, -420);
		for(int i = 0; i<this.mobaPlayers.size(); i++) {
			MobaPlayer mobaPlayer = this.mobaPlayers.get(i);
			Player player = mobaPlayer.getPlayer();
			if(mobaPlayer.getTeam() == Team.RED) {
				player.teleport(redSpawn);
			}else {
				player.teleport(blueSpawn);
			}
		}
	}
}
