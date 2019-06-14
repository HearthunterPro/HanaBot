package mc.CushyPro.HanaBot.Luas;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class SetEntity extends ZeroArgFunction {

	private LivingEntity entity;

	public SetEntity(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public LuaValue call() {
		LuaTable table = new LuaTable();
		table.set("setHealth", new setHealth());
		table.set("setMaxHealth", new setMaxHealth());
		table.set("addPotion", new addPotion());
		table.set("removePotion", new removePotion());
		
		return table;
	}

	public class removePotion extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String type = args.checkjstring();
				PotionEffectType potiontype = PotionEffectType.getByName(type);
				if (potiontype != null) {
					if (entity.hasPotionEffect(potiontype)) {
						entity.removePotionEffect(potiontype);
					}
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have potion type " + type);
			}
			return LuaValue.valueOf("removePotion(string potiontype)");
		}

	}

	public class addPotion extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1) && args.isnumber(2) && args.isnumber(3) && !args.isnil(4)) {
				String type = args.checkjstring(1);
				int am = args.checkint(2);
				int l = args.checkint(3);
				boolean par = args.checkboolean(4);
				PotionEffectType potiontype = PotionEffectType.getByName(type);
				if (potiontype != null) {
					entity.addPotionEffect(new PotionEffect(potiontype, am, l, true, par));
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have potion type " + type);
			}
			return LuaValue.valueOf("addPotion(string potiontype,int time,int level,boolean partical)");
		}

	}

	public class setMaxHealth extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isnumber()) {
				double health = args.checkdouble();
				entity.setMaxHealth(health);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setMaxHealth(double health)");
		}

	}

	public class setHealth extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isnumber()) {
				double health = args.checkdouble();
				entity.setHealth(health);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setHealth(double health)");
		}

	}

}
