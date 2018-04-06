package com.company.petrinet;

public interface PlaceWithTokensScope {

    PlaceWithTokensAndTransitionScope withIngoingTransition(String transition);

    PlaceWithTokensAndTransitionScope withOutgoingTransition(String transition);

}
