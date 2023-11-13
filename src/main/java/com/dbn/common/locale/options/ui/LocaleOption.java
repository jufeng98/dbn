package com.dbn.common.locale.options.ui;

import com.dbn.common.ui.Presentable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Getter
public class LocaleOption implements Presentable{
    public static final List<LocaleOption> ALL = new ArrayList<>();
    static {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            ALL.add(new LocaleOption(locale));
        }
        ALL.sort(Comparator.comparing(LocaleOption::getName));
    }


    private final Locale locale;

    public LocaleOption(Locale locale) {
        this.locale = locale;
    }

    @NotNull
    @Override
    public String getName() {
        return locale.equals(Locale.getDefault()) ?
                locale.getDisplayName() + " - System default" :
                locale.getDisplayName();
    }

    @Nullable
    public static LocaleOption get(Locale locale) {
        for (LocaleOption localeOption : ALL) {
            if (localeOption.locale.equals(locale)) {
                return localeOption;
            }
        }
        return null;
    }
}
