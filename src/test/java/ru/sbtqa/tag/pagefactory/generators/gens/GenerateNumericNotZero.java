package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateNumericNotZero implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
        String result = "0";
        while (Integer.parseInt(result) == 0)
            result = RandomStringUtils.randomNumeric(Integer.parseInt(args[0]));
        return result;
    }
}
