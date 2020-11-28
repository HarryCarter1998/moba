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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class TowerGun extends BukkitRunnable {
	private CraftWorld world = null;
	private Tower tower;
	private Entity target;
	private CraftEntity craftEntity;
	private double minDistance = -1;
	private Main plugin;
	
	public TowerGun(Tower tower, Main plugin) {
		this.world = (CraftWorld) Bukkit.getWorld("world_1602090282");
		this.tower = tower;
		this.plugin = plugin;
		craftEntity = this.tower.getBukkitEntity();
	}


	@Override
	public void run() {
		List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(10, 5, 10);
		boolean nearbyCreepFlag = false;
		
		if(!this.tower.isAlive()) {
			this.cancel();
		}
		
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
		if (nearbyCreepFlag == false) {
			List<MobaPlayer> mobaPlayers = this.plugin.getMobaPlayers();
			for(MobaPlayer nearbyEntity : mobaPlayers) {
				Location towerLocation = this.tower.getBukkitEntity().getLocation();
				
				
				Location playerLocation = nearbyEntity.getPlayer().getLocation();
				if((towerLocation).distance(playerLocation) < 10) {
					attackNearestEnemyPlayer(nearbyEntity);
				}
			}
		}
		this.minDistance = -1;
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
				Location towerLoc = this.tower.getBukkitEntity().getLocation();
				Location towerLocation = new Location(this.world, towerLoc.getX(),towerLoc.getY()+0.5,towerLoc.getZ());
				Location creepLoc = this.target.getLocation();
				Location creepLocation = new Location(this.world, creepLoc.getX(),creepLoc.getY()+0.5,creepLoc.getZ());
				Vector vector = getDirectionBetweenLocations(towerLocation, creepLocation);
				if(this.target.isDead()) {
					return;
				}
		        for (double i = 1; i <= towerLocation.distance(creepLocation); i += 0.5) {
		            vector.multiply(i);
		            towerLocation.add(vector);
		            towerLocation.getWorld().spigot().playEffect(towerLocation, Effect.FLAME, 0, 0, 0, 0, 0, 1, 0, 100);
		            towerLocation.subtract(vector);
		            vector.normalize();
		        }
			
			
		        LivingEntity liv = (LivingEntity) this.target;
		        liv.damage(2);
		}
			
		
	}
	
	public void attackNearestEnemyPlayer(MobaPlayer nearbyEntity) {
		Player nmsPlayer = nearbyEntity.getPlayer();
		if (this.tower.getTeam().toString() != nearbyEntity.getTeam().toString()) {
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
			Location towerLoc = this.tower.getBukkitEntity().getLocation();
			Location towerLocation = new Location(this.world, towerLoc.getX(),towerLoc.getY()+0.5,towerLoc.getZ());
			Location playerLoc = this.target.getLocation();
			Location playerLocation = new Location(this.world, playerLoc.getX(),playerLoc.getY()+1,playerLoc.getZ());
			Vector vector = getDirectionBetweenLocations(towerLocation, playerLocation);
				if(this.target.isDead()) {
					return;
				}
		        for (double i = 1; i <= towerLocation.distance(playerLocation); i += 0.5) {
		            vector.multiply(i);
		            towerLocation.add(vector);
		            towerLocation.getWorld().spigot().playEffect(towerLocation, Effect.FLAME, 0, 0, 0, 0, 0, 1, 0, 100);
		            towerLocation.subtract(vector);
		            vector.normalize();
		        }
			
			
		        LivingEntity liv = (LivingEntity) this.target;
		        liv.damage(2);
		}
			
	}
	
	Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	
}
