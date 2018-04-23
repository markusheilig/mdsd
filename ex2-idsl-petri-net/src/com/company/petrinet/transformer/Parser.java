package com.company.petrinet.transformer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

import static com.company.petrinet.transformer.Parser.PetrinetHandler.State.NONE;
import static java.util.Objects.requireNonNull;

public class Parser {

    private final String filepath;

    private static final String NODE_ATTR_NAME = "name";
    private static final String NODE_ATTR_TOKENS = "tokens";
    private static final String NODE_ATTR_COST = "cost";
    private static final String NODE_ATTR_TYPE = "type";
    private static final String NODE_ATTR_TYPE_INGOING = "ingoing";
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
                State back() {
                    return null;
                }

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
                State back() {
                    return NONE;
                }

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
                State back() {
                    firstTransition = true;
                    return PETRINET;
                }

                @Override
                State next() {
                    return TRANSITION;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_TRANSITION);
                }
            },
            TRANSITION {
                @Override
                State back() {
                    firstTransition = false;
                    return PLACE;
                }

                @Override
                State next() {
                    return this;
                }

                @Override
                boolean isNodeValid(String node) {
                    return node.equals(NODE_TRANSITION) || node.equals(NODE_PLACE);
                }
            };

            abstract State back();
            abstract State next();
            abstract boolean isNodeValid(String node);

            private static boolean firstTransition = true;
            public boolean isFirstTransition(){
                return firstTransition;
            }
        };


        @Override
        public void startElement(String uri,
                                 String localName, String nodeName, Attributes attributes) throws SAXException {

            final String name = "\"" + attributes.getValue(NODE_ATTR_NAME) + "\"";

            if(state.isNodeValid(nodeName)){
                state = state.next();
                if(nodeName.equals(NODE_PETRINET)){
                    javaCode += "PetriNet.create(" + name + ")\n";
                }else if(nodeName.equals(NODE_PLACE)){
                    final String tokens = attributes.getValue(NODE_ATTR_TOKENS);
                    javaCode += "\t.addPlace(" + name + ")\n";
                    javaCode += "\t\t.initWithTokens(" + tokens + ")\n";
                }else if(nodeName.equals(NODE_TRANSITION)){
                    final String type = attributes.getValue(NODE_ATTR_TYPE);
                    final String cost = attributes.getValue(NODE_ATTR_COST);
                    if (!state.isFirstTransition()) {
                        javaCode += "\t  .and()\n";
                    }
                    if (type.equals(NODE_ATTR_TYPE_INGOING)) {
                        javaCode += "\t\t  .withIngoingTransition(" + name + ")\n";
                    } else {
                        javaCode += "\t\t  .withOutgoingTransition(" + name + ")\n";
                    }
                    javaCode += "\t\t  .andCost(" + cost + ")\n";
                }
            }else {
                throw new IllegalArgumentException("illegal xml node " + nodeName);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            state = state.back();
            if(state == State.NONE){
                javaCode += "\t.end();\n";
            }

        }

        @Override public String toString() {
            return javaCode;
        }
    }

}

