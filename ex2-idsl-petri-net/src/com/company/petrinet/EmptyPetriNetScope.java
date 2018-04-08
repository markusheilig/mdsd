package com.company.petrinet;

/**
 * EmptyPetriNetScope represents a petri net without places.
 */
public interface EmptyPetriNetScope {

    /**
     * Add a place to the empty petri net.
     * @param name Name of the new place.
     * @return Scope for an empty place.
     */
    EmptyPlaceScope addPlace(String name);

}
