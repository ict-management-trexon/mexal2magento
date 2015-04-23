package it.enzo.forcostumer.mexal2magento.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;
import it.enzo.forcostumer.mexal2magento.AdapterM2M;
import it.enzo.forcostumer.mexal2magento.adapter.entity.Categoria;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogProductEntity;
import it.enzo.forcostumer.mexal2magento.mexal.prodotto.ProdottoEntita;


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
				console("Inserimento categoria "+listaMacroCategorie.get(key)+", con id interno "+key);
				XmlRpcStruct cat = new XmlRpcStruct();
				cat.put("name", listaMacroCategorie.get(key));
				cat.put("is_active","1");
				cat.put("catIdInt",key);
				cat.put("default_sort_by","position");
				cat.put("position", "1");
				cat.put("available_sort_by", "position");
				cat.put("include_in_menu", "1");
				adapter.getMagentoXMLRPCOperation().createCategory("2", cat);
			}else{
				console("Categoria "+listaMacroCategorie.get(key)+" con id interno "+key+" esistente");
			}
		}
		
	}
	
	public void assegnaTuttiIProdottiAlleCategorie(){
		console("Ricezione della lista di tutti i prodotti presenti in magento...");
		List<CatalogProductEntity> listaProdottiMexal = this.adapter.getMagentoXMLRPCOperation().getProductList();
		console("Lista ricevuta, prenti "+listaProdottiMexal.size()+" prodotti");
		
		console("Generazione dell'albero delle categorie...");
		HashMap<String, String> albero = new HashMap<String, String>();
		XmlRpcStruct defaultCatStruct = adapter.getMagentoXMLRPCOperation().getCategoryTree("2");
		XmlRpcArray defalutCatChildren = defaultCatStruct.getArray("children");
		
		for(int i = 0;i<defalutCatChildren.size();i++){
			XmlRpcStruct cat = defalutCatChildren.getStruct(i);
			String idCatMex = adapter.getMagentoXMLRPCOperation().getIdCatInternById(cat.getString("category_id"));
			albero.put(idCatMex, cat.getString("category_id"));
			console("Alla categoria id-magento "+cat.getString("category_id")+" e associato l'id di mexal "+idCatMex);
		}
		
		console("Sono presenti "+defalutCatChildren.size()+" categorie");
		
		int i = 0;
		for(CatalogProductEntity key : listaProdottiMexal){
			i++;
			//richiesta prodotto da mexal
			//if(i>=2943){
			XmlRpcStruct idMexal = adapter.getMagentoXMLRPCOperation().getProdInfo(key.getProduct_id());
			ProdottoEntita prod = adapter.getDatabaseOperation().getProdottoById(idMexal.getString("cky_art"));
			
			if(!prod.getCategoria().equals("")){
				String categoriaMagento = albero.get(prod.getCategoria());
				adapter.getMagentoXMLRPCOperation().assegnaProdottoAllaCategoria(key.getProduct_id(), categoriaMagento);
				console("("+i+" di "+listaProdottiMexal.size()+") Inserito prodotto "+key.getName()+" nella categoria con id "+prod.getCategoria());
			}else{
				console("("+i+" di "+listaProdottiMexal.size()+") Prodotto "+key.getName()+" ha categoria non assegnata");
			//}
			}
		}
		console("Terminato!");
		
	}
	
	
	public static void console(Object out){
		System.out.println(out);
	}
}
