package mc.CushyPro.HanaBot.Luas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class DataBase extends ZeroArgFunction {

	public List<LuaValue> list = new ArrayList<LuaValue>();
	public HashMap<String, LuaValue> Map = new HashMap<String, LuaValue>();

	@Override
	public LuaValue call() {
		LuaTable library = new LuaTable();
		library.set("set", new set());
		library.set("add", new add());
		library.set("remove", new remove());
		library.set("getList", new getList());
		library.set("get", new get());
		library.set("getInfo", new getInfo());
		return library;
	}

	public class getInfo extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			int size = 1;
			while (!args.isnil(size + 1)) {
				size++;
			}
			if (size == 1 && args.isstring(1)) {
				String Name = args.checkjstring(1);
				if (Bukkit.getPlayer(Name) != null) {
					return get(Bukkit.getPlayer(Name));
				}
				return LuaValue.valueOf("not have player in game");
			} else if (size == 2 && args.isstring(1) && args.isstring(2)) {
				String uuid = args.checkjstring(1);
				String world = args.checkjstring(2);
				if (Bukkit.getWorld(world) != null) {
					for (Entity entity : Bukkit.getWorld(world).getEntities()) {
						if (entity.getUniqueId().toString().equals(uuid)) {
							return get(entity);
						}
					}
					return LuaValue.valueOf("not have Entity this: " + uuid);
				}
				return LuaValue.valueOf("not have world this: " + world);
			} else if (size == 4 && args.isstring(1) && args.isnumber(2) && args.isnumber(3) && args.isnumber(4)) {
				String world = args.checkjstring(1);
				if (Bukkit.getWorld(world) != null) {
					Block block = Bukkit.getWorld(world).getBlockAt(args.checkint(2), args.checkint(3),
							args.checkint(4));
					return get(block);
				}
				return LuaValue.valueOf("not have world this: " + world);
			}
			return LuaValue.valueOf(
					"getInfo(String PlayerName) table | getInfo(String world,int x,int y,int z) table | getInfo(String uuidentity,String world) table");
		}

		public LuaTable get(Inventory inventory) {
			LuaTable table = new LuaTable();
			table.set("Title", inventory.getTitle());
			table.set("Size", inventory.getSize());
			table.set("Type", inventory.getType().toString());
			LuaTable luaTable = new LuaTable();
			for (int x = 0; x < inventory.getSize(); x++) {
				ItemStack item = inventory.getItem(x);
				luaTable.set("s" + x, get(item));
			}
			table.set("Items", luaTable);
			return table;
		}

		@SuppressWarnings("deprecation")
		public LuaTable get(ItemStack item) {
			LuaTable table = new LuaTable();
			if (item == null) {
				item = new ItemStack(Material.AIR);
			}
			table.set("Type", item.getType().toString());
			table.set("setItem", new SetItem(item));
			table.set("TypeID", item.getTypeId());
			table.set("Amount", item.getAmount());
			table.set("Durability", item.getDurability());
			LuaTable tablemeta = new LuaTable();
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName()) {
					tablemeta.set("DisplayName", meta.getDisplayName());
				}
				if (meta.hasLore()) {
					LuaTable tablelore = new LuaTable();
					for (String l : meta.getLore()) {
						tablelore.add(LuaValue.valueOf(l));
					}
					tablemeta.set("Lore", tablelore);
				}

				if (meta.getItemFlags().size() > 0) {
					LuaTable tableflag = new LuaTable();
					for (ItemFlag f : meta.getItemFlags()) {
						tableflag.add(LuaValue.valueOf(f.name()));
					}
					tablemeta.set("Flag", tableflag);
				}

				if (meta.getEnchants().size() > 0) {
					LuaTable tableench = new LuaTable();
					for (Enchantment ench : meta.getEnchants().keySet()) {
						tableench.set(ench.toString(), meta.getEnchantLevel(ench));
					}
					tablemeta.set("Enchant", tableench);
				}

			}
			table.set("Meta", tablemeta);
			return table;
		}
		
		
		@SuppressWarnings("deprecation")
		private LuaTable get(Block block) {
			LuaTable table = new LuaTable();
			table.set("Type", block.getType().toString());
			table.set("TypeID", block.getTypeId());
			table.set("Data", block.getData());
			table.set("Location", get(block.getLocation()));
			table.set("LightLevel", block.getLightLevel());
			table.set("World", block.getWorld().getName());
			return table;
		}
		
		private LuaTable get(Vector vector) {
			LuaTable table = new LuaTable();
			table.set("x", vector.getX());
			table.set("y", vector.getY());
			table.set("z", vector.getZ());
			return table;
		}
		
		private LuaTable get(Location location) {
			LuaTable table = new LuaTable();
			table.set("World", location.getWorld().getName());
			table.set("x", location.getX());
			table.set("y", location.getY());
			table.set("z", location.getZ());
			table.set("pitch", location.getPitch());
			table.set("yaw", location.getYaw());
			return table;
		}

		private LuaTable get(Entity entity) {
			LuaTable table = new LuaTable();
			if (entity instanceof LivingEntity || entity instanceof Player) {
				LivingEntity living = (LivingEntity) entity;
				table.set("setEntity", new SetEntity(living));
				table.set("Name", living.getName());
				table.set("CustomName", living.getCustomName() + "");
				table.set("Health", living.getHealth());
				table.set("MaxHealth", living.getMaxHealth());
				table.set("LastDamage", living.getLastDamage());
				table.set("FireTicks", living.getFireTicks());
				table.set("ItemInHand", get(living.getEquipment().getItemInHand()));
				table.set("ItemInHelmet", get(living.getEquipment().getHelmet()));
				table.set("ItemInChestplate", get(living.getEquipment().getChestplate()));
				table.set("ItemInLeggings", get(living.getEquipment().getLeggings()));
				table.set("ItemInBoots", get(living.getEquipment().getBoots()));
				table.set("Location", get(living.getLocation()));
				if (entity instanceof Player) {
					Player player = (Player) entity;
					table.set("DisplayName", player.getDisplayName());
					table.set("Health", player.getHealth());
					table.set("MaxHealth", player.getMaxHealth());
					table.set("Exp", player.getExp());
					table.set("Level", player.getLevel());
					table.set("LastDamage", player.getLastDamage());
					table.set("FoodLevel", player.getFoodLevel());
					table.set("FireTicks", player.getFireTicks());
					table.set("GameMode", player.getGameMode().toString());
					table.set("PlayerTime", player.getPlayerTime());
					table.set("Inventory", get(player.getInventory()));
					table.set("EyeLocation", get(player.getEyeLocation()));
					table.set("Velocity", get(player.getVelocity()));
				}
			}
			return table;
		}

	}

	public class get extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isnil()) {
				LuaTable table = new LuaTable();
				for (String key : Map.keySet()) {
					table.set(key, Map.get(key));
				}
				return table;
			}
			if (args.isstring()) {
				if (Map.containsKey(args.checkjstring())) {
					return Map.get(args.checkjstring());
				}
				return NIL;
			}
			return LuaValue.valueOf("get(string)");
		}

	}

	public class set extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue str, LuaValue args) {
			if (str.isstring()) {
				Map.put(str.checkjstring(), args);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("set(string key,local args)");
		}

	}

	public class add extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isnil()) {
				return LuaValue.valueOf("args is nil");
			}
			list.add(args);
			return LuaValue.valueOf(true);
		}

	}

	public class remove extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isint()) {
				if (list.size() > args.checkint()) {
					list.remove(args.checkint());
					return LuaValue.valueOf(true);
				}
			} else if (args.isstring()) {
				String m = args.checkjstring();
				if (Map.containsKey(m)) {
					Map.remove(m);
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf("remove([string/int])");
		}

	}

	public class getList extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			LuaTable table = new LuaTable();
			for (LuaValue value : list) {
				table.add(value);
			}
			return table;
		}

	}

}
