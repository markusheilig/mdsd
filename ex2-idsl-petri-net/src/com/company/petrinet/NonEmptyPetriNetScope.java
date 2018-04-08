package com.company.petrinet;

/**
 * NonEmptyPetriNetScope represents a scope for a petri net which already contains at least one place.
 */
public interface NonEmptyPetriNetScope {

    /**
     * Add another transition the the current place.
     * @return Scope for a non empty place.
     */
    PlaceWithTokensScope and();

    /**
     * Add a new place to the petri net.
     * @param place Name of the new place.
     * @return Scope for an empty place.
     */
    EmptyPlaceScope addPlace(String place);

    /**
     * Stop build process.
     * @return The configured petri net.
     */
    PetriNet end();

}
