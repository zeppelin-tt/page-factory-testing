package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.ru.Когда;
import gherkin.pickles.PickleRow;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.UIHelper;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.util.List;
import java.util.stream.Collectors;

public class CommonStepDefs {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CommonStepDefs.class);

    @Когда("^проверяет содержимое выпадающего списка \"(.*?)\"$")
    public void checkSelectValues(String selectTitle, DataTable dataTable) throws PageException {
        List<String> list = UIHelper.getListTitlesFromSelect(PageFactory.getInstance().getCurrentPage().getElementByTitle(selectTitle));
        List<String> expList = dataTable.getPickleRows().stream().map(PickleRow::getCells).map(lists -> lists.get(0).getValue()).collect(Collectors.toList());
        boolean isRows = expList.containsAll(list) && expList.removeAll(list);
        Assert.assertTrue("Список ".concat(list.toString()).concat(" не соответствует передаваемому списку ").concat(expList.toString()), isRows);
    }

    @Когда("^пользователь проверяет (наличие|отсутствие) элементов на странице$")
    public static void checkElementsOnPage(String strategy, DataTable dataTable) {
        dataTable.asList(String.class).forEach(title -> checkElementOnPage(strategy, title));
    }

    @Когда("^пользователь проверяет (наличие|отсутствие) элемента \"(.*)\" на странице$")
    public static void checkElementOnPage(String strategy, String eTitle) {
        WebElement el;
        Page currentPage;
        boolean isPresent;
        try {
            currentPage = PageFactory.getInstance().getCurrentPage();
            el = currentPage.getElementByTitle(eTitle);
        } catch (PageException pe) {
            LOG.error("Не найден указанный элемент по title в указанном классе страницы", pe);
            throw new AutotestError("Не найден указанный элемент по title в указанном классе страницы: " + pe.getMessage());
        }
        try {
            isPresent = el.isDisplayed();
        } catch (NoSuchElementException nse) {
            isPresent = false;
        }
        if ("наличие".equals(strategy)) {
            Assert.assertTrue("Элемент: ".concat(eTitle).concat(" отсутствует на странице: ").concat(currentPage.getTitle()), isPresent);
        } else {
            Assert.assertFalse("Элемент: ".concat(eTitle).concat(" присутствует на странице: ").concat(currentPage.getTitle()), isPresent);
        }
        LOG.info("Проверено ".concat(strategy).concat(" элемента: ").concat(eTitle));
    }
}
