package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateString implements GeneratorInterface {

    private static char[] chardictionary = {'й','ц','у','к','е','н','г','ш','щ','з','х','ъ','ф','ы',
            'в','а','п','р','о','л','д','ж','э','я','ч','с','м','и','т','ь','б','ю'};

    @Override
    public String generate(String... vars) {
        return RandomStringUtils.random(Integer.parseInt(vars[0].trim()), chardictionary).toLowerCase();
    }
}
