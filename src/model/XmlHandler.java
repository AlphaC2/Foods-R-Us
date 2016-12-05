package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlHandler
{
		//Read and list relevant XML entries
	public static void writeXml(File xmlFile, String userName, String shipping, String tax, String total, ArrayList<ItemBean> items)
	{
		try
		{
			String itemString = "";

				//Collected all items from the list of item beans
			for(int i = 0; i < items.size(); i++)
			{
				String name = items.get(i).getName();
				String price = items.get(i).getPrice().toString();
				String quantity = ""+items.get(i).getQuantity();
				itemString = itemString + "<item><name>" + name + "</name><quantity>" + quantity +"</quantity><price>" + price + "</price></item>"; 
			}
			
				//The new xml entry to append
			String xmlString = "<order><user>" + userName + "</user>" + itemString + "<tax>"+tax+"</tax>"
					+ "<shipping>" + shipping + "</shipping><total>"+total+"</total></order></orders>";
			System.out.println(xmlString);
			
			Path xml = Paths.get(xmlFile.getAbsolutePath());
			
				//Replace the xml endtag with the new element
			String content = new String(Files.readAllBytes(xml));
			content = content.replaceAll("</orders>", xmlString);
			Files.write(xml, content.getBytes());
			System.out.println(content);
			
			readXml(xmlFile, userName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Something went wrong writing to XML!");
		}
	}
	
		//Write new order entries to the XML record
	public static void readXml(File xmlFile, String userName)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			
			NodeList nList = doc.getElementsByTagName("order");
			System.out.println(nList.getLength());
			for(int i = 0; i < nList.getLength(); i++)
			{
				Node nNode = nList.item(i);
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) nNode;
					
					if(element.getElementsByTagName("user").item(0).getTextContent().equals(userName))
					{
						System.out.println("User: " + element.getElementsByTagName("user").item(0).getTextContent());
						
						
						NodeList itemList = element.getElementsByTagName("item");
						for(int j = 0; j < itemList.getLength(); j++)
						{
							Node currentItem = (Element) itemList.item(j);
							Element itemElement = (Element) currentItem;
							System.out.println("Item: " + itemElement.getElementsByTagName("name").item(0).getTextContent());
							System.out.println("Quantity: " + itemElement.getElementsByTagName("quantity").item(0).getTextContent());
							System.out.println("Price: " + itemElement.getElementsByTagName("price").item(0).getTextContent());
						}
						System.out.println("Shipping: " + element.getElementsByTagName("shipping").item(0).getTextContent());
						System.out.println("");
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Something went wrong reading from XML!");
		}
	}
}
