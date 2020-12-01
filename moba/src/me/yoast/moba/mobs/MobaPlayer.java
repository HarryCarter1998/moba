package me.yoast.moba.mobs;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class MobaPlayer{
	
	public enum Team {
		RED, BLUE
	}
	private Team team = null;
	private Player player = null;
	private List<ItemStack> classItems;
	private List<ItemStack> classArmor;

	public MobaPlayer(Player player) {
		super();
		this.player = player;
		player.setCustomName(ChatColor.RED + player.getName());
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}

	public List<ItemStack> getClassItems() {
		return classItems;
	}

	public void setClassItems(List<ItemStack> classItems) {
		this.classItems = classItems;
	}

	public List<ItemStack> getClassArmor() {
		return classArmor;
	}

	public void setClassArmor(List<ItemStack> classArmor) {
		this.classArmor = classArmor;
	}
	
	
	
	

}
