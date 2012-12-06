package s;

import java.util.ArrayList;

public class GeladeiraModel {
	private boolean turnedOn;
	private boolean lampadaOn;
	private TermostatoEnum termostato;
	private boolean portaAberta;
	private int defrostDays;
	private boolean fastFreezing;
	private ArrayList<Gaveta> gaveta; //máximo de 10 gavetas!

	public GeladeiraModel(){
		gaveta = new ArrayList<Gaveta>();
		termostato = TermostatoEnum.ZERO;
	}
	
	public boolean isTurnedOn() {
		return turnedOn;
	}
	public void setTurnedOn(boolean turnedOn) {
		this.turnedOn = turnedOn;
	}
	public boolean isLampadaOn() {
		return lampadaOn;
	}
	public void setLampadaOn(boolean lampadaOn) {
		this.lampadaOn = lampadaOn;
	}
	public TermostatoEnum getTermostato() {
		return termostato;
	}
	public void setTermostato(TermostatoEnum termostato) {
		this.termostato = termostato;
	}
	public boolean isPortaAberta() {
		return portaAberta;
	}
	public void setPortaAberta(boolean portaAberta) {
		this.portaAberta = portaAberta;
	}
	public int getDefrostDays() {
		return defrostDays;
	}
	public void setDefrostDays(int defrostDays) {
		this.defrostDays = defrostDays;
	}
	public boolean isFastFreezing() {
		return fastFreezing;
	}
	public void setFastFreezing(boolean fastFreezing) {
		this.fastFreezing = fastFreezing;
	}
	public ArrayList<Gaveta> getGaveta() {
		return gaveta;
	}
	public void setGaveta(ArrayList<Gaveta> gaveta) {
		this.gaveta = gaveta;
	}
	
}
