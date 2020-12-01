package me.yoast.moba.listeners;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import org.bukkit.Location;
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
	private CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
	private CreepDamageListener creepDamageListener;
	
	public CreepDamageListener(Main plugin) {
		creepDamageListener = this;
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent e) {
		// Check the entity is a LivingEntity. If it is, cast it to a LivingEntity
		
			Entity damaged = e.getEntity();
			Player damager;
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				damager = (Player) e.getDamager();
			}
			else {
				Projectile proj = (Projectile) e.getDamager();
				damager = (Player) proj.getShooter();
				
			}				    
		    MobaPlayer mobaPlayer = this.plugin.getMobaPlayer(damager);
			if (damaged instanceof CraftZombie) {
				Creep damagedCreep = (Creep) ((CraftZombie) damaged).getHandle();
				if(mobaPlayer.getTeam().toString() == damagedCreep.getTeam().toString()) {
					e.setCancelled(true);
				}
			}
			if (damaged instanceof CraftMagmaCube) {
				Tower damagedMagma = (Tower) ((CraftMagmaCube) damaged).getHandle();
				if(mobaPlayer.getTeam().toString() == damagedMagma.getTeam().toString()) {
					e.setCancelled(true);
				}
			}
			if (damaged instanceof CraftPlayer) {
				if(mobaPlayer.getTeam() == this.plugin.getMobaPlayer((Player) damaged).getTeam()) {
					e.setCancelled(true);
				}
				if(e.getFinalDamage() > ((CraftPlayer) damaged).getHealth()) {
					e.setCancelled(true);
					Bukkit.broadcastMessage(damaged.getName() + " was killed by " + damager.getName());
					damaged.teleport(new Location(this.world, -40, 40, -577));
					((CraftPlayer) damaged).setGameMode(GameMode.SPECTATOR);
					MobaPlayer damagedMobaPlayer = this.plugin.getMobaPlayer((Player) damaged);
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							if (damagedMobaPlayer.getTeam().toString() == "RED"){
								damaged.teleport(new Location(world, 28.5, 21, -577.5));
							}else {
								damaged.teleport(new Location(world, -91.5, 21, -577.5));
							}
							
							((CraftPlayer) damaged).setHealth(20);
							((CraftPlayer) damaged).setGameMode(GameMode.SURVIVAL);
						}
					}, 400);
				}
			}
				
			
			// Uses a scheduler with a 1 tick delay to ensure we get the health POST damage.
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					double currentHP = ((CraftLivingEntity) damaged).getHealth();
					String currentString = df.format(Double.max(0, currentHP));
					String maxHP = df.format(((CraftLivingEntity) damaged).getMaxHealth());
					String health = currentString + "/" + maxHP;
					// If it's a CraftZombie (aka a Creep) cast it to Creep
					if (e.getEntity() instanceof CraftZombie) {
						
						Creep damagedCreep = (Creep) ((CraftZombie)damaged).getHandle();
						
						if (damagedCreep.getTeam().equals(Team.RED)) {
							damaged.setCustomName(ChatColor.RED + health);
						} else {
							damaged.setCustomName(ChatColor.BLUE + health);
						}
					}
					if (e.getEntity() instanceof CraftMagmaCube) {
						e.getEntity().setVelocity(new Vector(0,0,0));
						Tower damagedTower = (Tower) ((CraftMagmaCube)damaged).getHandle();
						if (damagedTower.getTeam().toString().equals("RED")) {
							damaged.setCustomName(ChatColor.RED + health);
						} else {
							damaged.setCustomName(ChatColor.BLUE + health);
						}
					}
				}
			}, 1);
		}
	
}