package com.company.petrinet;

/**
 * Scope for a configured place and a transition (which has no cost yet).
 */
public interface PlaceWithTokensAndTransitionScope {

    /**
     * Set costs for the current transition arc.
     * @param cost Number of costs.
     * @return Scope for a non empty petri net.
     */
    NonEmptyPetriNetScope andCost(int cost);

}
