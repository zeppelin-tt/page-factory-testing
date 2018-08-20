package ru.sbtqa.tag.pagefactory.generators.gens;

import org.apache.commons.lang3.RandomStringUtils;


public class GenerateINN implements GeneratorInterface {

    @Override
    public String generate(String ...args) {
        int size = 12; // инн физического лица
        try {
            size = Integer.parseInt(args[0]);
        } catch (Exception e){
        }
        if ((size != 10) && (size != 12)) { //10 это инн ЮЛ
            size = 12; // инн физического лица
        }

        String inn =  RandomStringUtils.randomNumeric(size);
        while (!isValidINN(inn)){
            inn =  RandomStringUtils.randomNumeric(size);
        }

//        System.out.println("generated INN(size " + size + ") - " + inn + " status " + isValidINN(inn));
        return inn;
    }

    public boolean isValidINN(String innNumber) {
        int inn[] =  new int[innNumber.length()];
            for (int i = 0; i < innNumber.length(); i++) {
                inn[i] = Character.digit(innNumber.charAt(i), 10);
            }

        if ( inn.length == 10 ){
            return inn[9] == ((2*inn[0] + 4*inn[1] + 10*inn[2] +
                    3*inn[3] + 5*inn[4] + 9*inn[5] + 4*inn[6] + 6*inn[7] +  8*inn[8])%11)%10;
        }
        else if ( inn.length == 12 )        {
            return inn[10] == (((7*inn[0] + 2*inn[1] + 4*inn[2] +
                            10*inn[3] + 3*inn[4] + 5*inn[5] +
                            9*inn[6] + 4*inn[7] + 6*inn[8] +
                            8*inn[9]
            ) % 11) % 10) && inn[11] == (((
                    3*inn[0] +  7*inn[1] + 2*inn[2] +
                            4*inn[3] + 10*inn[4] + 3*inn[5] +
                            5*inn[6] +  9*inn[7] + 4*inn[8] +
                            6*inn[9] +  8*inn[10]
            ) % 11) % 10);
        }
        return false;
    }

}
