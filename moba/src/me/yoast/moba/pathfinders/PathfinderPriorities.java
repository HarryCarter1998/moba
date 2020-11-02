package me.yoast.moba.pathfinders;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import me.yoast.moba.mobs.Creep;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public class PathfinderPriorities extends PathfinderGoal {
	
	private Creep creep;
	private Navigation navigation;
	private Entity target;
	private CraftEntity craftEntity;
	
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
		this.creep.getNavigation().n();
		List<Entity> targets  = ((Entity) craftEntity).getNearbyEntities(200, 5, 200);
		double minDistance = -1;
		for(Entity nearbyEntity : targets) {
			if(nearbyEntity instanceof CraftZombie) {
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
		}
		if(this.target != null) {
			if(!this.target.isDead()) {
				CraftEntity craftTarget = (CraftEntity) this.target;
				this.navigation.a(craftTarget.getHandle(), 1.0D);
			}
		}
					
	}
	
}
