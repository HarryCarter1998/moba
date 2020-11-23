package me.yoast.moba.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Guardian;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.EntityTypes;
import me.yoast.moba.mobs.Tower;
import me.yoast.moba.utils.WaveSeperator;


public class SpawnMobsCommand implements CommandExecutor{
	
	private Main plugin;
	
	public SpawnMobsCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("spawnmobs").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
		Tower towerBlue = new Tower(Tower.Team.BLUE, world);
		EntityTypes.spawnEntity(towerBlue, new Location(world, -59, 4, -420));
		Tower towerRed = new Tower(Tower.Team.RED, world);
		EntityTypes.spawnEntity(towerRed, new Location(world, -60, 4, -420));
		new WaveSeperator(this.plugin, 2).runTaskTimer(this.plugin, 0, 1200);
		return false;
	}
}
