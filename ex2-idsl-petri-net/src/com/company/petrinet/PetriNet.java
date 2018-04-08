package com.company.petrinet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.company.petrinet.Util.findDuplicates;
import static com.company.petrinet.Util.require;
import static java.util.stream.Collectors.toList;

public final class PetriNet implements Validator {

    private final String name;

    private final List<Place> places;

    private final List<Transition> transitions;

    PetriNet(String petriNetName, List<Place> places, List<Transition> transitions) {
        this.name = petriNetName;
        this.places = places;
        this.transitions = transitions;
    }

    public static EmptyPetriNetScope create(String name) {
        return PetriNetBuilder.startBuilding(name);
    }

    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return Collections.unmodifiableList(places);
    }

    public List<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }

    @Override
    public String toString() {
        return String.format("PetriNet: '%s'", name);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "PetriNet must have a name!");
        List<String> duplicates = findDuplicates(getPlaceNames());
        require(duplicates.isEmpty(), String.format("PetriNet '%s' contains one or more duplicated places: '%s'!", name, duplicates));
        places.forEach(Validator::validate);
        transitions.forEach(Validator::validate);
    }

    private List<String> getPlaceNames() {
        return places.stream().map(Place::getName).collect(toList());
    }

}
