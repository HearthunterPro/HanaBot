package mc.CushyPro.HanaBot.Event;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.CushyPro.HanaBot.Data;

public class DataPlayerEventLoad implements Listener {

	@EventHandler
	public void Quit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (Data.mapcfg.containsKey(player)) {
			File file = Data.getFile("data//player//" + player.getName() + ".yml");
			try {
				Data.mapcfg.get(player).save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Data.mapcfg.remove(player);
		}
	}

	@EventHandler
	public void Join(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		Data.loadplayerdata(player);
	}

}
