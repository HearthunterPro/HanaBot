package mc.CushyPro.HanaBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

public class LuaFile {

	private List<String> codes = new ArrayList<String>();
	private String Code = "";
	private File file;

	public LuaFile(File file) {
		this.setFile(file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String st = "";
			while ((st = br.readLine()) != null) {
				codes.add(st);
				Code = Code + st + "\n";
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadfile(Globals globals) {
		try {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					LuaValue chunk = globals.loadfile(LuaFile.this.file.getPath());
					chunk.call(LuaFile.this.file.getPath());
				}
			});
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public List<String> getcodes(int start, int end) {
		List<String> list = new ArrayList<String>();
		while (end >= start) {
			list.add(codes.get(start - 1));
			start++;
		}
		return list;
	}

	public void load(Globals globals) {
		try {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					LuaValue chunk = globals.load(LuaFile.this.Code);
					chunk.call();
				}
			});
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
