package ru.sbtqa.tag.pagefactory.generators.gens;


import ru.sbtqa.tag.pagefactory.generators.lib.XlsParser;

/**
 * Возвращает одно из слов на вкладке Word
 * Слова составлены из стабильно распозноваемых Tesseract.dll символов {п,н,с,м,х,р,ф,б}
 * и гласных {а,е,и,у,ю}
 * Пример вызова (в Excel): generate:Word
 */
public class GenerateWord implements GeneratorInterface {

    @Override
    public String generate(String... vars) throws Exception {
        XlsParser parser = new XlsParser();
        return parser.getRandomRow(GeneratorFactory.namesStore, 3);
    }
}
