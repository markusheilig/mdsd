package htmldsl

object MyApp extends App {

  val input =
    """
      |Name: Mein Netz
      |Places:
      | > "p1" with 4 tokens
      | > "p2" with 1 tokens
      |Transitions:
      | > "t1" from "p1" to "p2" with cost 3
      | > "t2" from "p2" to "p2" with cost 1
    """.stripMargin

  val parser = new PetrinetParser
  val validator = new PetrinetValidator
  val result = parser.parse(input).map(validator.validate).merge
  result match {
    case Left(error) => println(error)
    case Right(petrinet) => println(petrinet)
  }
}

