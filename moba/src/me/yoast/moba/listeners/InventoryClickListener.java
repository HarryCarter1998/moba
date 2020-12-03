package me.yoast.moba.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.console.Operation;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.yoast.moba.Main;
import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;
import net.minecraft.server.v1_8_R3.AttributeModifier;
import net.minecraft.server.v1_8_R3.GenericAttributes;

public class InventoryClickListener implements Listener{
	
	private Main plugin;
	
	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getInventory().getTitle();
		if(title.equals(Utils.chat("&f&lClasses"))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			e.getWhoClicked().closeInventory();
			setClassItems((Player) e.getWhoClicked(), e.getSlot(), this.plugin);
		}
		if(title.equals(Utils.chat("&f&lShop"))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			ItemStack sword = getSword((Player) e.getWhoClicked());
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setLore(Arrays.asList("+5% damage"));
			sword.setItemMeta(swordMeta);
		}
	}
	
	public void setClassItems(Player player, int slot, Main plugin) {
		List<String> xmlItems = XMLReader.getItems(slot);
		List<ItemStack> items = new ArrayList<ItemStack>();
		List<ItemStack> armor = new ArrayList<ItemStack>();
		for(int i=0; i<xmlItems.size(); i++) {
			String itemString = xmlItems.get(i);
			ItemStack item = new ItemStack(Material.getMaterial(itemString), 1);
			ItemMeta itemMeta = item.getItemMeta();
		    itemMeta.spigot().setUnbreakable(true);
		    item.setItemMeta(itemMeta);
			if(isArmor(item)) {
				armor.add(item);
			} else {
				items.add(item);
			}
		}
		this.plugin.getMobaPlayer(player).setClassItems(items);
		this.plugin.getMobaPlayer(player).setClassArmor(armor);
	}
	
	public boolean isArmor(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
            return true;
            }

        return false;
    }
	
	public ItemStack getSword(Player player) {
		ItemStack[] items = player.getInventory().getContents();
		List<ItemStack> swords = new ArrayList<ItemStack>();
		for(ItemStack item : items) {
			if(item != null) {
			
				//Bukkit.broadcastMessage(item.getType().toString());
				if (item.getType().toString().endsWith("SWORD")) {
					swords.add(item);
				}
			}
		}
//		List<ItemStack> swords = Arrays.stream( player.getInventory().getContents() ) // Get all items in inventory
//		        .filter( item -> item.getType().name().endsWith("SWORD") ) // Filter all items with a type name finishing with SWORD
//		        .collect( Collectors.toList() ); // Collect all the items filtered
		return swords.get(0);
	}
}
