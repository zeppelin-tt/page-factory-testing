package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


/*
    Позволяет определять сосбтвенный набор символов для создания случайной строки.
    1-ый параметр длина выходной строки.
    2-ой набор символов.
*/
public class GenerateCustomString implements GeneratorInterface {

    @Override
    public String generate(String... vars) throws Exception {
        return RandomStringUtils.random(Integer.parseInt(vars[0].trim()), vars[1].toCharArray());
    }
}
