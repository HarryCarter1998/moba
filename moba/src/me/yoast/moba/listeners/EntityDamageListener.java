package me.yoast.moba.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Creep.Team;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Nexus;
import me.yoast.moba.mobs.Tower;
import net.md_5.bungee.api.ChatColor;

public class EntityDamageListener implements Listener{
	
	private Main plugin;
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
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				return;
			}
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				if(((EntityDamageByEntityEvent) e).getDamager() instanceof CraftMagmaCube) {
					e.setCancelled(true);
					return;
				}
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
				Bukkit.broadcastMessage(String.valueOf(e.getDamage()));
				e.setDamage(checkWeaponBonuses((EntityDamageByEntityEvent) e));
				Bukkit.broadcastMessage(String.valueOf(e.getDamage()));
				updateHealth(damaged);
			}
			if(damaged instanceof CraftMagmaCube) {
				
				CraftMagmaCube damagedMagma = (CraftMagmaCube) damaged;
				if(damagedMagma.getSize() == 2) {
					Tower damagedTower = (Tower) (damagedMagma.getHandle());
					if(mobaDamager.getTeam().toString() == damagedTower.getTeam().toString()) {
						e.setCancelled(true);
						return;
					}
				}
				else {
					Nexus damagedNexus = (Nexus) (damagedMagma.getHandle());
					if(mobaDamager.getTeam().toString() == damagedNexus.getTeam().toString()) {
						e.setCancelled(true);
						return;
					}
				}
				e.setDamage(checkWeaponBonuses((EntityDamageByEntityEvent) e));
				updateHealth(damaged);
			}
			
			if(damaged instanceof CraftPlayer) {
				Player damagedPlayer = (Player) damaged;
				MobaPlayer mobaDamaged = this.plugin.getMobaPlayer(damagedPlayer);
				e.setDamage(checkWeaponBonuses((EntityDamageByEntityEvent) e));
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
			this.plugin.getMobaPlayer(damager).setGold(this.plugin.getMobaPlayer(damager).getGold()+10);
			damager.sendMessage(ChatColor.GOLD + "+10 Gold");
		
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
				
				((CraftPlayer) damaged).setHealth(40);
				((CraftPlayer) damaged).setGameMode(GameMode.ADVENTURE);
			}
		}, 400);
	}
	
	public void updateHealth(Entity damaged) {
		// Uses a scheduler with a 1 tick delay to ensure we get the health POST damage.
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				double health = ((CraftLivingEntity) damaged).getHealth();
				double maxHealth = ((CraftLivingEntity) damaged).getMaxHealth();
				double percentageHealth = (health/maxHealth);
				if (damaged instanceof CraftZombie) {
					percentageHealth *= 25;
					String healthString = repeatString("|", (int) percentageHealth);
					String noHealthString = repeatString("|", 25 - ((int) percentageHealth));
					Creep damagedCreep = (Creep) ((CraftZombie)damaged).getHandle();
					if (damagedCreep.getTeam().equals(Team.RED)) {
						
						damaged.setCustomName(ChatColor.RED + healthString + ChatColor.GRAY + noHealthString);
					} else {
						damaged.setCustomName(ChatColor.BLUE + healthString + ChatColor.GRAY + noHealthString);
					}
				}
				if (damaged instanceof CraftMagmaCube) {
					percentageHealth *= 50;
					String healthString = repeatString("|", (int) percentageHealth);
					String noHealthString = repeatString("|", 50 - ((int) percentageHealth));
					CraftMagmaCube damagedMagma = (CraftMagmaCube) damaged;
					if(damagedMagma.getSize() == 2) {
						Tower damagedTower = (Tower) ((CraftMagmaCube)damaged).getHandle();
						if (damagedTower.getTeam().toString().equals("RED")) {
							damaged.setCustomName(ChatColor.RED + healthString + ChatColor.GRAY + noHealthString);
						} else {
							damaged.setCustomName(ChatColor.BLUE + healthString + ChatColor.GRAY + noHealthString);
						}
					}
					else {
						Nexus damagedNexus = (Nexus) ((CraftMagmaCube)damaged).getHandle();
						if (damagedNexus.getTeam().toString().equals("RED")) {
							damaged.setCustomName(ChatColor.RED + healthString + ChatColor.GRAY + noHealthString);
						} else {
							damaged.setCustomName(ChatColor.BLUE + healthString + ChatColor.GRAY + noHealthString);
						}
					}
				}
			}
		}, 1);
	}
	
	public String repeatString(String string, int n) {
		String healthString = "";
		for (int i = 0; i < n; ++i) {
		    healthString += string;
		}
		return healthString;
	}
	
	public double checkWeaponBonuses(EntityDamageByEntityEvent e) {
		ItemStack sword = ((Player) e.getDamager()).getItemInHand();
		ItemMeta swordMeta = sword.getItemMeta();
		List<String> swordLore = swordMeta.getLore();
		if(swordLore == null) {
			return e.getDamage();
		}
		if(swordLore.get(0).equals("+5% damage")) {
			return e.getDamage()+e.getDamage()/20;
		}
		else {
			return e.getDamage();
		}
		
	}
}