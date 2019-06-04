package mc.CushyPro.HanaBot.Luas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;
import mc.CushyPro.HanaBot.LuaFile;

public class EventCall extends ZeroArgFunction {

	public static List<EventCall> listevent = new ArrayList<EventCall>();

	public static LuaTable table;

	public static void loadall(String event) {
		for (EventCall eventcall : listevent) {
			if (eventcall.eventcall != null) {
				if (!eventcall.eventcall.equalsIgnoreCase(event)) {
					continue;
				}
			}
			eventcall.load();
		}
	}

	private LuaFunction luaFunction;
	protected String Code;
	protected LuaFile luafile;
	protected String eventcall;

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

	public void load() {
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
			luafile.load(globals);
		} else if (this.luaFunction != null) {
			if (this.Code == null) {
				LoadCodeFuntion();
			}
			globals.load(Code).call();
		}
	}

	public class pull extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue args, LuaValue args2) {
			if (args.isfunction() || args.isstring()) {
				EventCall event = null;
				if (args.isstring()) {
					LuaFile k = Data.loadlua.getFile(args.checkjstring());
					if (k != null) {
						event = new EventCall(k);
					} else {
						return LuaValue.valueOf("not have File : " + args.checkjstring());
					}
				} else if (args.isfunction()) {
					event = new EventCall(args.checkfunction());
				}

				if (event == null) {
					return LuaValue.valueOf(false);
				}
				if (args2.isstring()) {
					event.setEvent(args2.checkjstring());
				}
				listevent.add(event);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("pull(string filename,string event)");
		}

	}

}
