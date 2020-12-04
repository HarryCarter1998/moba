package me.yoast.moba.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import me.yoast.moba.Main;
import me.yoast.moba.listeners.EntityDamageListener;
import me.yoast.moba.mobs.Creep;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Tower;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NavigationAbstract;

public class DamageTarget extends BukkitRunnable {
	private Creep creep;
	private Main plugin;
	private CraftWorld world = null;
	
	public DamageTarget(Creep creep, Main plugin) {
		this.creep = creep;
		this.plugin = plugin;
		this.world = (CraftWorld) Bukkit.getWorld("world_1602090282");
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
			if(!(goalTarget.getBukkitEntity() instanceof CraftPlayer)) {
				goalTarget.damageEntity(DamageSource.GENERIC, 2);
				this.plugin.getDamageListener().updateHealth(goalTarget.getBukkitEntity());
			}
			else {
				Player player = (Player) goalTarget.getBukkitEntity();
				double damage = DamageApi.calculateDamageAddArmor(player, DamageCause.ENTITY_ATTACK, 8);
				damage /= this.plugin.getMobaPlayer(player).getDefence(); // change damage based on defence of damaged player
				if(goalTarget.getHealth()>damage) {
					goalTarget.damageEntity(DamageSource.GENERIC, (float) damage);
		        }
		        else {
		        	Bukkit.broadcastMessage(goalTarget.getName() + " died");
		        	((CraftPlayer)goalTarget.getBukkitEntity()).setGameMode(GameMode.SPECTATOR);
		        	goalTarget.getBukkitEntity().teleport(new Location(this.world, -40, 40, -577));
		        	respawn(goalTarget.getBukkitEntity());
		        }
			}
		}
		
	}
	
	public void respawn(Entity damaged){
		MobaPlayer damagedMobaPlayer = this.plugin.getMobaPlayer((Player) damaged);
		this.plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (damagedMobaPlayer.getTeam().toString() == "RED"){
					damaged.teleport(new Location(world, 28.5, 21, -577.5));
				}else {
					damaged.teleport(new Location(world, -91.5, 21, -577.5));
				}
				
				((CraftPlayer) damaged).setHealth(20);
				((CraftPlayer) damaged).setGameMode(GameMode.ADVENTURE);
			}
		}, 400);
	}

}
