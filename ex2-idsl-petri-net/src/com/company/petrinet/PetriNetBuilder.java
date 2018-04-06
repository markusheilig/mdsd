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

    void addPlace(String name) {
        Place place = new Place(name);
        places.add(place);
        currentPlace = place;
    }

    void setTokens(int tokens) {
        currentPlace.setTokens(tokens);
    }

    void addIngoingTransition(String name) {
        Transition transition = new Transition(name);
        transitions.add(transition);
        currentTransition = transition;
    }

    void addOutgoingTransition(String name) {
        Transition transition = new Transition(name);
        transitions.add(transition);
        currentTransition = transition;
    }

    void addCost(int cost) {
        currentTransition.setCost(cost);
    }

    public PetriNet end() {
        return new PetriNet(petriNetName, places, transitions);
    }
}
