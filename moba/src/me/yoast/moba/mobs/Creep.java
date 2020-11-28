package me.yoast.moba.mobs;
import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.yoast.moba.Main;
import me.yoast.moba.pathfinders.PathfinderPriorities;
import me.yoast.moba.utils.DamageTarget;
import me.yoast.moba.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Creep extends EntityZombie {
	
	public enum Team {
		RED, BLUE
	}
	
	private final float CREEPSPEED = 0.135f;
	private Team team = null;
	private Main plugin;
	
	public Creep(Team team, CraftWorld world, Main plugin) {
		
        super(((CraftWorld)world).getHandle());
        this.team = team;
        this.plugin = plugin;
        List goalB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)Utils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)Utils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        setParams();
        new DamageTarget(this, this.plugin).runTaskTimer(this.plugin, 0, 40);
        this.goalSelector.a(200, new PathfinderPriorities(this, this.plugin)); // Move to closest enemy creep
//        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityZombie.class, 1.0D, true)); // Enable attacks against zombies
//        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, true));
        
	}
	
	private void setParams() {
		DecimalFormat df = new DecimalFormat("#");
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.CREEPSPEED);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(200);
		this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
		this.setHealth(50);
		this.setCustomNameVisible(true);
		setBaby(true);
		this.b(true);
		
		String health = df.format(this.getMaxHealth()) + "/" + df.format(this.getMaxHealth());
		Color color;
		if (this.team == Team.RED) {
			color = Color.RED;
			this.setCustomName(ChatColor.RED + health);
		} else {
			color = Color.BLUE;
			this.setCustomName(ChatColor.BLUE + health);
			
		}
		this.setCreepArmour(Material.LEATHER_HELMET, color, this);
		this.setCreepArmour(Material.LEATHER_CHESTPLATE, color, this);
		this.setCreepArmour(Material.LEATHER_LEGGINGS, color, this);
		this.setCreepArmour(Material.LEATHER_BOOTS, color, this);
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