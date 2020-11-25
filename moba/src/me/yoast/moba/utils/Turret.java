package me.yoast.moba.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Tower;

public class Turret extends BukkitRunnable {
	private CraftWorld world = null;
	private Tower tower;
	private Entity target;
	private CraftEntity craftEntity;
	private double minDistance = -1;
	
	public Turret(Tower tower) {
		this.world = (CraftWorld) Bukkit.getWorld("world_1602090282");
		this.tower = tower;
		craftEntity = this.tower.getBukkitEntity();
	}


	@Override
	public void run() {
				List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(10, 5, 10);
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
				this.minDistance = -1;
			if(!this.tower.isAlive()) {
				this.cancel();
			}
		if (nearbyCreepFlag == false) {
			for(Entity nearbyEntity : targets) {
				if(nearbyEntity instanceof CraftPlayer) {
					attackNearestEnemyPlayer((CraftPlayer) nearbyEntity);
				}		
			}
		}
//		if(this.target != null) {
//			if(!this.target.isDead()) {
//				//CraftEntity craftTarget = (CraftEntity) this.target;
//				//this.navigation.a(craftTarget.getHandle(), 1.0D);
//			}
//		}	
	}

	public void attackNearestEnemyCreep(CraftZombie nearbyEntity) {
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
				Location Loc1 = this.tower.getBukkitEntity().getLocation();
				Location Loc2 = this.target.getLocation();
				Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
				if(this.target.isDead()) {
					return;
				}
		        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
		            vector.multiply(i);
		            Loc1.add(vector);
		            Loc1.getWorld().spigot().playEffect(Loc1, Effect.FLAME, 0, 0, 0, 0, 0, 1, 0, 100);
		            Loc1.subtract(vector);
		            vector.normalize();
		        }
			
			
		        LivingEntity liv = (LivingEntity) this.target;
		        liv.damage(10);
		}
			
		
	}
	
	public void attackNearestEnemyCreep(CraftPlayer nearbyEntity) {
		Player nmsPlayer = (Player) ((CraftPlayer) nearbyEntity).getHandle();
		if (this.tower.getTeam().toString() != nmsPlayer.getTeam().toString()) {
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
				Location Loc1 = this.tower.getBukkitEntity().getLocation();
				Location Loc2 = this.target.getLocation();
				Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
				if(this.target.isDead()) {
					return;
				}
		        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
		            vector.multiply(i);
		            Loc1.add(vector);
		            Loc1.getWorld().spigot().playEffect(Loc1, Effect.FLAME, 0, 0, 0, 0, 0, 1, 0, 100);
		            Loc1.subtract(vector);
		            vector.normalize();
		        }
			
			
		        LivingEntity liv = (LivingEntity) this.target;
		        liv.damage(10);
		}
			
		
	}
	
	Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	
}
