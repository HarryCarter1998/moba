package me.yoast.moba.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.yoast.moba.Main;
import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;

public class ClassSelector {
	private Main plugin;
	private String inventoryName;
	private Inventory inv;
	private int boxes = 4*9;
	
	public ClassSelector(Player player, Main plugin) {
		this.plugin = plugin;
		this.inventoryName = Utils.chat("&f&lClasses");
		this.inv = Bukkit.createInventory(null, boxes, inventoryName);
		
		List<String[]> classArray = XMLReader.getClasses();
		for(int i=0; i<classArray.size(); i++) {
		Utils.createItem(inv, classArray.get(i)[1], 1, i, classArray.get(i)[0]);
		}
		player.openInventory(inv);
	}
}
