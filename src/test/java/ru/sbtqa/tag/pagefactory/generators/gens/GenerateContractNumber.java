package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateContractNumber implements GeneratorInterface {

    @Override
    public String generate(String... vars) {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
