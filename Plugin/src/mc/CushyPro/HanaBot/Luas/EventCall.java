package mc.CushyPro.HanaBot.Luas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;
import mc.CushyPro.HanaBot.LuaFile;

public class EventCall extends ZeroArgFunction {

	public static List<EventCall> listevent = new ArrayList<EventCall>();
	public static List<EventCall> priorityListfrist = new ArrayList<EventCall>();
	public static List<EventCall> priorityList = new ArrayList<EventCall>();
	public static boolean loadsucc = false;

	public static LuaTable table;

	public static void loadallpriority(boolean k) {
		List<EventCall> list = new ArrayList<EventCall>();
		for (EventCall eventcall : listevent) {
			if (eventcall.seteventsuc == k) {
				list.add(eventcall);
			}
		}

		while (list.size() > 1) {
			EventCall ds = list.get(0);
			for (EventCall ec : list) {
				if (ds.priority > ec.priority) {
					ds = ec;
				}
			}

			if (k) {
				priorityListfrist.add(ds);
			} else {
				priorityList.add(ds);
			}
			list.remove(ds);
		}
		
		if (k) {
			priorityListfrist.addAll(list);
		} else {
			priorityList.addAll(list);
		}
	}

	public static void loadall(String event, boolean fast) {
		if (loadsucc == false) {
			loadallpriority(true);
			loadallpriority(false);
			loadsucc = true;
		}
		if (fast) {
			for (EventCall eventcall : priorityListfrist) {
				if (eventcall.eventcall != null) {
					if (!eventcall.eventcall.equalsIgnoreCase(event)) {
						continue;
					}
				}
				eventcall.load(fast);
			}
		} else {
			for (EventCall eventcall : priorityList) {
				if (eventcall.eventcall != null) {
					if (!eventcall.eventcall.equalsIgnoreCase(event)) {
						continue;
					}
				}
				eventcall.load(fast);
			}
		}
	}

	private LuaFunction luaFunction;
	protected String Code;
	protected LuaFile luafile;
	protected String eventcall;
	private boolean seteventsuc;
	private double priority;

	public EventCall() {

	}

	public EventCall(LuaFile luafile) {
		this.luafile = luafile;
	}

	public EventCall(LuaFunction luaFunction) {
		this.luaFunction = luaFunction;
	}

	public void setEvent(String event) {
		this.eventcall = event;
	}

	private void LoadCodeFuntion() {
		String m = this.luaFunction.toString();
		m = m.replace("function: @plugins\\HanaBot\\system\\", "");
		String line = m.substring(m.indexOf(":") + 1, m.length());
		m = m.substring(0, m.indexOf(":"));

		int start = Integer.parseInt(line.substring(0, line.indexOf("-")));
		int end = Integer.parseInt(line.substring(line.indexOf("-") + 1, line.length()));

		LuaFile luaFile = Data.loadlua.getFile(m);
		List<String> codes = luaFile.getcodes(start, end);

		String sm = codes.get(0);

		String em = codes.get(codes.size() - 1);

		sm = sm.substring(sm.indexOf("function") + "function".length(), sm.length());
		sm = sm.substring(sm.indexOf("()") + "()".length(), sm.length());
		codes.set(0, sm);
		em = em.substring(0, em.indexOf("end"));
		codes.set(codes.size() - 1, em);
		Code = "";
		for (String asd : codes) {
			Code = Code + asd + "\n";
		}
	}

	@Override
	public LuaValue call() {
		LuaTable library = new LuaTable();
		library.set("pull", new pull());
		return library;
	}

	public static void setTable(String event, HashMap<String, LuaValue> map) {
		LuaTable table = new LuaTable();
		table.set("Event", event);
		for (String k : map.keySet()) {
			table.set(k, map.get(k));
		}
		EventCall.table = table;
	}

	public void load(boolean fast) {
		if (table == null) {
			return;
		}
		String e = table.get("Event").checkjstring();
		if (this.eventcall != null) {
			if (!this.eventcall.equalsIgnoreCase(e)) {
				return;
			}
		}

		ReadCodeHana readCodeHana = Data.loadlua.readCodeHana;
		Globals globals = readCodeHana.getGlobals();
		globals.set("eventcall", table);

		if (luafile != null) {
			luafile.load(globals, (fast == false));
		} else if (this.luaFunction != null) {
			if (this.Code == null) {
				LoadCodeFuntion();
			}
			globals.load(Code).call();
		}
	}

	public boolean isSeteventsuc() {
		return seteventsuc;
	}

	public void setSeteventsuc(boolean seteventsuc) {
		this.seteventsuc = seteventsuc;
	}

	public class pull extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {

			if (args.isfunction(1) || args.isstring(1)) {
				EventCall event = null;
				if (args.isstring(1)) {
					LuaFile k = Data.loadlua.getFile(args.checkjstring(1));
					if (k != null) {
						event = new EventCall(k);
					} else {
						return LuaValue.valueOf("not have File : " + args.checkjstring(1));
					}
				} else if (args.isfunction(1)) {
					event = new EventCall(args.checkfunction(1));
				}

				if (event == null) {
					return LuaValue.valueOf(false);
				}
				if (args.isstring(2)) {
					event.setEvent(args.checkjstring(2));
				}
				if (!args.isnil(3)) {
					event.setSeteventsuc(args.checkboolean(3));
				}
				if (args.isnumber(4)) {
					event.priority(args.checknumber(4).checkdouble());
				}
				listevent.add(event);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("pull(string filename,string event,boolean setevent,number priority)");
		}

	}

	public double getpriority() {
		return priority;
	}

	public void priority(double number) {
		priority = number;
	}

}
