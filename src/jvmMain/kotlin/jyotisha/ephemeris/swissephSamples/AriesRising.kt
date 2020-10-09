package jyotisha.ephemeris.swissephSamples

import swisseph.*

// When is Aries rising on May 8, 2014 in India
// 80.1 E, 13.08 N in Krishnamurti ayanamsa system
//
// - "Aries rising" means, ascendant is at 0 deg. longitude
// - This is a transit calculation.
// - Transit calculations over AC or similar are done with
//   the TCHouses TransitCalculator
// - Times are UTC always
object AriesRising {
    private const val tzOffset = 5.5
    @JvmStatic
    fun main(p: Array<String>) {
        val sw = SwissEph(ClassLoader.getSystemResource("swisseph").file)

        // 0:00h on May 8, 2014 Indian Standard Time = UTC + 5.5
        // So, use a starting date of 2014-05-08, 0:00h - 5.5h:
        val jDate = SweDate(2014, 5, 8, 0 - tzOffset, true)
        println("Start searching on    " + printDate(jDate.julDay + tzOffset / 24.0) + " IST")
        jDate.makeValidDate()
        println("                     (" + printDate(jDate.julDay) + " UTC)")

        // Sidereal Krishnamurti mode:
        sw.swe_set_sid_mode(SweConst.SE_SIDM_KRISHNAMURTI, 0.0, 0.0)
        val flags = SweConst.SEFLG_SIDEREAL or SweConst.SEFLG_TRANSIT_LONGITUDE
        val backwards = false

        // House system is irrelevant, as long as the ascendant
        // calculation matches your requirements
        val tc: TransitCalculator = TCHouses(
            sw,
            SweConst.SE_ASC,
            SweConst.SE_HSYS_PLACIDUS,
            80.1, 13.08,
            flags,
            0.0
        ) // 0 degreee aries
        val nextTransitUT = sw.getTransitUT(tc, jDate.julDay, backwards)
        println("Next rise of aries on " + printDate(nextTransitUT + tzOffset / 24.0) + " IST")
        println("                     (" + printDate(nextTransitUT) + " UTC)")
    }

    fun printDate(jd: Double): String {
        val sd = SweDate(jd)
        var time = sd.hour
        val hour = time.toInt()
        time = 60 * (time - hour)
        val min = time.toInt()
        val sec = 60 * (time - min)
        return String.format("%4s-%02d-%02d %2d:%02d:%05.2f", sd.year, sd.month, sd.day, hour, min, sec)
    }
}