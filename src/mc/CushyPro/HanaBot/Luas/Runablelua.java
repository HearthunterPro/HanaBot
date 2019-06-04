package mc.CushyPro.HanaBot.Luas;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;

public class Runablelua extends ZeroArgFunction {

	public static HashMap<UUID, Runablelua> listthread = new HashMap<UUID, Runablelua>();

	public Runablelua() {
		// TODO Auto-generated constructor stub
	}

	private UUID uuid = UUID.randomUUID();
	private LuaFunction function;
	private int delay;
	private int period;
	private BukkitTask task;

	public Runablelua(int delay, int time) {
		this.delay = delay;
		this.period = time;
	}

	public void setFun(LuaFunction value) {
		function = value;
	}

	public void stop() {
		if (Bukkit.getScheduler().isQueued(task.getTaskId())) {
			task.cancel();
		}
	}

	public boolean isrun() {
		if (Bukkit.getScheduler().isQueued(task.getTaskId())) {
			return true;
		}
		return false;
	}

	public void run() {
		task = new BukkitRunnable() {

			@Override
			public void run() {
				try {
					function.call();
				} catch (Exception e) {
					e.printStackTrace();
					cancel();
				}
			}
		}.runTaskTimer(Data.plugin, delay, period);
	}

	@Override
	public LuaValue call() {
		LuaTable table = new LuaTable();
		table.set("create", new createthread());
		table.set("stop", new stopthread());
		table.set("isrun", new isrun());
		table.set("exitall", new exitall());
		table.set("run", new runthread());
		return table;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public class exitall extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			if (args.isstring(1)) {
				int size = 0;
				while (!args.isnil(size + 1)) {
					String uid = args.checkjstring(size + 1);
					UUID uuid = UUID.fromString(uid);
					if (listthread.containsKey(uuid)) {
						if (listthread.get(uuid).isrun()) {
							return LuaValue.valueOf(false);
						}
					} else {
						return LuaValue.valueOf("not have thread " + uid);
					}
					size++;
				}
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("exitall(string uuidthread, ...) boolean");
		}

	}

	public class isrun extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				UUID uuid = UUID.fromString(args.checkjstring());
				if (listthread.containsKey(uuid)) {
					if (listthread.get(uuid).isrun()) {
						return LuaValue.valueOf(true);
					}
					return LuaValue.valueOf(false);
				}
				return LuaValue.valueOf("not have thread uuid " + uuid.toString());
			}
			return LuaValue.valueOf("isrun(string uuidthread) boolean");
		}

	}

	public class stopthread extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				UUID uuid = UUID.fromString(args.checkjstring());
				if (listthread.containsKey(uuid)) {
					listthread.get(uuid).stop();
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have thread uuid " + uuid.toString());
			}
			return LuaValue.valueOf("stop(string uuidthread) string");
		}

	}

	public class createthread extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue delay, LuaValue time) {
			if (delay.isint() && time.isint()) {
				int d = delay.checkint();
				int t = time.checkint();
				Runablelua thread = new Runablelua(d, t);
				listthread.put(thread.getUuid(), thread);
				return LuaValue.valueOf(thread.getUuid().toString());
			}
			return LuaValue.valueOf("create(int delay,int time) string");
		}

	}

	public class runthread extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue fun) {
			if (args.isstring() && fun.isfunction()) {
				UUID uuid = UUID.fromString(args.checkjstring());
				if (listthread.containsKey(uuid)) {
					LuaFunction chunk = fun.checkfunction();
					listthread.get(uuid).setFun(chunk);
					listthread.get(uuid).run();
					return LuaValue.valueOf(true);
				}
				return LuaValue.valueOf("not have thread uuid " + uuid.toString());
			}
			return LuaValue.valueOf("run(string uuidthread,function fun) boolean");
		}
	}

	public static void stopall() {
		for (Runablelua runableLua : listthread.values()) {
			runableLua.stop();
		}

	}

}
