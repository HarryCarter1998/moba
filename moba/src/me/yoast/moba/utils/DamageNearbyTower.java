package me.yoast.moba.utils;

import java.util.List;

import org.bukkit.Bukkit;
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
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class DamageNearbyTower extends BukkitRunnable {
	private Creep creep;
	private CraftEntity craftEntity;
	
	public DamageNearbyTower(Creep creep) {
		this.creep = creep;
		this.craftEntity = this.creep.getBukkitEntity();
	}


	@Override
	public void run() {
		if(this.creep.getGoalTarget() != null) {
			if(this.creep.getGoalTarget().getClass().equals(EntityPlayer.class) || this.creep.getGoalTarget().getClass().equals(Creep.class)) {
				return; // if creep is targeting a creep or player, do not attack tower
			}
		}
		List<Entity> targets  = ((Entity) this.craftEntity).getNearbyEntities(2, 5, 2);
		for(Entity nearbyEntity : targets) {
			if(!nearbyEntity.isDead()) {
				if(nearbyEntity instanceof CraftMagmaCube) {
					Tower nmsTower = (Tower) ((CraftMagmaCube) nearbyEntity).getHandle();
					if (this.creep.getTeam().toString() != nmsTower.getTeam().toString()) {
					        LivingEntity tower = (LivingEntity) nearbyEntity;
					        tower.damage(1);
					}
				}	
			}
			if(!this.creep.isAlive()) {
				this.cancel(); //if creep dies, stop damaging tower
			}
		}
	}
}
