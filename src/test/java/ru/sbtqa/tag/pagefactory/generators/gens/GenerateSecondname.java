package ru.sbtqa.tag.pagefactory.generators.gens;

import ru.sbtqa.tag.pagefactory.generators.lib.XlsParser;

import java.text.ParseException;


public class GenerateSecondname implements GeneratorInterface {

    @Override
    public String generate(String... vars) {
        XlsParser parser = new XlsParser();
//        GeneratorFactory GF = new GeneratorFactory()
        return parser.getRandomRow(GeneratorFactory.namesStore, 2);
    }
}
