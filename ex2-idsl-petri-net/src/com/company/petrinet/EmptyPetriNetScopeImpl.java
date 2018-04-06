package com.company.petrinet;

class EmptyPetriNetScopeImpl implements EmptyPetriNetScope {

    private final PetriNetBuilder builder;

    EmptyPetriNetScopeImpl(PetriNetBuilder builder) {
        this.builder = builder;
    }

    @Override
    public EmptyPlaceScope addPlace(String name) {
        builder.addPlace(name);
        return new EmptyPlaceScopeImpl(builder);
    }
}
