package me.yoast.moba.mobs;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import me.yoast.moba.pathfinders.PathfinderPriorities;
import me.yoast.moba.utils.Utils;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Tower extends EntityGuardian {
	
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
		
        this.targetSelector.a(1, new PathfinderPriorities(this)); // Move to closest enemy creep
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityZombie.class, 1.0D, true)); // Enable attacks against zombies
	}
	
	private void setParams() {
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50);
		
	}

	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	@Override
	public void move(double d0, double d1, double d2) {
        this.ai = true;
    } 
	
}