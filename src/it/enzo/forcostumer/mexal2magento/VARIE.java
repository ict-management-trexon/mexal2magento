package it.enzo.forcostumer.mexal2magento;

public class VARIE {
	/*
	private static void visualizzaProdottieDisponibilita(){
		DatabaseOperation dbop = new DatabaseOperation();
		
		console("Attendere...");
		
		List<ProdottoEntita> artobList = dbop.selectWathelse();
		
		Iterator<ProdottoEntita> iterat = artobList.iterator();
		
		dbop.closeAll();
		
		console("Trovati "+artobList.size());
		
		while(iterat.hasNext()){
			ProdottoEntita prod = iterat.next();
			
			Integer disponibilitaFornitori = prod.getDisponibilita_fornitori_casoria()+prod.getDisponibilita_fornitori_casalnuovo();
			
			String out = prod.getNome()+" "+prod.getMexal_cky_art();
			
			if(prod.getDisponibilita_casoria()>0){out +="\n		Disponibilità casoria: "+prod.getDisponibilita_casoria();}
			if(prod.getDisponibilita_casalnuovo()>0){out +="\n		Disponibilità casalnuovo: "+prod.getDisponibilita_casalnuovo();}
			if(prod.getDisponibilita_fornitori_casoria()>0){
				out +="\n		Disponibilità fornitori casoria: "+prod.getDisponibilita_fornitori_casoria()+" arrivo "+prod.getData_prevista_casoria();}
			if(prod.getDisponibilita_fornitori_casalnuovo()>0){
				out +="\n		Disponibilità fornitori casalnuovo: "+prod.getDisponibilita_fornitori_casalnuovo()+" arrivo "+prod.getData_prevista_casalnuovo();}
			out += "\n		Disponibilità TOTALE: "+prod.getDisponibilita_totale();
			
			
			
			console(out);
		}
	}
	
	private static void caricaArticoli(){
		// TODO Auto-generated method stub
		
				MagentoXMLRPCOperation xmlOp = new MagentoXMLRPCOperation();
				
				
				//console("LISTA PRODOTTI: "+xmlOp.getProductList());
				//console("PRODOTTO SINGOLO: "+xmlOp.getProdInfo("2"));
				//console("Lista attributi:"+xmlOp.getAttributeListSet());
				
				//Iterare in un'array di ritorno da Magento
				//XmlRpcArray arr = xmlOp.getProductList();
				
				//Iterator<HashMap<String,Object>> it = arr.iterator();
				
				//while(it.hasNext()){
					//HashMap<String,Object> prod = it.next();
					//console(prod);
				//}
				
				
				DatabaseOperation dbop = new DatabaseOperation();
				
				console("Attendere...");
				
				List<ProdottoEntita> artobList = dbop.selectWathelse();
				
				
				Iterator<ProdottoEntita> iterat = artobList.iterator();
				
				console("Da Inserire: "+artobList.size()+" prodotti...");
				dbop.closeAll();
				
				while(iterat.hasNext()){
					ProdottoEntita artObj = iterat.next();
					
					String desc = artObj.getDescrizione();
					if(desc.equals("")){
						desc = artObj.getNome();
					}
					
					//inserimento di un prodotto
					
					//catalogInventoryStockItemUpdateEntity 
					HashMap<String, Object> stock_data = new HashMap<String, Object>();
					stock_data.put("qty", "10");
					stock_data.put("is_in_stock", 1);
					
					//catalogProductCreateEntity
					HashMap<String, Object> cpe = new HashMap<String, Object>();
					cpe.put("categories", new Object[]{"2"});
					cpe.put("websites", new Object[]{"1"});
					cpe.put("name", artObj.getNome());
					cpe.put("description", desc);
					cpe.put("short_description", artObj.getNome());
					cpe.put("weight", "10");
					cpe.put("status", "1");
					cpe.put("visibility", "4");
					cpe.put("price", artObj.getPrezzo());
					cpe.put("tax_class_id", "0");
					cpe.put("stock_data", stock_data);
					
					Object[] prodotto = new Object[]{
							"simple",			//type
							"4",				//id della lista degli attributi
							artObj.getMexal_cky_art(),			//Product SKU
							cpe,
							"1"					//id dello store
					};
				
				console("Prodotto inserito, id: "+xmlOp.addProduct(prodotto));
					
				}
				
				xmlOp.closeSession();
				
				console("Prodotti inseriti");
			
	}
	/*
	private void aggiornaDisponibilita(){
		//aggiornamento quantità e disponibilità
				MagentoXMLRPCOperation xmlOp = new MagentoXMLRPCOperation();
				DatabaseOperation dbop = new DatabaseOperation();
				
				console("Ricezione lista prodotti...");
				XmlRpcArray listaProdotti = xmlOp.getProductList();
				console("Lista ricevuta, "+listaProdotti.size()+" prodotti da aggiornare...");
				
				for(Integer i = 0;i<listaProdotti.size();i++){
					XmlRpcStruct prodotto = listaProdotti.getStruct(i);
					
					String idProdotto = (String) prodotto.get("product_id");
					ProdottoEntita prodottoDaMexal = dbop.getProdottoById((String) prodotto.get("sku"));
					
					HashMap<String, Object> stock_data = new HashMap<String, Object>();
					stock_data.put("qty", prodottoDaMexal.getDisponibilita_totale());
					
					HashMap<String, Object> disponibilita = new HashMap<String, Object>();
					disponibilita.put("cky_art", prodottoDaMexal.getMexal_cky_art());
					disponibilita.put("quantia_totale", prodottoDaMexal.getDisponibilita_totale());
					disponibilita.put("quantita_casoria", prodottoDaMexal.getDisponibilita_casoria());
					disponibilita.put("quantita_casalnuovo", prodottoDaMexal.getDisponibilita_casalnuovo());
					disponibilita.put("quantita_in_arrivo", prodottoDaMexal.getDisponibilita_fornitori_casoria()+prodottoDaMexal.getDisponibilita_fornitori_casalnuovo());
					disponibilita.put("stock_data", stock_data);
					
					xmlOp.updateProduct(idProdotto, disponibilita);
					console("Aggiornato prodotto con id "+idProdotto);
				}
				
				xmlOp.closeSession();
	}*/
}
