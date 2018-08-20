package ru.sbtqa.tag.pagefactory.generators.gens;


import ru.sbtqa.tag.pagefactory.generators.lib.XlsParser;

public class GenerateName implements GeneratorInterface {

    private static char[] chardictionary = {'й','ц','у','к','е','н','г','ш','щ','з','х','ъ','ф','ы',
            'в','а','п','р','о','л','д','ж','э','я','ч','с','м','и','т','ь','б','ю'};

    @Override
    public String generate(String... vars) {
//        return RandomStringUtils.random(1, chardictionary).toUpperCase() +
//                RandomStringUtils.random(8, chardictionary).toLowerCase();
        XlsParser parser = new XlsParser();
        return parser.getRandomRow(GeneratorFactory.namesStore, 1);
    }
}
