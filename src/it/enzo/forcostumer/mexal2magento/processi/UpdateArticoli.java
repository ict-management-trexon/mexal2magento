package it.enzo.forcostumer.mexal2magento.processi;

import it.enzo.forcostumer.mexal2magento.AdapterM2M;

public class UpdateArticoli extends Thread{

	private AdapterM2M adapter;
	
	private int millisec = 60000;
	
	public UpdateArticoli(AdapterM2M adap){
		this.adapter = adap;
		//this.start();
		
	}
	
	@Override
	public void run(){
		console("Update Articoli automatico avviato....");
		while(!isInterrupted()){
			
			//QUI VA L'OPERAZIONE
			console("Aggiornamento prodotti...");
			this.adapter.updateFromTriggered();
			try {
				Thread.sleep(millisec);
			} catch (InterruptedException e) {
				break;
			}
		}
		console("Update Articoli interrotto....");
	}
	
	public void stopUpdateArticoli(){
		interrupt();
	}
	
	
	public static void console(Object out){
		System.out.println(out);
	}
}
