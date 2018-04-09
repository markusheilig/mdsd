# Internal DSL for building Petri Nets

## Class Diagram

![alt text](class-diagram.png "Class Diagram")

## Abstract Syntax

PetriNet &rarr; Place

Place &rarr; name token Transition

Transition &rarr; name (Ingoing | Outgoing)

Ingoing &rarr; cost (Transition | Place | end)

Outgoing &rarr; cost (Transition | Place | end)


## Static Semantics
<span style="color:blue"> Builder Validation </span> <br> <span style="color:red"> Code Validation </span>

- A petri net
  - <span style="color:red">has a name (non-empty)
  - <span style="color:blue"> consists of at least one place
  - <span style="color:blue"> contains at least one transition

- A place
  - <span style="color:blue"> has a name (unique, non-empty)
  - <span style="color:red"> has an initial amount of tokens (>= 0)
  - <span style="color:blue"> has at least one ingoing arc from or one outgoing arc to a transition

- A transaction
  - <span style="color:red"> has a name (non-empty)
  - <span style="color:blue">has at least one ingoing arc
  - <span style="color:blue">has at least one outgoing arc  

- An arc
  - <span style="color:red">has a cost (>= 0)
  - <span style="color:blue">has either an ingoing place and an outgoing transition or
    an ingoing transition and an outgoing place