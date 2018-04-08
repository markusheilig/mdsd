package com.company.petrinet;

import static com.company.petrinet.Arc.ArcType.Ingoing;
import static com.company.petrinet.Arc.ArcType.Outgoing;
import static com.company.petrinet.Util.require;

public class Arc implements Validator {

    public enum ArcType {
        Ingoing,
        Outgoing
    }

    private final Place place;
    private final Transition transition;
    private final ArcType type;
    private int cost = -1;

    Arc(Place place, Transition transition, ArcType arcType) {
        this.place = place;
        this.transition = transition;
        this.type = arcType;
    }

    void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isIngoing() {
        return type == Ingoing;
    }

    public boolean isOutgoing() {
        return !isIngoing();
    }

    public Place getPlace() {
        return place;
    }

    public Transition getTransition() {
        return transition;
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(cost >= 0, "Costs must be >= 0");
    }
}
