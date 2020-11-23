package me.yoast.moba.listeners;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Creep.Team;
import me.yoast.moba.mobs.Tower;
import me.yoast.moba.ui.InventoryUI;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityZombie;

public class CreepDamageListener implements Listener{
	
	private Main plugin;
	private DecimalFormat df = new DecimalFormat("#");
	
	public CreepDamageListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageEvent e) {
		// Check the entity is a LivingEntity. If it is, cast it to a LivingEntity
		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity damaged = (LivingEntity) e.getEntity();
			// Uses a scheduler with a 2 tick delay to ensure we get the health POST damage.
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					double currentHP = damaged.getHealth();
					String currentString = df.format(Double.max(0, currentHP));
					String maxHP = df.format(damaged.getMaxHealth());
					String health = currentString + "/" + maxHP;
					// If it's a CraftZombie (aka a Creep) cast it to Creep
					if (e.getEntity() instanceof CraftZombie) {
						e.getEntity().setVelocity(new Vector(0,0,0));
						Creep damagedCreep = (Creep) ((CraftZombie)e.getEntity()).getHandle();
						if (damagedCreep.getTeam().equals(Team.RED)) {
							e.getEntity().setCustomName(ChatColor.RED + health);
						} else {
							e.getEntity().setCustomName(ChatColor.BLUE + health);
						}
					}
					if (e.getEntity() instanceof CraftSkeleton) {
						//e.getEntity().setVelocity(new Vector(0,0,0));
						Tower damagedTower = (Tower) ((CraftSkeleton)e.getEntity()).getHandle();
						if (damagedTower.getTeam().toString().equals("RED")) {
							e.getEntity().setCustomName(ChatColor.RED + health);
						} else {
							e.getEntity().setCustomName(ChatColor.BLUE + health);
						}
					}
				}
			}, 1);
		}
	}
}