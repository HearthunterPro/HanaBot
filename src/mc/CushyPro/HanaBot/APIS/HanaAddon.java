package mc.CushyPro.HanaBot.APIS;

import org.bukkit.Server;
import org.luaj.vm2.LuaValue;

import mc.CushyPro.HanaBot.Data;

public class HanaAddon {

	public void onEnable() {

	}

	public void onDisable() {

	}

	public Server getServer() {
		return Data.plugin.getServer();
	}

	public void AddLibrary(String modname, LuaValue module) {
		Data.loadlua.readCodeHana.loadLibrary(modname, module);
	}

}
