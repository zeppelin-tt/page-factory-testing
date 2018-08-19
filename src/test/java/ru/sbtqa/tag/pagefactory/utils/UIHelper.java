package ru.sbtqa.tag.pagefactory.utils;

import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.List;
import java.util.stream.Collectors;

public class UIHelper {

    public static List<String> getListTitlesFromSelect(WebElement el) {
        return new Select(el)
                .getOptions()
                .stream()
                .map(WebElement::getText)
                .filter(p -> !("").equals(p))
                .collect(Collectors.toList());
    }

}
