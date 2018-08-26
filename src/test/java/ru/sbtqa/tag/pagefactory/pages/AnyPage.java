package ru.sbtqa.tag.pagefactory.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

import java.util.List;

public class AnyPage extends Page {

    public static final int TIME_OUT = 10;


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

    @ActionTitle("существует элемент текстом")
    public boolean checkElementWithTextIsPresentBool(String text) {
        return this.checkElementWithTextIsPresent(text, PageFactory.getTimeOutInSeconds());
    }

    public boolean checkElementWithTextIsPresent(String text, int timeout) {
        try {
            WebDriverWait e = new WebDriverWait(PageFactory.getWebDriver(), (long) timeout);
            e.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), \'" + text + "\')]")));
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

}
