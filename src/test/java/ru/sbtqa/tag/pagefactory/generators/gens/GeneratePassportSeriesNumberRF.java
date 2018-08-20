package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GeneratePassportSeriesNumberRF implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
        return RandomStringUtils.randomNumeric(2) + " "
                + RandomStringUtils.randomNumeric(2) + " "
                + RandomStringUtils.randomNumeric(6);
    }

}
