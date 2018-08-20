package ru.sbtqa.tag.pagefactory.generators.gens;


import static ru.sbtqa.tag.pagefactory.generators.gens.DateManager.FindDate;

public class GenerateDate implements GeneratorInterface {

    //private static String dictionary[] = {"Случайно", "Завтра", "Сегодня", "Вчера"};

    @Override
    public String generate(String ...args) throws Exception {
        return FindDate(args[0].toLowerCase().trim().replace(" ", ""));
    }

}
