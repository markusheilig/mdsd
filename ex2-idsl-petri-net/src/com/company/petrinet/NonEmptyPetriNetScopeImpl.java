package com.company.petrinet;

public class NonEmptyPetriNetScopeImpl implements NonEmptyPetriNetScope {

    private final PetriNetBuilder builder;

    NonEmptyPetriNetScopeImpl(PetriNetBuilder builder) {
        this.builder = builder;
    }

    @Override
    public PlaceWithTokensScope and() {
        return new PlaceWithTokensScopeImpl(builder);
    }

    @Override
    public EmptyPlaceScope addPlace(String place) {
        builder.addPlace(place);
        return new EmptyPlaceScopeImpl(builder);
    }

    @Override
    public PetriNet end() {
        return builder.end();
    }
}
