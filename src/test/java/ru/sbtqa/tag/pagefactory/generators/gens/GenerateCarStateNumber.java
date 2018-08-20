package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateCarStateNumber implements GeneratorInterface {

    private static char charDictionary[] = {'У','К','Е','Н','Х','В','А','Р','О','С','М','И','Т'};

    @Override
    public String generate(String... vars) {
        return RandomStringUtils.random(1, charDictionary) +
                RandomStringUtils.randomNumeric(3) +
                RandomStringUtils.random(2, charDictionary) +
                RandomStringUtils.randomNumeric(2);
    }
}
