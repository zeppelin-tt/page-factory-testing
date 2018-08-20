package ru.sbtqa.tag.pagefactory.utils.action;

import ru.sbtqa.tag.pagefactory.utils.parser.JaxbParser;
import ru.sbtqa.tag.pagefactory.utils.parser.Parser;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.properties.Props;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Actions")
public class Actions {

    private static String XML_ACTIONS_PATH = Props.get("xmlActionPath");
    private static Actions instance;

    @XmlElement(name = "Action")
    private List<Action> actions;

    private Actions() {
    }

    private static void init() {
        if (instance == null) {
            Parser parser = new JaxbParser();
            File file = new File(XML_ACTIONS_PATH);
            try {
                instance = (Actions) parser.getObject(file, Actions.class);
            } catch (JAXBException e) {
                throw new AutotestError("Ошибка парсинга");
            }
        }
    }

    public static List<Action> getActions() {
        init();
        return instance.actions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Actions\r\n{");
        if (actions != null) {
            for (Action s : actions) {
                sb.append(s.toString()).append("\r\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
