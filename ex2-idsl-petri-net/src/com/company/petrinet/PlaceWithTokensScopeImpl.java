package com.company.petrinet;

public class PlaceWithTokensScopeImpl implements PlaceWithTokensScope {

    private final PetriNetBuilder builder;

    PlaceWithTokensScopeImpl(PetriNetBuilder builder) {
        this.builder = builder;
    }

    @Override
    public PlaceWithTokensAndTransitionScope withIngoingTransition(String transition) {
        builder.addIngoingTransition(transition);
        return new PlaceWithTokensAndTransitionScopeImpl(builder);
    }

    @Override
    public PlaceWithTokensAndTransitionScope withOutgoingTransition(String transition) {
        builder.addOutgoingTransition(transition);
        return new PlaceWithTokensAndTransitionScopeImpl(builder);
    }

}
