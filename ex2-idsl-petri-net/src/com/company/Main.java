package com.company;

import com.company.petrinet.PetriNet;
import com.company.petrinet.Place;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Main {

    public static void main(String[] args) {

        PetriNet myPetriNet = PetriNet.create("Wikipedia Beispiel")
                .addPlace("s1")
                  .initWithTokens(9)
                    .withOutgoingTransition("t")
                    .andCost(2)
                  .and()
                    .withIngoingTransition("t0")
                    .andCost(9)
                .addPlace("s2")
                  .initWithTokens(1)
                    .withOutgoingTransition("t")
                    .andCost(1)
                .addPlace("s3")
                  .initWithTokens(1)
                    .withIngoingTransition("t")
                    .andCost(1)
                .addPlace("s4")
                  .initWithTokens(0)
                    .withIngoingTransition("t")
                    .andCost(3)
                .end();

        try {
            myPetriNet.validate();
            describe(myPetriNet);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void describe(PetriNet net) {
        printf("Petri net name: '%s'", net.getName());
        printf("Number of places: %d", net.getPlaces().size());
        printf("Sum of tokens: %d", net.getPlaces().stream().map(Place::getTokens).reduce(0, (x, y) -> x + y));
    }

    private static void printf(String s, Object... args) {
        String formatted = String.format(s, args);
        System.out.println(formatted);
    }

}
