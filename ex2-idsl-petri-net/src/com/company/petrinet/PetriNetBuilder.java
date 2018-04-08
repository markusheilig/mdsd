package com.company.petrinet;

import java.util.LinkedList;
import java.util.List;

public class PetriNetBuilder {

    static EmptyPetriNetScope startBuilding(String petriNetName) {
        PetriNetBuilder builder = new PetriNetBuilder();
        builder.petriNetName = petriNetName;
        return new EmptyPetriNetScopeImpl(builder);
    }

    private String petriNetName;

    private final List<Place> places = new LinkedList<>();

    private final List<Transition> transitions = new LinkedList<>();

    private Place currentPlace;

    private Transition currentTransition;

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
        currentTransition = getOrCreateTransition(name);
        if (!transitions.contains(currentTransition)) {
            transitions.add(currentTransition);
        }
        Arc arc = Arc.ingoing(currentPlace, currentTransition);
        currentTransition.addArc(arc);
        currentPlace.addArc(arc);
        currentArc = arc;
    }

    void addOutgoingTransition(String name) {
        currentTransition = getOrCreateTransition(name);
        if (!transitions.contains(currentTransition)) {
            transitions.add(currentTransition);
        }
        Arc arc = Arc.outgoing(currentPlace, currentTransition);
        currentTransition.addArc(arc);
        currentPlace.addArc(arc);
        currentArc = arc;
    }

    private Transition getOrCreateTransition(String name) {
        return transitions.stream()
                .filter(transition -> transition.getName() != null && transition.getName().equals(name))
                .findFirst()
                .orElseGet(() -> new Transition(name));
    }

    void addCost(int cost) {
        currentArc.setCost(cost);
    }

    public PetriNet end() {
        return new PetriNet(petriNetName, places, transitions);
    }
}
