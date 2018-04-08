package com.company.petrinet;

import java.util.LinkedList;
import java.util.List;

public class PetriNetBuilder {

    private PetriNetBuilder() {
    }

    static EmptyPetriNetScope startBuilding(String petriNetName) {
        PetriNetBuilder builder = new PetriNetBuilder();
        builder.petriNetName = petriNetName;
        return new EmptyPetriNetScopeImpl(builder);
    }

    private String petriNetName;

    private final List<Place> places = new LinkedList<>();

    private final List<Transition> transitions = new LinkedList<>();

    private final List<Arc> arcs = new LinkedList<>();

    private Place currentPlace;

    private Arc currentArc;

    void addPlace(String name) {
        Place place = new Place(name);
        places.add(place);
        currentPlace = place;
    }

    void setTokens(int tokens) {
        currentPlace.setTokens(tokens);
    }

    void addIngoingTransition(String name) {
        addTransition(name, Arc.ArcType.Ingoing);
    }

    void addOutgoingTransition(String name) {
        addTransition(name, Arc.ArcType.Outgoing);
    }

    private void addTransition(String name, Arc.ArcType arcType) {
        Transition transition = getOrCreateTransition(name);
        if (!transitions.contains(transition)) {
            transitions.add(transition);
        }
        Arc arc = new Arc(currentPlace, transition, arcType);
        arcs.add(arc);
        transition.addArc(arc);
        currentPlace.addArc(arc);
        currentArc = arc;
    }

    private Transition getOrCreateTransition(String name) {
        return transitions
                .stream()
                .filter(transition -> transition.getName() != null && transition.getName().equals(name))
                .findFirst()
                .orElseGet(() -> new Transition(name));
    }

    void addCost(int cost) {
        currentArc.setCost(cost);
    }

    public PetriNet end() {
        return new PetriNet(petriNetName, places, transitions, arcs);
    }
}
