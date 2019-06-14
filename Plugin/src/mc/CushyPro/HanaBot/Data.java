package mc.CushyPro.HanaBot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Data {

	public static Main plugin;

	public static String PluginName;

	public static String Version;

	public static String Author;

	public static YamlConfiguration cfg;

	public static File file;

	public static LoadLua loadlua;

	public static HashMap<Player, YamlConfiguration> mapcfg = new HashMap<Player, YamlConfiguration>();

	public static File getFile(String Name) {
		return new File(plugin.getDataFolder(), Name);
	}

	public static void loadFileConfig() {
		file = getFile("config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	public static void LoadConfig() {
		if (true) {
			for (MessageType type : MessageType.values()) {
				if (!cfg.isSet("MessageType." + type.getID())) {
					cfg.set("MessageType." + type.getID(), type.toString());
				}
			}
		}

		if (!cfg.isSet("Prefix")) {
			cfg.set("Prefix", "&c[%lvl%❤&c]&8[&dAdmin&8] &4&l!&bHana>&r");
		}

		if (!cfg.isSet("MaxLevel")) {
			cfg.set("MaxLevel", "-50:100");
		}

		if (!cfg.isSet("Level")) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("-50", "&8");
			map.put("-25", "&0");
			map.put("-10", "&7");
			map.put("0", "&f");
			map.put("10", "&4");
			map.put("20", "&c");
			map.put("40", "&6");
			map.put("60", "&a");
			map.put("70", "&b");
			map.put("80", "&3");
			map.put("90", "&5");
			map.put("100", "&d");
			cfg.set("Level", map);
		}

		saveConfig();
	}

	public static String getlevel(int level) {
		String d = "&f";
		for (String key : cfg.getConfigurationSection("Level").getKeys(false)) {
			int k = Integer.parseInt(key);
			if (k == level) {
				d = cfg.getString("Level." + key);
			} else if (k > level) {
				d = cfg.getString("Level." + k);
			}
		}
		return d;
	}

	public static void loadplayerdata(Player player) {
		File file = Data.getFile("data//player//" + player.getName() + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if (!cfg.isSet("Level")) {
			cfg.set("Level", 0);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mapcfg.put(player, cfg);
	}

	public static void saveConfig() {
		if (cfg != null && file != null) {
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void Command(Player p, String command) {
		command = command.replace("<player>", p.getName());
		if (command.startsWith("[run:")) {
			int run = getRun(command);
			int line = command.indexOf("]");

			command = command.substring(line + 1);

			if (command.startsWith("[op]")) {
				command = command.replace("[op]", "");
				final String com = command;
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(run * 50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (p.isOp()) {
							Bukkit.dispatchCommand(p, com);
						} else {
							p.setOp(true);
							Bukkit.dispatchCommand(p, com);
							p.setOp(false);
						}
					}
				});

				thread.start();
			} else if (command.startsWith("[cmd]")) {
				command = command.replace("[cmd]", "");
				final String com = command;
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(run * 50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), com);
					}

				});
				thread.start();
			} else {
				final String com = command;
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(run * 50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Bukkit.dispatchCommand(p, com);
					}

				});
				thread.start();
			}

		} else {
			if (command.startsWith("[op]")) {
				command = command.replace("[op]", "");
				if (p.isOp()) {
					Bukkit.dispatchCommand(p, command);
				} else {
					p.setOp(true);
					Bukkit.dispatchCommand(p, command);
					p.setOp(false);
				}
			} else if (command.startsWith("[cmd]")) {
				command = command.replace("[cmd]", "");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			} else {
				Bukkit.dispatchCommand(p, command);
			}
		}

	}

	private static int getRun(String string) {
		if (string.startsWith("[run:")) {
			string = string.replace("[run:", "#");
			int line = string.indexOf("#");
			int line2 = string.indexOf("]");

			int run = Integer.parseInt(string.substring(line + 1, line2));

			return run;
		}
		return -1;
	}

	public static int getRandom(int lower, int upper) {
		return new Random().nextInt((upper - lower) + 1) + lower;
	}

	public static String getColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static boolean isint(String args) {
		try {
			Integer.parseInt(args);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static double round(float value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
