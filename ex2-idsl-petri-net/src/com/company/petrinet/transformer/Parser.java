package com.company.petrinet.transformer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.petrinet.transformer.Parser.PetrinetHandler.State.*;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Parser {

    private final String filepath;

    public Parser(String filepath) {
        this.filepath = requireNonNull(filepath);
    }

    public String parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        PetrinetHandler petrinetHandler = new PetrinetHandler();
        saxParser.parse(filepath, petrinetHandler);
        return petrinetHandler.toString();
    }

    static class PetrinetHandler extends DefaultHandler {

        private String javaCode = "";
        private State state = NONE;

        enum State {
            NONE,
            PETRINET,
            PLACE,
            FIRST_TRANSITION,
            NEXT_TRANSITION;

            static Map<State, List<State>> state = new HashMap<>();
            static {
                state.put(NONE, list(PETRINET));
                state.put(PETRINET, list(PLACE));
                state.put(PLACE, list(FIRST_TRANSITION));
                state.put(FIRST_TRANSITION, list(NEXT_TRANSITION, PLACE));
            }

            void next() {

            }

            private static List<State> list(State... states) {
                return Arrays.stream(states).collect(toList());
            }
        };


        @Override
        public void startElement(String uri,
                                 String localName, String nodeName, Attributes attributes) throws SAXException {

            final String name = "\"" + attributes.getValue("name") + "\"";
            if (nodeName.equals("petrinet") && state == NONE) {
                state = PETRINET;

                javaCode += "PetriNet.create(" + name + ")\n";
            } else if (nodeName.equals("place") && (state == PETRINET || state == FIRST_TRANSITION || state == NEXT_TRANSITION)) {
                state = PLACE;

                final String tokens = attributes.getValue("tokens");
                javaCode += "\t.addPlace(" + name + ")\n";
                javaCode += "\t\t.initWithTokens(" + tokens + ")\n";
            } else if (nodeName.equals("transition") && (state == PLACE || state == FIRST_TRANSITION || state == NEXT_TRANSITION)) {
                if (state == PLACE) {
                    state = FIRST_TRANSITION;
                } else if (state == FIRST_TRANSITION){
                    state = NEXT_TRANSITION;
                } else {
                    throw new IllegalArgumentException("Wrong state");
                }

                final String type = attributes.getValue("type");
                final String cost = attributes.getValue("cost");
                if (state != FIRST_TRANSITION) {
                    javaCode += "\t  .and()\n";
                }
                if (type.equals("ingoing")) {
                    javaCode += "\t\t  .withIngoingTransition(" + name + ")\n";
                } else {
                    javaCode += "\t\t  .withOutgoingTransition(" + name + ")\n";
                }
                javaCode += "\t\t  .andCost(" + cost + ")\n";
            } else {
                throw new IllegalArgumentException("illegal xml node " + nodeName);
            }
        }

        @Override public String toString() {
            return javaCode + "\t.end();\n";
        }
    }

}

