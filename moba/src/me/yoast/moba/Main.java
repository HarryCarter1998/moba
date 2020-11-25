package me.yoast.moba;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.yoast.moba.commands.ClassesCommand;
import me.yoast.moba.commands.SpawnMobsCommand;
import me.yoast.moba.listeners.ClickItemListener;
import me.yoast.moba.listeners.CreepDamageListener;
import me.yoast.moba.listeners.CreepDropListener;
import me.yoast.moba.listeners.DropItemListener;
import me.yoast.moba.listeners.InventoryClickListener;
import me.yoast.moba.listeners.SlimeSplitListener;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.ui.InventoryUI;

public class Main extends JavaPlugin {
	
	private ClickItemListener clickItemListener;
	
	@Override
	public void onEnable() {
		new ClassesCommand(this);
		clickItemListener = new ClickItemListener(this);
		new SpawnMobsCommand(this);
		new CreepDropListener(this);
		new InventoryClickListener(this); 
		
		new CreepDamageListener(this);
		new DropItemListener(this);
		new SlimeSplitListener(this);
		InventoryUI.initialize();
	}
	
	public List<MobaPlayer> getMobaPlayers() {
		return clickItemListener.getMobaPlayers();
	}
	// TODO
	// Towers
	// Creep AI
	// Update Player HP bars on Heal (EntityRegainHealthEvent if e.getEntity instanceof Player)
	// Shops
	// Items
	// More classes
	
}