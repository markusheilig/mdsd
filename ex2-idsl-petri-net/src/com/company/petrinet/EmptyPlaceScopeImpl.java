package com.company.petrinet;

class EmptyPlaceScopeImpl implements EmptyPlaceScope {

    private final PetriNetBuilder builder;

    EmptyPlaceScopeImpl(PetriNetBuilder builder) {
        this.builder = builder;
    }

    @Override
    public PlaceWithTokensScope initWithTokens(int tokens) {
        builder.setTokens(tokens);
        return new PlaceWithTokensScopeImpl(builder);
    }
}
