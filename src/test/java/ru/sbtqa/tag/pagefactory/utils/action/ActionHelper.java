package ru.sbtqa.tag.pagefactory.utils.action;

public class ActionHelper {

    public static Action getActionByName(String name) {
        for (Action action : Actions.getActions()) {
            if (action.getName().equals(name))
                return action;
        }
        return null;
    }

    public static Action getActionByReduction(String reduction) {
        for (Action action : Actions.getActions()) {
            if (action.getReduction().equals(reduction))
                return action;
        }
        return null;
    }

    public static String getReductionByName(String name) {
        return getActionByName(name).getReduction();
    }

    public static String getNameByReduction(String reduction) {
        return getActionByReduction(reduction).getName();
    }

}
