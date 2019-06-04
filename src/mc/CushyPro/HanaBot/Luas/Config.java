package mc.CushyPro.HanaBot.Luas;

import java.util.Set;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import mc.CushyPro.HanaBot.Data;
import mc.CushyPro.HanaBot.MessageType;

public class Config extends ZeroArgFunction {
	
	
	
	@Override
	public LuaValue call() {
		LuaTable library = new LuaTable();
		library.set("prefix", new prefix());
		library.set("maxlevel", new maxlevel());
		library.set("level", new level());
		library.set("message", new message());
		return library;
	}
	
	public class message extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			LuaTable table = new LuaTable();
			for(MessageType t : MessageType.values()) {
				table.set(t.getID(), t.toString());
			}
			return table;
		}
		
	}
	
	public class prefix extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Data.cfg.getString("Prefix"));
		}
		
	}
	
	
	public class maxlevel extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			String ml = Data.cfg.getString("MaxLevel");
			int l = ml.indexOf(":");
			int min = Integer.parseInt(ml.substring(0, l));
			int max = Integer.parseInt(ml.substring(l+1, ml.length()));
			LuaTable table = new LuaTable();
			table.set("min", min);
			table.set("max", max);
			return table;
		}
	}
	
	
	public class level extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			Set<String> l = Data.cfg.getConfigurationSection("Level").getKeys(false);
			
			LuaTable table = new LuaTable();
			for(String m : l) {
				table.set(m, Data.cfg.getString("Level." + m));
			}
			return table;
		}
	}

}
