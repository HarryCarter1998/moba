package me.yoast.moba.utils;

import java.util.ArrayList;
import java.util.List;
import me.yoast.moba.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.EntityTypes;

public class CreepSpawner extends BukkitRunnable {

	private final Main plugin;
	private final List<Location> blueSpawns = new ArrayList<>();
	private final List<Location> redSpawns = new ArrayList<>();
	private int counter;
	private CraftWorld world = null;
	
	public CreepSpawner(Main plugin, int counter) {
		this.plugin = plugin;
		this.counter = counter;
		this.world = (CraftWorld) Bukkit.getWorld("world_1602090282");
//		blueSpawns.add(new Location(world, -82, 10, -337));
//		redSpawns.add(new Location(world, 37, 10, -337));
		blueSpawns.add(new Location(world, -90, 4, -419.5));
		redSpawns.add(new Location(world, -42, 4, -419.5));
	}

	@Override
	public void run() {
		if (counter > 0) {
			// Change to a BukkitRunnable that calls another BukkitRunnable
				for(Location loc : redSpawns) {
					EntityTypes.spawnEntity(new Creep(Creep.Team.RED, this.world, this.plugin), loc);
				}
				for(Location loc : blueSpawns) {
					EntityTypes.spawnEntity(new Creep(Creep.Team.BLUE, this.world, this.plugin), loc);
				}
			counter--;
		} else {
			this.cancel();
		}
	}
}
