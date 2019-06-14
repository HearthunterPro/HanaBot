package mc.CushyPro.HanaBot.Luas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;

public class SystemFile extends ZeroArgFunction {

	@Override
	public LuaValue call() {
		LuaTable library = new LuaTable();
		library.set("loadyml", new loadyml());
		return library;
	}

	public class loadyml extends OneArgFunction {

		File file;
		YamlConfiguration cfg;

		@Override
		public LuaValue call(LuaValue file) {
			this.file = Data.getFile("data//" + file.checkjstring() + ".yml");
			this.cfg = YamlConfiguration.loadConfiguration(this.file);
			LuaTable library = new LuaTable();
			library.set("set", new set());
			library.set("getString", new getString());
			library.set("getNumber", new getNumber());
			library.set("getBoolean", new getBoolean());
			library.set("getTable", new getTable());
			library.set("save", new save());
			return library;
		}
		
		public class getTable extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue args) {
				if (args.isstring()) {
					String m = args.checkjstring();
					if (cfg.isSet(m)) {
						List<String> list = cfg.getStringList(m);
						LuaTable table = new LuaTable();
						for(String l : list) {
							table.add(LuaValue.valueOf(l));
						}
						return table;
					}
					return NIL;
				}
				return LuaValue.valueOf("getTable(String args) String");
			}

		}

		public class getBoolean extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue args) {
				if (args.isstring()) {
					String m = args.checkjstring();
					if (cfg.isSet(m)) {
						return LuaValue.valueOf(cfg.getBoolean(m));
					}
					return NIL;
				}
				return LuaValue.valueOf("getBoolean(String args) String");
			}

		}

		public class getNumber extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue args) {
				if (args.isstring()) {
					String m = args.checkjstring();
					if (cfg.isSet(m)) {
						return LuaValue.valueOf(cfg.getDouble(m));
					}
					return NIL;
				}
				return LuaValue.valueOf("getNumber(String args) String");
			}

		}

		public class getString extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue args) {
				if (args.isstring()) {
					String m = args.checkjstring();
					if (cfg.isSet(m)) {
						return LuaValue.valueOf(cfg.getString(m));
					}
					return NIL;
				}
				return LuaValue.valueOf("getString(String args) String");
			}

		}

		public class save extends ZeroArgFunction {

			@Override
			public LuaValue call() {
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return LuaValue.valueOf(true);
			}

		}

		public class set extends TwoArgFunction {

			@Override
			public LuaValue call(LuaValue args, LuaValue arg2) {
				if (args.isstring()) {
					String arg0 = args.checkjstring();
					if (arg2.isstring()) {
						cfg.set(arg0, arg2.checkjstring());
						return LuaValue.valueOf(true);
					} else if (arg2.isboolean()) {
						cfg.set(arg0, arg2.checkboolean());
						return LuaValue.valueOf(true);
					} else if (arg2.isnumber()) {
						cfg.set(arg0, arg2.checkdouble());
						return LuaValue.valueOf(true);
					} else if (arg2.istable()) {
						LuaTable table = arg2.checktable();
						List<String> list = new ArrayList<String>();
						for (LuaValue lv : table.keys()) {
							list.add(table.get(lv).checkjstring());
						}
						cfg.set(arg0, list);
						return LuaValue.valueOf(true);
					}
				}
				return LuaValue.valueOf("set(String args,[String/number/boolean/table] args2)");
			}

		}
	}

}
