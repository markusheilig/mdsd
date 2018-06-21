package htmldsl

import scala.collection.mutable.ListBuffer
trait ValidationError

case class DuplicateId(id: String) extends ValidationError
case class UndefinedPlace(id: String) extends ValidationError
case class UnusedPlace(place: Place) extends ValidationError
case class TransitionNotUnique(id: String) extends ValidationError
case class IllegalFromTo(transition: Transition) extends ValidationError

class PetrinetValidator {



  def validate(petrinet: Petrinet): Either[ListBuffer[ValidationError], Petrinet] = {
    val checks = List(
      new CheckForUndefinedPlaces,
      new CheckForIllegalTransitionFromTo,
      new CheckForUnusedPlaces,
      new CheckForDuplicateIds
    )

    // todo: filter any check that fails and combine error messages
    // or return Right(petrinet)
    var errors = new scala.collection.mutable.ListBuffer[ValidationError]()
    checks.map(check => {
      check.apply(petrinet) match {
        case Left(error) =>
          error.foreach(err => errors += err)
        case Right(petrinet) => ;
      }
    })

    if (errors.isEmpty) Right(petrinet)
    else Left(errors)
  }

  trait Check {
    def apply(petrinet: Petrinet): Either[List[ValidationError], Petrinet]
  }

  class CheckForUndefinedPlaces extends Check {

    override def apply(petrinet: Petrinet): Either[List[ValidationError], Petrinet] = {
      val existingPlaceIds = petrinet.places.map(_.id).toSet
      val usedPlaceIds = petrinet.transitions.flatMap(t => List(t.placeFrom, t.placeTo)).toSet
      val undefinedPlaceIds = usedPlaceIds.diff(existingPlaceIds)
      if (undefinedPlaceIds.isEmpty) {
        Right(petrinet)
      } else {
        val errors = undefinedPlaceIds.toList.map(id => UndefinedPlace(id))
        Left(errors)
      }
    }
  }

  class CheckForIllegalTransitionFromTo extends Check {
    // from == to
    override def apply(petrinet: Petrinet): Either[List[ValidationError], Petrinet] = {
      val existingTransitions = petrinet.transitions;
      val illegalTransitions: List[ValidationError] = existingTransitions.filter(t => t.placeTo.equals(t.placeFrom)).map(IllegalFromTo)
      if (illegalTransitions.isEmpty) Right(petrinet)
      else Left(illegalTransitions)
    }
  }

  class CheckForUnusedPlaces extends Check {
    override def apply(petrinet: Petrinet): Either[List[ValidationError], Petrinet] = {
      val existingTransitions = petrinet.transitions;
      val exisitingPlaces = petrinet.places;
      val unusedPlaces = exisitingPlaces.filter(p => existingTransitions.filter(t => (t.placeFrom.equals(p.id) || t.placeTo.equals(p.id))).isEmpty).map(UnusedPlace)
      if (unusedPlaces.isEmpty) Right(petrinet)
      else Left(unusedPlaces)
    }
  }

  class CheckForDuplicateIds extends Check {
    override def apply(petrinet: Petrinet): Either[List[ValidationError], Petrinet] = {
      val exisitingIds = petrinet.places.map(_.id) ::: petrinet.transitions.map(_.id);
      val duplicateIds = exisitingIds.filter(id1 => exisitingIds.filterNot(id2 =>
          id2!=id1).size>1
      ).distinct.map(DuplicateId)
      if (duplicateIds.isEmpty) Right(petrinet)
      else Left(duplicateIds)
    }
  }

}
