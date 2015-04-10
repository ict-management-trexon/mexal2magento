package it.enzo.forcostumer.mexal2magento.magento.entita;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

public class CatalogProductCreateEntity {

	@MagentoKey("categories")
	private Object[] defaultCategories = new Object[]{"2"};
	
	@MagentoKey("websites")
	private Object[] defaultWebsites = new Object[]{"1"};
	
	@MagentoKey("name")
	private String name = "";
	
	@MagentoKey("description")
	private String description = "";
	
	@MagentoKey("short_description")
	private String shortDescription = "";
	
	@MagentoKey("weight")
	private String weight = "";
	
	@MagentoKey("status")
	private String status = "1";
	
	@MagentoKey("visibility")
	private String visibility = "4";
	
	@MagentoKey("price")
	private String price = "";
	
	@MagentoKey("tax_class_id")
	private String taxClassId = "0";
	
	@MagentoKey("manufacturer")
	private String manufacturer = "";
	
	//@MagentoKey("meta_title")
	private String metaTitle;
	private String metaKeywords;
	private String metaDescription;
	
	
	//Campi personalizzati
	
	@MagentoKey("cky_art")
	private String codiceArticolo = "";
	
	@MagentoKey("ean_code")
	private String eanCode = "";
	
	@MagentoKey("quantita_totale")
	private Integer quantitaTotale = 0;
	
	@MagentoKey("quantita_casalnuovo")
	private Integer quantitaCasalnuovo = 0;
	
	@MagentoKey("quantita_casoria")
	private Integer quantitaCasoria = 0;
	
	@MagentoKey("quantita_drop_ship")
	private Integer quantitaDrop = 0;
	
	@MagentoKey("quantita_in_arrivo")
	private Integer quantitaInArrivo = 0;
	
	@MagentoKey("stock_data")
	private CatalogInventoryStockItemUpdateEntity stockItemUpdate = new CatalogInventoryStockItemUpdateEntity();
	
	private Date dataDisponibilita;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTaxClassId() {
		return taxClassId;
	}
	public void setTaxClassId(String taxClassId) {
		this.taxClassId = taxClassId;
	}
	public String getMetaTitle() {
		return metaTitle;
	}
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	public String getMetaKeywords() {
		return metaKeywords;
	}
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getCodiceArticolo() {
		return codiceArticolo;
	}
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}
	public String getEanCode() {
		return eanCode;
	}
	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}
	public Integer getQuantitaTotale() {
		return quantitaTotale;
	}
	public void setQuantitaTotale(Integer quantitaTotale) {
		this.quantitaTotale = quantitaTotale;
	}
	public Integer getQuantitaCasalnuovo() {
		return quantitaCasalnuovo;
	}
	public void setQuantitaCasalnuovo(Integer quantitaCasalnuovo) {
		this.quantitaCasalnuovo = quantitaCasalnuovo;
	}
	public Integer getQuantitaCasoria() {
		return quantitaCasoria;
	}
	public void setQuantitaCasoria(Integer quantitaCasoria) {
		this.quantitaCasoria = quantitaCasoria;
	}
	public Integer getQuantitaDrop() {
		return quantitaDrop;
	}
	public void setQuantitaDrop(Integer quantitaDrop) {
		this.quantitaDrop = quantitaDrop;
	}
	public Integer getQuantitaInArrivo() {
		return quantitaInArrivo;
	}
	public void setQuantitaInArrivo(Integer quantitaInArrivo) {
		this.quantitaInArrivo = quantitaInArrivo;
	}
	
	public Date getDataDisponibilita() {
		return dataDisponibilita;
	}
	public void setDataDisponibilita(Date dataDisponibilita) {
		this.dataDisponibilita = dataDisponibilita;
	}
	public CatalogInventoryStockItemUpdateEntity getstockItemUpdate() {
		return stockItemUpdate;
	}
	public void setstockItemUpdate(CatalogInventoryStockItemUpdateEntity cisiue) {
		this.stockItemUpdate = cisiue;
	}
	public HashMap<String, Object> toHashMap(){
		HashMap<String, Object> ret = new HashMap<String, Object>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		
		for(int i = 0;i<fields.length;i++){
			Field field = fields[i];
			MagentoKey annot = field.getAnnotation(MagentoKey.class);
			
			
				Object valore = "";
				try {
				
					if(field.getType().equals(CatalogInventoryStockItemUpdateEntity.class)){
						valore = ((CatalogInventoryStockItemUpdateEntity) field.get(this)).toHashMap();
					}else{
						valore = field.get(this);
					}
				
				
				}catch(IllegalArgumentException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();} 
				catch(NullPointerException e){}
			
				try{
					if(!valore.equals("")){
						ret.put(annot.value(), valore);
					}
				}catch(NullPointerException e){}
			
		}
		
		return ret;
	}
}
