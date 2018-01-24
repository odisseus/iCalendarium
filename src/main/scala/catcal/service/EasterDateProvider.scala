package catcal.service

import org.joda.time.LocalDate

/**
 * https://en.wikipedia.org/wiki/Easter#Table_of_the_dates_of_Easter
 */
object EasterDateProvider {

  def getEasterDate(year: Int): LocalDate = {
    LocalDate.parse(year match {
      case 2016 => "2016-05-01"
      case 2017 => "2017-04-16"
      case 2018 => "2018-04-08"
      case 2019 => "2019-04-28"
      case 2020 => "2020-04-19"
      case 2021 => "2021-05-02"
      case 2022 => "2022-04-24"
    })
  }

}
