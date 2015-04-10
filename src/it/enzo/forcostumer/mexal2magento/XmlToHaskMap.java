package it.enzo.forcostumer.mexal2magento;
import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.Hashtable;
import java.util.Vector;

public class XmlToHaskMap extends DefaultHandler {
	 private StringBuffer path;
	    private StringBuffer textContent;
	    private Hashtable<String, String> hashtable;

	    public XmlToHaskMap(String fn) throws SAXException, IOException,ParserConfigurationException {
	       SAXParserFactory factory = SAXParserFactory.newInstance();
	       SAXParser parser = factory.newSAXParser();

	        this.path = new StringBuffer();
	        this.hashtable = new Hashtable<String, String>();
	       parser.parse(fn, this);
	    }

	    public void startElement(String uri, String local, String qname,
	    Attributes atts) throws SAXException {
	       // Update path
	       path.append('/');
	       path.append(qname);
	       int nattrs = atts.getLength();
	       for (int i=0; i<nattrs; i++) {
	         addValue(path.toString()+"/@"+atts.getQName(i), atts.getValue(i));
	       }
	       this.textContent = new StringBuffer();
	    }

	    public void endElement(String uri, String local, String qname) throws SAXException {
	       if (this.textContent != null) {
	             addValue(path.toString(), this.textContent.toString());
	         this.textContent = null;
	       }
	       // Restore path
	       int pathlen = path.length();
	       path.delete(pathlen-qname.length()-1,pathlen);
	    }

	    public void characters(char[] ch, int start, int length) throws SAXException {
	       if (this.textContent != null) {
	         this.textContent.append(ch, start, length);
	       }
	    }

	    public Hashtable<String, String> getHashtable() {
	       return this.hashtable;
	    }

	    void addValue(String key, String value) {
	       String v = this.hashtable.get(key);
	       if (v == null) {
	        this.hashtable.put(key,value);
	       }
	    }
	 }