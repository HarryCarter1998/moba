package me.yoast.moba.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class DamageTarget extends BukkitRunnable {
	private Creep creep;
	private CraftEntity craftEntity;
	private Main plugin;
	
	public DamageTarget(Creep creep, Main plugin) {
		this.creep = creep;
	}


	@Override
	public void run() {
		if(!this.creep.isAlive()) {
			this.cancel(); //if creep dies, stop damaging entities
		}
		if(this.creep.getGoalTarget() == null) {
			return;
		}
		float reach = 3;
		EntityLiving goalTarget = this.creep.getGoalTarget();
		if(goalTarget.getBukkitEntity() instanceof CraftMagmaCube) {
			reach = 5;
		}
		if(this.creep.getBukkitEntity().getLocation().distance(goalTarget.getBukkitEntity().getLocation())<reach) {
			goalTarget.damageEntity(DamageSource.GENERIC, 2);
		}
		
	}

}
