package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateOGRN implements GeneratorInterface {

    private static char[] chardictionary13 = {'1','2','3','5'};
    private static char[] chardictionary15 = {'3','4'};

    @Override
    public String generate(String... args) {
        // 13 - ОГРН
        // 15 - ОГРНИП
        // Последняя цифра контрольная сумма.
        int len = -1;
        try{ len = Integer.parseInt(args[0]);}
        catch (Exception e){}
        if ((len != 13) && (len != 15))
            len = 13;
        int div = len == 13 ? 11 : 13;
        String ogrn = "";
        long checkSum;
        do  {
            // Неизвестна логика обработки если контрольная сумма больше или равна 10.
            // На разных сайтах разный результат.
            // Поэтому просто пропускаем подобный исход.
            ogrn = RandomStringUtils.randomNumeric(len - 1);
            checkSum = Long.parseLong(ogrn) % div;
        } while (checkSum >= 10);
        return ogrn + checkSum;
    }
}
