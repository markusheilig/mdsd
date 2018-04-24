package com.company;

import com.company.petrinet.Arc;
import com.company.petrinet.PetriNet;
import com.company.petrinet.Place;
import com.company.petrinet.Transition;
import com.company.petrinet.transformer.XmlGenerator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private Main() {
    }

    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException {

        final PetriNet myPetriNet = PetriNet.create("Wikipedia Beispiel")
                .addPlace("s1")
                .initWithTokens(8)
                .withIngoingTransition("t")
                .andCost(2)
                .and()
                .withOutgoingTransition("t2")
                .andCost(1)
                .addPlace("s2")
                .initWithTokens(4)
                .withIngoingTransition("t3")
                .andCost(45)
                .end();

        try {
            myPetriNet.validate();

            XmlGenerator xmlGenerator = new XmlGenerator(myPetriNet);
            String xml = xmlGenerator.generate();
            writeToFile(myPetriNet.getName(), xml);

        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void writeToFile(String petriNetName, String content) throws IOException {
        String filepath = petriNetName + ".xml";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false));
        writer.write(content);
        writer.close();
    }

    private static void describe(PetriNet net) {
        printf("Petri net name: '%s'", net.getName());
        printf("Number of places: %d", net.getPlaces().size());
        printf("Sum of tokens: %d", net.sumOfTokens());
        net.getPlaces().forEach(Main::describe);
        net.getTransitions().forEach(Main::describe);
        net.getArcs().forEach(Main::describe);
    }

    private static void describe(Place place) {
        printf("Place %s has %d initial tokens", place.getName(), place.getTokens());
        printf("Place %s has %d ingoing transactions", place.getName(), place.getIngoingTransitions().size());
        printf("Place %s has %d outgoing transactions", place.getName(), place.getOutgoingTransitions().size());
    }

    private static void describe(Transition transition) {
        printf("Transition %s has %d ingoing places", transition.getName(), transition.getIngoingPlaces().size());
        printf("Transition %s has %d outgoing places", transition.getName(), transition.getOutgoingPlace().size());
    }

    private static void describe(Arc arc) {
        final String arcType = arc.isIngoing() ? "Ingoing " : "Outgoing";
        printf("%s arc connecting place %s with Transition %s", arcType, arc.getPlace().getName(), arc.getTransition().getName());
    }

    private static void printf(String s, Object... args) {
        String formatted = String.format(s, args);
        System.out.println(formatted);
    }

}
