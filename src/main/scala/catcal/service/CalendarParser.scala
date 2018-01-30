package catcal.service

import catcal.domain.{ Errors, Event }

class CalendarParser(eventParser: EventParser) {

  def parseEventList(in: java.io.Reader): Either[Errors.Error, (List[Event], List[Errors.Error])] = {
    val entryList = InputTokenizer.entries(in)
    entryList.right.map { entries =>
      val (parsedEvents, badEntries) = entries.map(eventParser.parseEvent).partition(_.isRight)
      (parsedEvents.map(_.right.get), badEntries.map(_.left.get))
    }
  }

}
