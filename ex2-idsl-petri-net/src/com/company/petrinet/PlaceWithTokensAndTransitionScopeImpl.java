package com.company.petrinet;

public class PlaceWithTokensAndTransitionScopeImpl implements PlaceWithTokensAndTransitionScope {

    private final PetriNetBuilder builder;
    PlaceWithTokensAndTransitionScopeImpl(PetriNetBuilder builder) {
        this.builder = builder;
    }

    @Override
    public NonEmptyPetriNetScope andCost(int cost) {
        builder.addCost(cost);
        return new NonEmptyPetriNetScopeImpl(builder);
    }
}
