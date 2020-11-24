package me.yoast.moba.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.Tower;

public class DamageNearbyTower extends BukkitRunnable {
	private CraftWorld world = null;
	private Creep creep;
	private Entity target;
	private CraftEntity craftEntity;
	private double minDistance = -1;
	
	public DamageNearbyTower(Creep creep) {
		this.world = (CraftWorld) Bukkit.getWorld("world_1602090282");
		this.creep = creep;
		craftEntity = this.creep.getBukkitEntity();
	}


	@Override
	public void run() {
		//this.tower.getNavigation().n();
		List<Entity> targets1  = ((Entity) craftEntity).getNearbyEntities(10, 5, 10);
		for(Entity nearbyEntity : targets1) {
			if(!nearbyEntity.isDead()) {
				if(nearbyEntity instanceof CraftZombie) {
					Creep nmsCreep = (Creep) ((CraftZombie) nearbyEntity).getHandle();
					if (this.creep.getTeam() != nmsCreep.getTeam()) {
						return;
					}
					
				}	
			}
		}
		List<Entity> targets2  = ((Entity) craftEntity).getNearbyEntities(2, 5, 2);
		for(Entity nearbyEntity : targets2) {
			if(!nearbyEntity.isDead()) {
				if(nearbyEntity instanceof CraftMagmaCube) {
					Tower nmsTower = (Tower) ((CraftMagmaCube) nearbyEntity).getHandle();
					if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
						attackNearestEnemyTower((CraftMagmaCube) nearbyEntity);
					}
					
				}	
			}
		
			if(!this.creep.isAlive()) {
				this.cancel();
			}
		}
		this.minDistance = -1;
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
				
		    if(!this.target.isDead()) {   
		        LivingEntity liv = (LivingEntity) this.target;
		        liv.damage(1);
		    }
		}
	}
	
	Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	
}
