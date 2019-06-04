package mc.CushyPro.HanaBot.Luas;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;

public class HanaChat extends ZeroArgFunction {

	private String Prefix = "Prefix";

	@Override
	public LuaValue call() {
		Prefix = Data.getColor(Data.cfg.getString("Prefix"));
		LuaTable library = new LuaTable();
		library.set("setlevel", new setlevel());
		library.set("send", new send());
		library.set("sendPlayer", new sendPlayer());
		return library;
	}

	public class setlevel extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String name = args.checkjstring();
				Player player = Bukkit.getPlayer(name);
				if (player != null) {
					if (Data.mapcfg.containsKey(player)) {
						YamlConfiguration cfg = Data.mapcfg.get(player);
						String da = Data.getlevel(cfg.getInt("Level"));
						Prefix = Prefix.replace("%lvl%", da);
					}
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("player not online");
			} else if (args.isnumber()) {
				int a = args.checkint();
				String da = Data.getlevel(a);
				Prefix = Prefix.replace("%lvl%", da);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setlevel(number/string level/playername)");
		}

	}

	public class send extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue message) {
			Prefix = Prefix.replace("%lvl%", "&f");
			Bukkit.broadcastMessage(Data.getColor(Prefix) + " " + Data.getColor(message.checkjstring()));
			return NONE;
		}
	}

	public class sendPlayer extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue PlayerName, LuaValue Message) {
			if (!PlayerName.isstring()) {
				return LuaValue.valueOf("sendPlayer(String playername,String msg)");
			}
			Player player = Bukkit.getPlayer(PlayerName.checkjstring());
			if (player == null) {
				return LuaValue.valueOf("Player is not online");
			}
			String m = Data.getColor(Prefix) + " " + Data.getColor(Message.checkjstring());
			player.sendMessage(m);
			Bukkit.getConsoleSender().sendMessage(player.getName() + " send -> " + m);

			return NONE;
		}

	}

}
