package it.enzo.forcostumer.mexal2magento;

import it.enzo.forcostumer.mexal2magento.io.fileSystem.FileSystem;
import it.enzo.forcostumer.mexal2magento.magento.MagentoXMLRPCOperation;
import it.enzo.forcostumer.mexal2magento.mexal.DatabaseOperation;
import it.enzo.forcostumer.mexal2magento.mexal.prodotto.ProdottoEntita;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlrpc.XmlRpcRequest;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;

public class Main {
	
	public static AdapterM2M adapter = new AdapterM2M();

	public static void printConsole(){
		console("INSERISCI SCELTA...:\n");
		console("a.Avvia auto aggiornamento");
		console("1.Aggiorna tutti i prodotti");
		console("2.Download Immagini");
		console("3.Inserisci tutte le immagini");
		console("4.Verifica esistenza triggers");
		console("5.Cancella triggers");
		console("e.EXIT");
		console("x.Prove");
	}
	
	public static void executeAction(String scelta){
		switch(scelta){
		
		case "a":
			adapter.startProcessi();
		break;
		
		case "1":
			adapter.updateAllProduct();
		break;
		
		case "2":
		
			//Download di tutte le immagini
			adapter.getDatabaseOperation().downloadAllImage();
		break;
		
		case "3":
			
			//Caricamento di tutte le immagini
			adapter.updateAllProductImage();
		break;
		
		case "4":
			adapter.getDatabaseOperation().verificaEsistenzaTriggers();
		break;
		
		case "5":
			adapter.getDatabaseOperation().deleteTriggers();
		break;
		
		case "e":
			adapter.destroy();
		break;
		
		case "x":
			adapter.getGestioneCategorie().generaCategorieDaMexal();
			//console(adapter.getMagentoXMLRPCOperation().getCategoryInfo("12"));
		break;
		
		default:
			//System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		
		
		String scelta = "";
		 
		
		try {
			
			do{
				 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				printConsole();
				int in = br.read();
				scelta = Character.toString((char) in);
				executeAction(scelta);
			
			}while(!scelta.equals("e"));
			//adapter.destroy();
			
			
		} catch (IOException e) {e.printStackTrace();}
	
	}
	
	
	public static void console(Object out){
		System.out.println(out);
	}
}
