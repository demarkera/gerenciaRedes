package snmp;

public enum ObjectsMib {
	DEFAULTOID(0,".1.3.6.1.4.1.1264897646"),
	ISTURNEDON(1,".1"),
	ISLAMPADAON(2,".2"),
	TERMOSTATO(3,".3"),
	ISPORTAABERTA(4,".4"),
	DAYSWITHOUTDEFROST(5,".5"),
	FASTFREEZING(6,".6"),
	GAVETATABLE(7,".7"),
	GAVETACERVEJA(8,".7.X.1"),
	GAVETAOVOS(9,".7.X.2"),
	GAVETALEITE(10,".7.X.3"),
	GAVETAINDEX(11,".7.X.4");

	private int index;
	private String pduReference;

	ObjectsMib(int aIndex,String aPduReference){
		index = aIndex;
	    pduReference = aPduReference;
	}

	public static String getReference(ObjectsMib object){
		for (ObjectsMib cada : ObjectsMib.values()) {
			if(cada.equals(object.toString())){
				return cada.getPduReference();
			}
		}
		return null;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPduReference() {
		return pduReference;
	}

	public void setPduReference(String pduReference) {
		this.pduReference = pduReference;
	}
	

}
