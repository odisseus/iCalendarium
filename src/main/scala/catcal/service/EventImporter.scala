package catcal.service

import java.io._
import catcal.domain.Event
import scala.util.Try

class EventImporter(
    parser: CalendarParser) {

  def read(reader: Reader): Try[List[Event]] = Try {
    val (good, bad) = parser.parseEventList(reader).right.get
    bad.map(_.message).foreach(println)
    good
  }

  def read(inputStream: InputStream): Try[List[Event]] = {
    val reader = Try(new InputStreamReader(inputStream))
    reader.flatMap { in =>
      try
        read(in)
      finally
        in.close()
    }
  }

  def read(file: File): Try[List[Event]] = {
    val inputStream = Try(new FileInputStream(file))
    inputStream.flatMap { is =>
      try
        read(is)
      finally
        is.close()
    }
  }

  def read(path: String): Try[List[Event]] = {
    read(new File(path))
  }

}
