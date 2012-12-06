package snmp;

import java.util.ArrayList;
import java.util.List;

public class SNMPTranslator {
	
	public SNMPTranslator(byte[] data) {
		SNMPPDU snmp = new SNMPPDU();
		snmp.parse(data);
	}
}
