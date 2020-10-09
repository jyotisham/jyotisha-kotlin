package jyotisha

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import swisseph.TCPlanetPlanet
import swisseph.TransitCalculator

// Calculates one cycle of tithis
//
// Be sure, your swisseph sources had been "precompiled" with the -DTRANSITS switch.
//
object Tithis {
    const val calcTopocentric = false
    val tithi_names = arrayOf(
        "Prathama",
        "Dwitiya",
        "Tritiya",
        "Chaturthi",
        "Panchami",
        "Shashti",
        "Saptami",
        "Ashtami",
        "Navami",
        "Dasami",
        "Ekadasi",
        "Dvadasi",
        "Trayodasi",
        "Chaturdashi",
        "Purnima"
    )

    @JvmStatic
    fun main(p: Array<String>) {
        val sw = SwissEph("./ephe")
        if (calcTopocentric) {
            sw.swe_set_topo(73.827298, 15.500439, 0.0)
        }
        val sd = SweDate(2013, 9, 30, 0.0)
        var flags: Int = SweConst.SEFLG_SWIEPH or SweConst.SEFLG_TRANSIT_LONGITUDE
        if (calcTopocentric) {
            flags = flags or SweConst.SEFLG_TOPOCTR
        }
        val backwards = false
        var tithi = 0
        var tithi_deg = 0.0

        // Calculates positions of a planet relative to another planet
        val tc: TransitCalculator = TCPlanetPlanet(
            sw,
            SweConst.SE_MOON,
            SweConst.SE_SUN,
            flags,
            tithi_deg
        )
        while (tithi_deg < 360.0) {
            val nextTransitUT: Double = sw.getTransitUT(tc, sd.getJulDay(), backwards)
            var name = tithi_names[tithi % 15]
            if (tithi == 14 || tithi == 29) {
                name = if (tithi < 15) "Purnima" else "Amavasya"
            } else {
                name += (if (tithi < 15) " shukla" else " krishna") + " paksha"
            }
            val sout = SweDate(nextTransitUT)
            val mon: Int = sout.getMonth()
            val day: Int = sout.getDay()
            val year: Int = sout.getYear()
            var h: Double = sout.getHour()
            h += 0.5 / 3600.0
            val hour = h.toInt()
            val min = ((h - hour) * 60) as Int
            val sec = (((h - hour) * 60 - min) * 60) as Int
            java.lang.System.out.printf(
                "%26s: %2d/%02d/%04d %2d:%02d:%02dh, JD: %.10f\n",
                name, day, mon, year, hour, min, sec, nextTransitUT
            )
            tithi++
            tithi_deg += 360.0 / 30.0
            tc.setOffset(tithi_deg)
        }
    }
}