/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;


public class GenerateFluentNumeric implements GeneratorInterface {
    
    @Override
    public String generate(String ...args) {
        Integer rand = ThreadLocalRandom.current().nextInt(Integer.parseInt(args[0].trim()), Integer.parseInt(args[1].trim()));
        return RandomStringUtils.randomNumeric(rand);
    }
    
}
