package ru.sbtqa.tag.pagefactory.pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateName;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSecondname;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSurname;
import ru.sbtqa.tag.pagefactory.utils.Storage;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.sbtqa.tag.pagefactory.utils.Storage.Constants.TIME_FORMAT;
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
    @ElementTitle(value = "Номер счета")
    protected WebElement accnum;

    @FindBy(xpath = "//input[@name='resources']")
    @ElementTitle(value = "Сумма рублей")
    protected WebElement resources;

    @FindBy(xpath = "//input[@name='second_accnum']")
    @ElementTitle(value = "Номер счета получателя")
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

    @FindBy(xpath = "//button[contains(text(),'Показать все')]")
    @ElementTitle(value = "Показать все")
    protected WebElement openAll;

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
    }

    @ActionTitle("нажимает")
    public void clickElement(String element) throws PageException {
        this.getElementByTitle(element).click();
    }

    @ActionTitle("открывает новый счет")
    public void createAccount() {
        select(selectActionType, "Создать счет");
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
        System.out.println("qwe");
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
        Map<String, String> addingRow = actualGrid.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0)));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        LocalDateTime lastOpTime =  LocalDateTime.parse(rowMap.get("Время последней операции"), formatter);
        LocalDateTime createTime =  LocalDateTime.parse(rowMap.get("Время создания счёта"), formatter);
        LocalDateTime timeNow = LocalDateTime.now();
        Assert.assertTrue("Время операций создания и последней операции не равны.", lastOpTime.equals(createTime));
        Assert.assertTrue("Расхождение актуального Времени (".concat(timeNow.toString()).concat(" и Времени создания (").concat(createTime.toString()).concat(")"),
                timeNow.isAfter(createTime.minusMinutes(3)) && timeNow.isAfter(createTime));
    }

    @ActionTitle("проверяет пополнение счета")
    public void checkRefill() {
        Map<String, String> rowMap = Stash.getValue(ADDING_ROW);
        String sum = "555.55";
        select(selectActionType, "Пополнить счет");
        fillField(accnum, rowMap.get(Storage.Columns.ACC_NUM));
        fillField(resources, sum);
        successBtn.click();
        Map<String, List<String>> actualGrid = gridBuilder();
        String actualBalance = actualGrid.get(Storage.Columns.BALANCE).get(0);
        Assert.assertTrue("Баланс не соответствует добавленному: ".concat(sum).concat(" != ").concat(actualBalance), sum.equals(actualBalance));
        String actualLastAction = actualGrid.get(Storage.Columns.LAST_ACTION).get(0);
        Assert.assertTrue("Последняя операция некорректна: ".concat(actualLastAction), Storage.Actions.REFILL.equals(actualLastAction));
    }

    private void validateGrid(Map<String, List<String>> grid) {
        grid.forEach((key, value) -> value
                .forEach(e -> Assert.assertTrue("Некоррекнтно поле: ".concat(key).concat(" != ").concat(e), validateGridField(e, key)))
        );
    }

    private static Map<String, List<String>> gridBuilder() {
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
