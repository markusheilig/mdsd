package com.company.petrinet.transformer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

import static com.company.petrinet.transformer.Parser.PetrinetHandler.State.FIRST_TRANSITION;
import static com.company.petrinet.transformer.Parser.PetrinetHandler.State.NONE;
import static java.util.Objects.requireNonNull;

public class Parser {

    private final String filepath;

    private static final String NODE_PETRINET = "petrinet";
    private static final String NODE_TRANSITION = "transition";
    private static final String NODE_PLACE = "place";

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
            NONE {
                @Override
                State next() {
                    return PETRINET;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_PETRINET);
                }
            },
            PETRINET {
                @Override
                State next() {
                    return PLACE;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_PLACE);
                }
            },
            PLACE {
                @Override
                State next() {
                    return FIRST_TRANSITION;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_TRANSITION);
                }
            },
            FIRST_TRANSITION {
                @Override
                State next() {
                    return NEXT_TRANSITION;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_TRANSITION) || node.equals(NODE_PLACE);
                }
            },
            NEXT_TRANSITION {
                @Override
                State next() {
                    return NEXT_TRANSITION;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_TRANSITION) || node.equals(NODE_PLACE);
                }
            };

            abstract State next();
            abstract boolean isNodeValid(String node);
        };


        @Override
        public void startElement(String uri,
                                 String localName, String nodeName, Attributes attributes) throws SAXException {

            final String name = "\"" + attributes.getValue("name") + "\"";
            if (nodeName.equals(NODE_PETRINET) && state.isNodeValid(nodeName)) {
                state = state.next();
                javaCode += "PetriNet.create(" + name + ")\n";
            } else if (nodeName.equals(NODE_PLACE) && state.isNodeValid(nodeName)) {
                state = state.next();

                final String tokens = attributes.getValue("tokens");
                javaCode += "\t.addPlace(" + name + ")\n";
                javaCode += "\t\t.initWithTokens(" + tokens + ")\n";
            } else if (nodeName.equals(NODE_TRANSITION) && state.isNodeValid(nodeName)) {
                state = state.next();

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

