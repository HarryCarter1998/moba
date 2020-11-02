package me.yoast.moba.utils;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WaveSeperator extends BukkitRunnable {

	private final JavaPlugin plugin;
	private int counter;
	private CraftWorld world = null;
	
	public WaveSeperator(JavaPlugin plugin, int counter) {
		this.plugin = plugin;
		this.counter = counter;
	}

	@Override
	public void run() {
		if (counter > 0) {
			BukkitTask task = new CreepSpawner(this.plugin, 2).runTaskTimer(this.plugin, 0, 10);
			counter--;
		} else {
			this.cancel();
		}
	}
	
}
