package ru.sbtqa.tag.pagefactory.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
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
        clearAndFill(lastname, lastNameStr);
        clearAndFill(firstname, firstNameStr);
        clearAndFill(secondname, secondNameStr);
        Stash.put(LAST_NAME, lastNameStr);
        Stash.put(FIRST_NAME, firstNameStr);
        Stash.put(SECOND_NAME, secondNameStr);
        successBtn.click();
        String initials = lastNameStr.concat(" ").concat(firstNameStr).concat(" ").concat(secondNameStr);
        driverWait.until(ExpectedConditions.textToBePresentInElement(table, initials));
        LOG.info("Создан аккаунт для пользователя: ".concat(initials));
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
        Map<String, String> addingRow = getGridRow(actualGrid, 0);
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
        clearAndFill(resources, leftToLimitPoint.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.TOO_MORE_MONEY_ALERT),
                checkElementWithTextIsPresentBool(Storage.AlertText.TOO_MORE_MONEY_ALERT));
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс превышает максимально допустимую сумму: ".concat(actualBalance.toString()), sum.compareTo(actualBalance) == 0);
        clearAndFill(resources, leftToLimitPoint.subtract(Storage.Constants.MIN_MONEY).toString());
        successBtn.click();
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        BigDecimal maxBalance = Storage.Constants.LIMIT_MONEY.subtract(Storage.Constants.MIN_MONEY);
        Assert.assertTrue("Баланс не верен при максимальном граничном значении: ".concat(maxBalance.toString()).concat(" != ").concat(actualBalance.toString()),
                maxBalance.compareTo(actualBalance) == 0);
        Stash.put(ADDING_ROW, getLastRow());
        LOG.info("Пополнение счёта корректно.");
    }

    @ActionTitle("проверяет Снятие денег со счёта")
    public void checkRelief() {
        chooseAction(Storage.ActionsInfinitive.RELIEF_FULL);
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        BigDecimal originalBalance = new BigDecimal(rowMap.get(Storage.Columns.BALANCE));
        BigDecimal withdrawalMoney = getRndNumeric(5, 2);
        clearAndFill(accnum, rowMap.get(Storage.Columns.ACC_NUM));
        clearAndFill(resources, withdrawalMoney.toString());
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
        clearAndFill(resources, actualBalance.toString());
        successBtn.click();
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Некорректное снятие до нуля: ".concat(actualBalance.toString()), actualBalance.compareTo(BigDecimal.ZERO) == 0);
        clearAndFill(resources, Storage.Constants.MIN_MONEY.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.NOT_ENOUGH_MONEY_ALERT),
                checkElementWithTextIsPresentBool(Storage.AlertText.NOT_ENOUGH_MONEY_ALERT));
        actualBalance = new BigDecimal(getLastRow().get(Storage.Columns.BALANCE));
        Assert.assertTrue("Баланс изменился при перехождении порога нуля: ".concat(actualBalance.toString()), actualBalance.compareTo(BigDecimal.ZERO) == 0);
        Stash.put(ADDING_ROW, getLastRow());
        LOG.info("Снятие денег со счёта корректно.");
    }


    @ActionTitle("проверяет перечисление денег другому клиенту")
    public void checkTransferTo() {
        createAccount();
        BigDecimal donorBalance = refillRndNumeric(getLastRow().get(Storage.Columns.ACC_NUM), 7, 2);
        createAccount();
        Map<String, String> donorAccRow = getGridRow(1);
        Map<String, String> recipientAccRow = getGridRow(0);
        chooseAction(Storage.ActionsInfinitive.TRANSFER_TO);
        clearAndFill(accnum, donorAccRow.get(Storage.Columns.ACC_NUM));
        clearAndFill(secondAccnum, recipientAccRow.get(Storage.Columns.ACC_NUM));
        BigDecimal transferMoney = getRndNumeric(6, 2);
        clearAndFill(resources, transferMoney.toString());
        successBtn.click();
        Map<String, List<String>> gridAfterChecking = gridBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Storage.Constants.TIME_FORMAT);
        LocalDateTime firstLastActionTime = LocalDateTime.parse(gridAfterChecking.get(Storage.Columns.LAST_ACTION_TIME).get(0), formatter);
        LocalDateTime secondLastActionTime = LocalDateTime.parse(gridAfterChecking.get(Storage.Columns.LAST_ACTION_TIME).get(1), formatter);
        Assert.assertTrue("Время последней операции у донора (".concat(donorAccRow.get(Storage.Columns.ACC_NUM))
                        .concat(") и реципиента (").concat(recipientAccRow.get(Storage.Columns.ACC_NUM)).concat(") отличаетя: ")
                        .concat(firstLastActionTime.toString()).concat(" != ").concat(secondLastActionTime.toString()),
                firstLastActionTime.equals(secondLastActionTime));
        LocalDateTime creationTime = LocalDateTime.parse(gridAfterChecking.get(Storage.Columns.CREATE_TIME).get(1), formatter);
        Assert.assertTrue("Время последней операции: (".concat(firstLastActionTime.toString())
                        .concat("меньше или равно времени создания: (").concat(creationTime.toString()).concat(")"),
                firstLastActionTime.isAfter(creationTime));
        Assert.assertTrue("Последняя операция реципиента не равна: ".concat(Storage.Actions.REFILL),
                Storage.Actions.REFILL.equals(gridAfterChecking.get(Storage.Columns.LAST_ACTION).get(0)));
        Assert.assertTrue("Последняя операция донора не равна: ".concat(Storage.Actions.RELIEF),
                Storage.Actions.RELIEF.equals(gridAfterChecking.get(Storage.Columns.LAST_ACTION).get(1)));
        Assert.assertTrue("Средства не перечислены на счет реципиента или перечислина неверная сумма.",
                transferMoney.compareTo(new BigDecimal(gridAfterChecking.get(Storage.Columns.BALANCE).get(0))) == 0);
        Assert.assertTrue("Средства не сняты со счета донора или снята неверная сумма.",
                donorBalance.subtract(transferMoney).compareTo(new BigDecimal(gridAfterChecking.get(Storage.Columns.BALANCE).get(1))) == 0);
        LOG.info("Перечисление денег другому клиенту корректно.");
    }

    @ActionTitle("проверяет исключения в перечислении денег другому клиенту")
    public void checkTransferExceptions() {
        createAccount();
        Map<String, String> activeAcc = getLastRow();
        String activeAccNum = activeAcc.get(Storage.Columns.ACC_NUM);
        BigDecimal activeBalance = refillRndNumeric(activeAccNum, 7, 2);
        BigDecimal rndMoneyTransfer = getRndNumeric(4, 2);

        createAccount();
        String accNumToBlock = getLastRow().get(Storage.Columns.ACC_NUM);
        blockAcc(accNumToBlock);
        chooseAction(Storage.ActionsInfinitive.TRANSFER_TO);
        clearAndFill(accnum, accNumToBlock);
        clearAndFill(secondAccnum, activeAccNum);
        clearAndFill(resources, rndMoneyTransfer.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.ACCOUNT_IS_BLOCKED),
                checkElementWithTextIsPresentBool(Storage.AlertText.ACCOUNT_IS_BLOCKED));
        clearAndFill(accnum, activeAccNum);
        clearAndFill(secondAccnum, accNumToBlock);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.RECIPIENT_ACCOUNT_IS_BLOCKED),
                checkElementWithTextIsPresentBool(Storage.AlertText.RECIPIENT_ACCOUNT_IS_BLOCKED));

        createAccount();
        String accNumToClose = getLastRow().get(Storage.Columns.ACC_NUM);
        closeAcc(accNumToClose);
        chooseAction(Storage.ActionsInfinitive.TRANSFER_TO);
        clearAndFill(accnum, accNumToClose);
        clearAndFill(secondAccnum, activeAccNum);
        clearAndFill(resources, rndMoneyTransfer.toString());
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.ACCOUNT_IS_CLOSED),
                checkElementWithTextIsPresentBool(Storage.AlertText.ACCOUNT_IS_CLOSED));
        clearAndFill(accnum, activeAccNum);
        clearAndFill(secondAccnum, accNumToClose);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.RECIPIENT_ACCOUNT_IS_CLOSED),
                checkElementWithTextIsPresentBool(Storage.AlertText.RECIPIENT_ACCOUNT_IS_CLOSED));

        String nonexistentAccNum = "";
        boolean alertDetected = false;
        for (int i = 0; i < 5; i++) {
            nonexistentAccNum = getRndNumeric(16).toString();
            clearAndFill(accnum, nonexistentAccNum);
            successBtn.click();
            if (checkElementWithTextIsPresentBool(Storage.AlertText.ACCOUNT_NOT_EXIST)) {
                alertDetected = true;
                break;
            }
        }
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.ACCOUNT_NOT_EXIST), alertDetected);

        clearAndFill(accnum, activeAccNum);
        clearAndFill(secondAccnum, nonexistentAccNum);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.RECIPIENT_ACCOUNT_NOT_EXIST),
                checkElementWithTextIsPresentBool(Storage.AlertText.RECIPIENT_ACCOUNT_NOT_EXIST));

        createAccount();
        Map<String, String> secondActiveAcc = getLastRow();
        String secondActiveAccNum = secondActiveAcc.get(Storage.Columns.ACC_NUM);
        chooseAction(Storage.ActionsInfinitive.TRANSFER_TO);
        clearAndFill(accnum, activeAccNum);
        clearAndFill(secondAccnum, secondActiveAccNum);
        clearAndFill(resources, Storage.Constants.STRING_ZERO);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.ACTION_WITH_ZERO),
                checkElementWithTextIsPresentBool(Storage.AlertText.ACTION_WITH_ZERO));

        String shortAccNum = getRndNumeric(15).toString();
        clearAndFill(accnum, shortAccNum);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.TOO_SHORT_ACC_NUM),
                checkElementWithTextIsPresentBool(Storage.AlertText.TOO_SHORT_ACC_NUM));
        clearAndFill(accnum, activeAccNum);
        clearAndFill(secondAccnum, shortAccNum);
        successBtn.click();
        Assert.assertTrue("Нет алерта с текстом: ".concat(Storage.AlertText.RECIPIENT_TOO_SHORT_ACC_NUM),
                checkElementWithTextIsPresentBool(Storage.AlertText.RECIPIENT_TOO_SHORT_ACC_NUM));

        openClosed.click();
        BigDecimal actualActiveBalance = new BigDecimal(getRowByAccNum(activeAccNum).get(Storage.Columns.BALANCE));
        Assert.assertTrue("Изменился баланс активного клиента с номером счёта (".concat(activeAccNum).concat(") : ")
                        .concat(actualActiveBalance.toString()).concat(" != ").concat(activeBalance.toString()),
                activeBalance.compareTo(actualActiveBalance) == 0);
        Assert.assertTrue("Баланс должен быть равен нулю в данном закрытом счёте : ".concat(accNumToClose),
                getRowByAccNum(accNumToClose).get(Storage.Columns.BALANCE).equals(Storage.Constants.STRING_ZERO));
        Assert.assertTrue("Баланс должен быть равен нулю в данном заблокированном счёте : ".concat(accNumToBlock),
                getRowByAccNum(accNumToBlock).get(Storage.Columns.BALANCE).equals(Storage.Constants.STRING_ZERO));
        Assert.assertTrue("Баланс должен быть равен нулю в данном счёте : ".concat(secondActiveAccNum),
                getRowByAccNum(secondActiveAccNum).get(Storage.Columns.BALANCE).equals(Storage.Constants.STRING_ZERO));
        closeClosed.click();
        LOG.info("Все исключения воспроизводятся корректно.");
    }


    @ActionTitle("проверяет блокировку аккаунта")
    public void checkBlocking() {
        createAccount();
        String accNum = getLastRow().get(Storage.Columns.ACC_NUM);
        BigDecimal balance = refillRndNumeric(accNum, 6, 2);
        blockAcc(accNum);
        driverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(table, accNum)));
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
        LOG.info("Блокировка аккаунта корректна.");
    }

    @ActionTitle("проверяет закрытие аккаунта")
    public void checkClosing() {
        createAccount();
        String accNum = getLastRow().get(Storage.Columns.ACC_NUM);
        refillRndNumeric(accNum, 6, 2);
        closeAcc(accNum);
        driverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(table, accNum)));
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
        LOG.info("Закрытие аккаунта корректно.");
    }

    @ActionTitle("проверяет пагинацию")
    public void checkPagination() {
        Map<String, List<String>> grid = gridBuilder();
        List<String> numList = grid.get(Storage.Columns.ACC_NUM);
        String extremeAccNum = numList.get(numList.size()-1);
        createAccount();
        List<String> numListAfter = gridBuilder().get(Storage.Columns.ACC_NUM);
        Assert.assertFalse("После добавления нового аккаунта, последний акааунт со страницы не пропал.",
                numListAfter.contains(extremeAccNum));
        nextBtn.click();
        Assert.assertTrue("На второй странице нет аккаунта: ".concat(extremeAccNum),
                extremeAccNum.equals(gridBuilder().get(Storage.Columns.ACC_NUM).get(0)));
        prevBtn.click();
        String lastCreatedInitials = ((String) Stash.getValue(LAST_NAME)).concat(" ")
                .concat(Stash.getValue(FIRST_NAME)).concat(" ").concat(Stash.getValue(SECOND_NAME));
        String topInitials = gridBuilder().get(Storage.Columns.INITIALS).get(0);
        Assert.assertTrue("Возврат на первую страницу через (Prev) некорректен: ".concat(topInitials).concat(" != ").concat(lastCreatedInitials),
                lastCreatedInitials.equals(topInitials));
        nextBtn.click();
        PageFactory.getWebDriver().navigate().refresh();
        Assert.assertTrue("Возврат на первую страницу через (refresh) некорректен.",
                lastCreatedInitials.equals(gridBuilder().get(Storage.Columns.INITIALS).get(0)));
        LOG.info("Пагинация корректна.");
    }

    @ActionTitle("проверяет закрытие таблицы")
    public void checkClosingTable() {
        closeTable.click();
        Assert.assertTrue("Таблица не закрылась.",
                PageFactory.getWebDriver().findElements(By.xpath("//table[@class='table table-bordered']")).size() == 0);
        openTable.click();
        Assert.assertTrue("Таблица не открылась.", table.isDisplayed());
        LOG.info("Таблица закрывается и открывается корректно.");
    }

    /**
     * пополнение счёта на случайную сумму с заданной точностью и масштабом
     * @param acc номер аккаунта
     * @param precision точность
     * @param scale масштаб
     * @return сумма пополнения
     */
    private BigDecimal refillRndNumeric(String acc, int precision, int scale) {
        select(selectActionType, Storage.ActionsInfinitive.REFILL_FULL);
        BigDecimal rndNumeric = getRndNumeric(precision, scale);
        clearAndFill(accnum, acc);
        clearAndFill(resources, rndNumeric.toString());
        successBtn.click();
        driverWait.until(ExpectedConditions.textToBePresentInElement(table, rndNumeric.toString()));
        LOG.info("Счёт (".concat(acc).concat(") пополнен на сумму: ").concat(rndNumeric.toString()));
        return rndNumeric;
    }

    /**
     * блокировка аккаунта
     * @param acc номер аккаунта
     */
    private void blockAcc(String acc) {
        chooseAction(Storage.ActionsInfinitive.BLOCK_FULL);
        clearAndFill(accnum, acc);
        successBtn.click();
        DriverExtension.checkElementWithTextIsPresent(String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.BLOCK_FULL), TIME_OUT);
        LOG.info("Аккаунт заблокирован: ".concat(acc));
    }

    /**
     * закрытие аккаунта
     * @param acc номер аккаунта
     */
    private void closeAcc(String acc) {
        chooseAction(Storage.ActionsInfinitive.CLOSE_FULL);
        clearAndFill(accnum, acc);
        successBtn.click();
        DriverExtension.checkElementWithTextIsPresent(String.format(Storage.AlertText.SUCCESS_ACTION_ALERT, Storage.ActionsInfinitive.CLOSE_FULL), TIME_OUT);
        LOG.info("Аккаунт закрыт: ".concat(acc));
    }

    /**
     * валидация по регклярным выражениям
     * проверка даты (последняя операция) after (дата создания)
     * если (последняя операция) == "Создание", то (последняя операция) и (дата создания) равны
     * @param grid страница таблицы
     */
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
        LOG.info("Поля таблицы корректны.");
    }

    /**
     * @return верхняя строка соответвтует 1 аккаунту (с последней операцией) из актуальной страницы таблицы
     */
    private Map<String, String> getLastRow() {
        return getGridRow(gridBuilder(), 0);
    }

    /**
     * @param accNum номер аккаунта (len 16)
     * @return строка соответвтует 1 аккаунту
     */
    private Map<String, String> getRowByAccNum(String accNum) {
        Map<String, List<String>> grid = gridBuilder();
        int numRow = grid.get(Storage.Columns.ACC_NUM).indexOf(accNum);
        return getGridRow(grid, numRow);
    }

    /**
     * @param numRow номер строки таблицы (0+)
     * @return строка соответвтует 1 аккаунту из актуальной страницы таблицы
     */
    private Map<String, String> getGridRow(int numRow) {
        return getGridRow(gridBuilder(), numRow);
    }

    /**
     * @param grid страница таблицы
     * @param numRow номер строки таблицы (0+)
     * @return строка соответвтует 1 аккаунту
     */
    private Map<String, String> getGridRow(Map<String, List<String>> grid, int numRow) {
        return grid.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(numRow)));
    }

    /**
     * @return актуальная страница таблицы
     */
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
