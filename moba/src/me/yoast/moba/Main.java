package me.yoast.moba;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.yoast.moba.commands.SetupCommand;
import me.yoast.moba.commands.StartCommand;
import me.yoast.moba.listeners.ChunkUnloadListener;
import me.yoast.moba.listeners.ClickItemListener;
import me.yoast.moba.listeners.CreepDropListener;
import me.yoast.moba.listeners.DropItemListener;
import me.yoast.moba.listeners.EntityDamageListener;
import me.yoast.moba.listeners.EntityDeathListener;
import me.yoast.moba.listeners.FoodLevelChangeListener;
import me.yoast.moba.listeners.InventoryClickListener;
import me.yoast.moba.listeners.PlayerDeathListener;
import me.yoast.moba.listeners.PlayerInteractEntityListener;
import me.yoast.moba.listeners.SlimeSplitListener;
import me.yoast.moba.mobs.MobaPlayer;

public class Main extends JavaPlugin {
	
	private List<MobaPlayer> mobaPlayers = new ArrayList<MobaPlayer>();
	private EntityDamageListener entityDamageListener;
	
	@Override
	public void onEnable() {
		new ClickItemListener(this);
		new StartCommand(this);
		new CreepDropListener(this);
		new InventoryClickListener(this); 
		this.entityDamageListener = new EntityDamageListener(this);
		new DropItemListener(this);
		new SlimeSplitListener(this);
		new FoodLevelChangeListener(this);
		new ChunkUnloadListener(this);
		new SetupCommand(this);
		new PlayerDeathListener(this);
		new EntityDeathListener(this);
		new PlayerInteractEntityListener(this);
	}
	
	public List<MobaPlayer> getMobaPlayers() {
		return this.mobaPlayers;
	}
	public void addMobaPlayer(MobaPlayer mobaPlayer) {
		this.mobaPlayers.add(mobaPlayer);
	}
	public void removeMobaPlayer(MobaPlayer mobaPlayer) {
		this.mobaPlayers.remove(mobaPlayers.indexOf(mobaPlayer));
	}
	
	public MobaPlayer getMobaPlayer(Player player) {
		for(MobaPlayer mobaPlayer : this.mobaPlayers) {
			if (mobaPlayer.getPlayer().equals(player)) {
				return mobaPlayer;
			}
		}
		return null;
	}
	
	public EntityDamageListener getDamageListener() {
		return this.entityDamageListener;
	}
	// TODO
	// Shops
	// Items
	// More classes
	//speed upgrade
	//Defence upgrade
	
	
	
}