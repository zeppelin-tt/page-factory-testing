package ru.sbtqa.tag.pagefactory.utils;

import ru.sbtqa.tag.pagefactory.generators.gens.GenerateNumeric;

import java.math.BigDecimal;

public class GenerateHelper {

    public static BigDecimal getRndNumeric(int precision, int scale) {
        return new BigDecimal(new GenerateNumeric().generate(String.valueOf(precision - scale))
                .concat(".").concat(new GenerateNumeric().generate(String.valueOf(scale))));
    }
}
