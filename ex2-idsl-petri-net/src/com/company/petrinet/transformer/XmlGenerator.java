package com.company.petrinet.transformer;

import com.company.petrinet.Arc;
import com.company.petrinet.PetriNet;
import com.company.petrinet.Place;
import com.company.petrinet.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class XmlGenerator {

    final PetriNet petriNet;

    public XmlGenerator(PetriNet petriNet) {
        this.petriNet = petriNet;
    }

    public String generate() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("petrinet");
        root.setAttribute("name", petriNet.getName());

        petriNet.getPlaces().forEach(place -> {
            Element p = doc.createElement("place");
            p.setAttribute("name", place.getName());
            p.setAttribute("tokens", Integer.toString(place.getTokens()));
            place.getIngoingTransitions().forEach(transition -> {
                Element t = toElement(doc, transition, place);
                p.appendChild(t);
            });
            place.getOutgoingTransitions().forEach(transition -> {
                Element t = toElement(doc, transition, place);
                p.appendChild(t);
            });
            root.appendChild(p);
        });

        doc.appendChild(root);

        return xmlToString(doc);
    }

    private String xmlToString(Document document) throws TransformerException {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(document), new StreamResult(out));
        return out.toString();
    }

    private Element toElement(Document doc, Transition t, Place p) {
        Element element = doc.createElement("transition");
        element.setAttribute("name", t.getName());
        element.setAttribute("type", "ingoing");
        element.setAttribute("cost", Integer.toString(getArc(p, t).getCost()));
        return element;
    }

    private Arc getArc(Place p, Transition t) {
        List<Transition> transitions = new LinkedList<>();
        transitions.addAll(p.getIngoingTransitions());
        transitions.addAll(p.getOutgoingTransitions());
        return transitions.stream()
                .flatMap(x -> x.getArcs().stream())
                .filter(a -> a.getPlace().getName().equals(p.getName())
                        && a.getTransition().getName().equals(t.getName()))
                .findFirst().get();

    }

}
