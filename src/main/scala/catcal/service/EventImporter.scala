package catcal.service

import java.io._
import catcal.domain.Event
import scala.util.Try
import catcal.domain.Errors.Error

class EventImporter(
    parser: CalendarParser) {

  def read(reader: Reader): Try[(List[Event], List[Error])] = Try {
    val (good, bad) = parser.parseEventList(reader).left.map(println(_))
      .right.get
    bad.foreach { x => println(x); println }
    (good, bad)
  }

  def read(inputStream: InputStream): Try[(List[Event], List[Error])] = {
    val reader = Try(new InputStreamReader(inputStream))
    reader.flatMap { in =>
      try
        read(in)
      finally
        in.close()
    }
  }

  def read(file: File): Try[(List[Event], List[Error])] = {
    val inputStream = Try(new FileInputStream(file))
    inputStream.flatMap { is =>
      try
        read(is)
      finally
        is.close()
    }
  }

  def read(path: String): Try[(List[Event], List[Error])] = {
    read(new File(path))
  }

}
