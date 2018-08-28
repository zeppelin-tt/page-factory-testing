package ru.sbtqa.tag.pagefactory.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

import java.util.List;

public class AnyPage extends Page {

    static final int TIME_OUT = 10;
    WebDriverWait driverWait = new WebDriverWait(PageFactory.getWebDriver(), TIME_OUT);

    private static void scrollToElement(WebElement webElement) {
        try {
            String getElementIntoViewPort = String.format(
                    "window.scrollTo(0, $(\"#%s\").offset().top - $(window).height()/2);", webElement.getAttribute("id"));
            ((JavascriptExecutor) PageFactory.getWebDriver()).executeScript(getElementIntoViewPort);
        } catch (Exception ignored) {
        }
    }

    void select(WebElement webElement, String text) {
        scrollToElement(webElement);
        select(webElement, text, MatchStrategy.CONTAINS);
    }

    static List<WebElement> getElementsByXPath(String xpath) {
        return PageFactory.getWebDriver().findElements(By.xpath(xpath));
    }

    @ActionTitle("существует элемент текстом")
    public boolean checkElementWithTextIsPresentBool(String text) {
        return this.checkElementWithTextIsPresent(text, PageFactory.getTimeOutInSeconds());
    }

    private boolean checkElementWithTextIsPresent(String text, int timeout) {
        try {
            WebDriverWait e = new WebDriverWait(PageFactory.getWebDriver(), (long) timeout);
            e.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), \'" + text + "\')]")));
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

    /**
     * я понимаю, что это ерунда какая-то, но СТАБИЛЬНО работает только так...
     * Данная проблема с очисткой проявилась после того, как я на фронте
     * добавил динамическую проверку через regExp event.target.value в input
     * Соответственно, вручную никак не воспроизводится.
     * fillField из Page не чистит рандомный Input примерно в 1 из 100 случаев.
     * @param webElement
     * @param text
     */
    public void clearAndFill(WebElement webElement, String text) {
        webElement.sendKeys(Keys.chord(Keys.CONTROL, Keys.HOME));
        webElement.sendKeys(Keys.chord(Keys.CONTROL, Keys.SHIFT, Keys.END));
        webElement.sendKeys(text);
    }
}
