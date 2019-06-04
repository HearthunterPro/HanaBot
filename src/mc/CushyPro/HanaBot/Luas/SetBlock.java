package mc.CushyPro.HanaBot.Luas;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class SetBlock extends ZeroArgFunction {
	
	private Block b;

	public SetBlock(Block b){
		this.b = b;
	}
	
	@Override
	public LuaValue call() {
		LuaTable table = new LuaTable();
		table.set("setType", new setType());
		table.set("setData", new setData());
		return table;
	}
	
	public class setData extends OneArgFunction {

		@SuppressWarnings("deprecation")
		@Override
		public LuaValue call(LuaValue args) {
			if (args.isint()) {
				int data = args.checkint();
				b.setData((byte) data);
				return LuaValue.valueOf(true);
			}
			return LuaValue.valueOf("setData(int data)");
		}
		
	}

	public class setType extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue args) {
			if (args.isstring()) {
				String material = args.checkjstring();
				if (Material.getMaterial(material) != null) {
					if (Material.getMaterial(material).isBlock()) {
						b.setType(Material.getMaterial(material));
						return LuaValue.valueOf(true);
					}
					return LuaValue.valueOf(material + " is not a block");
				}
			}
			return LuaValue.valueOf("setType(string material)");
		}
		
	}

	
	
}
