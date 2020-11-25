package me.yoast.moba.mobs;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.Plugin;

import me.yoast.moba.Main;
import me.yoast.moba.utils.Turret;
import me.yoast.moba.utils.Utils;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Tower extends EntityMagmaCube {
	
	public enum Team {
		RED, BLUE
	}
	
	private Team team = null;
	private Main plugin;
	
	public Tower(Team team, CraftWorld world, Main plugin) {
		
		super(((CraftWorld)world).getHandle());
        this.team = team;
        this.plugin = plugin;
        List goalB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        setParams();
        new Turret(this, this.plugin).runTaskTimer(this.plugin, 0, 10);
        //this.targetSelector.a(2, new AttackPriorities(this)); // Move to closest enemy creep
        //this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        //this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
	}
	
	private void setParams() {
		this.setSize(2);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50);
		this.setHealth(50);
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