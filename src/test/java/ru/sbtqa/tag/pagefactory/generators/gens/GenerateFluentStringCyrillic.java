package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;


public class GenerateFluentStringCyrillic implements GeneratorInterface {

    private static char[] charCyrillicDictionary = {'й','ц','у','к','е','ё','н','г','ш','щ','з','х','ъ','ф','ы',
        'в','а','п','р','о','л','д','ж','э','я','ч','с','м','и','т','ь','б','ю'};
    
    @Override
    public String generate(String... vars) {
        Integer rand = ThreadLocalRandom.current().nextInt(Integer.parseInt(vars[0].trim()), Integer.parseInt(vars[1].trim()));
        return RandomStringUtils.random(rand, charCyrillicDictionary).toLowerCase();
    }
}
