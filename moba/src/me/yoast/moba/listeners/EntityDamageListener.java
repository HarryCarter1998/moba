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

public class EntityDamageListener implements Listener{
	
	private Main plugin;
	private DecimalFormat df = new DecimalFormat("#");
	private CraftWorld world = (CraftWorld) Bukkit.getWorld("world_1602090282");
	
	public EntityDamageListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageEvent e) {
		
			Entity damaged = e.getEntity();
			Player damager = null;
			MobaPlayer mobaDamager = null;
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				damager = (Player) ((EntityDamageByEntityEvent) e).getDamager();
				mobaDamager = this.plugin.getMobaPlayer(damager);
			}
			else if(e.getCause() == DamageCause.PROJECTILE) {
				Projectile proj = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
				damager = (Player) proj.getShooter();
				mobaDamager = this.plugin.getMobaPlayer(damager);
			}
			if(mobaDamager == null) {
				return;
			}
			
			if(damaged instanceof CraftZombie) {
				Creep damagedCreep = (Creep) ((CraftZombie) damaged).getHandle();
				if(mobaDamager.getTeam().toString() == damagedCreep.getTeam().toString()) {
					e.setCancelled(true);
					return;
				}
				updateHealth(e, damaged);
			}
			if(damaged instanceof CraftMagmaCube) {
				Tower damagedMagma = (Tower) ((CraftMagmaCube) damaged).getHandle();
				if(mobaDamager.getTeam().toString() == damagedMagma.getTeam().toString()) {
					e.setCancelled(true);
					return;
				}
				updateHealth(e, damaged);
			}
			
			if(damaged instanceof CraftPlayer) {
				Player damagedPlayer = (Player) damaged;
				MobaPlayer mobaDamaged = this.plugin.getMobaPlayer(damagedPlayer);
				if(mobaDamager.getTeam() == mobaDamaged.getTeam()) {
					e.setCancelled(true);
					return;
				}
				if(e.getFinalDamage() > ((CraftPlayer) damaged).getHealth()) {
					e.setCancelled(true);
					killedByPlayer((EntityDamageByEntityEvent) e, damaged, damager);
					respawn(damaged);
				}
			}
		}
	
	public void killedByPlayer(EntityDamageByEntityEvent e, Entity damaged, Player damager) {
		
			Bukkit.broadcastMessage(damaged.getName() + " was killed by " + damager.getName());
			damaged.teleport(new Location(this.world, -40, 40, -577));
			((CraftPlayer) damaged).setGameMode(GameMode.SPECTATOR);
			
			
		
	}
	
	public void respawn(Entity damaged){
		MobaPlayer damagedMobaPlayer = this.plugin.getMobaPlayer((Player) damaged);
		this.plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (damagedMobaPlayer.getTeam().toString() == "RED"){
					damaged.teleport(new Location(world, 28.5, 21, -577.5));
				}else {
					damaged.teleport(new Location(world, -91.5, 21, -577.5));
				}
				
				((CraftPlayer) damaged).setHealth(20);
				((CraftPlayer) damaged).setGameMode(GameMode.ADVENTURE);
			}
		}, 400);
	}
	
	public void updateHealth(EntityDamageEvent e, Entity damaged) {
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