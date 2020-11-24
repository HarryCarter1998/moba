package me.yoast.moba.commands;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.util.Vector;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.EntityTypes;
import me.yoast.moba.mobs.Tower;
import me.yoast.moba.utils.Turret;
import me.yoast.moba.utils.WaveSeperator;
import net.minecraft.server.v1_8_R3.WorldServer;


public class SpawnMobsCommand implements CommandExecutor{
	
	private Main plugin;
	
	public SpawnMobsCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("spawnmobs").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
		
		

		Tower towerRed1 = new Tower(Tower.Team.RED, world, this.plugin);
		EntityTypes.spawnEntity(towerRed1, new Location(world, -59.5, 7, -419.5));
		Tower towerRed2 = new Tower(Tower.Team.RED, world, this.plugin);
		EntityTypes.spawnEntity(towerRed2, new Location(world, -49.5, 7, -419.5));
		Tower towerBlue1 = new Tower(Tower.Team.BLUE, world, this.plugin);
		EntityTypes.spawnEntity(towerBlue1, new Location(world, -72.5, 7, -419.5));
		Tower towerBlue2 = new Tower(Tower.Team.BLUE, world, this.plugin);
		EntityTypes.spawnEntity(towerBlue2, new Location(world, -83.5, 7, -419.5));
		
		new WaveSeperator(this.plugin, 10).runTaskTimer(this.plugin, 0, 200);
		
		
		
		
		
		return false;
	}
}
