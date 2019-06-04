package mc.CushyPro.HanaBot;

public enum MessageType {

	NOPERMISSION("&aYou not have permission"),
	;

	private String defual;
	private String[] defual2;

	MessageType(String defual) {
		this.defual = defual;
	}

	MessageType(String[] defual) {
		defual2 = defual;
	}

	@Override
	public String toString() {
		if (defual != null) {
			if (Data.cfg.isSet("MessageType." + super.toString())) {
				return Data.getColor(Data.cfg.getString("MessageType." + super.toString()));
			}
			return Data.getColor(defual);
		} else {
			return "toArray";
		}
	}

	public String[] toTitle() {
		if (defual != null) {
			if (Data.cfg.isSet("MessageType." + super.toString())) {
				return Data.getColor(Data.cfg.getString("MessageType." + super.toString())).split(",");
			}
			return defual.split(",");
		}
		return new String[] { "error", "error", "0", "20", "5" };
	}

	public String[] toArray() {
		if (defual2 != null) {
			if (Data.cfg.isSet("MessageType." + super.toString())) {
				return Data.cfg.getStringList("MessageType." + super.toString()).toArray(new String[0]);
			}
			return defual2;
		}
		return new String[] { "toString" };
	}

	public String getID() {
		return super.toString();
	}

}
