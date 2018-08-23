package ru.sbtqa.tag.pagefactory.utils;

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

    public static class Constants {
        public static final String TIME_FORMAT = "yyyy.MM.dd HH:mm:ss";
    }

}
