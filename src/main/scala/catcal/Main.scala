package catcal

import java.io.File
import catcal.domain.{ EventsConfiguration, ParserConfiguration }
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

object Main extends Core with App {
  val config = ConfigFactory.load("lang-uk.conf")
  val currentYear = DateTime.now().getYear

  val sourcesPath = args.headOption.getOrElse(".")
  val sourcesDirectory = new File(sourcesPath)
  val sourceFiles = sourcesDirectory.list().filter(_.endsWith(".txt")).map(x => sourcesDirectory.toPath.resolve(x).toString)
  val events = sourceFiles.map(eventImporter.read).flatMap(_.get) ++ predefinedEvents(currentYear).events
  val resolvedEvents = eventResolver(currentYear, events).resolveAll()

  resolvedEvents.foreach(println)

  override def parserConfiguration = ParserConfiguration(config)

  override def eventsConfiguration = EventsConfiguration(config)
}

