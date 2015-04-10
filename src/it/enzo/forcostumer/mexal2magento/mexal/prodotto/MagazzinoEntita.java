package it.enzo.forcostumer.mexal2magento.mexal.prodotto;

import java.util.ArrayList;

public class MagazzinoEntita {

	//emulazione entità
	private static ArrayList<MagazzinoEntita> magazzinoEntita;
	
	static{
		
		MagazzinoEntita casoria = new MagazzinoEntita();
		casoria.setId(1);
		casoria.setNome("Casoria");
		
		MagazzinoEntita casalnuovo = new MagazzinoEntita();
		casalnuovo.setId(6);
		casalnuovo.setNome("Casalnuovo");
	
		MagazzinoEntita.magazzinoEntita.add(casoria);
		MagazzinoEntita.magazzinoEntita.add(casalnuovo);
	}
	
	
	
	//POJO
	private String nome;
	private Integer id;
	private ArrayList<ProdottoEntita> prodotti;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
}
