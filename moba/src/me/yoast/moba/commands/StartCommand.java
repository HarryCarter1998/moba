package me.yoast.moba.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.EntityTypes;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.MobaPlayer.Team;
import me.yoast.moba.mobs.Tower;
import me.yoast.moba.utils.WaveSeperator;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;


public class StartCommand implements CommandExecutor{
	
	private Main plugin;
	private List<MobaPlayer> mobaPlayers;
	private CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
	
	public StartCommand(Main plugin) {
		this.plugin = plugin;
		this.mobaPlayers = plugin.getMobaPlayers();
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
		Tower towerRed2 = new Tower(Tower.Team.RED, world, this.plugin);
		Tower towerBlue1 = new Tower(Tower.Team.BLUE, world, this.plugin);
		Tower towerBlue2 = new Tower(Tower.Team.BLUE, world, this.plugin);
		NBTTagCompound red1 = new NBTTagCompound();
        towerRed1.c(red1);
        red1.setBoolean("Silent", true);
		LivingEntity livRed1 = (LivingEntity) towerRed1.getBukkitEntity();
		LivingEntity livRed2 = (LivingEntity) towerRed2.getBukkitEntity();
		LivingEntity livBlue1 = (LivingEntity) towerBlue1.getBukkitEntity();
		LivingEntity livBlue2 = (LivingEntity) towerBlue2.getBukkitEntity();
		livRed1.setRemoveWhenFarAway(false);
		livRed2.setRemoveWhenFarAway(false);
		livBlue1.setRemoveWhenFarAway(false);
		livBlue2.setRemoveWhenFarAway(false);
		
		EntityTypes.spawnEntity(towerRed1, new Location(world, -9.5, 7, -419.5));
		EntityTypes.spawnEntity(towerRed2, new Location(world, 8.5, 7, -419.5));
		EntityTypes.spawnEntity(towerBlue1, new Location(world, -63.5, 7, -419.5));
		EntityTypes.spawnEntity(towerBlue2, new Location(world, -83.5, 7, -419.5));
		
//		EntityTypes.spawnEntity(towerRed1, new Location(world, -14, 23, -575));
//		EntityTypes.spawnEntity(towerRed2, new Location(world, 12, 23, -576));
//		EntityTypes.spawnEntity(towerBlue1, new Location(world, -50, 23, -575));
//		EntityTypes.spawnEntity(towerBlue2, new Location(world, -75, 23, -578));
		
		new WaveSeperator(this.plugin, 100).runTaskTimer(this.plugin, 0, 400);
	}
	
	public void spawnPlayers() {
		
		Location redSpawn = new Location(world, -11, 4, -420);
		Location blueSpawn = new Location(world, -84, 4, -420);
//		Location redSpawn = new Location(world, -0, 27, -582);
//		Location blueSpawn = new Location(world, -0, 27, -582);
		for(int i = 0; i<this.mobaPlayers.size(); i++) {
			MobaPlayer mobaPlayer = this.mobaPlayers.get(i);
			Player player = mobaPlayer.getPlayer();
			Location colorSpawn;
			if(mobaPlayer.getTeam() == Team.RED) {
				colorSpawn = redSpawn;
			}else {
				colorSpawn = blueSpawn;
			}
			player.teleport(colorSpawn);
			player.setBedSpawnLocation(colorSpawn, true);
		}
	}
}
