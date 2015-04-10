package it.enzo.forcostumer.mexal2magento.processi;

import java.util.ArrayList;
import java.util.Iterator;

import it.enzo.forcostumer.mexal2magento.magento.MagentoXMLRPCOperation;

public class UpdateAllProducts extends Thread{

	private boolean execution = true;
	private MagentoXMLRPCOperation mox;
	
	private Integer maxOperation = 10;
	
	private Integer sended = 0;
	
	private boolean flag = false;
	
	private void sendToMagento(){
		
		
			
		
	}
	
	
	public UpdateAllProducts(MagentoXMLRPCOperation arg){
		this.mox = arg;
	}
	
	@Override
	public void run(){
		try {
			while(execution){
				this.sendToMagento();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	

	
	public void stopExecution(){
		this.setExecutionOff();
		this.flag = true;
		this.sendToMagento();
	}


	public boolean isInExecution() {
		return execution;
	}

	private void setExecutionOff() {
		this.execution = false;
	}
	
	private class SendToMagentoProcess extends Thread{
		
		private MagentoXMLRPCOperation mox;
		private ArrayList<Object[]> stack = new ArrayList<Object[]>();
		
		public SendToMagentoProcess(MagentoXMLRPCOperation arg){
			this.mox = arg;
		}
		
		@Override
		public void run() {
			mox.executeMultiCall1(stack);
		}
	}
	
	public static void console(Object out){
		System.out.println(out);
	}
}
