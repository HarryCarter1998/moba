package me.yoast.moba.pathfinders;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftGuardian;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public class PathfinderPriorities extends PathfinderGoal {
	
	private Creep creep;
	private Navigation navigation;
	private Entity target;
	private CraftEntity craftEntity;
	private EntityInsentient entity;
	
	public PathfinderPriorities(Creep creep) {
		this.creep = creep;
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
		//Bukkit.broadcastMessage("checking for nearby entities");
		this.creep.getNavigation().n();
		List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(50, 5, 50);
		boolean nearbyCreepFlag = false;
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
				if(nearbyEntity instanceof CraftGuardian) {
					attackNearestEnemyTower((CraftGuardian) nearbyEntity);
				}		
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
		double minDistance = -1;
		Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
		if (this.creep.getTeam() != nmsCreep.getTeam()) {
			if (minDistance == -1) {
				minDistance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				this.target = nearbyEntity;
			} else {
				double distance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				if (distance < minDistance) {
					minDistance = distance;
					this.target = nearbyEntity;
				}
			}
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
				
			
		}
	}
	
	public void attackNearestEnemyTower(CraftGuardian nearbyEntity) {
		double minDistance = -1;
		if(nearbyEntity == null) {
			return;
		}
		Tower nmsTower = (Tower) ((CraftGuardian) nearbyEntity).getHandle();
		if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
			if (minDistance == -1) {
				minDistance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				this.target = nearbyEntity;
			} else {
				double distance = nearbyEntity.getLocation().distance(craftEntity.getLocation());
				if (distance < minDistance) {
					minDistance = distance;
					this.target = nearbyEntity;
				}
			}
				this.creep.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
			
		}
	}
	
}
