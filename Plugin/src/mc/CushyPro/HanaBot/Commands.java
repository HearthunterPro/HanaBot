package mc.CushyPro.HanaBot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.CushyPro.HanaBot.Luas.ReadCodeHana;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	private static ReadCodeHana readCodeHana = new ReadCodeHana();

	public Commands() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (!cmd.getName().equals("hana")) {
			return false;
		}

		if (!sender.hasPermission("hana.use*")) {
			sender.sendMessage("" + MessageType.NOPERMISSION.toString());
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(
					ChatColor.YELLOW + "Version: " + Data.Version + ChatColor.AQUA + "Author: " + Data.Author);
			sender.sendMessage(ChatColor.GREEN + "/hana luafile : list file");
			sender.sendMessage(ChatColor.GREEN + "/hana load <locate file : test file lua ");
			sender.sendMessage(ChatColor.GREEN + "/hana saveconfig : save this");
			sender.sendMessage(ChatColor.GREEN + "/hana reload : reload config");
			sender.sendMessage(ChatColor.GREEN + "/hana reload system : reload file lua");
			return true;
		}

		if (args[0].equalsIgnoreCase("load")) {
			if (!sender.hasPermission("hana.load")) {
				sender.sendMessage("" + MessageType.NOPERMISSION.toString());
				return false;
			}
			if (args.length == 1) {
				sender.sendMessage("/hana load <file>");
				return false;
			}
			String m = args[1];
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			readCodeHana.load(player, m);
			return true;
		}

		if (args[0].equalsIgnoreCase("luafile")) {
			if (!sender.hasPermission("hana.luafile")) {
				sender.sendMessage("" + MessageType.NOPERMISSION.toString());
				return false;
			}

			sender.sendMessage(ChatColor.YELLOW + "============================");
			sender.sendMessage("RAM: " + (System.currentTimeMillis() / 1000));
			sender.sendMessage(Data.getColor("&aName &5Size"));
			for (LuaFile luaFile : Data.loadlua.listfilelua.values()) {
				sender.sendMessage(Data.getColor(
						ChatColor.GREEN + luaFile.getFile().getName() + " - &5" + luaFile.getFile().length() + " B"));
			}
			sender.sendMessage(ChatColor.YELLOW + "============================");
			return true;
		}

		if (args[0].equalsIgnoreCase("saveconfig")) {
			if (!sender.hasPermission("hana.saveconfig")) {
				sender.sendMessage("" + MessageType.NOPERMISSION.toString());
				return false;
			}
			Data.saveConfig();
			Data.loadFileConfig();
			sender.sendMessage("saveconfig");
			return true;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("hana.reload")) {
				sender.sendMessage("" + MessageType.NOPERMISSION.toString());
				return false;
			}
			if (args.length == 1) {
				Data.loadFileConfig();
				sender.sendMessage("load file succ");
				return true;
			}

			if (args[1].equalsIgnoreCase("system")) {
				LoadLua.loadFileandClass();
				sender.sendMessage("load File Lua ALL FILE");
				return true;
			}
			return true;
		}

		return false;
	}

}
