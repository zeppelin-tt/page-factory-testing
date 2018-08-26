package ru.sbtqa.tag.pagefactory.utils;

import java.math.BigDecimal;

public class Storage {

    public static class StashKeys {
        public static final String LAST_NAME = "lastName";
        public static final String FIRST_NAME = "firstName";
        public static final String SECOND_NAME = "secondName";
        public static final String GRID = "actualGrid";
        public static final String ADDING_ROW = "addingRow";
    }

    public static class Columns {
        public static final String ACC_NUM = "Номер счёта";
        public static final String INITIALS = "ФИО";
        public static final String BALANCE = "Баланс";
        public static final String LAST_ACTION = "Последняя операция";
        public static final String LAST_ACTION_TIME = "Время последней операции";
        public static final String CREATE_TIME = "Время создания счёта";
    }

    public static class Actions {
        public static final String REFILL = "Пополнение";
        public static final String RELIEF = "Снятие";
        public static final String CREATE = "Создание";
        public static final String CLOSE = "Закрытие";
        public static final String BLOCK = "Блокировка";
    }

    public static class ActionsInfinitive {
        public static final String REFILL_FULL = "Пополнить счёт";
        public static final String RELIEF_FULL = "Снять деньги";
        public static final String CREATE_FULL = "Создать счёт";
        public static final String CLOSE_FULL = "Закрыть счёт";
        public static final String BLOCK_FULL = "Заблокировать счёт";
        public static final String TRANSFER_TO = "Перечислить клиенту";
    }

    public static class Constants {
        public static final String TIME_FORMAT = "yyyy.MM.dd HH:mm:ss";
        public static final BigDecimal LIMIT_MONEY = new BigDecimal("10000000");
        public static final BigDecimal MIN_MONEY = new BigDecimal("0.01");
    }

    public static class AlertText {
        public static final String SUCCESS_ACTION_ALERT = "Операция %s выполнена успешно!";
        public static final String TOO_MORE_MONEY_ALERT = "У вас больше 10 миллионов! Это слишком много!";
        public static final String NOT_ENOUGH_MONEY_ALERT = "Баланс не может быть отрицательным!";
    }

}
