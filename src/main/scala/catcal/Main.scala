package catcal

import java.io.File
import catcal.domain.{ EventsConfiguration, ParserConfiguration }
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

object Main extends Core with App {
  val config = ConfigFactory.load("lang-uk.conf")
  val currentYear = DateTime.now().getYear

  val sourcesPath = args.applyOrElse[Int, String](0, _ => ".")
  val targetPath = args.applyOrElse[Int, String](1, _ => "output.ics")

  val sourcesDirectory = new File(sourcesPath)
  val sourceFiles = sourcesDirectory.list().filter(_.endsWith(".txt")).map(x => sourcesDirectory.toPath.resolve(x).toString)
  val importResults = sourceFiles.map(eventImporter.read(_).get)
  val importedEvents = importResults.map(_._1).flatten
  val importErrors = importResults.map(_._2).flatten
  importErrors.foreach(println)
  val events = importedEvents ++ predefinedEvents(currentYear).events
  val (resolvedEvents, errors) = eventResolver(currentYear, events).resolveAll()
  errors.foreach(println)

  val icalendar = eventExporter.toICalendar(resolvedEvents)

  val outputFile = new File(targetPath)
  icalendar.write(outputFile)

  override def parserConfiguration = ParserConfiguration(config)

  override def eventsConfiguration = EventsConfiguration(config)
}

