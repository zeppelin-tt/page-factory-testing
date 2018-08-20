package ru.sbtqa.tag.pagefactory.generators.gens;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class GenerateDateAgeInYears implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
        int yearsMinLimit = Integer.parseInt(args[0].trim())*366;
        int yearsMaxLimit = Integer.parseInt(args[1].trim())*365;
        int rAge = new Random(System.currentTimeMillis()).
                nextInt(yearsMaxLimit - yearsMinLimit) + yearsMinLimit; // age range years
        String rDate = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -rAge);
        rDate = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
        return rDate;
    }

}
