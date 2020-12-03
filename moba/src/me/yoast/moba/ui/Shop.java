package me.yoast.moba.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.yoast.moba.Main;
import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;

public class Shop {
	private Main plugin;
	private String inventoryName;
	private Inventory inv;
	private int boxes = 4*9;
	
	public Shop(Player player, Main plugin) {
		this.plugin = plugin;
		this.inventoryName = Utils.chat("&f&lShop");
		this.inv = Bukkit.createInventory(null, boxes, inventoryName);
		
		List<String[]> shopArray = XMLReader.getShop();
		for(int i=0; i<shopArray.size(); i++) {
		Utils.createItem(inv, shopArray.get(i)[1], 1, i, shopArray.get(i)[0]);
		}
		//toReturn.setContents(inv.getContents());
		player.openInventory(inv);
	}
}
