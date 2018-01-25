package catcal

import java.io.{ InputStream, SequenceInputStream }

import catcal.domain.{ EventsConfiguration, ParserConfiguration }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ FlatSpec, Matchers }

import scala.collection.JavaConverters._

class IntegrationTest extends FlatSpec with Matchers {
  behavior of "application core"

  trait TestCore extends Core {

    val config = ConfigFactory.load("lang-uk.conf")
    val currentYear = 2018

    override def parserConfiguration = ParserConfiguration(config)

    override def eventsConfiguration = EventsConfiguration(config)
  }

  it should "convert a list of fixed days events to iCalendar" in new TestCore {
    val imported = eventImporter.read(fixedDays()).get
    val resolver = eventResolver(currentYear, imported)
    val resolved = imported.map(resolver.resolveEvent)
    val exported = eventExporter.toICalendar(resolved)
    val events = exported.getEvents.asScala
    //exported.write(System.out)
    events should have size 7
  }

  it should "convert a list of fixed days, predefined and movable events to iCalendar" in new TestCore {
    val imported1 = eventImporter.read(fixedDays()).get
    val imported2 = eventImporter.read(movables()).get
    val knownEvents = imported1 ++ imported2 ++ predefinedEvents(currentYear).events
    val resolved = eventResolver(currentYear, knownEvents).resolveAll()
    val exported = eventExporter.toICalendar(resolved)
    val events = exported.getEvents.asScala
    exported.write(System.out)
    events should have size 14
  }

  def fixedDays(): InputStream = getClass.getResourceAsStream("/fixed-days.txt")

  def movables(): InputStream = getClass.getResourceAsStream("/movables.txt")

}
