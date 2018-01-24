package catcal

import catcal.domain.ParserConfiguration
import com.typesafe.config.ConfigFactory
import org.scalatest.{ FlatSpec, Matchers }

import scala.collection.JavaConverters._
import scala.io.Source

class IntegrationTest extends FlatSpec with Matchers {
  behavior of "application core"

  trait TestCore extends Core {
    val config = ConfigFactory.load("lang-uk.conf")
    val currentYear = 2018
    override def parserConfiguration = ParserConfiguration(config)
  }

  it should "convert event list to iCalendar" in new TestCore {
    val src = Source.fromURL(getClass.getResource("/fixed-days.txt"))
    val imported = eventImporter.read(src.reader()).get
    val resolver = eventResolver(currentYear, imported)
    val resolved = imported.map(resolver.resolveEvent)
    val exported = eventExporter.toICalendar(resolved)
    val events = exported.getEvents.asScala
    //events.foreach(println)
    events should have size 7
  }

}
