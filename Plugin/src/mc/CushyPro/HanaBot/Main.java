package mc.CushyPro.HanaBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mc.CushyPro.HanaBot.APIS.AddonH;
import mc.CushyPro.HanaBot.Event.DataPlayerEventLoad;
import mc.CushyPro.HanaBot.Event.EventListener;

public class Main extends JavaPlugin {

	protected static List<AddonH> addons = new ArrayList<AddonH>();

	@Override
	public void onEnable() {
		Data.plugin = this;

		Data.PluginName = getDescription().getName();

		Data.Version = getDescription().getVersion();
		Data.Author = getDescription().getAuthors().get(0);

		try {
			final File[] libs = new File[] { new File(getDataFolder(), "lib//luaj-jse-3.0.1.jar"),
					new File(getDataFolder(), "lib//luaj-jme-3.0.1.jar"),
					new File(getDataFolder(), "lib//luaj-sources-3.0.1.jar")};
			for (final File lib : libs) {
				if (!lib.exists()) {
					JarUtils.extractFromJar(lib.getName(), lib.getAbsolutePath());
				}
			}
			for (final File lib : libs) {
				if (!lib.exists()) {
					getLogger().warning(
							"There was a critical error loading My plugin! Could not find lib: " + lib.getName());
					Bukkit.getServer().getPluginManager().disablePlugin(this);
					return;
				}
				addClassPath(JarUtils.getJarUrl(lib));
			}
			Bukkit.getConsoleSender().sendMessage("Load Addon Hanabot ---");
			for (final File file : Data.getFile("lib//").listFiles()) {
				loadJarFile(file);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		LoadLua.loadlua();

		try {
			for (AddonH addonH : addons) {
				addonH.load();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Data.loadFileConfig();
		Data.LoadConfig();

		Data.getFile("system//").mkdir();
		Data.getFile("system//Library//").mkdir();
		Data.getFile("data//").mkdir();
		Data.getFile("data//player//").mkdir();

		LoadLua.loadFileandClass();

		for (Player player : Bukkit.getOnlinePlayers()) {
			Data.loadplayerdata(player);
		}

		getCommand("hana").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		getServer().getPluginManager().registerEvents(new DataPlayerEventLoad(), this);

		super.onEnable();
	}

	public static void loadJarFile(File file) {
		try {
			JarFile jarFile = new JarFile(file);
			JarEntry entry = jarFile.getJarEntry("addonhana.txt");
			if (entry != null) {
				InputStream input = jarFile.getInputStream(entry);
				HashMap<String, String> m = process(input);
				if (m.containsKey("Name")) {
					Bukkit.getConsoleSender().sendMessage("- " + m.get("Name"));
				}
				if (m.containsKey("Main")) {
					AddonH addonH = new AddonH(file, m.get("Main"), m.get("Name"), m.get("Author"));
					addons.add(addonH);
				}
				jarFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static HashMap<String, String> process(InputStream input) throws IOException {
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(isr);
		HashMap<String, String> map = new HashMap<String, String>();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("name: ")) {
				map.put("Name", line.replace("name: ", ""));
			} else if (line.startsWith("main: ")) {
				map.put("Main", line.replace("main: ", ""));
			} else if (line.startsWith("author: ")) {
				map.put("Author", line.replace("author: ", ""));
			}
		}
		reader.close();
		return map;
	}

	@Override
	public void onDisable() {
		Data.saveConfig();

		for (Player player : Bukkit.getOnlinePlayers()) {
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
		super.onDisable();
	}

	private static void addClassPath(final URL url) throws IOException {
		final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			final Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { url });
		} catch (final Throwable t) {
			t.printStackTrace();
			throw new IOException("Error adding " + url + " to system classloader");
		}
	}

}
