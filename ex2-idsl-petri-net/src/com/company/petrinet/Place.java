package com.company.petrinet;

import java.util.*;

import static com.company.petrinet.Util.require;

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

    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    @Override
    public String toString() {
        return String.format("Place '%s' = %d", name, tokens);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "Place must have a name");
        require(tokens >= 0, String.format("Place '%s' has invalid number of tokens!", name));
    }
}
