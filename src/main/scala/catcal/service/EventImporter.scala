package catcal.service

import java.io.{ File, FileReader, Reader }

import catcal.domain.Event

import scala.util.Try

class EventImporter(
    parser: CalendarParser) {

  def read(reader: Reader): Try[List[Event]] = Try {
    parser.parseEventList(reader).get
  }

  def read(file: File): Try[List[Event]] = {
    val reader = Try(new FileReader(file))
    reader.flatMap { in =>
      try
        read(in)
      finally
        in.close()
    }
  }

  def read(path: String): Try[List[Event]] = {
    read(new File(path))
  }

}
