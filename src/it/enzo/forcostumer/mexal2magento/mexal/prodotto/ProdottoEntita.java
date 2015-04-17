package it.enzo.forcostumer.mexal2magento.mexal.prodotto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ProdottoEntita{
	private String mexal_cky_art = "";
	private String nome = "";
	private String descrizione = "";
	private String shortDescription = "";
	private String unita_misura = "";
	private String peso = "";
	private String prezzo = "";
	private String ean = "";
	private String categoria = "";
	private String manufacturer = "";
	private Integer disponibilita_totale = 0;
	private Integer disponibilita_casoria = 0;
	private Integer disponibilita_casalnuovo = 0;
	private Integer disponibilita_fornitori_casoria = 0;
	private Date data_prevista_casoria;
	private Integer disponibilita_fornitori_casalnuovo = 0;
	private Date data_prevista_casalnuovo;
	private String stato = "";
	
	
	public String getStato() {
		return stato;
	}

	public void setStato(String visualizzabile) {
		this.stato = visualizzabile;
	}

	public ProdottoEntita(String cky_art, String nome){
		this.mexal_cky_art = cky_art;
		this.nome = nome;
		String data = "00/00/0000";
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		try {
			this.data_prevista_casalnuovo = form.parse(data);
			this.data_prevista_casoria = form.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMexal_cky_art() {
		return mexal_cky_art;
	}

	public void setMexal_cky_art(String mexal_cky_art) {
		this.mexal_cky_art = mexal_cky_art;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUnita_misura() {
		return unita_misura;
	}

	public void setUnita_misura(String unita_misura) {
		this.unita_misura = unita_misura;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	

	public Integer getDisponibilita_totale() {
		return disponibilita_totale;
	}

	public void setDisponibilita_totale(Integer disponibilita_totale) {
		this.disponibilita_totale = disponibilita_totale;
	}

	public Integer getDisponibilita_casoria() {
		return disponibilita_casoria;
	}

	public void setDisponibilita_casoria(Integer disponibilita_casoria) {
		this.disponibilita_casoria = disponibilita_casoria;
	}

	public Integer getDisponibilita_casalnuovo() {
		return disponibilita_casalnuovo;
	}

	public void setDisponibilita_casalnuovo(Integer disponibilita_casalnuovo) {
		this.disponibilita_casalnuovo = disponibilita_casalnuovo;
	}

	public Integer getDisponibilita_fornitori_casoria() {
		return disponibilita_fornitori_casoria;
	}

	public void setDisponibilita_fornitori_casoria(
			Integer disponibilita_fornitori_casoria) {
		this.disponibilita_fornitori_casoria = disponibilita_fornitori_casoria;
	}

	public Date getData_prevista_casoria() {
		return data_prevista_casoria;
	}

	public void setData_prevista_casoria(Date data_prevista_casoria) {
		this.data_prevista_casoria = data_prevista_casoria;
	}

	public Integer getDisponibilita_fornitori_casalnuovo() {
		return disponibilita_fornitori_casalnuovo;
	}

	public void setDisponibilita_fornitori_casalnuovo(
			Integer disponibilita_fornitori_casalnuovo) {
		this.disponibilita_fornitori_casalnuovo = disponibilita_fornitori_casalnuovo;
	}

	public Integer getDisponibilta_fornitori_totale(){
		return this.getDisponibilita_fornitori_casoria() + this.getDisponibilita_fornitori_casalnuovo();
	}
	
	public Date getData_prevista_casalnuovo() {
		return data_prevista_casalnuovo;
	}

	public void setData_prevista_casalnuovo(Date data_prevista_casalnuovo) {
		this.data_prevista_casalnuovo = data_prevista_casalnuovo;
	}
	
	public Date getData_prevista_arrivo(){
		if(this.getData_prevista_casoria().before(this.getData_prevista_casalnuovo())){
			//è più recente
			return this.getData_prevista_casoria();
		}else{
			//è più vecchia
			return this.getData_prevista_casalnuovo();
		}
	}
	
	

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

}