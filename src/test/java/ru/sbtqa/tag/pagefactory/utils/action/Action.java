package ru.sbtqa.tag.pagefactory.utils.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Action")
public class Action {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "reduction")
    private String reduction;

    public Action(String name, String reduction) {
        this.name = name;
        this.reduction = reduction;
    }
    public Action() {
    }

    public String getName() {
        return name;
    }

    public String getReduction() {
        return reduction;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("{")
                .append(name).append(", ")
                .append(reduction)
                .append("}")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action status = (Action) o;
        return Objects.equals(name, status.name) &&
                Objects.equals(reduction, status.reduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, reduction);
    }
}
