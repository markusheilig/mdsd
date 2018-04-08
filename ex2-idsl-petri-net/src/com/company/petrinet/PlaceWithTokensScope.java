package com.company.petrinet;

/**
 * PlaceWithTokensScope represents a scope for a configured place without transition(s).
 */
public interface PlaceWithTokensScope {

    /**
     * Add an ingoing transition arc to the current place.
     * @param transition Name of the transition.
     * @return Scope for a place with a transition.
     */
    PlaceWithTokensAndTransitionScope withIngoingTransition(String transition);

    /**
     * Add an outgoing transition arc to the current place.
     * @param transition Name of the transition.
     * @return Scope for a place with a transition.
     */
    PlaceWithTokensAndTransitionScope withOutgoingTransition(String transition);

}
