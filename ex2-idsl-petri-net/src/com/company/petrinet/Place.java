package com.company.petrinet;

import static com.company.petrinet.Util.require;

public final class Place implements Validator {

    Place(String name) {
        this.name = name;
    }

    private final String name;

    private int tokens;

    void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getTokens() {
        return tokens;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Place '%s' = %d", name, tokens);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        require(name != null && !name.isEmpty(), "Place must have a name");
        require(tokens >= 0, String.format("Place '%s' has invalid number of tokens!", name));
    }
}
