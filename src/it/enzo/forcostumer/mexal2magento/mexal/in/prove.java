package it.enzo.forcostumer.mexal2magento.mexal.in;

//import sm.passepartout.MSprixJ;

public class prove {

	private void provetm(){
		/*
		MSprixJ se = new MSprixJ();
		
		se.setINDIRIZZO("172.16.2.20");
		se.setPORTA(9000);
		//se.setLOGINMXSRV("mexal");
		//se.setPASSWORDMXSRV("mextre");
		se.setPASSWORDPASS("AUTOSPX:AUTOSPX");
		se.setAZIENDA("TRN");
		se.setDATAAPTERM("2");
		
		se.AVVIACONNESSIONE();
		/*
		
		console(se.DESART_S("0000020613"));
		console(se.DESART_S("0000019104"));
		console(se.DESART_S("0000013175"));
		console(se.DESART_S("0000017128"));
		//console(se.SPXREMOTO_S("172.16.2.20:9000", "-aTRN -kAUTOSPX:AUTOSPX -pspx740 -vC:\\ferro", "", "datiSprix.txt", "errori.txt", 0));
		Object s = se.GETAR("0000017128", 0);
		console(se.getARCOD_S());
		console(se.getARGGR(0));
		console(se.getARGGR(1));
		console(se.getARGGR(2));
		console(se.getARGGR(3));
		console(se.getERRAR());
		
		se.GETPC("502.04766"); //Vincenzo D'Aniello
		console(se.getPCCOG_S());
		console(se.getPCNOM_S());
		
		/*
		for(int i = 0;i<26;i++){
			for(int y = 0;y<24;y++){
				console("("+i+")("+y+")"+se.getPCWWW_S(i, y));
			}
		}
		
		for(int i = 1;i<=26;i++){
			console("VDF("+i+")"+se.getPCVDF_S(i));
			console("VDR("+i+")"+se.getPCVDR_S(i));
		}
		
		//se.setPCVDF_S(1, "00000000000000000000000000000000000000000000000000000000");
		
		//se.PUTPC();
		//console(se.getPCVDF_S(1));
		
		console("Dizionario "+se.DIZ("pc1aa", 0, new Object[]{"502.04766"}));
		
		
		
		
		
		
		//CREAZIONE DI UN ORDINE
		
		//Testa dell'ordine
		se.setMMNUM(0);//Auto numerazione
		se.setMMSIG_S("OX");//Sigla del documento, OX sta per ordini per corrispondenza
		se.setMMSER(1);//Numero di serie obbligatorio, sempre 1 nel nostro caso
		se.setMMDAT_S("20150429");//data nel formato YYYYMMDD
		se.setMMMAG(1);//Magazzino, 1 = cash Casoria
		
		
		se.setMMCLI_S("502.04766");//Codice conto cliente
		
		se.setMMSIGRE_S("MAG");//Riferimento documento esterno
		se.setMMNUMRE_S("10000047-1");//Numero documento esterno
		se.setMMDATRE_S("20150428");//Data documento esterno
		
		se.setMMCMO(1, 1);//Casusale movimento, primo valore si riferisce al numero di testata, in caso di fusione di documenti, il secondo valore la causale, 1=vendita
		se.setMMPOR_S(1, "D");//Tipo di porto, primo valore come MMCMO, secondo tipo di porto
		se.setMMTPS_S(1, "V");//Tipo spese di spedizione
		se.setMMVAS(1, 7.50);//Spese di spedizione
		se.setMMAGE_S("631.00030");//Codice conto agente
		se.setMMVET_S(1, "601.00112");//codice del vettore
		se.setMMTPE(1, 2.5);//Totale peso
		se.setMMAPE_S("N");//Calcolo automatico peso
		se.setMMPAG(10);//Metodo di pagamento
		
		
		//PIEDE DELL'ORDINE
		//Inserimento riga 1 articolo
				se.setMMTPR_S(1, "R");//Tipo di riga
				se.setMMART_S(1, "0000002543");//codice articolo
				se.setMMDES_S(1, "");//descrizione articolo, "" = AUTO
				se.setMMORD_S(1, "E");//Stato riga d'ordine
				se.setMMALI_S(1, se.DIZ("ariva",0,new Object[]{"0000002543"}));//Aliquota iva del prodotto
				se.setMMQTA(1, 1);//quantita da impegnare
				se.setMMPRZ(1, 13);//Prezzo ivato
		
		//Inserimento riga 2 articolo
				se.setMMTPR_S(2, "R");//Tipo di riga
				se.setMMART_S(2, "0000022445");//codice articolo
				se.setMMDES_S(2, "");//descrizione articolo, "" = AUTO
				se.setMMORD_S(2, "E");//Stato riga d'ordine
				se.setMMALI_S(2, se.DIZ("ariva",0,new Object[]{"0000022445"}));//Aliquota iva del prodotto
				se.setMMQTA(2, 1);//quantita da impegnare
				se.setMMPRZ(2, 6);//Prezzo ivato
				
		//Inserimento riga 3 descrizione
				se.setMMTPR_S(3, "D");//Tipo di riga
				se.setMMTSD_S(3, ".");//Tipo di riga Descrizione
				se.setMMDEE_S(3, "Descrizione sulla riga ");
		
		//Scrittura nel magazzino
				se.PUTMM(0);//0 sta per MAGAZZINO solamente
		
		//Numero documento automatico
				console("numero progressivo assegnato: "+se.getMMNUM());
		
		console("Errore movimento magazzino: "+se.getERRMM_S()+", codice "+se.getERRMM());
		
		console("Errpc_s"+se.getERRPC_S());
		console("Errpc"+se.getERRPC());
		console(se.getERRORE());
		/*
		try {
			Runtime.getRuntime().exec("netstat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		se.CHIUDICONNESSIONE();
		*/
	}
}
