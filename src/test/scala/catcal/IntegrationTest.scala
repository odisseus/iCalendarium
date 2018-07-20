package catcal

import java.io.{ InputStream, SequenceInputStream }

import catcal.domain.{ EventsConfiguration, ParserConfiguration }
import catcal.service.parsing._
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

    override lazy val tokenParser: TokenParser = TokenParser.withDatesAtBeginning
  }

  it should "convert a list of fixed days events to iCalendar" in new TestCore {
    val (imported, importErrors) = eventImporter.read(fixedDays()).get
    val resolver = eventResolver(currentYear, imported)
    val (resolved, unresolved) = resolver.resolveAll()
    val exported = eventExporter.toICalendar(resolved)
    val events = exported.getEvents.asScala
    //exported.write(System.out)
    events should have size 7
    importErrors shouldBe 'empty
    unresolved shouldBe 'empty
  }

  it should "convert a list of fixed days, predefined and movable events to iCalendar" in new TestCore {
    val (imported1, importErrors1) = eventImporter.read(fixedDays()).get
    val (imported2, importErrors2) = eventImporter.read(movables()).get
    val knownEvents = imported1 ++ imported2 ++ predefinedEvents(currentYear).events
    val (resolved, unresolved) = eventResolver(currentYear, knownEvents).resolveAll()
    val exported = eventExporter.toICalendar(resolved)
    val events = exported.getEvents.asScala
    exported.write(System.out)
    events should have size 14
    importErrors1 shouldBe 'empty
    importErrors2 shouldBe 'empty
    unresolved shouldBe 'empty
  }

  def fixedDays(): InputStream = getClass.getResourceAsStream("/test-fixed-days.txt")

  def movables(): InputStream = getClass.getResourceAsStream("/test-movables.txt")

}
