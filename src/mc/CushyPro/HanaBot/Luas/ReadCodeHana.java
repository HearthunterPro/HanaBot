package mc.CushyPro.HanaBot.Luas;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import mc.CushyPro.HanaBot.Data;
import mc.CushyPro.HanaBot.LuaFile;

public class ReadCodeHana {

	Globals globals = null;
	public Server server;
	public Config config;
	public DataBase database;
	public HanaChat hanachat;
	public SystemFile systemFile;
	public EventCall eventCall;
	public Runablelua thread;

	public ReadCodeHana() {
		globals = JsePlatform.standardGlobals();
		config = loadconfig();
		database = loadDataBase();
		hanachat = loadHanaChat();
		server = loadServer();
		systemFile = loadsystemFile();
		eventCall = loadeventCall();
		thread = loadthread();

		LuaTable os = globals.get("os").checktable();
		os.set("loadLibrary", new loadLibrary());
		os.set("loadlua", new loadlua());

		LuaTable st = globals.get("string").checktable();
		st.set("startswith", new startsWith());
		st.set("endswith", new endsWith());
		st.set("contains", new contains());
		st.set("equalsignorecase", new equalsIgnoreCase());

		globals.set("loadfile", new loadlua());
		globals.set("print", new printsys(null));

		globals.set("sleep", new sleep());

		loadChatColor();

	}

	public class equalsIgnoreCase extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring()) {
				String m = args.checkjstring();
				String ms = args2.checkjstring();
				if (m.equalsIgnoreCase(ms)) {
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf(false);
		}

	}

	public class contains extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring()) {
				String m = args.checkjstring();
				String ms = args2.checkjstring();
				if (m.contains(ms)) {
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf(false);
		}

	}

	public class endsWith extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring()) {
				String m = args.checkjstring();
				String ms = args2.checkjstring();
				if (m.endsWith(ms)) {
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf(false);
		}

	}

	public class startsWith extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring()) {
				String m = args.checkjstring();
				String ms = args2.checkjstring();
				if (m.startsWith(ms)) {
					return LuaValue.valueOf(true);
				}
			}
			return LuaValue.valueOf(false);
		}

	}

	public class sleep extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (!args.isnil() && args.isnumber()) {
				double m = args.checkdouble();
				try {
					Thread.sleep((long) (m * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf(false);
		}
	}

	private void loadChatColor() {
		LuaTable table = new LuaTable();
		table.set("BLACK", ChatColor.BLACK.toString());
		table.set("DARK_BLUE", ChatColor.DARK_BLUE.toString());
		table.set("DARK_GREEN", ChatColor.DARK_GREEN.toString());
		table.set("DARK_AQUA", ChatColor.DARK_AQUA.toString());
		table.set("DARK_RED", ChatColor.DARK_RED.toString());
		table.set("DARK_PURPLE", ChatColor.DARK_PURPLE.toString());
		table.set("GOLD", ChatColor.GOLD.toString());
		table.set("GRAY", ChatColor.GRAY.toString());
		table.set("DARK_GRAY", ChatColor.DARK_GRAY.toString());
		table.set("BLUE", ChatColor.BLUE.toString());
		table.set("GREEN", ChatColor.GREEN.toString());
		table.set("AQUA", ChatColor.AQUA.toString());
		table.set("RED", ChatColor.RED.toString());
		table.set("LIGHT_PURPLE", ChatColor.LIGHT_PURPLE.toString());
		table.set("YELLOW", ChatColor.YELLOW.toString());
		table.set("WHITE", ChatColor.WHITE.toString());
		table.set("MAGIC", ChatColor.MAGIC.toString());
		table.set("BOLD", ChatColor.BOLD.toString());
		table.set("UNDERLINE", ChatColor.UNDERLINE.toString());
		table.set("ITALIC", ChatColor.ITALIC.toString());
		table.set("STRIKETHROUGH", ChatColor.STRIKETHROUGH.toString());
		table.set("RESET", ChatColor.RESET.toString());
		table.set("get", new getcolor());
		globals.set("ChatColor", table);
	}

	public class getcolor extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			String m = args.checkjstring();
			return LuaValue.valueOf("§" + m);
		}

	}

	public class printsys extends VarArgFunction {

		private Player player;

		public printsys(Player player) {
			this.player = player;
		}

		public String getType(LuaValue args) {
			String m = "";
			if (args.istable()) {
				m = "{";
				LuaTable table = args.checktable();
				for (LuaValue k : table.keys()) {
					m = m + k.checkjstring() + " = " + args.get(k) + ",";
				}
				m = m + "}";
			} else {
				m = args.checkjstring();
			}

			return m;
		}

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isnil(1)) {
				return LuaValue.valueOf("print(string args, ...)");
			}
			String msg = ChatColor.RESET + getType(args.checkvalue(1));
			int x = 1;
			while (!args.isnil(x + 1)) {
				msg = msg + "   " + ChatColor.RESET + getType(args.checkvalue(x + 1));
				x++;
			}
			if (player == null) {
				Bukkit.getConsoleSender().sendMessage(msg);
			} else {
				player.sendMessage(msg);
			}
			return NONE;
		}

	}

	public void load(Player player, String filename) {
		try {
			globals.set("print", new printsys(player));
			File file = Data.getFile("system//" + filename);
			LuaValue chunk = globals.loadfile(file.getPath());
			chunk.call(file.getPath());
			globals.set("print", new printsys(null));
		} catch (LuaError e) {
			if (player != null) {
				String error = e.getMessage();
				error = error.substring(error.indexOf("LuaError:") + "LuaError:".length() + 1);
				error = error.replace("plugins\\HanaBot\\system\\", "");
				player.sendMessage("" + error);
			} else {
				e.printStackTrace();
			}
		}
	}

	public class loadlua extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String namefile = args.checkjstring();
				if (Data.loadlua.hasFile(namefile)) {
					LuaFile luafile = Data.loadlua.getFile(namefile);
					luafile.loadfile(globals);
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have File " + namefile);
			}
			return LuaValue.valueOf("loadlua(string file)");
		}
	}

	public class loadLibrary extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring() && args2.isstring()) {
				String modname = args.checkjstring();
				String namefile = args2.checkjstring();

				if (Data.loadlua.hasFile(namefile)) {
					LuaFile luafile = Data.loadlua.getFile(namefile);
					preloadLuaModule(modname, luafile.getFile());
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have File " + namefile);
			}
			return LuaValue.valueOf("loadLibrary(string modname,string file)");
		}

	}

	public Runablelua loadthread() {
		Runablelua runable = new Runablelua();
		loadLibrary("runable", runable);
		return runable;
	}

	public void loadLibrary(String modname, LuaValue module) {
		globals.get("package").get("preload").set(modname, module);
	}

	public Server loadServer() {
		Server server = new Server();
		loadLibrary("server", server);
		return server;
	}

	public EventCall loadeventCall() {
		EventCall eventCall = new EventCall();
		loadLibrary("event", eventCall);
		return eventCall;
	}

	public DataBase loadDataBase() {
		DataBase database = new DataBase();
		loadLibrary("database", database);
		return database;
	}

	public HanaChat loadHanaChat() {
		HanaChat hanachat = new HanaChat();
		loadLibrary("hanachat", hanachat);
		return hanachat;
	}

	public Config loadconfig() {
		Config config = new Config();
		loadLibrary("config", config);
		return config;
	}

	public SystemFile loadsystemFile() {
		SystemFile systemFile = new SystemFile();
		loadLibrary("systemfile", systemFile);
		return systemFile;
	}

	public Globals getGlobals() {
		return globals;
	}

	public void ReadFile(File file) {
		LuaValue chunk = globals.loadfile(file.getPath());
		chunk.call(LuaValue.valueOf(file.getPath()));
	}

	public LuaValue preloadLuaModule(String modname, File file) {
		LuaValue module = globals.loadfile(file.getPath());
		loadLibrary(modname, module);
		return globals.get(modname);
	}

	public void end() {

	}

	public void readcode(String code) {
		globals.load(code).call();
	}

}
