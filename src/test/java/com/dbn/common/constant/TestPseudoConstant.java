package com.dbn.common.constant;

import com.dbn.common.util.Strings;
import lombok.Getter;

public class TestPseudoConstant extends PseudoConstant<TestPseudoConstant> {
    // various static initialization ways
    public static final TestPseudoConstant PSEUDO_CONSTANT_0 = get("PSEUDO_CONSTANT_0");
    public static final TestPseudoConstant PSEUDO_CONSTANT_1 = get("PSEUDO_CONSTANT_1");
    public static final TestPseudoConstant PSEUDO_CONSTANT_2 = get("PSEUDO_CONSTANT_2");
    public static final TestPseudoConstant PSEUDO_CONSTANT_4 = new TestPseudoConstant("PSEUDO_CONSTANT_4");
    public static final TestPseudoConstant PSEUDO_CONSTANT_5 = new TestPseudoConstant("PSEUDO_CONSTANT_5");
    public static final TestPseudoConstant PSEUDO_CONSTANT_6 = get("PSEUDO_CONSTANT_6");
    public static final TestPseudoConstant PSEUDO_CONSTANT_7 = new TestPseudoConstant("PSEUDO_CONSTANT_7");
    public static final TestPseudoConstant PSEUDO_CONSTANT_8 = get("PSEUDO_CONSTANT_8");

    private final @Getter TestCrossRefConstant inner;

    private TestPseudoConstant(String id) {
        super(id);
        inner = Strings.isEmpty(id) ? null : TestCrossRefConstant.get("CROSS_REF_CONSTANT" + id.substring(id.lastIndexOf("_")));
    }

    public static TestPseudoConstant get(String id) {
        return PseudoConstant.get(TestPseudoConstant.class, id);
    }


}
