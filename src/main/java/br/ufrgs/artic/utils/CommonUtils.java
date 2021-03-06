package br.ufrgs.artic.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class with common constants + static content available to the application.
 */
public final class CommonUtils {

    private static final Logger LOGGER = Logger.getLogger("CommonUtils");

    private CommonUtils() {
    }

    public static final Pattern NUMERAL_PATTERN = Pattern.compile("(\\d)+(th|st|nd|rd)");
    public static final Pattern NUMBER_ONLY_PATTERN = Pattern.compile("\\d+");
    public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    public static final Pattern WEBSITE_PATTERN = Pattern.compile("www|https?");
    public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final Pattern EMAIL_PATTERN = Pattern.compile("@|email|e\\-mail");
    public static final Pattern UNIVERSITY_PATTERN = Pattern.compile("university|faculty");
    public static final Pattern DEPARTMENT_PATTERN = Pattern.compile("department|center|laboratory|division|" +
            "school|group|research|computer|dept|education|technology|science|defence|develop|development|" +
            "information|branch|institute|library|medic|system|database|software|state|academic|tech|point|normal|community|language|centre|computation");
    public static final Pattern CONTINENT_OCEANS_PATTERN = Pattern.compile("asia|africa|north|america|south|antarctica|europe|australia|ocean|pacific|atlantic|indian|southern|arctic");
    public static final Pattern CONFERENCE_NAME_PATTERN = Pattern.compile("[A-Z]+(('|’|`)?\\d\\d)?(,|\\.|;)?");
    public static final Pattern DAYS_PATTERN = Pattern.compile("(\\d\\d)(-|–)(\\d\\d)(th|st|nd|rd)?(,|\\.|;)?");
    public static final Pattern ISBN_PATTERN = Pattern.compile("(\\d+(\\-|–))(\\d+(\\-|–))(\\d+(\\-|–))[\\d+(\\-|–)]+(/)?([/\\d+]+)(\\.\\.\\.[$|\\.|,|\\d]+)?(,|\\.|;)*");
    public static final Pattern PUBLISHER_PATTERN = Pattern.compile("acm|elsevier|ieee|springer-verlag|springer");
    public static final Pattern ISSN_PATTERN = Pattern.compile("(\\d\\d\\d\\d)(-|–)(\\d\\d\\d\\d)[/|$]*(,|\\.|;)?");
    public static final Pattern DOI_PATTERN = Pattern.compile("(doi:)?(\\d\\d\\.\\d\\d\\d\\d)/[[A-Za-z0-9]+\\.]+");
    public static final Pattern SEPARATOR_SIMPLE_PATTERN = Pattern.compile(",|;");
    public static final List<String> MONTH_LIST;
    public static final List<String> COUNTRY_LIST;
    public static final List<String> CONFERENCE_LIST;

    static {
        MONTH_LIST = new ArrayList<>();
        MONTH_LIST.add("january");
        MONTH_LIST.add("february");
        MONTH_LIST.add("march");
        MONTH_LIST.add("april");
        MONTH_LIST.add("may");
        MONTH_LIST.add("june");
        MONTH_LIST.add("july");
        MONTH_LIST.add("august");
        MONTH_LIST.add("september");
        MONTH_LIST.add("october");
        MONTH_LIST.add("november");
        MONTH_LIST.add("december");
        MONTH_LIST.add("jan");
        MONTH_LIST.add("feb");
        MONTH_LIST.add("mar");
        MONTH_LIST.add("apr");
        MONTH_LIST.add("may");
        MONTH_LIST.add("jun");
        MONTH_LIST.add("jul");
        MONTH_LIST.add("aug");
        MONTH_LIST.add("sep");
        MONTH_LIST.add("oct");
        MONTH_LIST.add("nov");
        MONTH_LIST.add("dec");

        COUNTRY_LIST = new ArrayList<>();
        COUNTRY_LIST.add("abkhazia");
        COUNTRY_LIST.add("afghanistan");
        COUNTRY_LIST.add("akrotiri and dhekelia");
        COUNTRY_LIST.add("aland");
        COUNTRY_LIST.add("albania");
        COUNTRY_LIST.add("algeria");
        COUNTRY_LIST.add("american samoa");
        COUNTRY_LIST.add("andorra");
        COUNTRY_LIST.add("angola");
        COUNTRY_LIST.add("anguilla");
        COUNTRY_LIST.add("antigua and barbuda");
        COUNTRY_LIST.add("argentina");
        COUNTRY_LIST.add("armenia");
        COUNTRY_LIST.add("aruba");
        COUNTRY_LIST.add("ascension island");
        COUNTRY_LIST.add("australia");
        COUNTRY_LIST.add("austria");
        COUNTRY_LIST.add("azerbaijan");
        COUNTRY_LIST.add("bahamas, the");
        COUNTRY_LIST.add("bahrain");
        COUNTRY_LIST.add("bangladesh");
        COUNTRY_LIST.add("barbados");
        COUNTRY_LIST.add("belarus");
        COUNTRY_LIST.add("belgium");
        COUNTRY_LIST.add("belize");
        COUNTRY_LIST.add("benin");
        COUNTRY_LIST.add("bermuda");
        COUNTRY_LIST.add("bhutan");
        COUNTRY_LIST.add("bolivia");
        COUNTRY_LIST.add("bosnia and herzegovina");
        COUNTRY_LIST.add("botswana");
        COUNTRY_LIST.add("brazil");
        COUNTRY_LIST.add("brunei");
        COUNTRY_LIST.add("bulgaria");
        COUNTRY_LIST.add("burkina faso");
        COUNTRY_LIST.add("burundi");
        COUNTRY_LIST.add("cambodia");
        COUNTRY_LIST.add("cameroon");
        COUNTRY_LIST.add("canada");
        COUNTRY_LIST.add("cape verde");
        COUNTRY_LIST.add("cayman islands");
        COUNTRY_LIST.add("central africa republic");
        COUNTRY_LIST.add("chad");
        COUNTRY_LIST.add("chile");
        COUNTRY_LIST.add("china");
        COUNTRY_LIST.add("christmas island");
        COUNTRY_LIST.add("cocos (keeling) islands");
        COUNTRY_LIST.add("colombia");
        COUNTRY_LIST.add("comoros");
        COUNTRY_LIST.add("congo");
        COUNTRY_LIST.add("cook islands");
        COUNTRY_LIST.add("costa rica");
        COUNTRY_LIST.add("cote d'lvoire");
        COUNTRY_LIST.add("croatia");
        COUNTRY_LIST.add("cuba");
        COUNTRY_LIST.add("cyprus");
        COUNTRY_LIST.add("czech republic");
        COUNTRY_LIST.add("denmark");
        COUNTRY_LIST.add("djibouti");
        COUNTRY_LIST.add("dominica");
        COUNTRY_LIST.add("dominican republic");
        COUNTRY_LIST.add("east timor ecuador");
        COUNTRY_LIST.add("egypt");
        COUNTRY_LIST.add("el salvador");
        COUNTRY_LIST.add("equatorial guinea");
        COUNTRY_LIST.add("eritrea");
        COUNTRY_LIST.add("estonia");
        COUNTRY_LIST.add("ethiopia");
        COUNTRY_LIST.add("falkland islands");
        COUNTRY_LIST.add("faroe islands");
        COUNTRY_LIST.add("fiji");
        COUNTRY_LIST.add("finland");
        COUNTRY_LIST.add("france");
        COUNTRY_LIST.add("french polynesia");
        COUNTRY_LIST.add("gabon");
        COUNTRY_LIST.add("cambia, the");
        COUNTRY_LIST.add("georgia");
        COUNTRY_LIST.add("germany");
        COUNTRY_LIST.add("ghana");
        COUNTRY_LIST.add("gibraltar");
        COUNTRY_LIST.add("greece");
        COUNTRY_LIST.add("greenland");
        COUNTRY_LIST.add("grenada");
        COUNTRY_LIST.add("guam");
        COUNTRY_LIST.add("guatemala");
        COUNTRY_LIST.add("guemsey");
        COUNTRY_LIST.add("guinea");
        COUNTRY_LIST.add("guinea-bissau");
        COUNTRY_LIST.add("guyana");
        COUNTRY_LIST.add("haiti");
        COUNTRY_LIST.add("honduras");
        COUNTRY_LIST.add("hong kong");
        COUNTRY_LIST.add("hungary");
        COUNTRY_LIST.add("iceland");
        COUNTRY_LIST.add("india");
        COUNTRY_LIST.add("indonesia");
        COUNTRY_LIST.add("iran");
        COUNTRY_LIST.add("iraq");
        COUNTRY_LIST.add("ireland");
        COUNTRY_LIST.add("isle of man");
        COUNTRY_LIST.add("israel");
        COUNTRY_LIST.add("italy");
        COUNTRY_LIST.add("jamaica");
        COUNTRY_LIST.add("japan");
        COUNTRY_LIST.add("jersey");
        COUNTRY_LIST.add("jordan");
        COUNTRY_LIST.add("kazakhstan");
        COUNTRY_LIST.add("kenya");
        COUNTRY_LIST.add("kiribati");
        COUNTRY_LIST.add("korea, n");
        COUNTRY_LIST.add("korea, s");
        COUNTRY_LIST.add("kosovo");
        COUNTRY_LIST.add("kuwait");
        COUNTRY_LIST.add("kyrgyzstan");
        COUNTRY_LIST.add("laos");
        COUNTRY_LIST.add("latvia");
        COUNTRY_LIST.add("lebanon");
        COUNTRY_LIST.add("lesotho");
        COUNTRY_LIST.add("liberia");
        COUNTRY_LIST.add("libya");
        COUNTRY_LIST.add("liechtenstein");
        COUNTRY_LIST.add("lithuania");
        COUNTRY_LIST.add("luxembourg");
        COUNTRY_LIST.add("macao");
        COUNTRY_LIST.add("macedonia");
        COUNTRY_LIST.add("madagascar");
        COUNTRY_LIST.add("malawi");
        COUNTRY_LIST.add("malaysia");
        COUNTRY_LIST.add("maldives");
        COUNTRY_LIST.add("mali");
        COUNTRY_LIST.add("malta");
        COUNTRY_LIST.add("marshall islands");
        COUNTRY_LIST.add("mauritania");
        COUNTRY_LIST.add("mauritius");
        COUNTRY_LIST.add("mayotte");
        COUNTRY_LIST.add("mexico");
        COUNTRY_LIST.add("micronesia");
        COUNTRY_LIST.add("moldova");
        COUNTRY_LIST.add("monaco");
        COUNTRY_LIST.add("mongolia");
        COUNTRY_LIST.add("montenegro");
        COUNTRY_LIST.add("montserrat");
        COUNTRY_LIST.add("morocco");
        COUNTRY_LIST.add("mozambique");
        COUNTRY_LIST.add("myanmar");
        COUNTRY_LIST.add("nagorno-karabakh");
        COUNTRY_LIST.add("namibia");
        COUNTRY_LIST.add("nauru");
        COUNTRY_LIST.add("nepal");
        COUNTRY_LIST.add("netherlands");
        COUNTRY_LIST.add("netherlands antilles");
        COUNTRY_LIST.add("new caledonia");
        COUNTRY_LIST.add("new zealand");
        COUNTRY_LIST.add("nicaragua");
        COUNTRY_LIST.add("niger");
        COUNTRY_LIST.add("nigeria");
        COUNTRY_LIST.add("niue");
        COUNTRY_LIST.add("norfolk island");
        COUNTRY_LIST.add("northern cyprus");
        COUNTRY_LIST.add("northern mariana islands");
        COUNTRY_LIST.add("norway");
        COUNTRY_LIST.add("oman");
        COUNTRY_LIST.add("pakistan");
        COUNTRY_LIST.add("palau");
        COUNTRY_LIST.add("palestine");
        COUNTRY_LIST.add("panama");
        COUNTRY_LIST.add("papua new guinea");
        COUNTRY_LIST.add("paraguay");
        COUNTRY_LIST.add("peru");
        COUNTRY_LIST.add("philippines");
        COUNTRY_LIST.add("pitcaim islands");
        COUNTRY_LIST.add("poland");
        COUNTRY_LIST.add("portugal");
        COUNTRY_LIST.add("puerto rico");
        COUNTRY_LIST.add("qatar");
        COUNTRY_LIST.add("romania");
        COUNTRY_LIST.add("russia");
        COUNTRY_LIST.add("rwanda");
        COUNTRY_LIST.add("sahrawi arab democratic republic");
        COUNTRY_LIST.add("saint-barthelemy");
        COUNTRY_LIST.add("saint helena");
        COUNTRY_LIST.add("saint kitts and nevis");
        COUNTRY_LIST.add("saint lucia");
        COUNTRY_LIST.add("saint martin");
        COUNTRY_LIST.add("saint pierre and miquelon");
        COUNTRY_LIST.add("saint vincent and grenadines");
        COUNTRY_LIST.add("samos");
        COUNTRY_LIST.add("san marino");
        COUNTRY_LIST.add("sao tome and principe");
        COUNTRY_LIST.add("saudi arabia");
        COUNTRY_LIST.add("senegal");
        COUNTRY_LIST.add("serbia");
        COUNTRY_LIST.add("seychelles");
        COUNTRY_LIST.add("sierra leone");
        COUNTRY_LIST.add("singapore");
        COUNTRY_LIST.add("slovakia");
        COUNTRY_LIST.add("slovenia");
        COUNTRY_LIST.add("solomon islands");
        COUNTRY_LIST.add("somalia");
        COUNTRY_LIST.add("somaliland");
        COUNTRY_LIST.add("south africa");
        COUNTRY_LIST.add("south ossetia");
        COUNTRY_LIST.add("spain");
        COUNTRY_LIST.add("sri lanka");
        COUNTRY_LIST.add("sudan");
        COUNTRY_LIST.add("suriname");
        COUNTRY_LIST.add("svalbard");
        COUNTRY_LIST.add("swaziland");
        COUNTRY_LIST.add("sweden");
        COUNTRY_LIST.add("switzerland");
        COUNTRY_LIST.add("syria");
        COUNTRY_LIST.add("tajikistan");
        COUNTRY_LIST.add("tanzania");
        COUNTRY_LIST.add("thailand");
        COUNTRY_LIST.add("togo");
        COUNTRY_LIST.add("tokelau");
        COUNTRY_LIST.add("tonga");
        COUNTRY_LIST.add("transnistria");
        COUNTRY_LIST.add("trinidad and tobago");
        COUNTRY_LIST.add("tristan da cunha");
        COUNTRY_LIST.add("tunisia");
        COUNTRY_LIST.add("turkey");
        COUNTRY_LIST.add("turkmenistan");
        COUNTRY_LIST.add("turks and caicos islands");
        COUNTRY_LIST.add("tuvalu");
        COUNTRY_LIST.add("uganda");
        COUNTRY_LIST.add("ukraine");
        COUNTRY_LIST.add("united arab emirates");
        COUNTRY_LIST.add("united kingdom");
        COUNTRY_LIST.add("united states");
        COUNTRY_LIST.add("usa");
        COUNTRY_LIST.add("uruguay");
        COUNTRY_LIST.add("uzbekistan");
        COUNTRY_LIST.add("vanuatu");
        COUNTRY_LIST.add("vatican city");
        COUNTRY_LIST.add("venezuela");
        COUNTRY_LIST.add("vietnam");
        COUNTRY_LIST.add("virgin islands, british");
        COUNTRY_LIST.add("virgin islands, u.s.");
        COUNTRY_LIST.add("wallis and futuna");
        COUNTRY_LIST.add("yemen");
        COUNTRY_LIST.add("zambia");
        COUNTRY_LIST.add("zimbabwe");

        CONFERENCE_LIST = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(getTempFile("conferences.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;

            while ((line = br.readLine()) != null) {
                CONFERENCE_LIST.add(line);
            }

        } catch (IOException e) {
            LOGGER.error("A problem occurred trying to load the conference list.", e);
        }
    }

    public static File getTempFile(String localFile) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File destination = null;
        try {
            destination = File.createTempFile("temp", Long.toString(System.nanoTime()));
            FileUtils.copyURLToFile(classLoader.getResource(localFile), destination);
        } catch (IOException e) {
            LOGGER.error("Not able to create temp file", e);
        }
        return destination;
    }

}
