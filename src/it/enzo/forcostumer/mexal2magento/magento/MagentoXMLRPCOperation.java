package it.enzo.forcostumer.mexal2magento.magento;

import it.enzo.forcostumer.mexal2magento.io.fileSystem.FileSystem;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogProductCreateEntity;
import it.enzo.forcostumer.mexal2magento.magento.entita.CatalogProductEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//import org.apache.xmlrpc.XmlRpcException;
//import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;







import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcStruct;

public class MagentoXMLRPCOperation {

	public String urlString = "";
	public String uId = "";
	public String password = "";
	
	private String sessionId;
	
	private XmlRpcClient client;
	
	private ArrayList<Object[]> multiCall = new ArrayList<Object[]>();
	
	private String defaultType = "simple";
	private String defaultListSet = "4";
	private String defaultStoreId = "1";
	
	public Integer prodottiDaVerificare = 0;
	
	public static void console(Object out){
		System.out.println(out);
	}
	
	public MagentoXMLRPCOperation(){
	
		
		
	}
	
	public void connect(){
		try {
			this.client = new XmlRpcClient(urlString, false);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.startSession();
	}
	
	private void startSession(){
		Object[] login = new Object[]{new String(this.uId), new String(this.password)};
		this.sessionId = (String) this.getFromMagento("login", login);
	}
	
	public void closeSession(){
		this.getFromMagento("endSession", new Object[]{sessionId});
	}
	
	public Object getFromMagento(String command, Object[] params){
		Object ret = new Object();
		try {
			ret = this.client.invoke(command, params);
		} catch (XmlRpcException | XmlRpcFault e) {
			
				switch(e.getMessage()){
			
				case "Session expired. Try to relogin.":
					console("Fault Message: "+e.getMessage());
					break;
			
				case "Category not exists.":
				
					break;
			
				default:
				
					console("Fault Message: "+e.getMessage());
			
				}
		}
		return ret;
	}
	
	public Object getFromMagentoAsync(Object[] params){
		try {
			XmlRpcClient client1 = new XmlRpcClient(urlString, false);
			//Object[] login = new Object[]{new String("Enzo"), new String("password")};
			//String sessionId1 = (String) client1.invoke("login", login);
			
			Object[] exec = new Object[]{sessionId, params};

			return client1.invoke("multiCall", exec);
			
			//client1.invoke("endSession", new Object[]{sessionId1});
		} catch (XmlRpcException e) {
			
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String addProduct(Object[] product){
		Object[] par = new Object[]{this.sessionId, "catalog_product.create", product};
		return (String) this.executeCall(par);
	}
	
	public List<CatalogProductEntity> getProductList(){
		
		List<CatalogProductEntity> ret = new ArrayList<CatalogProductEntity>();
		
		Object[] par = new Object[]{this.sessionId, "catalog_product.list"};
		XmlRpcArray arr = (XmlRpcArray) this.executeCall(par);
		
		Iterator<XmlRpcStruct> it = arr.iterator();
		
		while(it.hasNext()){
			
			CatalogProductEntity prod = new CatalogProductEntity();
			
			XmlRpcStruct prodMag = it.next();
			
			prod.setProduct_id(prodMag.getString("product_id"));
			prod.setSku(prodMag.getString("sku"));
			prod.setName(prodMag.getString("name"));
			prod.setSet(prodMag.getString("set"));
			prod.setType(prodMag.getString("type"));
			
			ret.add(prod);
		}
		
		return ret;
	}
	
	/**
	 * Conrolla se il prodotto è presente, l'id si rifersce all'attributo "codice prodotto"
	 * inserito in magento come attributo personale
	 */
	public boolean isPresentById(String id){
		HashMap<String, Object> attr = new HashMap<String, Object>();
		attr.put("cky_art", id);
		
		Object[] par = new Object[]{this.sessionId, "catalog_product.list", new Object[]{attr}};
		XmlRpcArray arr = (XmlRpcArray) this.executeCall(par);
		if(arr.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	 * Ritorna l'id che usa Magento, dato l'id "codice articolo" usato come campo personale
	 * @param id
	 * @return
	 */
	public String getMagentoIdbyCodeiceArticolo(String id){
		HashMap<String, Object> attr = new HashMap<String, Object>();
		attr.put("cky_art", id);
		
		String ret = "";
		
		Object[] par = new Object[]{this.sessionId, Methods.CATALOG_PRODUCT_LIST.toString(), new Object[]{attr}};
		XmlRpcArray arr = (XmlRpcArray) this.executeCall(par);
		if(!arr.isEmpty()){
		
			//se lo trova restituisce l'id del primo in lista trovato, anche perchè dovrebbe essere l'ultimo
			XmlRpcStruct prodotto = arr.getStruct(0);
			ret = prodotto.getString("product_id");
		}
		
		return ret;
	}
	
	public XmlRpcStruct getProdInfo(String productId){
		
		Object[] proId = new Object[]{productId};
		Object[] par = new Object[]{this.sessionId, "catalog_product.info", proId};
		
		return (XmlRpcStruct)this.executeCall(par);
	}
	
	public XmlRpcArray getAttributeListSet(){
		Object[] par = new Object[]{this.sessionId, "catalog_product_attribute_set.list"};
		return (XmlRpcArray) this.executeCall(par);
	}
	
	public XmlRpcArray getWebsitesList(){
		Object[] par = new Object[]{this.sessionId, "catalog_product_attribute_set.list"};
		return (XmlRpcArray) this.executeCall(par);
	}
	
	public XmlRpcArray getAttributeList(){
		Object[] par = new Object[]{this.sessionId, "catalog_product_attribute.list", new Object[]{"4"}};
		return (XmlRpcArray) this.executeCall(par);
	}
	
	public boolean updateProduct(String id, HashMap<String, Object> valori){
		
		Object[] proId = new Object[]{id, valori};
		Object[] par = new Object[]{this.sessionId, "catalog_product.update", proId};
		
		return (boolean)this.executeCall(par);
	}
	
	/**
	 * Esegue una singola azione verso magento
	 */
	public Object executeCall(Object[] parametri){
		return this.getFromMagento("call", parametri);
	}
	
	/**
	 * Aggiunge un'azione ad un'array di istanza
	 */
	public void putInMulticall(Object[] method){
		this.getMultiCall().add(method);
	}
	
	/**
	 * Manda tutte le azioni aggiunte all'array di istanza a magento
	 */
	public void executeMultiCall(){
		Iterator<Object[]> it = this.getMultiCall().iterator();
		
		Integer maxOperation = 20;
		Integer counter = 0;
		
		Integer sended = 0;
		
		ArrayList<Object[]> stack = new ArrayList<Object[]>();
		
		while(it.hasNext()){
			
			while(it.hasNext()){
				if(counter>=maxOperation){
					counter = 0;
					break;
				}else{
					Object[] ob = it.next();
					stack.add(ob);
					counter++;
				}
			}
				sended += stack.size();
				
				Object[] exec = new Object[]{this.sessionId, stack.toArray()};
				this.getFromMagento("multiCall", exec);
				
				console("inviati "+sended+" su "+this.getMultiCall().size());
			
				stack = new ArrayList<Object[]>();
				
		}
	}
	
	public Object executeMultiCall1(ArrayList<Object[]> stack){
		
				return this.getFromMagentoAsync(stack.toArray());
		
	}
	
	public Object[] createProduct(CatalogProductCreateEntity product){
		
		Object[] prod = new Object[]{	this.defaultType, 				//tipo di Prodotto
										this.defaultListSet, 			//Id della lista degli attributi
										product.getCodiceArticolo(),	//SKU
										product.toHashMap(),			//Entità prodotto con tutti i dati
										this.defaultStoreId};			//Id dello store
		
		return new Object[]{this.sessionId, Methods.CATALOG_PRODUCT_CREATE.toString(), prod};
		
	}
	
	public Object[] deleteProduct(String id){
		
		Object[] prod = new Object[]{id};
		
		return new Object[]{this.sessionId, Methods.CATALOG_PRODUCT_DELETE.toString(), prod};
		
	}
	
	public Object[] createProductMultiCall(CatalogProductCreateEntity product){
		
		Object[] prod = new Object[]{	this.defaultType, 				//tipo di Prodotto
										this.defaultListSet, 			//Id della lista degli attributi
										product.getCodiceArticolo(),	//SKU
										product.toHashMap(),			//Entità prodotto con tutti i dati
										this.defaultStoreId};			//Id dello store
		
		return new Object[]{Methods.CATALOG_PRODUCT_CREATE.toString(), prod};
		
	}
	
	/**
	 * Aggiornamento del prodotto tramite id di magento, single call
	 */
	public Object[] updateProduct(String id, CatalogProductCreateEntity product){
		Object[] prod = new Object[]{	id, 					//id di magento
										product.toHashMap(),};	//Entità prodotto con tutti i dati
	

		return new Object[]{this.sessionId, Methods.CATALOG_PRODUCT_UPDATE.toString(), prod};
	}
	
	/**
	 * Aggiornamento del prodotto tramite id di magento, MultiCall
	 */
	public Object[] updateProductMultiCall(String id, CatalogProductCreateEntity product){
		Object[] prod = new Object[]{	id, 					//id di magento
										product.toHashMap(),};	//Entità prodotto con tutti i dati
	

		return new Object[]{Methods.CATALOG_PRODUCT_UPDATE.toString(), prod};
	}
	
	/**
	 * Aggiornamento di un prodotto tramite "codice articolo", campo personalizzato in magento
	 * @return
	 */
	public Object[] updateProductByCodiceArticolo(String id, CatalogProductCreateEntity product){
		String idMagento = this.getMagentoIdbyCodeiceArticolo(id);
		
		if(!idMagento.isEmpty()|!idMagento.equals("")){
			return this.updateProduct(idMagento, product);
		}else{
			return null;
		}
	}

	public ArrayList<Object[]> getMultiCall() {
		synchronized(this.multiCall){
			return multiCall;
		}
	}

	public synchronized void setMultiCall(ArrayList<Object[]> multiCallArg) {
		synchronized(this.multiCall){
			this.multiCall = multiCallArg;
		}
	}
	
	public Object[] insertImageForProductCodiceArticolo(String cky_art, String label){
		String idMagento = this.getMagentoIdbyCodeiceArticolo(cky_art);
		if(!idMagento.equals("")){
			
			String imageBase64 = FileSystem.getImageBase64ById(cky_art);
			if(imageBase64.equals("")){return new Object[]{};}
			
			//catalogProductImageFileEntity
			HashMap<String, Object> cpife = new HashMap<String, Object>();
			cpife.put("content", imageBase64);
			cpife.put("mime", "image/jpeg");
			cpife.put("name", cky_art);
			
			//catalogProductAttributeMediaCreateEntity 
			HashMap<String, Object> cpam = new HashMap<String, Object>();
			cpam.put("file", cpife); //
			cpam.put("label", label);
			cpam.put("position", "1");
			cpam.put("types", new Object[]{"image", "small_image", "thumbnail"});
			cpam.put("exclude", "0");
			
			
			Object[] prod = new Object[]{
					idMagento,
					cpam
			};
			
			return new Object[]{this.sessionId, Methods.CATALOG_PRODUCT_ATTRIBUTE_MEDIA_CREATE.toString(), prod};
			
		}
		return null;
	}
	
	public XmlRpcStruct getCategoryTree(){
		
		XmlRpcStruct   ret = new XmlRpcStruct();
		
		ret = (XmlRpcStruct) this.executeCall(new Object[]{this.sessionId, Methods.CATALOG_CATEGORY_TREE.toString()});
		
		return ret;
	}
	
	public XmlRpcStruct getCategoryTree(String parentId){
		
		XmlRpcStruct   ret = new XmlRpcStruct();
		
		ret = (XmlRpcStruct) this.executeCall(new Object[]{this.sessionId, Methods.CATALOG_CATEGORY_TREE.toString(), new Object[]{parentId}});
		
		return ret;
	}
	
	public ArrayList<String> getCategoryTreeLinear(){
		
		ArrayList<String>  ret = new ArrayList<String>();
		
		XmlRpcStruct cat = getCategoryTree();
		
		XmlRpcArray rootTree = (XmlRpcArray) cat.get("children");
		
		chargeLinearCategory(ret, rootTree);
		
		return ret;
	}
	
	private void chargeLinearCategory(ArrayList<String> ret, XmlRpcArray cat){
		for(Object key : cat){
			XmlRpcStruct key1 = (XmlRpcStruct) key;
			ret.add((String)key1.get("category_id"));
			XmlRpcArray cat1 = (XmlRpcArray) key1.get("children");
			if(!cat1.isEmpty()){chargeLinearCategory(ret, cat1);}
		}
	}
	
	public ArrayList<String> getCategoryTreeLinearIdInterno(){
		
		ArrayList<String>  ret = new ArrayList<String>();
		
		ArrayList<String> arr = getCategoryTreeLinear();
		
		for(String key : arr){
			ret.add(getIdCatInternById(key));
		}
		
		return ret;
	}
	
	/**
	 * Da ricordare che deve essere presente all'interno dell'eav di magento, l'attributo "catIdInt"
	 * aggiunto come attributo personalizzato delle categorie
	 * @param id
	 * @return
	 */
	
	public String getIdCatInternById(String id){
		String ret = "";
		
		XmlRpcStruct cat = getCategoryInfo(id);
		
		if(!cat.isEmpty()){
			ret = cat.getString("catIdInt");
		}
		
		return ret;
	}
	
	public XmlRpcStruct getCategoryInfo(String id){
		
		XmlRpcStruct   ret = new XmlRpcStruct();
		
		//per richiamare solo alcuni parametri
		//Object call = this.executeCall(new Object[]{this.sessionId, "catalog_category.info", new Object[]{id, "" , new Object[]{""}}});
		
		Object call = this.executeCall(new Object[]{this.sessionId, "catalog_category.info", new Object[]{id}});
		
		if((call.getClass()).equals(XmlRpcStruct.class)){
		
			ret = (XmlRpcStruct) call;
		}
		return ret;
	} 
	
	//public boolean isCategoryIdPresent(String id){
		
	//}
	
	public void createCategory(String parentCat, XmlRpcStruct cat){
		Object[] var = new Object[]{parentCat, cat};
		Object[] par = new Object[]{this.sessionId, Methods.CATALOG_CATEGORY_CREATE.toString(), var};
		this.executeCall(par);
	}

	public void assegnaProdottoAllaCategoria(String product_id, String id_category) {
		Object[] var = new Object[]{id_category, product_id};
		Object[] call = new Object[]{this.sessionId, Methods.CATALOG_CATEGORY_ASSIGNPRODUCT.toString(), var};
		
		this.executeCall(call);
	}
	
}
