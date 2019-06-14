package mc.CushyPro.HanaBot.Luas;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.ActionBar;
import mc.CushyPro.HanaBot.Chat;
import mc.CushyPro.HanaBot.Data;
import mc.CushyPro.HanaBot.Title;

public class Server extends ZeroArgFunction {

	@Override
	public LuaValue call() {
		LuaTable library = new LuaTable();
		library.set("kick", new kick());
		library.set("command", new command());
		library.set("hasPermission", new hasPermission());
		library.set("isOp", new isOp());
		library.set("getOnlinePlayer", new getOnlinePlayer());
		library.set("getWorlds", new getWorlds());
		library.set("teleport", new teleport());
		library.set("tellraw", new tellraw());
		library.set("actionbar", new actionbar());
		library.set("title", new title());
		library.set("calculator", new calculator());
		return library;
	}

	public class calculator extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String m = args.checkjstring();
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine engine = mgr.getEngineByName("JavaScript");
				try {
					return LuaValue.valueOf(new BigDecimal(engine.eval(m).toString()).doubleValue());
				} catch (ScriptException e) {
					e.printStackTrace();
				}
			}
			return LuaValue.valueOf("calculator(string args) double");
		}

	}

	public class title extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1) && args.isstring(2)) {
				String name = args.checkjstring(1);
				String t = args.checkjstring(2);
				Player player = Bukkit.getPlayer(name);
				if (player != null) {
					try {
						Title title = new Title(t);
						if (args.isstring(3)) {
							title.setSubtitle(args.checkjstring(3));
						}
						if (args.isnumber(4)) {
							title.setFadeInTime(args.checkint(4));
						}
						if (args.isnumber(5)) {
							title.setStayTime(args.checkint(5));
						}
						if (args.isnumber(6)) {
							title.setFadeOutTime(args.checkint(6));
						}
						title.send(player);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have player in game");
			}
			return LuaValue.valueOf(
					"actionbar(string playername,string title,string subtitle,int fadein,int stay,int fadeout)");
		}

	}

	public class actionbar extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring() && args2.isstring()) {
				String name = args.checkjstring();
				String msg = args2.checkjstring();
				Player player = Bukkit.getPlayer(name);
				if (player != null) {
					try {
						ActionBar.sendActionbar(player, msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have player in game");
			}
			return LuaValue.valueOf("actionbar(string playername,string args)");
		}

	}

	public class tellraw extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring() && args2.isstring()) {
				String name = args.checkjstring();
				String msg = args2.checkjstring();
				Player player = Bukkit.getPlayer(name);
				if (player != null) {
					try {
						Chat.sendChat(player, msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have player in game");
			}
			return LuaValue.valueOf("tellraw(string playername,string args)");
		}

	}

	public class teleport extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1) && args.isstring(2) && args.isnumber(3) && args.isnumber(4) && args.isnumber(5)) {
				String name = args.checkjstring(1);
				String world = args.checkjstring(2);
				double x = args.checkdouble(3);
				double y = args.checkdouble(4);
				double z = args.checkdouble(5);
				Player player = Bukkit.getPlayer(name);
				double picth = player.getLocation().getPitch();
				double yaw = player.getLocation().getYaw();
				if (args.isnumber(6)) {
					picth = args.checkdouble(6);
				}
				if (args.isnumber(7)) {
					yaw = args.checkdouble(7);
				}
				if (player != null) {
					World w = Bukkit.getWorld(world);
					if (w != null) {
						player.teleport(new Location(w, x, y, z, (float) yaw, (float) picth));
					}
					return LuaValue.valueOf("not have world " + world);
				}
			}
			return LuaValue.valueOf(
					"teleport(string playerName,string world,double x,double y,double z,double pitch,double yaw)");
		}

	}

	public class getWorlds extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			LuaTable table = new LuaTable();
			for (World world : Bukkit.getWorlds()) {
				table.add(LuaValue.valueOf(world.getName()));
			}
			return table;
		}

	}

	public class getOnlinePlayer extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			LuaTable table = new LuaTable();
			for (Player player : Bukkit.getOnlinePlayers()) {
				table.add(LuaValue.valueOf(player.getName()));
			}
			return table;
		}

	}

	public class kick extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1) && args.isstring(2)) {
				Player player = Bukkit.getPlayer(args.checkjstring(1));
				if (player != null) {
					new BukkitRunnable() {

						@Override
						public void run() {
							try {
								player.kickPlayer(Data.getColor(args.checkjstring(2)));
							} catch (Exception e) {

							}
							cancel();
						}
					}.runTaskLater(Data.plugin, 0);
					return LuaValue.valueOf(true);
				}
				return LuaValue.varargsOf(
						new LuaValue[] { LuaValue.valueOf(false), LuaValue.valueOf("Player is not online") });
			}
			return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(false),
					LuaValue.valueOf("kick(String PlayerName,String message)") });
		}

	}

	public class command extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isstring() && args2.isstring()) {
				String t = args.checkjstring();
				String command = args2.checkjstring();
				if (t.equalsIgnoreCase("console")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
					return LuaValue.valueOf(true);
				} else {
					Player player = Bukkit.getPlayer(args.checkjstring(1));
					if (player != null) {
						Data.Command(player, command);
						return LuaValue.valueOf(true);
					}
					return LuaValue.valueOf("player is not online");
				}
				
			}
			return LuaValue.valueOf("kick(String PlayerName/console,String cpmmand)");
		}

	}

	public class hasPermission extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1) && args.isstring(2)) {
				Player player = Bukkit.getPlayer(args.checkjstring(1));
				if (player != null) {
					if (player.hasPermission(args.checkjstring(2))) {
						return LuaValue.valueOf(true);
					} else {
						return LuaValue.valueOf(false);
					}
				}
				return LuaValue.varargsOf(
						new LuaValue[] { LuaValue.valueOf(false), LuaValue.valueOf("Player is not online") });
			}
			return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(false),
					LuaValue.valueOf("hasPermission(String PlayerName,String permission)") });
		}

	}

	public class isOp extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			// TODO Auto-generated method stub

			if (args.isstring(1)) {
				Player player = Bukkit.getPlayer(args.checkjstring(1));
				if (player != null) {
					if (player.isOp()) {
						return LuaValue.valueOf(true);
					} else {
						return LuaValue.valueOf(false);
					}
				}
				return LuaValue.varargsOf(LuaValue.valueOf(false), LuaValue.valueOf("Player is not online"));
			}
			return LuaValue
					.varargsOf(new LuaValue[] { LuaValue.valueOf(false), LuaValue.valueOf("isOp(String playerName)") });
		}

	}

}
