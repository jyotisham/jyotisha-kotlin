package jyotisha.swissephSamples

import swisseph.*
import java.util.*

/**
 * This class calculates some nakshatra starting and ending dates
 * for february 1986 in India (time zone offset 5.5 hours).
 *
 * It calculates topocentric (for Bangalore) and geocentric dates,
 * even though geocentric seems to be normally used in India.
 */
class NakshatraDates {
    /*
	final static String[] nakshatraNames = new String[] {
		"अश्विनि",
		"भरणी",
		"क्रृत्तिका",
		"रोहिणी",
		"म्रृगशीर्षा",
		"आर्द्रा",
		"पुनर्वसु",
		"पुष्य",
		"आश्लेषा",
		"मघा",
		"पूर्व फाल्गुनी",
		"उत्तर फाल्गुनी",
		"हस्त",
		"चित्रा",
		"स्वाति",
		"विशाखा",
		"अनुराधा",
		"ज्येष्ठा",
		"मूल",
		"पूर्वाषाढ़ा",
		"उत्तराषाढ़ा",
		"श्रावण",
		"श्रविष्ठा",
		"शतभिषा",
		"पूर्व भाद्रपदा",
		"उत्तर भाद्रपदा",
		"रेवती",
	};
*/
    var sw = SwissEph("./ephe")
    fun getNextNakshatraStart(juld: Double, nakshatra: Int, topoctr: Boolean): Double {
        val geopos = doubleArrayOf(77.5667, 12.9667, 0.0)
        sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0.0, 0.0)
        sw.swe_set_topo(geopos[0], geopos[1], 0.0)
        var flags = SweConst.SEFLG_SWIEPH or
                SweConst.SEFLG_TRANSIT_LONGITUDE or
                SweConst.SEFLG_SIDEREAL
        if (topoctr) {
            flags = flags or SweConst.SEFLG_TOPOCTR
        }
        val tc: TransitCalculator = TCPlanet(
            sw,
            SweConst.SE_MOON,
            flags,
            nakshatra * (360.0 / 27.0)
        )
        return sw.getTransitUT(tc, juld, false)
    }

    fun getNextNakshatraEnd(juld: Double, nakshatra: Int, topoctr: Boolean): Double {
        return getNextNakshatraStart(juld, (nakshatra + 1) % 27, topoctr)
    }

    fun toDateString(d: Double, tzHours: Double): String {
        val sd = SweDate(d + tzHours / 24.0 + 0.5 / 24.0 / 3600.0 /* round to second */)
        val hour = sd.hour
        return String.format(
            Locale.US, "%4d/%02d/%02d, %2d:%02d:%02dh",
            sd.year,
            sd.month,
            sd.day,
            hour.toInt(),
            ((hour - hour.toInt()) * 60) .toInt(),
            ((hour * 60 - (hour * 60) .toInt()) * 60) .toInt()
        )
    }

    companion object {
        const val TZ_OFFSET_HOURS = 5.5 // IST
        val nakshatraNames = arrayOf(
            "Ashvini",
            "Bharani",
            "Krittika",
            "Rohini",
            "Mrigashirsha",
            "Ardra",
            "Punarvasu",
            "Pushya",
            "Ashlesha",
            "Magha",
            "Purva Phalguni",
            "Uttar Phalguni",
            "Hasta",
            "Chitra",
            "Svati",
            "Vishakha",
            "Anuradha",
            "Jyeshtha",
            "Mula",
            "Purvashadha",
            "Uttarashadha",
            "Shravan",
            "Shravishtha",
            "Shatabhisha",
            "Purva Bhadrapada",
            "Uttara Bhadrapada",
            "Revati"
        )

        @JvmStatic
        fun main(p: Array<String>) {
            val sj = NakshatraDates()
            val sd = SweDate()
            sd.setDate(1986, 1, 1, -TZ_OFFSET_HOURS)
            println("Some nakshatras for Bangalore (77.5667E 12.9667N) starting with Ashvini in January 1986.")
            for (naksh in 0..26) {    // Nakshatra number zero based
                var nakshStart = sj.getNextNakshatraStart(sd.julDay, naksh, true)
                var nakshEnd = sj.getNextNakshatraEnd(sd.julDay, naksh, true)
                println(
                    String.format(
                        "topocentric %-20s: %s - %s",
                        nakshatraNames[naksh],
                        sj.toDateString(nakshStart, TZ_OFFSET_HOURS),
                        sj.toDateString(nakshEnd, TZ_OFFSET_HOURS)
                    )
                )

                // Same with geocentric positions:
                nakshStart = sj.getNextNakshatraStart(sd.julDay, naksh, false)
                nakshEnd = sj.getNextNakshatraEnd(sd.julDay, naksh, false)
                println(
                    String.format(
                        " geocentric %-20s: %s - %s\n",
                        nakshatraNames[naksh],
                        sj.toDateString(nakshStart, TZ_OFFSET_HOURS),
                        sj.toDateString(nakshEnd, TZ_OFFSET_HOURS)
                    )
                )
            }
        }
    }
}