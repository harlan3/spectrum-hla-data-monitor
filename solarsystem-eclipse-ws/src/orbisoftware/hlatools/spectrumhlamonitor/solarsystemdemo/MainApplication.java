/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jargs.gnu.CmdLineParser;
import orbisoftware.hlatools.spectrumhlamonitor.hla_sender.HLASenderMainApplication;

public class MainApplication {
   
	private void parseElements(Element root) {

		String name = "";

		if (root != null) {

			NodeList nl = root.getChildNodes();

			if (nl != null) {

				for (int i = 0; i < nl.getLength(); i++) {
					Node node = nl.item(i);

					if (node.getNodeName().equalsIgnoreCase("setting")) {

						NodeList childNodes = node.getChildNodes();

						for (int j = 0; j < childNodes.getLength(); j++) {

							Node child = childNodes.item(j);

							if (child.getNodeName().equalsIgnoreCase("name"))
								name = child.getTextContent();
							else if (child.getNodeName().equalsIgnoreCase("value"))
								MainSharedData.getInstance().xmlMap.put(name, child.getTextContent());
						}
					}
				}
			}
		}
	}

   private static void printUsage() {

      System.out.println("Usage: hla-solar-system-demo [OPTION]...");
      System.out.println("Run the hla-solar-system-demo");
      System.out.println();
      System.out.println("   -f, --federate          Federate Name");
   }
	  
	public static void main(String[] args) {

		try {
		   SolarSystem solarSystem = new SolarSystem();
			MainApplication mainApplication = new MainApplication();
			HLASenderMainApplication hlaSenderMainApplication = new HLASenderMainApplication();
			
			// Process XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("settings.xml");
			Element rootElem = doc.getDocumentElement();

			if (rootElem != null) {
				mainApplication.parseElements(rootElem);
			}
			
	      CmdLineParser parser = new CmdLineParser();

	      CmdLineParser.Option federateOption = parser.addStringOption('f', "federate");
	      
	      try {
	         parser.parse(args);
	      } catch (CmdLineParser.OptionException e) {
	         System.out.println(e.getMessage());
	         printUsage();
	         System.exit(0);
	      }
	      
	      String federateValue = (String) parser.getOptionValue(federateOption);
	            
         if ((federateValue == null) || (federateValue.equals(""))) {
            printUsage();
            System.exit(0);
         }
	      
	      MainSharedData.getInstance().xmlMap.put("Federate", federateValue);
			
			hlaSenderMainApplication.startThreads();
			
			solarSystem.startSimulation();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}