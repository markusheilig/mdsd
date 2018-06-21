package htmldsl

import scala.util.parsing.combinator.{JavaTokenParsers, RegexParsers}

case class Petrinet(name: String, places: List[Place], transitions: List[Transition]) {
  override def toString: String = {
    s"Net: '$name' has ${places.size} places and ${transitions.size} transitions"
  }
}
case class Transition(id: String, placeFrom: String, placeTo: String, cost: Int)
case class Place(id: String, tokens: Int)

class PetrinetParser extends JavaTokenParsers {

  def parse(input: String): Either[String, Petrinet] = {
    parseAll(petrinet, input) match {
      case Success(p, _) => Right(p)
      case NoSuccess(msg, next) =>
        val pos = next.pos
        Left(s"[$pos] failed parsing: $msg")
    }
  }

  private def petrinet = {
    "Name:" ~ name ~
      "Places:" ~ places ~
      "Transitions:" ~ transitions ^^ {
      case _ ~ name ~ _ ~ places ~ _ ~ transitions =>
        Petrinet(name, places, transitions)
    }
  }

  private def name =
    """[^\v>]+""".r ^^ {
      _.trim
    }

  private def places = place ~ place ~ rep(place) ^^ {
    case p1 ~ p2 ~ rest => List(p1, p2) ::: rest
  }

  private def place = {
    ">" ~ identifier ~ "with" ~ positiveInteger ~ "tokens" ^^ {
      case _ ~ id ~ _ ~ tokens ~ _ =>
        Place(id, tokens)
    }
  }

  private def transitions = rep1(transition)

  private def transition = {
    ">" ~ identifier ~ "from" ~ identifier ~ "to" ~ identifier ~ "with cost" ~ positiveInteger ^^ {
      case _ ~ id ~ _ ~ placeFrom ~ _ ~ placeTo ~ _ ~ costs =>
        Transition(id, placeFrom, placeTo, costs)
    }
  }

  private def identifier = stringLiteral ^^ {
    str => str.substring(1, str.length - 1)
  }

  private def positiveInteger =
    """[1-9][0-9]*""".r ^^ {
      _.toInt
    }
}
