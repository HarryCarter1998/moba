package me.yoast.moba.mobs;

import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import me.yoast.moba.mobs.Creep.Team;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

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
