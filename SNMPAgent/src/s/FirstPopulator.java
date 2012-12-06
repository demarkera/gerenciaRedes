package s;

import java.util.ArrayList;

public class FirstPopulator {

	public static ArrayList<GeladeiraModel> populate(){
		ArrayList<GeladeiraModel> lista = new ArrayList<>();
		GeladeiraModel geladeira = new GeladeiraModel();
		geladeira.setDefrostDays(150);
		geladeira.setFastFreezing(false);
		geladeira.setLampadaOn(true);
		geladeira.setPortaAberta(false);
		geladeira.setTermostato(TermostatoEnum.DOIS);
		geladeira.setTurnedOn(true);
		
		ArrayList<Gaveta> listaGavetas = new ArrayList<>();
		
		Gaveta gaveta1 = new Gaveta();
		gaveta1.setCervejas(15);
		gaveta1.setIndexGaveta(1);
		gaveta1.setLeite(3);
		gaveta1.setOvos(12);
		listaGavetas.add(gaveta1);
		
		Gaveta gaveta2 = new Gaveta();
		gaveta1.setCervejas(0);
		gaveta1.setIndexGaveta(2);
		gaveta1.setLeite(10);
		gaveta1.setOvos(15);
		listaGavetas.add(gaveta2);
		
		geladeira.setGaveta(listaGavetas);
		lista.add(geladeira);
		return lista;
	}
}
