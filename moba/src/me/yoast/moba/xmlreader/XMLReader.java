package me.yoast.moba.xmlreader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	
	public static List<String[]> getClasses()   {  
		List<String[]> classArray = new ArrayList<String[]>();
		String[] nameAndIcon;
		String name;
		String icon;
	
		try {  
			//creating a constructor of file class and parsing an XML file  
			//File file = new File("./classes.xml");  
			InputStream file = XMLReader.class.getClassLoader()
		            .getResourceAsStream("classes.xml");
			//an instance of factory that gives a document builder  
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			//an instance of builder to parse the specified xml file  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();
			
			NodeList classList = doc.getElementsByTagName("class");  
			// nodeList is not iterable, so we are using for loop  
			for (int itr = 0; itr < classList.getLength(); itr++) {  
				Node classNode = classList.item(itr);   
				if (classNode.getNodeType() == Node.ELEMENT_NODE) {  
					Element classElement = (Element) classNode;  
					  
					name = classElement.getElementsByTagName("name").item(0).getTextContent();
					icon = classElement.getElementsByTagName("icon").item(0).getTextContent();
					nameAndIcon = new String[] {name, icon};
					System.out.println(Arrays.toString(nameAndIcon));
					classArray.add(nameAndIcon);
				}  
			}
			
		}
		catch (Exception e){  
			e.printStackTrace();  
		}
		return classArray; 	
	} 
	
	
	public static List<String> getItems(int slot)   {  
		List<String> itemsArray = new ArrayList<String>();
		String item;
		try {  
			//creating a constructor of file class and parsing an XML file  
			InputStream file = XMLReader.class.getClassLoader()
		            .getResourceAsStream("classes.xml"); 
			//an instance of factory that gives a document builder  
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			//an instance of builder to parse the specified xml file  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();
			  
			NodeList classList = doc.getElementsByTagName("class");  
			// nodeList is not iterable, so we are using for loop  
			  
			Node classNode = classList.item(slot);     
			Element classElement = (Element) classNode; 
			NodeList itemsList = classElement.getElementsByTagName("items"); 
			Node itemsNode = itemsList.item(0);     
			Element itemsElement = (Element) itemsNode;
			NodeList itemList = itemsElement.getElementsByTagName("item");
			for (int itr = 0; itr < itemList.getLength(); itr++) {
				
				item = classElement.getElementsByTagName("item").item(itr).getTextContent();
				itemsArray.add(item);
			}  
			
			
		}
		catch (Exception e){  
			e.printStackTrace();  
		}
		
		return itemsArray;  
	}  

}
