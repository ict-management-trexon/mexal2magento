package it.enzo.forcostumer.mexal2magento.io.net;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Net {

	
	static{
		
	}
	
	public static ReadableByteChannel getFileFromWeb(URL url){
		try {
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			return rbc;
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
}
