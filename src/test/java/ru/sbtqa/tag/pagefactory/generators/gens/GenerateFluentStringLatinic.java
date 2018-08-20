package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;


public class GenerateFluentStringLatinic implements GeneratorInterface {

    private static char[] charLatinDictionary = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h',
        'j','k','l','z','x','c','v','b','n','m'};
    
    @Override
    public String generate(String... vars) {
        Integer rand = ThreadLocalRandom.current().nextInt(Integer.parseInt(vars[0].trim()), Integer.parseInt(vars[1].trim()));
        return RandomStringUtils.random(rand, charLatinDictionary).toLowerCase();
    }
}