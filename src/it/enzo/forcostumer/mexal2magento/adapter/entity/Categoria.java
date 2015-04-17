package it.enzo.forcostumer.mexal2magento.adapter.entity;

import java.util.HashMap;

public class Categoria {

	private String mexalName;
	private String mexalId;
	
	private String magentoName;
	private String magentoId;
	
	private HashMap<String, Categoria> sottocategorie;
}
