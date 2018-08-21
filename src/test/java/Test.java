import java.io.IOException;

import static ru.sbtqa.tag.pagefactory.utils.action.ActionHelper.getActionByName;

public class Test {
    public static void main(String[] args) throws IOException {

        System.out.println(getActionByName("Создать счет"));

    }
}
