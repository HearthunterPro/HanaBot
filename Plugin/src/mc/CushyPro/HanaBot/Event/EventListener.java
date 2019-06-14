package mc.CushyPro.HanaBot.Event;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import mc.CushyPro.HanaBot.Luas.EventCall;

public class EventListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void Quit(PlayerCommandPreprocessEvent e) {
		HashMap<String, LuaValue> map = new HashMap<String, LuaValue>();
		map.put("PlayerName", LuaValue.valueOf(e.getPlayer().getName()));
		map.put("Command", LuaValue.valueOf(e.getMessage()));
		map.put("Cancel", LuaValue.valueOf(e.isCancelled()));

		EventCall.setTable("PlayerCommand", map);
		// load
		EventCall.loadall("PlayerCommand");
		LuaTable table = EventCall.table;

		e.setMessage(table.get("Command").checkjstring());
		e.setCancelled(table.get("Cancel").checkboolean());

		EventCall.table = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void Quit(PlayerQuitEvent e) {
		HashMap<String, LuaValue> map = new HashMap<String, LuaValue>();
		map.put("PlayerName", LuaValue.valueOf(e.getPlayer().getName()));
		map.put("Message", LuaValue.valueOf(e.getQuitMessage()));

		EventCall.setTable("PlayerQuitEvent", map);

		// load
		EventCall.loadall("PlayerQuitEvent");

		LuaTable table = EventCall.table;

		e.setQuitMessage((table.get("Message").checkjstring()));

		EventCall.table = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void Join(PlayerJoinEvent e) {
		HashMap<String, LuaValue> map = new HashMap<String, LuaValue>();
		map.put("PlayerName", LuaValue.valueOf(e.getPlayer().getName()));
		map.put("Message", LuaValue.valueOf(e.getJoinMessage()));

		EventCall.setTable("PlayerJoinEvent", map);

		// load
		EventCall.loadall("PlayerJoinEvent");

		LuaTable table = EventCall.table;

		e.setJoinMessage((table.get("Message").checkjstring()));

		EventCall.table = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void Chat(AsyncPlayerChatEvent e) {
		HashMap<String, LuaValue> map = new HashMap<String, LuaValue>();
		map.put("PlayerName", LuaValue.valueOf(e.getPlayer().getName()));
		map.put("Message", LuaValue.valueOf(e.getMessage()));
		map.put("Cancel", LuaValue.valueOf(e.isCancelled()));

		EventCall.setTable("PlayerChatEvent", map);
		// load
		EventCall.loadall("PlayerChatEvent");
		LuaTable table = EventCall.table;

		e.setMessage(table.get("Message").checkjstring());
		e.setCancelled(table.get("Cancel").checkboolean());
		
		EventCall.table = null;
	}

}
