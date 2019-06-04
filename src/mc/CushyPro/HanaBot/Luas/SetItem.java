package mc.CushyPro.HanaBot.Luas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class SetItem extends ZeroArgFunction {
	
	private ItemStack item;

	public SetItem(ItemStack item){
		this.item = item;
	}
	
	@Override
	public LuaValue call() {
		LuaTable table = new LuaTable();
		table.set("setType", new setType());
		table.set("setAmount", new setAmount());
		table.set("setData", new setData());
		table.set("setDisplayName", new setDisplayName());
		table.set("addLore", new addlore());
		table.set("removeLore", new removelore());
		table.set("setEnch", new setEnch());
		table.set("setFlag", new setFlag());
		return table;
	}
	
	public class setFlag extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring() && args2.isboolean()) {
				String flag = args.checkjstring();
				boolean a = args2.checkboolean();
				ItemMeta meta = item.getItemMeta();
				ItemFlag itemflag = ItemFlag.valueOf(flag);
				if (itemflag != null) {
					if(a == false && meta.hasItemFlag(itemflag)) {
						meta.removeItemFlags(itemflag);
					} else {
						meta.addItemFlags(itemflag);
					}
					item.setItemMeta(meta);
				}
				return LuaValue.valueOf("not have itemflag " + flag);
			}
			return LuaValue.valueOf("setFlag(string flag,boolean args)");
		}
		
	}
	
	
	public class setEnch extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args,LuaValue args2) {
			if (args.isstring() && args2.isint()) {
				String ench = args.checkjstring();
				int level = args2.checkint();
				Enchantment en = Enchantment.getByName(ench);
				if (en != null) {
					ItemMeta meta = item.getItemMeta();
					if (meta.hasEnchant(en)) {
						meta.removeEnchant(en);
					}
					if (level > 0) {
						meta.addEnchant(en, level, true);
					}
					item.setItemMeta(meta);
				}
				return LuaValue.valueOf("not have enchantment " + ench);
			}
			return LuaValue.valueOf("setEnch(string ench,int level)");
		}
		
	}
	
	
	public class removelore extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				int line = args.checkint();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if (meta.hasLore()) {
					lore = meta.getLore();
				}
				if (lore.size() > line) {
					lore.remove(line);
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("removelore(int line)");
		}
		
	}
	
	public class addlore extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String Name = args.checkjstring();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if (meta.hasLore()) {
					lore = meta.getLore();
				}
				lore.add(Name);
				meta.setLore(lore);
				item.setItemMeta(meta);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("addlore(string Name)");
		}
		
	}
	
	public class setDisplayName extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String Name = args.checkjstring();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Name);
				item.setItemMeta(meta);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setDisplayName(string Name)");
		}
		
	}
	
	public class setData extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isint()) {
				int data = args.checkint();
				item.setDurability((short) data);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setData(int data)");
		}
		
	}
	
	public class setAmount extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isint()) {
				int amount = args.checkint();
				item.setAmount(amount);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setAmount(int amount)");
		}
		
	}

	public class setType extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String material = args.checkjstring();
				if (Material.getMaterial(material) != null) {
					item.setType(Material.getMaterial(material));
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf("setType(string material)");
		}
		
	}

	
	
}
