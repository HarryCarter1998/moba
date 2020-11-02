package me.yoast.moba.mobs;
import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.yoast.moba.pathfinders.PathfinderAttackEnemyCreep;
import me.yoast.moba.pathfinders.PathfinderAttackEnemyTower;
import me.yoast.moba.pathfinders.PathfinderPriorities;
import me.yoast.moba.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Creep extends EntityZombie {
	
	public enum Team {
		RED, BLUE
	}
	
	private final float CREEPSPEED = 0.135f;
	private Team team = null;
	
	public Creep(Team team, CraftWorld world) {
		
        super(((CraftWorld)world).getHandle());
        this.team = team;
        List goalB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        setParams();
		
//		this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.targetSelector.a(1, new PathfinderPriorities(this)); // Move to closest enemy creep
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityZombie.class, 1.0D, true)); // Enable attacks against zombies
        
//        this.targetSelector.a(3, new PathfinderAttackEnemyTower(this)); // Move to closest enemy tower
//        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntitySkeleton.class, 1.0D, true)); // Enables attacks against skeletons
        
//        this.goalSelector.a(7, new PathfinderGoalMeleeAttack(this, EntitySkeleton.class, 2.0D, true));
//        this.targetSelector.a(8, new PathfinderGoalNearestAttackableTarget(this, EntitySkeleton.class, false));
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
		DecimalFormat df = new DecimalFormat("#");
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.CREEPSPEED);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(5);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(200);
		this.setHealth(50);
		this.setCustomNameVisible(true);
		setBaby(true);
		
		
		if (this.team == Team.RED) {
			String health = df.format(this.getMaxHealth()) + "/" + df.format(this.getMaxHealth());
			this.setCustomName(ChatColor.RED + health);
			this.setCreepArmour(Material.LEATHER_HELMET, Color.RED, this);
			this.setCreepArmour(Material.LEATHER_CHESTPLATE, Color.RED, this);
			this.setCreepArmour(Material.LEATHER_LEGGINGS, Color.RED, this);
			this.setCreepArmour(Material.LEATHER_BOOTS, Color.RED, this);
		} else {
			String health = df.format(this.getMaxHealth()) + "/" + df.format(this.getMaxHealth());
			this.setCustomName(ChatColor.BLUE + health);
			this.setCreepArmour(Material.LEATHER_HELMET, Color.BLUE, this);
			this.setCreepArmour(Material.LEATHER_CHESTPLATE, Color.BLUE, this);
			this.setCreepArmour(Material.LEATHER_LEGGINGS, Color.BLUE, this);
			this.setCreepArmour(Material.LEATHER_BOOTS, Color.BLUE, this);
		}
	}
	private void setCreepArmour(Material material, Color colour, Creep creep) {
		LivingEntity livingEntity = (LivingEntity) creep.getBukkitEntity();
		ItemStack armor = new ItemStack(material);
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.getItemMeta();
		armorMeta.setColor(colour);
		armor.setItemMeta(armorMeta);
		switch (material) {
			case LEATHER_HELMET:
				livingEntity.getEquipment().setHelmet(armor);
			case LEATHER_CHESTPLATE:
				livingEntity.getEquipment().setChestplate(armor);
			case LEATHER_LEGGINGS:
				livingEntity.getEquipment().setLeggings(armor);
			case LEATHER_BOOTS:
				livingEntity.getEquipment().setBoots(armor);
		default:
			break;
		}
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
}