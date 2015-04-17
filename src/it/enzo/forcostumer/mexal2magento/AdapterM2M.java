package it.enzo.forcostumer.mexal2magento;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import redstone.xmlrpc.XmlRpcStruct;
import it.enzo.forcostumer.mexal2magento.adapter.GestioneCategorie;
import it.enzo.forcostumer.mexal2magento.magento.MagentoXMLRPCOperation;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogInventoryStockItemUpdateEntity;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogProductCreateEntity;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogProductEntity;
import it.enzo.forcostumer.mexal2magento.mexal.DatabaseOperation;
import it.enzo.forcostumer.mexal2magento.mexal.prodotto.ProdottoEntita;
import it.enzo.forcostumer.mexal2magento.processi.UpdateAllProducts;
import it.enzo.forcostumer.mexal2magento.processi.UpdateArticoli;


public class AdapterM2M {

	private DatabaseOperation dbop;
	private MagentoXMLRPCOperation mxrop;
	private UpdateArticoli upateArticoli;
	
	private GestioneCategorie gestioneCategorie;
	
	public void startProcessi(){
		if(this.upateArticoli.isInterrupted()){
			upateArticoli = new UpdateArticoli(this);
		}
		this.upateArticoli.start();
	}
	
	public void stopProcessi(){
		if(!this.upateArticoli.isInterrupted()){
			this.upateArticoli.interrupt();
			console("Attendere il completamento delle operazioni...");
			try {
				this.upateArticoli.join();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	
		
		console("Operazioni terminate!");
	}
	
	public static void console(Object out){
		System.out.println(out);
	}
	
	public AdapterM2M(){
		
		this.dbop = new DatabaseOperation();
		this.mxrop = new MagentoXMLRPCOperation();
		
		XmlToHaskMap prov;
		try {
			
			prov = new XmlToHaskMap("target/classes/m2mconfig.xml");
			
			Hashtable<String, String> hash = prov.getHashtable();
			
			String magentoConnStr = hash.get("/mexal2magento/magento/url");
			this.mxrop.urlString = magentoConnStr;
			
			String magentoUtente = hash.get("/mexal2magento/magento/xmlRcpUtente");
			this.mxrop.uId = magentoUtente;
			
			String magentoPassword = hash.get("/mexal2magento/magento/xmlRcpPassword");
			this.mxrop.password = magentoPassword;
			
			String mexalConnStr = hash.get("/mexal2magento/mexal/db/connectionString");
			this.dbop.connection_string = mexalConnStr;
			
			String mexalDb = hash.get("/mexal2magento/mexal/db/databaseName");
			this.dbop.databaseName = mexalDb;
			
			String mexalUid = hash.get("/mexal2magento/mexal/db/userId");
			this.dbop.uId = mexalUid;
			
			String mexalPass = hash.get("/mexal2magento/mexal/db/password");
			this.dbop.password = mexalPass;
			
			
			
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.dbop.startConn();
		this.mxrop.connect();
		
		upateArticoli = new UpdateArticoli(this);
		
		gestioneCategorie = new GestioneCategorie(this);
		
	}
	
	public DatabaseOperation getDatabaseOperation(){
		return this.dbop;
	}
	
	public MagentoXMLRPCOperation getMagentoXMLRPCOperation(){
		return this.mxrop;
	}
	
	public GestioneCategorie getGestioneCategorie() {
		return gestioneCategorie;
	}

	public UpdateArticoli getUpateArticoli() {
		return upateArticoli;
	}

	public void setUpateArticoli(UpdateArticoli upateArticoli) {
		this.upateArticoli = upateArticoli;
	}

	public void destroy(){
		this.stopProcessi();
		this.dbop.closeAll();
		this.mxrop.closeSession();
		
	}
	
	public void updateAllProduct(){
		List<String> listaProdottiMexal = dbop.getAllProduct();
		Iterator<String> it = listaProdottiMexal.iterator();
		
		//List<CatalogProductEntity> listaProdottiMagento = this.mxrop.getProductList();
		
		console("da verificare "+listaProdottiMexal.size()+" articoli");
		this.mxrop.prodottiDaVerificare = listaProdottiMexal.size();
		
		//UpdateAllProducts uall = new UpdateAllProducts(this.mxrop);
		
		//uall.start();
		
		while(it.hasNext()){
			
			//Recupero del prodotto dal database di Mexal
			ProdottoEntita prodottoMexal = dbop.getProdottoById(it.next());
			
			//Conversione in prodotto per Magento
			CatalogProductCreateEntity prodottoMagento = this.productMexalToMagento(prodottoMexal);
			
			//verifica se il prodotto è da creare o da aggiornare, in base all'esistenza
			//o meno in mexal
			
			String idMagento = this.mxrop.getMagentoIdbyCodeiceArticolo(prodottoMexal.getMexal_cky_art());
			if(!idMagento.equals("")){
				//Se esiste, aggiorna il prodotto
				console("prodotto con codice articolo "+prodottoMexal.getMexal_cky_art()+" e id di Magento "+idMagento+" da aggiornare");
				this.mxrop.putInMulticall(this.mxrop.updateProductMultiCall(idMagento, prodottoMagento));
			
			}else{
				//Se non esiste crea un nuovo prodotto
				console("prodotto con codice articolo "+prodottoMexal.getMexal_cky_art()+" da inserire come nuovo");
				this.mxrop.putInMulticall(this.mxrop.createProductMultiCall(prodottoMagento));
				
			}
		}
		
		//uall.stopExecution();
		
		//Invio a Magento di tutte le operazioni da effettuare
		console("Invio a magento, multiCall...");
		this.mxrop.executeMultiCall();
		console("Operazioni effettuare con successo");
	}
	
	
	public void stampaListaProdottiMagento(){
		
		console("Ricezione lista");
		Iterator<CatalogProductEntity> catalog = this.mxrop.getProductList().iterator();
		
		
		while(catalog.hasNext()){
			CatalogProductEntity prod = catalog.next();
			console(prod.getProduct_id()+" "+prod.getName()+" "+prod.getSku());
		}
	}
	
	public CatalogProductCreateEntity productMexalToMagento(ProdottoEntita prodotto){
		CatalogProductCreateEntity ret = new CatalogProductCreateEntity();
		
		ret.setName(prodotto.getNome());
		ret.setDescription(prodotto.getDescrizione());
		ret.setShortDescription(prodotto.getShortDescription());
		if(!(prodotto.getPeso()==null)){ret.setWeight(prodotto.getPeso());}else{ret.setWeight("1");}
		ret.setStatus(prodotto.getStato());
		ret.setPrice(prodotto.getPrezzo());
		ret.setCodiceArticolo(prodotto.getMexal_cky_art());
		ret.setEanCode(prodotto.getEan());
		ret.setQuantitaTotale(prodotto.getDisponibilita_totale());
		ret.setQuantitaCasoria(prodotto.getDisponibilita_casoria());
		ret.setQuantitaCasalnuovo(prodotto.getDisponibilita_casalnuovo());
		ret.setQuantitaInArrivo(prodotto.getDisponibilta_fornitori_totale());
		ret.getstockItemUpdate().setQuantity(Integer.toString(prodotto.getDisponibilita_totale()));
		
		
		return ret;
	}
	
	public void updateAllProductImage(){
		
		HashMap<String, String> productListConImmagine = dbop.getProductsWithImage();
		Set<String> arrKey = productListConImmagine.keySet();
		
		for(String key : arrKey){
			
			String idMexal = key;
			String label = productListConImmagine.get(key);
			
			Object[] ob = new Object[]{};
			ob = this.mxrop.insertImageForProductCodiceArticolo(idMexal, label);
			try{
				if(ob.length>0){
					String fileName = (String) this.mxrop.executeCall(ob);
					
						if(!fileName.equals("")){
						console("Immagine inserita per l'articolo "+idMexal+" nome file "+fileName);
						}
				
			}else{
				console("Immagine per il prodotto "+idMexal+" non presente");
			}
			}catch(Exception e){}
		}
	}
	
	public void updateFromTriggered(){
		
		console("");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		console(dateFormat.format(date));
		
		ArrayList<String> deleted = dbop.getFromTriggered(DatabaseOperation.DELETE);
		console("Da eliminare "+deleted.size()+" articoli");
		ArrayList<String> deleted1 = new ArrayList<String>();
		for(String id : deleted){
			if(!deleted1.contains(id)){
				deleted1.add(id);
				String idMexal = mxrop.getMagentoIdbyCodeiceArticolo(id);
				if(!idMexal.equals("")){
					//cancella articolo
					this.mxrop.executeCall(this.mxrop.deleteProduct(idMexal));
					this.dbop.deleteFromTriggered(id);
					console("Articolo con id "+id+" eliminato");
				}
			}
		}
		deleted = null;
		deleted1 = null;
		
		
		ArrayList<String> inserted = dbop.getFromTriggered(DatabaseOperation.INSERT);
		console("Da inserire "+inserted.size()+" articoli");
		ArrayList<String> inserted1 = new ArrayList<String>();
		for(String id : inserted){
			if(!inserted1.contains(id)){
				inserted1.add(id);
				String idMexal = mxrop.getMagentoIdbyCodeiceArticolo(id);
				if(idMexal.equals("")){
					//Inserisci l'articolo
					
					this.mxrop.executeCall(
							this.mxrop.createProduct(
									this.productMexalToMagento(
											dbop.getProdottoById(id)
											)
									)
							);
					
					
				}else{
					this.mxrop.executeCall(
							this.mxrop.updateProduct(idMexal,
									this.productMexalToMagento(
											dbop.getProdottoById(id)
											)
									)
							);
				}
				this.dbop.deleteFromTriggered(id);
				console("Articolo con id "+id+" inserito");
			}
		}
		inserted = null;
		inserted1 = null;
		
		ArrayList<String> updated = dbop.getFromTriggered(DatabaseOperation.UPDATE);
		console("Da aggiornare "+updated.size()+" articoli");
		ArrayList<String> updated1 = new ArrayList<String>();
		for(String id : updated){
			if(!updated1.contains(id)){
				updated1.add(id);
				String idMexal = mxrop.getMagentoIdbyCodeiceArticolo(id);
				
				//aggiorna l'articolo
				if(!idMexal.equals("")){
					//l'articolo esiste
					this.mxrop.executeCall(
							this.mxrop.updateProduct( idMexal, 
									this.productMexalToMagento(
											dbop.getProdottoById(id)
											)
									)
							);
				}else{
					//l'articolo non esiste
					this.mxrop.executeCall(
							this.mxrop.createProduct( 
									this.productMexalToMagento(
											dbop.getProdottoById(id)
											)
									)
							);
				}
				this.dbop.deleteFromTriggered(id);
				console("Articolo con id "+id+" aggiornato");
			}
		}
		updated = null;
		updated1 = null;
		
	}
}
