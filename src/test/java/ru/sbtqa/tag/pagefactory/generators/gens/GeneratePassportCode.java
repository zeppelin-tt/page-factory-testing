package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GeneratePassportCode implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
        return RandomStringUtils.randomNumeric(3) + "-"
                + RandomStringUtils.randomNumeric(3);
    }

}
