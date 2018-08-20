package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.ru.Когда;
import gherkin.pickles.PickleRow;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.UIHelper;

import java.util.List;
import java.util.stream.Collectors;

public class CommonStepDefs {
    @Когда("^проверяет содержимое выпадающего списка \"(.*?)\"$")
    public void checkSelectValues(String selectTitle, DataTable dataTable) throws PageException {
        List<String> list = UIHelper.getListTitlesFromSelect(PageFactory.getInstance().getCurrentPage().getElementByTitle(selectTitle));
        List<String> expList = dataTable.getPickleRows().stream().map(PickleRow::getCells).map(lists -> lists.get(0).getValue()).collect(Collectors.toList());
        boolean isRows = expList.containsAll(list) && expList.removeAll(list);
        Assert.assertTrue("Список " + list.toString() + " не соответствует передаваемому списку " + expList.toString(), isRows);
    }


}
