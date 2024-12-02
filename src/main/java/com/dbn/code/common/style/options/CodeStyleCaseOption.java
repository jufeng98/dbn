package com.dbn.code.common.style.options;

import com.dbn.common.options.PersistentConfiguration;
import com.dbn.common.util.Naming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import java.util.Objects;

import static com.dbn.common.options.setting.Settings.stringAttribute;
import static com.dbn.common.util.Strings.*;

@Getter
@Setter
@EqualsAndHashCode
public class CodeStyleCaseOption implements PersistentConfiguration {
    private String name;
    private boolean ignoreMixedCase;
    private CodeStyleCase styleCase;

    public CodeStyleCaseOption(String id, CodeStyleCase styleCase, boolean ignoreMixedCase) {
        this.name = id;
        this.styleCase = styleCase;
        this.ignoreMixedCase = ignoreMixedCase;
    }

    public String format(String string) {
        if (string == null) {
            return null;
        }

        return switch (styleCase) {
            case UPPER -> ignore(string) ? string : toUpperCase(string);
            case LOWER -> ignore(string) ? string : toLowerCase(string);
            case CAPITALIZED -> ignore(string) ? string : Naming.capitalize(string);
            case PRESERVE -> string;
        };
    }

    boolean ignore(String string) {
        return string.startsWith("`") || string.startsWith("'") || string.startsWith("\"") || (ignoreMixedCase && isMixedCase(string));
    }

    /*********************************************************
     *                 PersistentConfiguration               *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        name = stringAttribute(element, "name");
        String style = stringAttribute(element, "value");
        styleCase =
                Objects.equals(style, "upper") ? CodeStyleCase.UPPER :
                Objects.equals(style, "lower") ? CodeStyleCase.LOWER :
                Objects.equals(style, "capitalized") ? CodeStyleCase.CAPITALIZED :
                        CodeStyleCase.PRESERVE;
    }

    @Override
    public void writeConfiguration(Element element) {
        String value =
                styleCase == CodeStyleCase.UPPER ? "upper" :
                styleCase == CodeStyleCase.LOWER ? "lower" :
                styleCase == CodeStyleCase.CAPITALIZED ? "capitalized" :
                        "preserve";

        element.setAttribute("name", name);
        element.setAttribute("value", value);
    }

    @Override
    public String toString() {
        return name + "=" + styleCase.name();
    }
}
