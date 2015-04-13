package it.enzo.forcostumer.mexal2magento.adapter;

import java.util.ArrayList;

import it.enzo.forcostumer.mexal2magento.AdapterM2M;
import it.enzo.forcostumer.mexal2magento.adapter.entity.Categoria;


public class GestioneCategorie {

	
	private AdapterM2M adapter;
	
	private Categoria root;
	
	
	public GestioneCategorie(AdapterM2M ad){
		this.adapter = ad;
	}
	
	public void generaCategorieDaMexal(){
		
		ArrayList<String> listaMacroCategorie = adapter.getDatabaseOperation().getListaMacrocategorie();
		
		
	}
	
}
