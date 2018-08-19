package ru.sbtqa.tag.pagefactory.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

import java.util.List;

public class AnyPage extends Page {

    /**
     * Скроллит страницу, после чего элемент оказывается как можно ближе к центру. При возникновении любого исключения
     * не происходит ничего.
     *
     * @param webElement
     */
    public static void scrollToElement(WebElement webElement) {
        try {
            String getElementIntoViewPort = String.format(
                    "window.scrollTo(0, $(\"#%s\").offset().top - $(window).height()/2);", webElement.getAttribute("id"));
            ((JavascriptExecutor) PageFactory.getWebDriver()).executeScript(getElementIntoViewPort);
        } catch (Exception ignored) {
        }
    }


    public void select(WebElement webElement, String text) {
        scrollToElement(webElement);
        select(webElement, text, MatchStrategy.CONTAINS);
    }

    public static List<WebElement> getElementsByXPath(String xpath) {
        return PageFactory.getWebDriver().findElements(By.xpath(xpath));
    }

}
