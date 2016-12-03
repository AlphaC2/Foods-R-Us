package model;

import java.io.File;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlHandler
{
		//Read and list relevant XML entries
	public static void writeXml(File xmlFile, String userName)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			
			NodeList nodes = doc.getElementsByTagName("order");
			Node newNode = nodes.item(0).cloneNode(false);
			
			Text a = doc.createTextNode("Dogman"); 
			Element p = doc.createElement("user"); 
			p.appendChild(a); 
			
			newNode.insertBefore(p, null);
			nodes.item(0).getParentNode().insertBefore(newNode, nodes.item(0));
			
			readXml(xmlFile, userName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Something went wrong reading from XML!");
		}
	}
	
		//Write new order entries to the XML record
	public static void readXml(File xmlFile, String userName, Writer write)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			
			doc.getDocumentElement().normalize();
			
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("order");
			
			for(int i = 0; i < nList.getLength(); i++)
			{
				Node nNode = nList.item(i);
				System.out.println("Current Item: " + nNode.getNodeName());
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) nNode;
					write.append("User: " + element.getElementsByTagName("user").item(0).getTextContent() + "\n");
					write.append("Item: " + element.getElementsByTagName("item").item(0).getTextContent() + "\n");
					System.out.println("User: " + element.getElementsByTagName("user").item(0).getTextContent());
					System.out.println("Item: " + element.getElementsByTagName("item").item(0).getTextContent());
					System.out.println("");
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
