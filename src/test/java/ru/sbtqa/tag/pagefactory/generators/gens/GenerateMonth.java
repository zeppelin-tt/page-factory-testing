package ru.sbtqa.tag.pagefactory.generators.gens;


import static ru.sbtqa.tag.pagefactory.generators.gens.DateManager.FindMonth;

public class GenerateMonth implements GeneratorInterface {

    @Override
    public String generate(String ...args) throws Exception {
        return FindMonth(args[0].toLowerCase().trim().replace(" ", ""));
    }

}
