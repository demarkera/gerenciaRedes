package s;

import java.util.ArrayList;
import java.util.List;

//http://forcedfx.dyndns.org/images/snmp_packet_structure.png
//nossa PDU tem uma diferença: objeto tem 50 caracteres e value 5
//nossa PDU total tem 67 caracteres
public class SNMPPDU {
	private String snmpVersion = "1";
	private String communityString = "0000000";
	private SNMPPDUType pduType;
	private String requestId;
	private String errorStatus;
	private String errorIndex;
	private List<CampoValor> camposRequisidatos;

	public String getFullPDU() {
		// tipoPDU
		String pdu = pduType.ordinal() + "";

		// ajeitando length do request
		String request = "1";

		String erro = "0";

		// ajeitando index do erro
		String indexErro = "0";

		// ajeitando o campoValor de cada Campo Requisitado
		String fullCamposRequisitados = "";
		for (CampoValor campoValor : camposRequisidatos) {
			String objeto = campoValor.getField();
			while (objeto.length() < 17) {
				objeto = objeto + " ";
			}
			String valor = campoValor.getValue();
			while (valor.length() < 5) {
				valor = valor + " ";
			}
			fullCamposRequisitados = fullCamposRequisitados + objeto + valor;
		}

		return pdu + request + erro + indexErro + fullCamposRequisitados;
	}

	public void parse(byte[] data) {
		String dados = data.toString();
		snmpVersion = dados.substring(0, 1);
		communityString = dados.substring(1, 8);
		pduType = SNMPPDUType.valueOf(dados.substring(8, 9));
		requestId = dados.substring(9, 10);
		errorStatus = dados.substring(10, 11);
		errorIndex = dados.substring(11, 12);
		camposRequisidatos = parseFields(dados.substring(12));
	}

	private List<CampoValor> parseFields(String substring) {
		List<CampoValor> lista = new ArrayList<>();
		String dados = substring;
		int tamanhoVisto = 0;
		if(pduType != SNMPPDUType.TRAP){
			while(dados.length() >= 55){
				CampoValor campoValor = new CampoValor();
				campoValor.setField(dados.substring(0,50));
				campoValor.setValue(dados.substring(50,55));
				lista.add(campoValor);
			}
		}
		else{
			CampoValor campoValor = new CampoValor();
			campoValor.setField(dados.substring(0,50));
			campoValor.setValue(dados.substring(50));
			lista.add(campoValor);
		}
		return lista;
	}

	public String getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public String getCommunityString() {
		return communityString;
	}

	public void setCommunityString(String communityString) {
		this.communityString = communityString;
	}

	public SNMPPDUType getPduType() {
		return pduType;
	}

	public void setPduType(SNMPPDUType pduType) {
		this.pduType = pduType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getErrorIndex() {
		return errorIndex;
	}

	public void setErrorIndex(String errorIndex) {
		this.errorIndex = errorIndex;
	}

	public List<CampoValor> getCamposRequisidatos() {
		return camposRequisidatos;
	}

	public void setCamposRequisidatos(List<CampoValor> camposRequisidatos) {
		this.camposRequisidatos = camposRequisidatos;
	}

	
	
}
