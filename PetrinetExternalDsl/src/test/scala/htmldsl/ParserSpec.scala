package htmldsl

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer
import scala.util.Right

class ValidPetrinetSpec extends FlatSpec with Matchers{

  it should "produce valid petrinet" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        | > "p3" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "t2" from "p3" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be(
      Right(Petrinet("Mein Netz", List(Place("p1", 4),Place("p2", 1),Place("p3", 1)), List(Transition("t1","p1","p2",3),Transition("t2","p3","p2",3))))
    )
  }
}

class UnusedPlaceIdsSpec extends FlatSpec with Matchers{

  it should "find unused place p4" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        | > "p3" with 1 tokens
        | > "p4" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "t2" from "p3" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(UnusedPlace(Place("p4",1)))))
  }

  it should "find unused place p1" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        | > "p3" with 1 tokens
        | > "p4" with 1 tokens
        |Transitions:
        | > "t1" from "p4" to "p2" with cost 3
        | > "t2" from "p3" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(UnusedPlace(Place("p1",4)))))
  }
}

class UndefindePlaceSpec extends FlatSpec with Matchers{

  it should "find undefined place p4" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "t2" from "p3" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(UndefinedPlace("p3"))))
  }

  it should "find undefined place p1" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p2" with 1 tokens
        | > "p3" with 1 tokens
        | > "p4" with 1 tokens
        |Transitions:
        | > "t1" from "p4" to "p2" with cost 3
        | > "t2" from "p3" to "p1" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(UndefinedPlace("p1"))))
  }
}

class DuplicatePlaceIdsSpec extends FlatSpec with Matchers {
  it should "find duplicate place ids" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        | > "p2" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "t2" from "p1" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(DuplicateId("p2"))))
  }

  it should "find duplicate place/transition id" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "p2" from "p1" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(DuplicateId("p2"))))
  }

  it should "find duplicate transition id" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 1 tokens
        | > "p2" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p2" with cost 3
        | > "t1" from "p1" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(DuplicateId("t1"))))
  }
}

class IllegalTransitionSpec extends FlatSpec with Matchers {
  it should "find p1 to p1" in {
    val input =
      """
        |Name: Mein Netz
        |Places:
        | > "p1" with 4 tokens
        | > "p2" with 1 tokens
        |Transitions:
        | > "t1" from "p1" to "p1" with cost 3
        | > "t2" from "p1" to "p2" with cost 3
      """.stripMargin
    val parser = new PetrinetParser
    val validator = new PetrinetValidator
    val result = parser.parse(input).map(validator.validate).merge
    result should be (Left(ListBuffer(IllegalFromTo(Transition("t1","p1","p1",3)))))
  }
}