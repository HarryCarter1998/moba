package me.yoast.moba.pathfinders;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.util.Vector;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.NavigationGuardian;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public class AttackPriorities extends PathfinderGoal {
	
	private Tower tower;
	private NavigationGuardian navigation;
	private Entity target;
	private CraftEntity craftEntity;
	private double minDistance = -1;
	
	public AttackPriorities(Tower tower) {
		this.tower = tower;
		this.navigation = (NavigationGuardian) this.tower.getNavigation();
		this.target = null;
		craftEntity = this.tower.getBukkitEntity();
	}

	@Override
	public boolean a() {
			return true;
	}
	
	
	@Override
	public void e() {
		this.tower.getNavigation().n();
		List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(50, 5, 50);
		boolean nearbyCreepFlag = false;
		for(Entity nearbyEntity : targets) {
			if(!nearbyEntity.isDead()) {
				if(nearbyEntity instanceof CraftZombie) {
					Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
					if (this.tower.getTeam().toString() != nmsCreep.getTeam().toString()) {
						nearbyCreepFlag = true;
						attackNearestEnemyCreep((CraftZombie) nearbyEntity);
					}
					
				}	
			}
		}
//		if (nearbyCreepFlag == false) {
//			for(Entity nearbyEntity : targets) {
//				if(nearbyEntity instanceof CraftGuardian) {
//					attackNearestEnemyPlayer((CraftGuardian) nearbyEntity);
//				}		
//			}
//		}
		if(this.target != null) {
			if(!this.target.isDead()) {
				CraftEntity craftTarget = (CraftEntity) this.target;
				this.navigation.a(craftTarget.getHandle(), 1.0D);
			}
		}	
	}
	
	public void attackNearestEnemyCreep(CraftZombie nearbyEntity) {
		this.minDistance = -1;
		Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
		if (this.tower.getTeam().toString() != nmsCreep.getTeam().toString()) {
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
//				Location Loc1 = this.tower.getBukkitEntity().getLocation();
//				Location Loc2 = this.target.getLocation();
//				Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
//		        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
//		            vector.multiply(i);
//		            Loc1.add(vector);
//		            Loc1.getWorld().spigot().playEffect(Loc1, Effect.FLAME, 0, 0, 0, 0, 0, 1, 0, 100);
//		            Loc1.subtract(vector);
//		            vector.normalize();
//		        }
				//this.tower.setGoalTarget((EntityLiving) ((CraftEntity) this.target).getHandle(), TargetReason.CLOSEST_ENTITY, false);
				//Bukkit.broadcastMessage(this.tower.getGoalTarget().toString());
			
		}
	}
	
	Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	

	
}
