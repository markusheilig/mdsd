package com.company.petrinet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.company.petrinet.Util.require;
import static java.util.stream.Collectors.toList;

public final class Transition implements Validator {

    private String name;

    private final List<Arc> arcs = new LinkedList<>();

    Transition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void addArc(Arc arc) {
        arcs.add(arc);
    }

    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    public List<Place> getIngoingPlaces() {
        return arcs.stream().filter(Arc::isIngoing).map(Arc::getPlace).collect(toList());
    }

    public List<Place> getOutgoingPlace() {
        return arcs.stream().filter(Arc::isOutgoing).map(Arc::getPlace).collect(toList());
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "Transition has no name");
    }
}
