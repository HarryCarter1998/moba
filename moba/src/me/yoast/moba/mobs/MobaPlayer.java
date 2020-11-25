package me.yoast.moba.mobs;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MobaPlayer{
	
	public enum Team {
		RED, BLUE
	}


	private Team team = null;
	private Player player = null;


	public MobaPlayer(Player player, Team team) {
		super();
		this.player = player;

		this.team  = team;
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
	
	
	
	

}
