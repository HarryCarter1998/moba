package me.yoast.moba.mobs;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;

public class MobaPlayer{
	
	public enum Team {
		RED, BLUE
	}
	private Team team = null;
	private Player player = null;
	private List<ItemStack> classItems;
	private List<ItemStack> classArmor;
	private ScoreboardManager manager;
	private Scoreboard scoreboard;
	private Objective objective;
	private Score goldScore;
	private int gold = 0;
	private Nexus nexus;

	

	public MobaPlayer(Player player) {
		super();
		this.player = player;
		player.setCustomName(ChatColor.RED + player.getName());
		createScoreboard();
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		((EntityLiving) nmsPlayer).getAttributeInstance(GenericAttributes.maxHealth).setValue(40);
		//((EntityLiving) nmsPlayer).getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(0);
	}
	
	public int getGold() {
		return this.gold;
	}

	public void setGold(int gold) {
		scoreboard.resetScores(ChatColor.GOLD + "Gold: " + this.gold);
		this.gold = gold;
		goldScore = objective.getScore(ChatColor.GOLD + "Gold: " + gold);
		goldScore.setScore(1);
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
	
	public void createScoreboard() {
		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		objective = scoreboard.registerNewObjective("MobaCraft", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "MobaCraft");
		goldScore = objective.getScore(ChatColor.GOLD + "Gold: " + 0); 
		goldScore.setScore(1);
		this.player.setScoreboard(scoreboard);
	}
	 
	public Nexus getNexus() {
		return nexus;
	}

	public void setNexus(Nexus nexus) {
		this.nexus = nexus;
	}
	

}
