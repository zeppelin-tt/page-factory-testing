package ru.sbtqa.tag.pagefactory.generators.gens;

import java.util.Random;


public class GenerateCarType implements GeneratorInterface {

    private static String[] car_dictionary = {"ВАЗ", "ЗАЗ", "Лада", "Волга", "BMW", "AUDI", "MB",
            "Mitsubishi", "Opel", "ГАЗ", "TOYOTA", "MAN"};

    @Override
    public String generate(String... vars) {
        return car_dictionary[new Random(System.currentTimeMillis()).nextInt(12)];
    }
}
