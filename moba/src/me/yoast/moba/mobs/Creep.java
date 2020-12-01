package me.yoast.moba.mobs;
import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.yoast.moba.Main;
import me.yoast.moba.utils.PathfinderPriorities;
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
        new PathfinderPriorities(this,this.plugin).runTaskTimer(this.plugin, 0, 20);
        
	}
	
	private void setParams() {
		DecimalFormat df = new DecimalFormat("#");
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.CREEPSPEED);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(200);
		this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
		this.setHealth(50);
		
		setBaby(true);
		this.b(true);
		int n = 25;
		String bar = "|";

		String healthString = "";
		for (int i = 0; i < n; ++i) {
		    healthString += bar;
		}
		if (this.team.equals(Team.RED)) {
			
			this.setCustomName(ChatColor.RED + healthString);
		} else {
			this.setCustomName(ChatColor.BLUE + healthString);
		}
		Color color;
		if (this.team == Team.RED) {
			color = Color.RED;
		} else {
			color = Color.BLUE;
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