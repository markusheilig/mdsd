package com.company.petrinet;

/**
 * EmptyPlaceScope represents an empty place.
 */
public interface EmptyPlaceScope {

    /**
     * Initialize empty place with a number of tokens.
     * @param tokens Number of initial tokens for the current place.
     * @return Scope for a place with tokens.
     */
    PlaceWithTokensScope initWithTokens(int tokens);

}
