package me.yoast.moba.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.yoast.moba.Main;
import me.yoast.moba.ui.InventoryUI;

public class ClassesCommand implements CommandExecutor{
	
	private Main plugin;
	public ClassesCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("classes").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		p.openInventory(InventoryUI.classSelector(p));
		return false;
	}
}
