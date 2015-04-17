package it.enzo.forcostumer.mexal2magento.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;
import it.enzo.forcostumer.mexal2magento.AdapterM2M;
import it.enzo.forcostumer.mexal2magento.adapter.entity.Categoria;


public class GestioneCategorie {

	
	private AdapterM2M adapter;
	
	private String rootId = "1";
	
	private String defaultCategoryId = "2";
	private String defaultNameDefaultCategory = "Default Category";
	
	private String dafaultIdAzienda = "X1";
	private String defaultNomeAzienda = "trexon";
	
	
	public GestioneCategorie(AdapterM2M ad){
		this.adapter = ad;
	}
	
	public void generaCategorieDaMexal(){
		
		HashMap<String, String> listaMacroCategorie = adapter.getDatabaseOperation().getListaMacrocategorie();
		
		ArrayList<String> listaCategorieMagento = adapter.getMagentoXMLRPCOperation().getCategoryTreeLinearIdInterno();
		
		//Inserimento categorie mancanti
		for(String key : listaMacroCategorie.keySet()){
			if(!listaCategorieMagento.contains(key)){
				//crea la categoria
				XmlRpcStruct cat = new XmlRpcStruct();
				cat.put("name", listaMacroCategorie.get(key));
				cat.put("is_active","1");
				cat.put("catIdInt",key);
				cat.put("available_sort_by",key);
				adapter.getMagentoXMLRPCOperation().createCategory("1", cat);
			}
		}
		
	}
	
}
