package ru.sbtqa.tag.pagefactory.pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateName;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSecondname;
import ru.sbtqa.tag.pagefactory.generators.gens.GenerateSurname;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        fillField(lastname, new GenerateSurname().generate());
        fillField(firstname, new GenerateName().generate());
        fillField(secondname, new GenerateSecondname().generate());
        successBtn.click();
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


    public static Map<String, List<String>> gridBuilder() {
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
