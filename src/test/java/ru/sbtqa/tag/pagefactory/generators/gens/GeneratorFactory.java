package ru.sbtqa.tag.pagefactory.generators.gens;


import java.util.HashMap;


public class GeneratorFactory {
    public static String namesStore = "data/namesStore.xlsx";

    private static HashMap<String, GeneratorInterface> generatorsMap = new HashMap();

    static{
        generatorsMap.put("GenerateSurname", new GenerateSurname());
        generatorsMap.put("GenerateSecondname", new GenerateSecondname());
        generatorsMap.put("GenerateName", new GenerateName());
        generatorsMap.put("GenerateWord", new GenerateWord());
        generatorsMap.put("GenerateDate", new GenerateDate());
        generatorsMap.put("GenerateMonth", new GenerateMonth());
        generatorsMap.put("GenerateNumeric", new GenerateNumeric());
        generatorsMap.put("GenerateNumericNotZero", new GenerateNumericNotZero());
        generatorsMap.put("GeneratePassportCode", new GeneratePassportCode());
        generatorsMap.put("GeneratePassportSeriesNumberRF", new GeneratePassportSeriesNumberRF());
        generatorsMap.put("GenerateDateAgeInYears", new GenerateDateAgeInYears());
        generatorsMap.put("GenerateCarStateNumber", new GenerateCarStateNumber());
        generatorsMap.put("GenerateCarType", new GenerateCarType());
        generatorsMap.put("GenerateContractNumber", new GenerateContractNumber());
        generatorsMap.put("GenerateString", new GenerateString());
        generatorsMap.put("GenerateCustomString", new GenerateCustomString());
        generatorsMap.put("GenerateINN", new GenerateINN());
        generatorsMap.put("GenerateOGRN", new GenerateOGRN());
        generatorsMap.put("GenerateSNILS", new GenerateSNILS());
        generatorsMap.put("GenerateFluentStringCyrillic", new GenerateFluentStringCyrillic());
        generatorsMap.put("GenerateFluentStringLatinic", new GenerateFluentStringLatinic());
        generatorsMap.put("GenerateFluentStringAll", new GenerateFluentStringAll());
        generatorsMap.put("GenerateFluentStringLettersNumeric", new GenerateFluentStringLettersNumeric());
    }



    public GeneratorInterface getGenerator(String command) throws GeneratorInitializationException {
        GeneratorInterface generator;
        try{
                generator = generatorsMap.get("Generate" + command);
        } catch (Exception e) {
            throw new GeneratorInitializationException("Cant find generator class - " + command, e);
        }
        return generator;
    }
}