/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;


public class GenerateFluentStringAll implements GeneratorInterface {

    private static char[] charAllDictionary = {'й','ц','у','к','е','ё','н','г','ш','щ','з','х','ъ','ф','ы',
        'в','а','п','р','о','л','д','ж','э','я','ч','с','м','и','т','ь','б','ю','q','w','e','r','t','y','u',
        'i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','1','2','3','4','5','6',
        '7','8','9','0','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X',
        'C','V','B','N','M','Й','Ц','У','К','Е','Н','Г','Ш','Щ','З','Х','Ъ','Ё','Ф','Ы','В','А','П','Р','О',
        'Л','Д','Ж','Э','Я','Ч','С','М','И','Т','Ь','Б','Ю','!','"','№',';','%',':','?','*','(',')','_','-',
        '=','+',' ',' ',' ',' ',' ',' ',' ',' ','.',',','~','`','@','#','$','^','&','[',']','{','}','<','>'};
    
    @Override
    public String generate(String... vars) {
        Integer rand = ThreadLocalRandom.current().nextInt(Integer.parseInt(vars[0].trim()), Integer.parseInt(vars[1].trim()));
        return RandomStringUtils.random(rand, charAllDictionary);
    }
    
}
