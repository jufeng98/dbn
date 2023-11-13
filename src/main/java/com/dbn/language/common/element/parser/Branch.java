package com.dbn.language.common.element.parser;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Branch {
    protected String name;

    public Branch() {}

    public Branch(String name) {
        this.name = name;
    }
}