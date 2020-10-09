package jyotisha.swissephSamples

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph

/**
 * This class is an example of how to calculate
 * planets and houses in the indian vedic style
 * of astrology (jyotish).
 */
object VedicHouses {
    /**
     * The method to determine ayanamsha value:
     */
    private const val SID_METHOD = SweConst.SE_SIDM_LAHIRI
    @JvmStatic
    fun main(args: Array<String>) {
        // Input data:
        val year = 2013
        val month = 9
        val day = 4
        val longitude = 80 + 17 / 60.0 // Chennai
        val latitude = 13 + 5 / 60.0
        val hour = 7 + 30.0 / 60.0 - 5.5 // IST


        // Use ... new SwissEph("/path/to/your/ephemeris/data/files"); when
        // your data files don't reside somewhere in the paths defined in
        // SweConst.SE_EPHE_PATH, which is ".:./ephe:/users/ephe2/:/users/ephe/"
        // currently.
        val sw = SwissEph()
        val sd = SweDate(year, month, day, hour)

        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0.0, 0.0)

        // Some required variables:
        val cusps = DoubleArray(13)
        val acsc = DoubleArray(10)
        val xp = DoubleArray(6)
        val serr = StringBuffer()

        // Print input details:
        println("Date (YYYY/MM/DD): " + sd.year + "/" + sd.month + "/" + sd.day + ", " + toHMS(sd.hour))
        println("Jul. day:  " + sd.julDay)
        println("DeltaT:    " + sd.deltaT * 24 * 3600 + " sec.")
        println(
            "Location:  " +
                    toDMS(Math.abs(longitude)) + (if (longitude > 0) "E" else "W") +
                    " / " +
                    toDMS(Math.abs(latitude)) + if (latitude > 0) "N" else "S"
        )

        // Get and print ayanamsa value for info:
        val ayanamsa = sw.swe_get_ayanamsa_ut(sd.julDay)
        println("Ayanamsa:  " + toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")")

        // Get and print lagna:
        var flags = SweConst.SEFLG_SIDEREAL
        val result = sw.swe_houses(
            sd.julDay,
            flags,
            latitude,
            longitude,
            'P'.toInt(),
            cusps,
            acsc
        )
        println(
            """
    Ascendant: ${toDMS(acsc[0])}
    
    """.trimIndent()
        )
        val ascSign = (acsc[0] / 30).toInt() + 1

        // Calculate all planets:
        val planets = intArrayOf(
            SweConst.SE_SUN,
            SweConst.SE_MOON,
            SweConst.SE_MARS,
            SweConst.SE_MERCURY,
            SweConst.SE_JUPITER,
            SweConst.SE_VENUS,
            SweConst.SE_SATURN,
            SweConst.SE_TRUE_NODE
        ) // Some systems prefer SE_MEAN_NODE
        flags = SweConst.SEFLG_SWIEPH or  // fastest method, requires data files
                SweConst.SEFLG_SIDEREAL or  // sidereal zodiac
                SweConst.SEFLG_NONUT or  // will be set automatically for sidereal calculations, if not set here
                SweConst.SEFLG_SPEED // to determine retrograde vs. direct motion
        var sign: Int
        var house: Int
        var retrograde = false
        for (p in planets.indices) {
            val planet = planets[p]
            val planetName = sw.swe_get_planet_name(planet)
            val ret = sw.swe_calc_ut(
                sd.julDay,
                planet,
                flags,
                xp,
                serr
            )
            if (ret != flags) {
                if (serr.length > 0) {
                    System.err.println("Warning: $serr")
                } else {
                    System.err.println(String.format("Warning, different flags used (0x%x)", ret))
                }
            }
            sign = (xp[0] / 30).toInt() + 1
            house = (sign + 12 - ascSign) % 12 + 1
            retrograde = xp[3] < 0
            System.out.printf(
                "%-12s: %s %c; sign: %2d; %s in house %2d\n",
                planetName, toDMS(xp[0]), if (retrograde) 'R' else 'D', sign, toDMS(xp[0] % 30), house
            )
        }
        // KETU
        xp[0] = (xp[0] + 180.0) % 360
        val planetName = "Ketu (true)"
        sign = (xp[0] / 30).toInt() + 1
        house = (sign + 12 - ascSign) % 12 + 1
        System.out.printf(
            "%-12s: %s %c; sign: %2d; %s in house %2d\n",
            planetName, toDMS(xp[0]), if (retrograde) 'R' else 'D', sign, toDMS(xp[0] % 30), house
        )
    }

    fun toHMS(d: Double): String {
        var d = d
        d += 0.5 / 3600.0 // round to one second
        val h = d.toInt()
        d = (d - h) * 60
        val min = d.toInt()
        val sec = ((d - min) * 60) .toInt()
        return String.format("%2d:%02d:%02d", h, min, sec)
    }

    fun toDMS(d: Double): String {
        var d = d
        d += 0.5 / 3600.0 / 10000.0 // round to 1/1000 of a second
        val deg = d.toInt()
        d = (d - deg) * 60
        val min = d.toInt()
        d = (d - min) * 60
        val sec = d
        return String.format("%3d°%02d'%07.4f\"", deg, min, sec)
    }
}