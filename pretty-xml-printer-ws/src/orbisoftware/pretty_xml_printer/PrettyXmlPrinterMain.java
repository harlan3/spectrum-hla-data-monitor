package orbisoftware.pretty_xml_printer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class PrettyXmlPrinterMain {

    public String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(xmlInput, xmlOutput);
            
            // Post-process to remove blank lines
            String[] lines = stringWriter.toString().split("\n");
            StringBuilder filteredOutput = new StringBuilder();
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    filteredOutput.append(line).append("\n");
                }
            }
            return filteredOutput.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    private static void printUsage() {
        System.out.println("Usage: pretty_xml_printer [XML FILENAME]");
        System.out.println("Run the pretty-xml-printer");
        System.out.println();
    }

    public static void main(String[] args) {
        PrettyXmlPrinterMain prettyXmlPrinterMain = new PrettyXmlPrinterMain();

        // Check if a filename argument is provided
        if (args.length < 1) {
            printUsage();
            return;
        }

        // Get the filename from the first argument
        String filePath = args[0];
        StringBuilder concatenatedContent = new StringBuilder();

        // Use try-with-resources to ensure the reader is closed properly
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                concatenatedContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String formattedOutput = prettyXmlPrinterMain.prettyFormat(concatenatedContent.toString(), 3);
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        System.out.println(formattedOutput);
    }
}