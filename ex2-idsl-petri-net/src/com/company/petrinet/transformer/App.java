package com.company.petrinet.transformer;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String path = "/Users/markusheilig/Desktop/example.xml";
        Parser parser = new Parser(path);
        String javaCode = parser.parse();

        String exampleApp = generateExampleApp(javaCode);
        writeToFile(exampleApp);
    }

    static void writeToFile(String s) throws IOException {
        String filename = "src/GeneratedApp.java";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
        writer.write(s);
        writer.close();
    }

    static String generateExampleApp(String net) {
        return
                "import com.company.petrinet.PetriNet;\n\n" +
                "public class GeneratedApp {\n" +
                  "\tpublic static void main(String[] args) {\n" +
                        "\t\tPetriNet myPetriNet = " + net.replace("\n", "\n\t\t") + "\n" +
                        "\t\tmyPetriNet.validate();\n" +
                  "\t}\n" +
                "}";
    }


}
