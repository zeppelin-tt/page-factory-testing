package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateNumeric implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
            return RandomStringUtils.randomNumeric(Integer.parseInt(args[0]));
    }

}
