package ru.sbtqa.tag.pagefactory.pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.extensions.DriverExtension;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateName;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSecondname;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSurname;
import ru.sbtqa.tag.pagefactory.utils.Storage;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.sbtqa.tag.pagefactory.utils.GenerateHelper.getRndNumeric;
import static ru.sbtqa.tag.pagefactory.utils.Storage.StashKeys.*;
import static ru.sbtqa.tag.pagefactory.utils.ValidateHelper.validateAccNum;
import static ru.sbtqa.tag.pagefactory.utils.ValidateHelper.validateGridField;
import static ru.sbtqa.tag.pagefactory.utils.action.ActionHelper.getNameByReduction;

@PageEntry(title = "Главная страница процессинга")
public class ProcessingPage extends AnyPage {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ProcessingPage.class);

    @FindBy(xpath = "//select[@name='selectAction']")
    @ElementTitle(value = "Тип операции")
    protected WebElement selectActionType;

    @FindBy(xpath = "//input[@name='lastname']")
    @ElementTitle(value = "Фамилия")
    protected WebElement lastname;

    @FindBy(xpath = "//input[@name='firstname']")
    @ElementTitle(value = "Имя")
    protected WebElement firstname;

    @FindBy(xpath = "//input[@name='secondname']")
    @ElementTitle(value = "Отчество")
    protected WebElement secondname;

    @FindBy(xpath = "//input[@name='accnum']")
    @ElementTitle(value = "Номер счёта")
    protected WebElement accnum;

    @FindBy(xpath = "//input[@name='resources']")
    @ElementTitle(value = "Сумма рублей")
    protected WebElement resources;

    @FindBy(xpath = "//input[@name='second_accnum']")
    @ElementTitle(value = "Номер счёта получателя")
    protected WebElement secondAccnum;

    @FindBy(xpath = "//button[@class='btn btn-primary']")
    @ElementTitle(value = "Выполнить")
    protected WebElement successBtn;

    @FindBy(xpath = "//button[contains(text(),'Скрыть таблицу')]")
    @ElementTitle(value = "Скрыть таблицу")
    protected WebElement closeTable;

    @FindBy(xpath = "//button[contains(text(),'Открыть таблицу')]")
    @ElementTitle(value = "Открыть таблицу")
    protected WebElement openTable;

    @FindBy(xpath = "//button[contains(text(),'Показать закрытые')]")
    @ElementTitle(value = "Показать закрытые")
    protected WebElement openClosed;

    @FindBy(xpath = "//button[contains(text(),'Скрыть закрытые')]")
    @ElementTitle(value = "Скрыть закрытые")
    protected WebElement closeClosed;

    @FindBy(xpath = "//table[@class='table table-bordered']")
    @ElementTitle(value = "Таблица")
    protected WebElement table;

    @FindBy(xpath = "//a[@name='Prev']")
    @ElementTitle(value = "Предыдущая страница")
    protected WebElement prevBtn;

    @FindBy(xpath = "//a[@name='Next']")
    @ElementTitle(value = "Следующая страница")
    protected WebElement nextBtn;

    public ProcessingPage() {
        PageFactory.initElements(
                new HtmlElementDecorator(new HtmlElementLocatorFactory(PageFactory.getDriver())), this);
    }

    @ActionTitle("выбирает тип операции")
    public void chooseAction(String action) {
        select(selectActionType, action);
        switch (action) {
            case Storage.ActionsInfinitive.CREATE_FULL:
                DriverExtension.waitUntilElementPresent(lastname, TIME_OUT);
                break;
            case Storage.ActionsInfinitive.BLOCK_FULL:
            case Storage.ActionsInfinitive.CLOSE_FULL:
                DriverExtension.waitUntilElementPresent(accnum, TIME_OUT);
                break;
            case Storage.ActionsInfinitive.REFILL_FULL:
            case Storage.ActionsInfinitive.RELIEF_FULL:
                DriverExtension.waitUntilElementPresent(resources, TIME_OUT);
                break;
            case Storage.ActionsInfinitive.TRANSFER_TO:
                DriverExtension.waitUntilElementPresent(secondAccnum, TIME_OUT);
                break;
            default:
                throw new AutotestError("Такого типа операции не существует!");
        }
    }

    @ActionTitle("нажимает")
    public void clickElement(String element) throws PageException {
        this.getElementByTitle(element).click();
    }

    @ActionTitle("открывает новый счёт")
    public void createAccount() {
        select(selectActionType, Storage.ActionsInfinitive.CREATE_FULL);
        String lastNameStr = new GenerateSurname().generate();
        String firstNameStr = new GenerateName().generate();
        String secondNameStr = new GenerateSecondname().generate();
        fillField(lastname, lastNameStr);
        fillField(firstname, firstNameStr);
        fillField(secondname, secondNameStr);
        Stash.put(LAST_NAME, lastNameStr);
        Stash.put(FIRST_NAME, firstNameStr);
        Stash.put(SECOND_NAME, secondNameStr);
        successBtn.click();
        LOG.info("Создан пользователь: ".concat(lastNameStr).concat(" ").concat(firstNameStr).concat(" ").concat(secondNameStr));
    }

    @ActionTitle("получает грид")
    public void click() {
        Map<String, List<String>> mapGrid = gridBuilder();
        LOG.info(String.valueOf(mapGrid));
    }

    @ActionTitle("проверяет значение типа операции")
    public void checkSelectValues(String targetValue) {
        String actualReduction = selectActionType.getAttribute("value");
        String actualNameByReduction = getNameByReduction(actualReduction);
        Assert.assertTrue("Значение ".concat(actualReduction).concat(" != ").concat(targetValue), targetValue.equals(actualNameByReduction));
    }

    @ActionTitle("проверяет корректность значений первой страницы таблицы")
    public void checkGridValues() {
        Map<String, List<String>> grid = gridBuilder();
        validateGrid(grid);
        Stash.put(GRID, grid);
    }

    @ActionTitle("сохраняет в стэш актуальную таблицу")
    public void saveGridIntoStash(String targetValue) {
        Stash.put(GRID, gridBuilder());
    }

    @ActionTitle("проверяет добавление новой строки в таблицу")
    public void checkGridAfterAddingAcc() {
        Map<String, List<String>> lastGrid = Stash.getValue(GRID);
        String initials = ((String) Stash.getValue(LAST_NAME))
                .concat(" ").concat(Stash.getValue(FIRST_NAME))
                .concat(" ").concat(Stash.getValue(SECOND_NAME));
        Map<String, List<String>> actualGrid = gridBuilder();
        Map<String, String> addingRow = getLastRow(actualGrid);
        Assert.assertTrue("Поле ФИО некорректно добавилось: ".concat(initials).concat(" != ").concat(addingRow.get(Storage.Columns.INITIALS)),
                addingRow.get(Storage.Columns.INITIALS).equals(initials));
        Assert.assertTrue("Предыдущее поле не находится на второй позиции: ".concat(actualGrid.get(Storage.Columns.INITIALS).get(1))
                        .concat(" != ").concat(lastGrid.get(Storage.Columns.INITIALS).get(0)),
                actualGrid.get(Storage.Columns.INITIALS).get(1).equals(lastGrid.get(Storage.Columns.INITIALS).get(0)));
        Stash.put(ADDING_ROW, addingRow);
    }

    @ActionTitle("проверяет корректность добавления поля")
    public void checkNewAccRow(String columnName) {
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        Assert.assertTrue("В добавленном аккаунте некорректено поле: ".concat(columnName), validateAccNum(rowMap.get(columnName)));
    }

    @ActionTitle("проверяет проверяет значение поля")
    public void checkNewAccValue(String columnName, String expectedResult) {
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        String actualValue = rowMap.get(columnName);
        Assert.assertTrue("В добавленном аккаунте некорректно поле: "
                        .concat(columnName).concat(" ( ").concat(actualValue)
                        .concat(" != ").concat(expectedResult),
                expectedResult.equals(actualValue));
    }

    @ActionTitle("проверяет равенство времени создания и последней операции")
    public void checkNewTimes() {
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Storage.Constants.TIME_FORMAT);
        LocalDateTime lastOpTime = LocalDateTime.parse(rowMap.get("Время последней операции"), formatter);
        LocalDateTime createTime = LocalDateTime.parse(rowMap.get("Время создания счёта"), formatter);
        LocalDateTime timeNow = LocalDateTime.now();
        Assert.assertTrue("Время операций создания и последней операции не равны.", lastOpTime.equals(createTime));
        Assert.assertTrue("Расхождение актуального Времени (".concat(timeNow.toString()).concat(" и Времени создания (").concat(createTime.toString()).concat(")"),
                timeNow.isAfter(createTime.minusMinutes(3)) && timeNow.isAfter(createTime));
    }

    @ActionTitle("проверяет пополнение счёта")
    public void checkRefill() {
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        BigDecimal sum = refillRndNumeric(rowMap.get(Storage.Columns.ACC_NUM), 5, 2);
        Map<String, String> actualRow = getLastRow();
        BigDecimal actualBalance = new BigDecimal(actualRow.get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс не соответствует добавленному: ".concat(sum.toString()).concat(" != ").concat(actualBalance.toString()),
                sum.compareTo(actualBalance) == 0);
        String actualLastAction = actualRow.get(Storage.Columns.LAST_ACTION);
        Assert.assertTrue("Последняя операция некорректна: ".concat(actualLastAction), Storage.Actions.REFILL.equals(actualLastAction));
        String alertText = String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.REFILL_FULL);
        Assert.assertTrue("Нет алерта с текстом: ".concat(alertText), checkElementWithTextIsPresentBool(alertText));
        BigDecimal leftToLimitPoint = Storage.Constants.LIMIT_MONEY.subtract(sum);
        fillField(resources, leftToLimitPoint.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.TOO_MORE_MONEY_ALERT),
                checkElementWithTextIsPresentBool(Storage.AlertText.TOO_MORE_MONEY_ALERT));
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс превышает максимально допустимую сумму: ".concat(actualBalance.toString()), sum.compareTo(actualBalance) == 0);
        fillField(resources, leftToLimitPoint.subtract(Storage.Constants.MIN_MONEY).toString());
        successBtn.click();
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс не верен при максимальном граничном значении",
                Storage.Constants.LIMIT_MONEY.subtract(Storage.Constants.MIN_MONEY).compareTo(actualBalance) == 0);
        Stash.put(ADDING_ROW, getLastRow());
    }

    @ActionTitle("проверяет Снятие денег со счёта")
    public void checkRelief() {
        chooseAction(Storage.ActionsInfinitive.RELIEF_FULL);
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        BigDecimal originalBalance = new BigDecimal(rowMap.get(Storage.Columns.BALANCE));
        BigDecimal withdrawalMoney = getRndNumeric(5, 2);
        fillField(accnum, rowMap.get(Storage.Columns.ACC_NUM));
        fillField(resources, withdrawalMoney.toString());
        successBtn.click();
        Map<String, String> actualRow = getLastRow();
        BigDecimal actualBalance = new BigDecimal(actualRow.get(Storage.Columns.BALANCE));
        Assert.assertTrue("После снятия баланс некорректен: ".concat(originalBalance.toString()).concat(" - ")
                        .concat(withdrawalMoney.toString()).concat(" != ").concat(actualBalance.toString()),
                originalBalance.subtract(withdrawalMoney).compareTo(actualBalance) == 0);
        String actualLastAction = actualRow.get(Storage.Columns.LAST_ACTION);
        Assert.assertTrue("Последняя операция некорректна: ".concat(actualLastAction), Storage.Actions.RELIEF.equals(actualLastAction));
        String alertText = String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.RELIEF_FULL);
        Assert.assertTrue("Нет алерта с текстом: ".concat(alertText), checkElementWithTextIsPresentBool(alertText));
        fillField(resources, actualBalance.toString());
        successBtn.click();
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Некорректное снятие до нуля: ".concat(actualBalance.toString()), actualBalance.compareTo(BigDecimal.ZERO) == 0);
        fillField(resources, Storage.Constants.MIN_MONEY.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.NOT_ENOUGH_MONEY_ALERT),
                checkElementWithTextIsPresentBool(Storage.AlertText.NOT_ENOUGH_MONEY_ALERT));
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс изменился при перехождении порога нуля: ".concat(actualBalance.toString()), actualBalance.compareTo(BigDecimal.ZERO) == 0);
        Stash.put(ADDING_ROW, getLastRow());
    }


    @ActionTitle("проверяет перечисление денег другому клиенту")
    public void checkTransferTo() {
        chooseAction(Storage.ActionsInfinitive.TRANSFER_TO);
    }

    @ActionTitle("проверяет блокировку аккаунта")
    public void checkBlocking() {
        createAccount();
        String accNum = getLastRow().get(Storage.Columns.ACC_NUM);
        BigDecimal balance = refillRndNumeric(accNum, 6, 2);
        blockAcc(accNum);
        new WebDriverWait(PageFactory.getWebDriver(), TIME_OUT)
                .until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(table, accNum)));
        String alertText = String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.BLOCK_FULL);
        Assert.assertTrue("Нет алерта с текстом: ".concat(alertText), checkElementWithTextIsPresentBool(alertText));
        Assert.assertFalse("После блокировки счет не пропал из таблицы: ".concat(accNum),
                accNum.equals(getLastRow().get(Storage.Columns.ACC_NUM)));
        openClosed.click();
        Map<String, String> row = getLastRow();
        Assert.assertTrue("При просмотре заблокированных счетов нет актуального: ".concat(accNum),
                accNum.equals(row.get(Storage.Columns.ACC_NUM)));
        BigDecimal actualBalance = new BigDecimal(row.get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс изменился после блокировки: ".concat(actualBalance.toString()).concat(" != ").concat(balance.toString()),
                balance.compareTo(actualBalance) == 0);
        String lastAction = row.get(Storage.Columns.LAST_ACTION);
        Assert.assertTrue("Некорректная последняя операция: ".concat(lastAction),
                lastAction.equals(Storage.Actions.BLOCK));
        closeClosed.click();
    }

    @ActionTitle("проверяет закрытие аккаунта")
    public void checkClosing() {
        createAccount();
        String accNum = getLastRow().get(Storage.Columns.ACC_NUM);
        refillRndNumeric(accNum, 6, 2);
        closeAcc(accNum);
        new WebDriverWait(PageFactory.getWebDriver(), TIME_OUT)
                .until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(table, accNum)));
        String alertText = String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.CLOSE_FULL);
        Assert.assertTrue("Нет алерта с текстом: ".concat(alertText), checkElementWithTextIsPresentBool(alertText));
        Assert.assertFalse("После закрытия счет не пропал из таблицы: ".concat(accNum),
                accNum.equals(getLastRow().get(Storage.Columns.ACC_NUM)));
        openClosed.click();
        Map<String, String> row = getLastRow();
        Assert.assertTrue("При просмотре закрытых счетов нет актуального: ".concat(accNum),
                accNum.equals(row.get(Storage.Columns.ACC_NUM)));
        BigDecimal actualBalance = new BigDecimal(row.get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс не обнулился после закрытия", BigDecimal.ZERO.compareTo(actualBalance) == 0);
        String lastAction = row.get(Storage.Columns.LAST_ACTION);
        Assert.assertTrue("Некорректная последняя операция: ".concat(lastAction),
                lastAction.equals(Storage.Actions.CLOSE));
        closeClosed.click();
    }

    private BigDecimal refillRndNumeric(String acc, int precision, int scale) {
        select(selectActionType, Storage.ActionsInfinitive.REFILL_FULL);
        BigDecimal rndNumeric = getRndNumeric(precision, scale);
        fillField(accnum, acc);
        fillField(resources, rndNumeric.toString());
        successBtn.click();
        return rndNumeric;
    }

    private void blockAcc(String acc) {
        chooseAction(Storage.ActionsInfinitive.BLOCK_FULL);
        fillField(accnum, acc);
        successBtn.click();
    }

    private void closeAcc(String acc) {
        chooseAction(Storage.ActionsInfinitive.CLOSE_FULL);
        fillField(accnum, acc);
        successBtn.click();
    }

    private void validateGrid(Map<String, List<String>> grid) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Storage.Constants.TIME_FORMAT);
        grid.forEach((key, value) -> {
            AtomicInteger it = new AtomicInteger();
            value.forEach(e -> {
                Assert.assertTrue("Некоррекнтно поле: ".concat(key).concat(", строка: ").concat(String.valueOf(it.get())).concat(" = ").concat(e),
                        validateGridField(e, key));
                if (key.equals(Storage.Columns.LAST_ACTION)) {
                    String lastAction = grid.get(key).get(it.get());
                    LocalDateTime lastOpTime = LocalDateTime.parse(grid.get(Storage.Columns.LAST_ACTION_TIME).get(it.get()), formatter);
                    LocalDateTime createTime = LocalDateTime.parse(grid.get(Storage.Columns.CREATE_TIME).get(it.get()), formatter);
                    if (lastAction.equals(Storage.Actions.CREATE)) {
                        Assert.assertTrue("Время последней операции: \"".concat(key).concat("\" некорректно. Номер аккаунта: ")
                                        .concat(grid.get(Storage.Columns.ACC_NUM).get(it.get())),
                                lastOpTime.equals(createTime));
                    } else {
                        Assert.assertTrue("Время последней операции: \"".concat(key).concat("\" некорректно. Номер аккаунта: ")
                                        .concat(grid.get(Storage.Columns.ACC_NUM).get(it.get())),
                                lastOpTime.isAfter(createTime));
                    }
                }
                it.getAndIncrement();
            });
        });
    }

    private Map<String, String> getLastRow() {
        return getLastRow(gridBuilder());
    }

    private Map<String, String> getLastRow(Map<String, List<String>> grid) {
        return grid.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0)));
    }

    private Map<String, List<String>> gridBuilder() {
        DriverExtension.waitUntilElementPresent(table, TIME_OUT);
        List<String> listColumnNames = getElementsByXPath("//table[@class='table table-bordered']/thead/tr/th")
                .stream().map(WebElement::getText).collect(Collectors.toList());
        List<String> listColumnFields;
        Map<String, List<String>> mapGrid = new HashMap<>();
        for (int i = 0; i < listColumnNames.size(); i++) {
            listColumnFields = getElementsByXPath(String.format("//table[@class='table table-bordered']/tbody/tr/td[%s]", i + 1))
                    .stream().map(WebElement::getText).collect(Collectors.toList());
            mapGrid.put(listColumnNames.get(i), listColumnFields);
        }
        return mapGrid;
    }

}
