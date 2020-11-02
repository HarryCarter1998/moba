package me.yoast.moba.mobs;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.yoast.moba.pathfinders.PathfinderAttackEnemyCreep;
import me.yoast.moba.utils.Utils;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Tower extends EntitySkeleton {
	
	public enum Team {
		RED, BLUE
	}
	
	private Team team = null;
	
	public Tower(Team team, CraftWorld world) {
		
		super(((CraftWorld)world).getHandle());
        this.team = team;
        List goalB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        setParams();
		
		//this.goalSelector.a(0, new PathfinderGoalFloat(this));
//        this.targetSelector.a(1, new PathfinderAttackEnemyCreep(this)); // Move to closest enemy creep
//        this.targetSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityZombie.class, 1.0D, true));
//        
//        this.targetSelector.a(1, new PathfinderAttackEnemyCreep(this)); // Move to closest enemy creep
//        this.targetSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityZombie.class, 2.0D, true));
//        
//        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityGuardian.class, 2.0D, true));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityGuardian.class, false));
//        
//        this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 4.0D, true));
//        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, false));
//        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
//        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
//        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
//        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
//        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
//        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, false));
        
	}
	
	private void setParams() {
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50);
		//this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0);
		
	}

	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
}