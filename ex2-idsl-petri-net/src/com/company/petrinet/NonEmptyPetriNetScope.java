package com.company.petrinet;

public interface NonEmptyPetriNetScope {

    PlaceWithTokensScope and();

    EmptyPlaceScope addPlace(String place);

    PetriNet end();

}
