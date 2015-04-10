package it.enzo.forcostumer.mexal2magento.magento.entita;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CatalogInventoryStockItemUpdateEntity {

	@MagentoKey("qty")
	private String quantity;
	
	@MagentoKey("is_in_stock")
	private String isInStock = "1";
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getIsInStock() {
		return isInStock;
	}
	public void setIsInStock(String isInStock) {
		this.isInStock = isInStock;
	}
	
	public HashMap<String, Object> toHashMap(){
		HashMap<String, Object> ret = new HashMap<String, Object>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		
		for(int i = 0;i<fields.length;i++){
			MagentoKey annot = fields[i].getAnnotation(MagentoKey.class);
			Object valore = "";
			try {
				valore = fields[i].get(this);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!valore.equals("")){
				ret.put(annot.value(), valore);
			}
		}
		
		return ret;
	}
}
