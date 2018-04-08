package com.company.petrinet;

import static com.company.petrinet.Arc.ArcType.Ingoing;
import static com.company.petrinet.Arc.ArcType.Outgoing;
import static com.company.petrinet.Util.require;

public class Arc implements Validator {

    enum ArcType {
        Ingoing,
        Outgoing
    }

    private final Place place;
    private final Transition transition;
    private int cost = -1;
    private final ArcType type;

    private Arc(Place place, Transition transition, ArcType arcType) {
        this.place = place;
        this.transition = transition;
        this.type = arcType;
    }

    void setCost(int cost) {
        this.cost = cost;
    }

    static Arc ingoing(Place place, Transition transition) {
        return new Arc(place, transition, Ingoing);
    }

    static Arc outgoing(Place place, Transition transition) {
        return new Arc(place, transition, Outgoing);
    }

    public boolean isIngoing() {
        return type == Ingoing;
    }

    public boolean isOutgoing() {
        return !isIngoing();
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(cost >= 0, "Costs must be >= 0");
    }
}
