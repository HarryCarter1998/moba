package me.yoast.moba.pathfinders;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import me.yoast.moba.Main;

public class PathfinderPriorities extends PathfinderGoal {
	
	private Creep creep;
	private Navigation navigation;
	private Entity target;
	private CraftEntity craftEntity;
	private EntityInsentient entity;
	private double minDistance = -1;
	private Main plugin;
	
	public PathfinderPriorities(Creep creep, Main plugin) {
		this.creep = creep;
		this.plugin = plugin;
		this.navigation = (Navigation) this.creep.getNavigation();
		this.target = null;
		craftEntity = this.creep.getBukkitEntity();
	}

	@Override
	public boolean a() {
			return true;
	}
	
	
	@Override
	public void e() {
		this.creep.getNavigation().n();
		List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(50, 5, 50);
		boolean nearbyCreepFlag = false;
		boolean nearbyTowerFlag = false;
		for(Entity nearbyEntity : targets) {
			if(!nearbyEntity.isDead()) {
				if(nearbyEntity instanceof CraftZombie) {
					Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
					if (this.creep.getTeam() != nmsCreep.getTeam()) {
						nearbyCreepFlag = true;
						attackNearestEnemyCreep((CraftZombie) nearbyEntity);
					}
					
				}	
			}
		}
		if (nearbyCreepFlag == false) {
			for(Entity nearbyEntity : targets) {
				if(!nearbyEntity.isDead()) {
					if(nearbyEntity instanceof CraftMagmaCube) {
						Tower nmsTower = (Tower) ((CraftMagmaCube) nearbyEntity).getHandle();
					
						if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
							nearbyTowerFlag = true;
							attackNearestEnemyTower((CraftMagmaCube) nearbyEntity);
						
						}		
					}
				}
			}
		}
	//		Bukkit.broadcastMessage("nearbyCreepFlag = " + nearbyCreepFlag);
	//		Bukkit.broadcastMessage("nearbyTowerFlag = " + nearbyTowerFlag);
		if (!nearbyCreepFlag) {
			List<MobaPlayer> mobaPlayers = this.plugin.getMobaPlayers();
			for(MobaPlayer nearbyEntity : mobaPlayers) {
					attackNearestEnemyPlayer(nearbyEntity);
				
			}
		}
		if(this.target != null) {
			if(!this.target.isDead()) {
				CraftEntity craftTarget = (CraftEntity) this.target;
				this.navigation.a(craftTarget.getHandle(), 1.0D);
			}
		}	
		
		
	}
	
	public void attackNearestEnemyCreep(CraftZombie nearbyEntity) {
		
		Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
		if (this.creep.getTeam() != nmsCreep.getTeam()) {
			if (this.minDistance == -1) {
				this.minDistance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				this.target = nearbyEntity;
			} else {
				double distance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				if (distance < this.minDistance) {
					this.minDistance = distance;
					this.target = nearbyEntity;
				}
			}
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
				
			
		}
	}
	
	public void attackNearestEnemyTower(CraftMagmaCube nearbyEntity) {
		
		Tower nmsTower = (Tower) ((CraftMagmaCube) nearbyEntity).getHandle();
		if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
			if (this.minDistance == -1) {
				this.minDistance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				this.target = nearbyEntity;
			} else {
				double distance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				if (distance < this.minDistance) {
					this.minDistance = distance;
					this.target = nearbyEntity;
				}
			}
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
			
		}
	}
	
	public void attackNearestEnemyPlayer(MobaPlayer nearbyEntity) {
		
		Player nmsPlayer = nearbyEntity.getPlayer();
		if (this.creep.getTeam().toString() != nearbyEntity.getTeam().toString()) {
			if (this.minDistance == -1) {
				this.minDistance = nmsPlayer.getLocation().distance(craftEntity.getLocation());
				this.target = nmsPlayer;
			} else {
				double distance = nmsPlayer.getLocation().distance(craftEntity.getLocation());
				if (distance < this.minDistance) {
					this.minDistance = distance;
					this.target = nmsPlayer;
				}
			}
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
			
		}
	}
	
}
