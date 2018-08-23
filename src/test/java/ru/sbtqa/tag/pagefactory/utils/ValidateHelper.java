package ru.sbtqa.tag.pagefactory.utils;

import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateHelper {

    public static boolean validateGridField(String field, String column) {
        boolean result;
        switch (column) {
            case "Номер счёта":
                result = validateAccNum(field);
                break;
            case "ФИО":
                result = validateInitials(field);
                break;
            case "Баланс":
                result = validateBalance(field);
                break;
            case "Последняя операция":
                result = validateAction(field);
                break;
            case "Время последней операции":
            case "Время создания счёта":
                result = validateDateTime(field);
                break;
            default:
                throw new AutotestError("В таблице нет столбца: ".concat(column));
        }
        return result;
    }

    public static boolean validateAccNum(String accNum) {
        return compliesRegEx(accNum, "\\d{16}");
    }

    private static boolean validateInitials(String accNum) {
        return compliesRegEx(accNum, "[A-ZА-Я][ёа-яa-z-]+\\s[A-ZА-Я][ёа-яa-z]+\\s[A-ZА-Я][ёа-яa-z-]+(\\s[A-ZА-Я][ёа-яa-z-]+)?");
    }

    private static boolean validateBalance(String accNum) {
        return compliesRegEx(accNum, "\\d{0,10}.\\d{2}");
    }

    private static boolean validateAction(String accNum) {
        return compliesRegEx(accNum, "(Пополнение|Снятие|Создание|Закрытие|Блокировка)");
    }

    private static boolean validateDateTime(String accNum) {
        return compliesRegEx(accNum, "[1-2][0-9][0-9][0-9].[0-1][0-9].[0-3][0-9]\\s[0-2][0-9]:[0-5][0-9]:[0-5][0-9]");
    }

    private static boolean compliesRegEx(String str, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
