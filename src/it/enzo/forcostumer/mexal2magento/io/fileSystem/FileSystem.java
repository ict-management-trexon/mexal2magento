package it.enzo.forcostumer.mexal2magento.io.fileSystem;

import it.enzo.forcostumer.mexal2magento.io.net.Net;
import it.enzo.forcostumer.mexal2magento.mexal.DatabaseOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class FileSystem {

	public static final String percorsoDescrizioni = "C:/WORK/Schede_tecniche";
	public static final String percorsoImmagini = "C:/WORK/Immagini";
	
	public static String getTextById(String id){
		String ret = "";
		
		
		File percorsoFile = new File(percorsoDescrizioni);
		if(percorsoFile.isDirectory()){
			String file = percorsoDescrizioni+"/"+id+".txt";
			File fileFile = new File(file);
			if(fileFile.isFile()){
				//il file esiste
				try {
					BufferedReader in = new BufferedReader(new FileReader(file));
					String line;
					while((line = in.readLine())!=null){
						ret += line; 
					}
				} catch (IOException e) {e.printStackTrace();}
			}else{
				//il file NON esiste
			}
			
		}
		return ret;
	}
	
	public static boolean ifExist(String filePath){
		File fil = new File(filePath);
		if(fil.exists()){
			return true;
		}
		return false;
	}
	
	
	public static void downloadImageToDiskById(URL url, String id){
		
		String fileImmagine = percorsoImmagini+"/"+id+".jpg";
		
		
		
			ReadableByteChannel rbc = Net.getFileFromWeb(url);
			try {
			
				FileOutputStream fos = new FileOutputStream(fileImmagine);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
				fos.close();
				rbc.close();
			
			} catch (Exception e) {e.printStackTrace();}
		
		
	}

	public static String getImageBase64ById(String id) {
		
		File image = new File(percorsoImmagini+"/"+id+".jpg");
		if(image.exists()){
			try {
				return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(image));
			} catch (IOException e) {e.printStackTrace();}
		}
		
		return "";
	}
}
