package mc.CushyPro.HanaBot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import mc.CushyPro.HanaBot.Luas.EventCall;
import mc.CushyPro.HanaBot.Luas.ReadCodeHana;
import mc.CushyPro.HanaBot.Luas.Runablelua;

public class LoadLua {

	public String Name;
	public ReadCodeHana readCodeHana = new ReadCodeHana();
	public int timedelay = 5;
	public HashMap<String, LuaFile> listfilelua = new HashMap<String, LuaFile>();

	private LoadLua() {

	}

	public boolean hasFile(String Name) {
		Name = "plugins\\HanaBot\\system\\" + Name;
		if (listfilelua.containsKey(Name)) {
			return true;
		}
		return false;
	}

	public LuaFile getFile(String Name) {
		Name = "plugins\\HanaBot\\system\\" + Name;
		if (listfilelua.containsKey(Name)) {
			return listfilelua.get(Name);
		}
		return null;
	}

	public static void loadlua() {
		Data.loadlua = new LoadLua();
	}

	public static void loadFileandClass() {
		EventCall.listevent.clear();
		Runablelua.stopall();
		Runablelua.listthread.clear();
		File file = Data.getFile("system//ini.lua");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Data.loadlua.listfilelua.clear();
		for (File f : Data.getFile("system//").listFiles()) {
			Data.loadlua.loadFile(f);
		}
		Data.loadlua.loadini();
	}

	private void loadini() {
		if (hasFile("ini.lua")) {
			LuaFile LuaFile = getFile("ini.lua");
			LuaFile.loadfile(readCodeHana.getGlobals());
		}
	}

	private void loadFile(File file) {
		if (file.getName().endsWith(".lua")) {
			LuaFile luaFile = new LuaFile(file);
			this.listfilelua.put(file.getPath(), luaFile);
		} else if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				this.loadFile(f);
			}
		}
	}

}
