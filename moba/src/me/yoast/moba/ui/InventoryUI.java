package me.yoast.moba.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;

public class InventoryUI {
	
	public static Inventory inv;
	public static String inventory_name ;
	public static int boxes = 4 * 9;
	
	public static void initialize() {
		inventory_name = Utils.chat("&f&lClasses");
		inv = Bukkit.createInventory(null, boxes);
	}
	
	public static Inventory classSelector (Player p) {
		
		Inventory toReturn = Bukkit.createInventory(null, boxes, inventory_name);
		
		List<String[]> classArray = XMLReader.getClasses();
		for(int i=0; i<classArray.size(); i++) {
		Utils.createItem(inv, classArray.get(i)[1], 1, i, classArray.get(i)[0]);
		}
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	public static void clicked(Player player, int slot) {
		List<String> itemsArray = XMLReader.getItems(slot);
		
		int numOfArmor = 0;
		int numOfItems = 0;
		player.getInventory().clear();
		clearArmor(player);
		for(int i=0; i<itemsArray.size(); i++) {
			String itemString = itemsArray.get(i);
			ItemStack item = new ItemStack(Material.getMaterial(itemString), 1);
			if(isArmor(item)) {
				numOfArmor++;
			} else {
				numOfItems++;
			}
			
		}
		ItemStack[] armorStack = new ItemStack[numOfArmor];
		ItemStack[] itemStack = new ItemStack[numOfItems];
		int j=0;
		int k=0;
		for(int i=0; i<itemsArray.size(); i++) {
			String itemString = itemsArray.get(i);
			ItemStack item = new ItemStack(Material.getMaterial(itemString), 1);
			if(isArmor(item)) {
				armorStack[j] = item;
				j++;
			} else {
				if(itemString.equals("BOW")) {
					item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
				}
				itemStack[k] = item;
				k++;
			}
			
		}
		Potion potion = new Potion(PotionType.INSTANT_HEAL, 2, true);
        
		player.getInventory().setArmorContents(armorStack);
		player.getInventory().setContents(itemStack);
		player.getInventory().addItem(potion.toItemStack(1));
		
		
			
		
	}
	
	public static boolean isArmor(ItemStack itemStack) {
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
	
	public static void clearArmor(Player player){
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		}
	

}
