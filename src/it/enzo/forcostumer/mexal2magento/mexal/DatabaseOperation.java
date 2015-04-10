package it.enzo.forcostumer.mexal2magento.mexal;

import it.enzo.forcostumer.mexal2magento.io.fileSystem.FileSystem;
import it.enzo.forcostumer.mexal2magento.mexal.prodotto.ProdottoEntita;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DatabaseOperation {

	private static final String jdbc_drivername = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public String connection_string  = "";
	public String databaseName = "";
	public String uId = "";
	public String password = "";
	
	//private String tableName= "trn_rp.dbo.TRN_ARTI";
	
	private static final Integer MAGAZZINO_CASORIA = 1;
	private static final Integer MAGAZZINO_CASALNUOVO = 6;
	
	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	
	private static final String nomeTabellaTrigger = "TRIGGER_TABLE";
	private ArrayList<Triggers> listaTriggers = new ArrayList<Triggers>();
	
	private Connection conn;

	static{
		
		//Registrazione del connector
		try {
			Class.forName(jdbc_drivername);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public DatabaseOperation(){
			//this.startConn();
			
			
			Triggers trig1 = new Triggers("TRIGGER_TRN_ARTI_INSERT","TRN_ARTI","INSERT");
			Triggers trig2 = new Triggers("TRIGGER_TRN_ARTI_UPDATE","TRN_ARTI","UPDATE");
			Triggers trig3 = new Triggers("TRIGGER_TRN_ARTI_DELETE","TRN_ARTI","DELETE");
			
			Triggers trig4 = new Triggers("TRIGGER_TRN_ARTI_LISTINI_INSERT","TRN_ARTI_LISTINI","INSERT");
			Triggers trig5 = new Triggers("TRIGGER_TRN_ARTI_LISTINI_UPDATE","TRN_ARTI_LISTINI","UPDATE");			
			
			Triggers trig7 = new Triggers("TRIGGER_TRN_ARTP_INSERT","TRN_ARTP","INSERT");
			Triggers trig8= new Triggers("TRIGGER_TRN_ARTP_UPDATE","TRN_ARTP","UPDATE");			
			
			Triggers trig10 = new Triggers("TRIGGER_TRN_ARTP_ORDI_INSERT","TRN_ARTP_ORDI","INSERT");
			Triggers trig11= new Triggers("TRIGGER_TRN_ARTP_ORDI_UPDATE","TRN_ARTP_ORDI","UPDATE");
			
			Triggers trig13 = new Triggers("TRIGGER_TRN_ARTP_QTA_INSERT","TRN_ARTP_QTA","INSERT");
			Triggers trig14= new Triggers("TRIGGER_TRN_ARTP_QTA_UPDATE","TRN_ARTP_QTA","UPDATE");
			
			Triggers trig15 = new Triggers("TRIGGER_TRN_PREZ_INSERT","TRN_PREZ","INSERT");
			Triggers trig16= new Triggers("TRIGGER_TRN_PREZ_UPDATE","TRN_PREZ","UPDATE");
			
			
			listaTriggers.add(trig1);
			listaTriggers.add(trig2);
			listaTriggers.add(trig3);
			listaTriggers.add(trig4);
			listaTriggers.add(trig5);
			listaTriggers.add(trig7);
			listaTriggers.add(trig8);
			listaTriggers.add(trig10);
			listaTriggers.add(trig11);
			listaTriggers.add(trig13);
			listaTriggers.add(trig14);
			listaTriggers.add(trig15);
			listaTriggers.add(trig16);
	}
	
	public void startConn(){
		try {
			this.conn = DriverManager.getConnection(this.connection_string, this.uId, this.password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConn(){
		try {
			this.conn.commit();
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeAll(){
		
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static String getNometabellatrigger() {
		return nomeTabellaTrigger;
	}

	/**
	 * Restituisce un'entità che rappresente l'articolo così com'è visualizzato in mexal, nel database
	 * @param CKY_ART
	 * @return
	 */
	public ProdottoEntita getProdottoById(String CKY_ART){
		Statement stm;
		ResultSet rs;
		ProdottoEntita artOb = new ProdottoEntita("","");
		String sql = "select CKY_ART, IST_ART, CDS_ART, CDS_AGGIUN_ART, NCF_CONV, NGB_CAT_STAT_ART, CSG_UNIMIS_PRI from trn_rp.dbo.TRN_ARTI where CKY_ART = '"+CKY_ART+"' and IST_ART = 'A'";
		try{
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				
				String nome = rs.getString("CDS_ART").replaceAll("\\s+$", "") + rs.getString("CDS_AGGIUN_ART").replaceAll("\\s+$", "");
				Character firstChr = (Character) nome.charAt(0);
				
				String stato = "1";//ABILITATO
				if(firstChr.equals('!')){
					stato = "2";//DISABILITATO
				}
				
				if(firstChr.equals('$')||firstChr.equals('<')||firstChr.equals('>')||firstChr.equals('%')||firstChr.equals('^')||firstChr.equals('!')){
					//Elimina il primo carattere;
					nome = nome.substring(1);
				}
				
				artOb = new ProdottoEntita(CKY_ART, nome);
				artOb.setStato(stato);
				artOb.setUnita_misura(rs.getString("CSG_UNIMIS_PRI").replaceAll("\\s+$", ""));
				artOb.setPeso(rs.getString("NCF_CONV").replaceAll("\\s+$", ""));
				artOb.setCategoria(rs.getString("NGB_CAT_STAT_ART").replaceAll("\\s+$", ""));
				artOb.setManufacturer(getManufacturerById(CKY_ART));
				artOb.setDescrizione(getDescriptionById(artOb.getMexal_cky_art()));
				artOb.setPrezzo(getPriceById(artOb.getMexal_cky_art()));
				artOb.setDisponibilita_casoria(	getDisponibilita(artOb.getMexal_cky_art(), MAGAZZINO_CASORIA));
				artOb.setDisponibilita_casalnuovo(	getDisponibilita(artOb.getMexal_cky_art(), MAGAZZINO_CASALNUOVO));
				artOb.setEan(getEanById(artOb.getMexal_cky_art()));
				artOb.setShortDescription(getShortDescriptionById(CKY_ART));
				
				artOb.setDisponibilita_fornitori_casoria(	getOrdineDaFornitori(artOb.getMexal_cky_art(), MAGAZZINO_CASORIA));
				if(artOb.getDisponibilita_fornitori_casoria()>0){
					artOb.setData_prevista_casoria(getDataPrevistaArrivoOrdine(artOb.getMexal_cky_art()));
				}
				
				artOb.setDisponibilita_fornitori_casalnuovo(	getOrdineDaFornitori(artOb.getMexal_cky_art(), MAGAZZINO_CASALNUOVO));
				if(artOb.getDisponibilita_fornitori_casalnuovo()>0){
					artOb.setData_prevista_casalnuovo(getDataPrevistaArrivoOrdine(artOb.getMexal_cky_art()));
				}
				
				
				artOb.setDisponibilita_totale(artOb.getDisponibilita_casoria()+artOb.getDisponibilita_casalnuovo()+artOb.getDisponibilita_fornitori_casoria()+artOb.getDisponibilita_fornitori_casalnuovo());
			}
			rs.close();
			stm.close();
		} catch (SQLException e){e.printStackTrace();}
		return artOb;
	}
	
	public List<String> getAllProduct(){
		List<String> ret = new ArrayList<String>();
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART "
				+ "from trn_rp.dbo.TRN_ARTI "
				+ "where CDS_ART not like '!%' " //che non inizia per !
				+ "and IST_ART = 'A'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ret.add(rs.getString("CKY_ART").replaceAll("\\s+$", ""));
			}
			
			
			rs.close();
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		
	
		
		return ret;
	}
	
	public List<ProdottoEntita> selectWathelse(){
		
		List<ProdottoEntita> ret = new ArrayList<ProdottoEntita>();
		
		Statement stm;	
		
		try {
			
			stm = conn.createStatement();
		
			String sql = "select CKY_ART, CDS_ART, CDS_AGGIUN_ART, CSG_UNIMIS_PRI from trn_rp.dbo.TRN_ARTI where CDS_ART not like '!%' and IST_ART = 'A'";
			
			ResultSet rs = stm.executeQuery(sql);
			
			
			
			while(rs.next()){
				ProdottoEntita artOb = new ProdottoEntita(rs.getString("CKY_ART").replaceAll("\\s+$", ""), rs.getString("CDS_ART").replaceAll("\\s+$", "") + rs.getString("CDS_AGGIUN_ART").replaceAll("\\s+$", ""));
				//artOb.setDescrizione(getDescriptionById(rs.getString("CKY_ART").replaceAll("\\s+$", "")));
				artOb.setUnita_misura(rs.getString("CSG_UNIMIS_PRI").replaceAll("\\s+$", ""));
				ret.add(artOb);
			}
			
			rs.close();
			stm.close();
			
			Iterator<ProdottoEntita> it = ret.iterator();
			
			//caricamento descrizione
			while(it.hasNext()){
				ProdottoEntita artOb = it.next();
				artOb.setDescrizione(	getDescriptionById(	artOb.getMexal_cky_art()	)	);
			}
			
			it = ret.iterator();
			
			//caricamento prezzo
			while(it.hasNext()){
				ProdottoEntita artOb = it.next();
				artOb.setPrezzo(	getPriceById(	artOb.getMexal_cky_art()	)	);
			}
			
			it = ret.iterator();
			
			//caricamento disponibilità
			while(it.hasNext()){
				
				ProdottoEntita artOb = it.next();
				artOb.setDisponibilita_casoria(	getDisponibilita(artOb.getMexal_cky_art(), MAGAZZINO_CASORIA));
				artOb.setDisponibilita_casalnuovo(	getDisponibilita(artOb.getMexal_cky_art(), MAGAZZINO_CASALNUOVO));
				
				artOb.setDisponibilita_fornitori_casoria(	getOrdineDaFornitori(artOb.getMexal_cky_art(), MAGAZZINO_CASORIA));
				if(artOb.getDisponibilita_fornitori_casoria()>0){
					artOb.setData_prevista_casoria(getDataPrevistaArrivoOrdine(artOb.getMexal_cky_art()));
				}
				
				artOb.setDisponibilita_fornitori_casalnuovo(	getOrdineDaFornitori(artOb.getMexal_cky_art(), MAGAZZINO_CASALNUOVO));
				if(artOb.getDisponibilita_fornitori_casalnuovo()>0){
					artOb.setData_prevista_casalnuovo(getDataPrevistaArrivoOrdine(artOb.getMexal_cky_art()));
				}
				
				
				artOb.setDisponibilita_totale(artOb.getDisponibilita_casoria()+artOb.getDisponibilita_casalnuovo()+artOb.getDisponibilita_fornitori_casoria()+artOb.getDisponibilita_fornitori_casalnuovo());
			}
	
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	public String getDescriptionById(String cky_art){
		
		String ret = "";
		
		
		Statement stm;
		
		try {
			
			stm = conn.createStatement();
		
			String sql = "select CDS_NOTA from trn_rp.dbo.TRN_ARTM_NOTE where CKY_ART = '" + cky_art + "'";
			
			ResultSet rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ret += rs.getString("CDS_NOTA").replaceAll("\\s+$", "")+"\n<br>";
				//System.out.println(rs.getString("CDS_NOTA"));
			}
			
			rs.close();

			stm.close();
			
			ret += FileSystem.getTextById(cky_art);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ret;
	}

	public String getPriceById(String cky_art){
		Statement stm;
		
		try {
			stm = conn.createStatement();
		
		
			String sql = "select NPZ_LIS from trn_rp.dbo.TRN_ARTI_LISTINI where CKY_ART = '" + cky_art + "' and ID_CR = 8";
			
			ResultSet rs = stm.executeQuery(sql);
			
			String price = "";
		
			while(rs.next()){
				price = rs.getString("NPZ_LIS").replaceAll("\\s+$", "");
			}
			
			if(price.isEmpty()){
				price = "0,00";
			}
			
			rs.close();

			stm.close();
			
			
			
			return price;
		
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return null;
	}
	
	private Integer getAnno(){
		//Ritorna il valore dell'anno in corso secondo quanto serve per Mexal
		DateFormat dateForm = new SimpleDateFormat("yy");
		Date date = new Date();
		return Integer.valueOf(dateForm.format(date))-10;
	}
	
	public Integer getEsistenza(String CKY_ART, Integer NKY_DEP){
		
		Integer esistenza = 0;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, NKY_DEP, NGB_ANNO, NMP_INV, NMP_CAR, NMP_SCAR "
				+ "from trn_rp.dbo.TRN_ARTP_QTA "
				+ "where CKY_ART = '"+CKY_ART+"' "
				+ "and NKY_DEP = '"+NKY_DEP+"' "
				+ "and NGB_ANNO = '"+getAnno()+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				
				Integer scarico = rs.getInt("NMP_SCAR");
				Integer carico = rs.getInt("NMP_CAR");
				Integer inventario = rs.getInt("NMP_INV");
				
				if(scarico==null){scarico = 0;}
				if(carico==null){carico = 0;}
				if(inventario==null){inventario = 0;}
				
				if(carico>scarico){
					esistenza = (carico-scarico)+inventario;
				}else{
					esistenza = inventario - (scarico-carico);
				}
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return esistenza;
	}
	
	public Integer getImpegniClienti(String CKY_ART, Integer NKY_DEP){
		
		Integer impegniClienti = 0;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, NKY_DEP, NQT_ORD_CLI "
				+ "from trn_rp.dbo.TRN_ARTP_ORDI "
				+ "where CKY_ART = '"+CKY_ART+"' "
				+ "and NKY_DEP = '"+NKY_DEP+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				impegniClienti =  rs.getInt("NQT_ORD_CLI");
				if (impegniClienti == null) {impegniClienti = 0;}
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return impegniClienti;
	}
	
	public Integer getDisponibilita(String CKY_ART, Integer NKY_DEP){
		
		Integer ret = getEsistenza(CKY_ART, NKY_DEP)-getImpegniClienti(CKY_ART, NKY_DEP);
		
		if((ret==null)|(ret==-1)){ret=0;}
		
		return ret;
	}
	
	public Integer getOrdineDaFornitori(String CKY_ART, Integer NKY_DEP){
		Integer impegniFornitori = 0;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, NKY_DEP, NQT_ORD_FOR "
				+ "from trn_rp.dbo.TRN_ARTP_ORDI "
				+ "where CKY_ART = '"+CKY_ART+"' "
				+ "and NKY_DEP = '"+NKY_DEP+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				impegniFornitori =  rs.getInt("NQT_ORD_FOR");
				if (impegniFornitori==null) {impegniFornitori = 0;}
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return impegniFornitori;
	}
	
	public Date getDataPrevistaArrivoOrdine(String CKY_ART){
		Date dataPrevista = null;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, DTT_SCAD "
				+ "from trn_rp.dbo.TRN_ORDF_D "
				+ "where CKY_ART = '"+CKY_ART+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				dataPrevista =  rs.getDate("DTT_SCAD");
				//if (dataPrevista==null) {dataPrevista = 0;}
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return dataPrevista;
	}
	
	public String getEanById(String CKY_ART){
		String ean = "";
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, V4AA_CODICE_PRODUTTORE "
				+ "from trn_rp.dbo.TRN_ARTM_VID "
				+ "where CKY_ART = '"+CKY_ART+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ean =  rs.getString("V4AA_CODICE_PRODUTTORE").replaceAll("\\s+$", "");
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return ean;
	}
	
	public String getShortDescriptionById(String CKY_ART){
		String shortDesc = "";
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, WAA_TITOLO_COMMERCIALE, "
				+ "WAC_DESCRIZIONE_BREVE_1, WAD_DESCRIZIONE_BREVE_2 "
				+ "from trn_rp.dbo.TRN_VIDAD "
				+ "where CKY_ART = '"+CKY_ART+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				shortDesc =  rs.getString("WAA_TITOLO_COMMERCIALE").replaceAll("\\s+$", "") + "\n<br>" + rs.getString("WAC_DESCRIZIONE_BREVE_1").replaceAll("\\s+$", "") + rs.getString("WAD_DESCRIZIONE_BREVE_2").replaceAll("\\s+$", "");
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return shortDesc;
	}
	
	public String getManufacturerById(String CKY_ART){
		String ret = "";
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, WAB_MARCA "
				+ "from trn_rp.dbo.TRN_VIDAD "
				+ "where CKY_ART = '"+CKY_ART+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ret =  rs.getString("WAB_MARCA").replaceAll("\\s+$", "");
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return ret;
	}
	
	public URL getImageURLById(String CKY_ART){
		URL url = null;
		String ret = null;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, WAG_LINK_IMMAGINE "
				+ "from trn_rp.dbo.TRN_VIDAD "
				+ "where CKY_ART = '"+CKY_ART+"'";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ret = "http://www.trexon.it/products/";
				ret +=  rs.getString("WAG_LINK_IMMAGINE").replaceAll("\\s+$", "");
				url = new URL(ret);
				FileSystem.downloadImageToDiskById(url, CKY_ART);
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return url;
	}
	
	public void downloadAllImage(){
		URL url = null;
		String ret = null;
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, WAG_LINK_IMMAGINE "
				+ "from trn_rp.dbo.TRN_VIDAD";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				String cky_art = rs.getString("CKY_ART").replaceAll("\\s+$", "");
				if(!FileSystem.ifExist(FileSystem.percorsoImmagini+"/"+cky_art+".jpg")){
					ret = "http://www.trexon.it/products/";
					ret +=  rs.getString("WAG_LINK_IMMAGINE").replaceAll("\\s+$", "");
					url = new URL(ret);
					console("Download in corso immaggine articolo id "+cky_art+"..."+url.toString());
					FileSystem.downloadImageToDiskById(url, cky_art);
					console("Effettuato...");
				}
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public HashMap<String, String> getProductsWithImage(){
		
		HashMap<String, String> ret = new HashMap<String, String>();
		
		Statement stm;
		ResultSet rs;
		
		String sql = "select CKY_ART, WAA_TITOLO_COMMERCIALE "
				+ "from trn_rp.dbo.TRN_VIDAD";
		
		try{
			stm = conn.createStatement();
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				ret.put(rs.getString("CKY_ART").replaceAll("\\s+$", ""), rs.getString("WAA_TITOLO_COMMERCIALE").replaceAll("\\s+$", ""));
			}
			
			rs.close();
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		return ret;
	}
	
	private void createTrigger(String nome, String tabella, String operazione){
		
		String riga = "inserted";
		if(operazione.equals(DELETE)){riga = "deleted";}
		
		
		Statement stm;
		
		String sql = "create trigger  dbo."+nome+"\n"
				+"ON trn_rp.dbo."+tabella+"\n"
				+"FOR "+operazione+"\n"
				+"AS\n"
				+"DECLARE @cky_art char(60)\n"
				+"SELECT @cky_art = CKY_ART from "+riga+"\n"
				+"BEGIN\n"
					+"SET NOCOUNT ON\n"
					+"DELETE FROM trn_rp.dbo."+nomeTabellaTrigger +" where CKY_ART = @cky_art\n"
					+"INSERT INTO trn_rp.dbo."+nomeTabellaTrigger +" (CKY_ART, OPERAZIONE, TABELLA_PROV, DATA_OPERAZIONE) VALUES(@cky_art, '"+operazione+"', '"+tabella+"', getdate())\n"
					+"\n"
				+"END";
				
		
		try{
			stm = conn.createStatement();
			
			stm.execute(sql);
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
		
		
	}
	
	private void verificaEsistenzaTabelleTrigger(){
		
		String nomeTab = nomeTabellaTrigger;
		
		String queryCreazione = "create table "+nomeTab+" (ID int IDENTITY PRIMARY KEY, CKY_ART char(32), OPERAZIONE char(6), TABELLA_PROV char(30), DATA_OPERAZIONE datetime)";
		
		String queryEsistenza = "select name from sys.objects where name = '"+nomeTab+"'";
		
		Statement stm;
		ResultSet rs;
		boolean create = false;
		
		try{
			
			stm = conn.createStatement();
			
			rs = stm.executeQuery(queryEsistenza);
			
			if(!rs.next()){
				create = true;
			}else{console("Tabella "+nomeTab+" eistente");}
			
			rs.close();
			
			stm.close();
			
			if(create){
				console("Tabella "+nomeTab+" inesistente");
				stm = conn.createStatement();
				stm.execute(queryCreazione);
				stm.close();
				console("Tabella "+nomeTab+" creata");
			}
			
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public void verificaEsistenzaTriggers(){
		
		this.verificaEsistenzaTabelleTrigger();
		
		Statement stm;
		ResultSet rs;
		
		for(Triggers trig : listaTriggers){
			
			String esistenza = "select name from sys.objects where type = 'TR' and name = '"+trig.getNomeTrigger()+"'";
			
			try{
				
				stm = conn.createStatement();
				
				rs = stm.executeQuery(esistenza);
				
				if(!rs.next()){
					console("Trigger "+trig.getNomeTrigger()+" inesistente");
					this.createTrigger(trig.nomeTrigger, trig.nomeTabella, trig.operazione);
					console("Trigger "+trig.getNomeTrigger()+" creato");
				}else{console("Trigger "+trig.getNomeTrigger()+" esistente");}
				
				rs.close();
				
				stm.close();
				
			}catch(Exception e){e.printStackTrace();}
		}
		
		
				
		
		
	}
	
	public void deleteTriggers(){
		for(Triggers trig : listaTriggers){
			eseguiQuery("if OBJECT_ID('"+trig.getNomeTrigger()+"', 'TR') IS NOT NULL \n"
					+ "drop trigger dbo."+trig.getNomeTrigger()+"");
		}
		eseguiQuery("if OBJECT_ID('"+nomeTabellaTrigger +"', 'U') IS NOT NULL \n"
				+ "drop table dbo."+nomeTabellaTrigger+"");
	}
	
	public void eseguiQuery(String query){
		
		Statement stm;
		
		String sql = query;
				
		
		try{
			stm = conn.createStatement();
			
			stm.execute(sql);
			
			stm.close();
			
		}catch(Exception e){e.printStackTrace();}
	}
	
	public ArrayList<String> getFromTriggered(String oper){
		ArrayList<String> ret = new ArrayList<String>();
		
		Statement stm;
		ResultSet rs;
		
		String query = "select CKY_ART from trn_rp.dbo.TRIGGER_TABLE where OPERAZIONE = '"+oper+"' order by DATA_OPERAZIONE desc";
		
		try{
			
			stm = conn.createStatement();
			
			rs = stm.executeQuery(query);
			
			while(rs.next()){
				ret.add(rs.getString("CKY_ART").replaceAll("\\s+$", ""));
			}
			
			rs.close();
			
			stm.close();
			
			
		}catch(Exception e){e.printStackTrace();}
		
		return ret;
	}
	
	
	public void deleteFromTriggered(String id){
		Statement stm;
		try{
			stm = conn.createStatement();
			String sql = "delete from dbo."+nomeTabellaTrigger+" where CKY_ART = '"+id+"'";
			stm.execute(sql);
			stm.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	public static void console(Object out){
		System.out.println(out);
	}
	
	class Triggers{
		private String nomeTrigger;
		private String nomeTabella;
		private String operazione;
		
		public Triggers(String nome, String tabella, String operazione){
			this.setNomeTrigger(nome);
			this.setNomeTabella(tabella);
			this.setOperazione(operazione);
		}

		public String getNomeTrigger() {
			return nomeTrigger;
		}

		public void setNomeTrigger(String nomeTrigger) {
			this.nomeTrigger = nomeTrigger;
		}

		public String getNomeTabella() {
			return nomeTabella;
		}

		public void setNomeTabella(String nomeTabella) {
			this.nomeTabella = nomeTabella;
		}

		public String getOperazione() {
			return operazione;
		}

		public void setOperazione(String operazione) {
			this.operazione = operazione;
		}
	}

	
}
