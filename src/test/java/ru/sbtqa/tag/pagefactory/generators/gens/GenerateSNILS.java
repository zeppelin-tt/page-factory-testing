package ru.sbtqa.tag.pagefactory.generators.gens;

import java.text.DecimalFormat;
import java.util.Random;


public class GenerateSNILS implements GeneratorInterface {

    @Override
    public String generate(String... args) {
        // Формат СНИЛС: XXX-XXX-XXX YY
        String result = "";
        Random ran = new Random();
        // Генерация XXX-XXX-XXX
        Integer[] XXX = new Integer[3];
        XXX[0] = 2 + ran.nextInt(998);
        XXX[1] = 2 + ran.nextInt(998);
        XXX[2] = 2 + ran.nextInt(998);
        int position = 9;
        Integer acc = 0;
        DecimalFormat dc = new DecimalFormat("000");
        // Расчет контрльной суммы.
        for (int i = 0; i < XXX.length; i++) {
            String str = dc.format(XXX[i]);
            result += str;
            if (i < XXX.length - 1)
                result += "-";
            char[] nums = str.toCharArray();
            for (char num : nums) acc += Integer.parseInt("" + num) * position--;
        }
        result += " ";
        dc = new DecimalFormat("00");
        // Определение YY по контрольной сумме.
        if (acc > 101)
            acc = acc % 101;
        if (acc == 100 || acc == 101)
            result += "00";
        else result += dc.format(acc);
        return result;
    }
}
