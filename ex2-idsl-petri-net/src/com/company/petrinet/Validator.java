package com.company.petrinet;

/**
 * Interface for validation objects.
 */
public interface Validator {

    /**
     * Check for validation errors.
     * @throws IllegalArgumentException if the implementing object is in an invalid state.
     */
    void validate() throws IllegalArgumentException;

}
