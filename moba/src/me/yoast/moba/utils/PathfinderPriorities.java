package me.yoast.moba.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Navigation;

public class PathfinderPriorities extends BukkitRunnable {
	private Creep creep;
	private Navigation navigation;
	private Entity target;
	private CraftEntity craftEntity;
	private double minDistance = -1;
	private Main plugin;
	private List<MobaPlayer> mobaPlayers; 
	
	public PathfinderPriorities(Creep creep, Main plugin) {
		this.creep = creep;
		this.plugin = plugin;
		this.navigation = (Navigation) this.creep.getNavigation();
		this.target = null;
		this.craftEntity = this.creep.getBukkitEntity();
		this.mobaPlayers = this.plugin.getMobaPlayers();
		
	}
	
	
	@Override
	public void run() {
		if(!this.creep.isAlive()) {
			this.cancel();
		}
		List<Entity> nearbyEntities  = ((Entity) craftEntity).getNearbyEntities(200, 5, 200);
		
		List<MobaPlayer> nearbyMobaPlayers = new ArrayList<MobaPlayer>();
		List<CraftZombie> nearbyCreeps = new ArrayList<CraftZombie>();
		List<CraftMagmaCube> nearbyTowers = new ArrayList<CraftMagmaCube>();
		this.creep.getNavigation().n();
		
		for(Entity nearbyEntity : nearbyEntities) {
				if(nearbyEntity instanceof CraftZombie) {
					Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
					if (this.creep.getTeam() != nmsCreep.getTeam()) {
						Location thisCreepLocation = this.creep.getBukkitEntity().getLocation();
						Location targetCreepLocation = nmsCreep.getBukkitEntity().getLocation();
						if(thisCreepLocation.distance(targetCreepLocation) < 10) {
							nearbyCreeps.add((CraftZombie) nearbyEntity);
						}
					}
					
				}	
				
				if(nearbyEntity instanceof CraftMagmaCube) {
					if(((CraftMagmaCube) nearbyEntity).getSize() == 2) {
						Tower nmsTower = (Tower) ((CraftMagmaCube) nearbyEntity).getHandle();
						if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
							nearbyTowers.add((CraftMagmaCube) nearbyEntity);
						
						}
					}
				}
		}
		for(MobaPlayer mobaPlayer : mobaPlayers) {
			Location creepLocation = this.creep.getBukkitEntity().getLocation();
			Location playerLocation = mobaPlayer.getPlayer().getLocation();
			if(creepLocation.distance(playerLocation) < 10) {
				if (this.creep.getTeam().toString() != mobaPlayer.getTeam().toString()) {
					nearbyMobaPlayers.add(mobaPlayer);
				}
			}
		}
		if(this.craftEntity.getLocation().subtract(0, 3, 0).getBlock().getType() != Material.GOLD_BLOCK) {
			attackNearestEnemyTower(nearbyTowers);
		}
		else if (nearbyCreeps.size()>0) {
			attackNearestEnemyCreep(nearbyCreeps);
		}
		else if (nearbyMobaPlayers.size()>0) {
			attackNearestEnemyPlayer(nearbyMobaPlayers);
		}
		else {
			
			attackNearestEnemyTower(nearbyTowers);
		}
		
		if(this.target != null) {
			if(!this.target.isDead()) {
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
				CraftEntity craftTarget = (CraftEntity) this.target;
				this.navigation.a(craftTarget.getHandle(), 1.0D);
			}
		}	
		
		this.minDistance = -1;
		
	}
	
	public void attackNearestEnemyCreep(List<CraftZombie> nearbyCreeps) {
		
		for(Entity nearbyCreep : nearbyCreeps) {
			double distance = nearbyCreep.getLocation().distance(craftEntity.getLocation());
			if(this.minDistance == -1 || distance < this.minDistance) {
				this.target = nearbyCreep;
				this.minDistance = distance;
			}
		}
		LivingEntity livingCreep = (LivingEntity) this.creep.getBukkitEntity();
		if(this.minDistance<1.5) {
			livingCreep.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100));
		}else {
			livingCreep.getActivePotionEffects().clear();
		}
	}
	
	public void attackNearestEnemyTower(List<CraftMagmaCube> nearbyTowers) {
		
		for(Entity nearbyTower : nearbyTowers) {
			double distance = nearbyTower.getLocation().distance(craftEntity.getLocation());
			if(this.minDistance == -1 || distance < this.minDistance) {
				this.target = nearbyTower;
				this.minDistance = distance;
			}
		}
	}
	
	public void attackNearestEnemyPlayer(List<MobaPlayer> nearbyPlayers) {
		
		for(MobaPlayer nearbyMobaPlayer : nearbyPlayers) {
			Player nearbyPlayer = nearbyMobaPlayer.getPlayer();
			double distance = nearbyPlayer.getLocation().distance(craftEntity.getLocation());
			if(this.minDistance == -1 || distance < this.minDistance) {
				this.target = nearbyPlayer;
				this.minDistance = distance;
			}
		}
	}

}