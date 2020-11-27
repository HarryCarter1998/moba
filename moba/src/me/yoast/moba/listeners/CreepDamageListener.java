package me.yoast.moba.listeners;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Creep.Team;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityPlayer;

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
		CreepDamageListener thisListener = this;
		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity damaged = (LivingEntity) e.getEntity();
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) e;
				if(entityEvent.getDamager() instanceof CraftPlayer) { 
					CraftPlayer damager = (CraftPlayer) entityEvent.getDamager();
					List<MobaPlayer> mobaPlayers = thisListener.plugin.getMobaPlayers();
					for(MobaPlayer mobaPlayer : mobaPlayers) {
						if (mobaPlayer.getPlayer().equals(damager)) {
							if (e.getEntity() instanceof CraftZombie) {
								Creep damagedEntity = (Creep) ((CraftZombie)e.getEntity()).getHandle();
								if(mobaPlayer.getTeam().toString() == damagedEntity.getTeam().toString()) {
									e.setCancelled(true);
									
								}
							}
							if (e.getEntity() instanceof CraftMagmaCube) {
								Tower damagedEntity = (Tower) ((CraftMagmaCube)e.getEntity()).getHandle();
								if(mobaPlayer.getTeam().toString() == damagedEntity.getTeam().toString()) {
									e.setCancelled(true);
									
								}
							}
						}
					}
				}
			}
			// Uses a scheduler with a 2 tick delay to ensure we get the health POST damage.
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					double currentHP = damaged.getHealth();
					String currentString = df.format(Double.max(0, currentHP));
					String maxHP = df.format(damaged.getMaxHealth());
					String health = currentString + "/" + maxHP;
					// If it's a CraftZombie (aka a Creep) cast it to Creep
					if (e.getEntity() instanceof CraftZombie) {
						
						Creep damagedCreep = (Creep) ((CraftZombie)e.getEntity()).getHandle();
						
						if (damagedCreep.getTeam().equals(Team.RED)) {
							e.getEntity().setCustomName(ChatColor.RED + health);
						} else {
							e.getEntity().setCustomName(ChatColor.BLUE + health);
						}
					}
					if (e.getEntity() instanceof CraftMagmaCube) {
						e.getEntity().setVelocity(new Vector(0,0,0));
						Tower damagedTower = (Tower) ((CraftMagmaCube)e.getEntity()).getHandle();
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