package com.company.petrinet;

import static com.company.petrinet.Util.require;

public final class Transition implements Validator {

    private String name;

    private int cost = -1;

    Transition(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Transition '%s' = %d", name, cost);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "Transition has no name");
        require(cost > 0, String.format("Cost for transition '%s' must be >= 1", name));
    }
}
