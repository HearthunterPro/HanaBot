package mc.CushyPro.HanaBot.APIS;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import mc.CushyPro.HanaBot.Main;

public class AddonH {

	private File file;
	private String Main;
	private String Name;
	private String Author;

	public AddonH(File file, String main, String name, String author) {
		this.file = file;
		this.Main = main;
		this.Name = name;
		this.Author = author;
	}

	public File getFile() {
		return file;
	}

	public String getMain() {
		return Main;
	}

	public String getName() {
		return Name;
	}

	public String getAuthor() {
		return Author;
	}
	
	@SuppressWarnings("resource")
	public void load() {
		try {
			File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			URL u = new URL(
					"jar:" + file.toURI().toURL().toExternalForm() + "!/mc/CushyPro/HanaBot/APIS/HanaAddon.class");
			URL u2 = new URL("jar:" + this.file.toURI().toURL().toExternalForm() + "!/");
			
			Class<?> cl = new URLClassLoader(new URL[] {u, u2}, Main.class.getClassLoader()).loadClass(this.Main);
			Object a = cl.newInstance();
			if (a instanceof HanaAddon) {
				HanaAddon addon = (HanaAddon) a;
				addon.onEnable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
