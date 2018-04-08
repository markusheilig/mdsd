package com.company.petrinet;

import java.util.LinkedList;
import java.util.List;

import static com.company.petrinet.Util.require;
import static java.util.stream.Collectors.toList;

public final class Place implements Validator {

    private final String name;

    private int tokens;

    private final List<Arc> arcs = new LinkedList<>();

    Place(String name) {
        this.name = name;
    }

    void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getTokens() {
        return tokens;
    }

    public String getName() {
        return name;
    }

    void addArc(Arc arc) {
        arcs.add(arc);
    }

    public List<Transition> getIngoingTransitions() {
        return arcs.stream().filter(Arc::isIngoing).map(Arc::getTransition).collect(toList());
    }

    public List<Transition> getOutgoingTransitions() {
        return arcs.stream().filter(Arc::isOutgoing).map(Arc::getTransition).collect(toList());
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "Place must have a name");
        require(tokens >= 0, String.format("Place '%s' has invalid number of tokens!", name));
    }
}
