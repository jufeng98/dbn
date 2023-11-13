package com.dbn.common.constant;

public class TestCrossRefConstant extends PseudoConstant<TestCrossRefConstant> {
    // various static initialization ways
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_0 = get("CROSS_REF_CONSTANT_0");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_1 = get("CROSS_REF_CONSTANT_1");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_2 = get("CROSS_REF_CONSTANT_2");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_4 = new TestCrossRefConstant("CROSS_REF_CONSTANT_4");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_5 = new TestCrossRefConstant("CROSS_REF_CONSTANT_5");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_6 = get("CROSS_REF_CONSTANT_6");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_7 = new TestCrossRefConstant("CROSS_REF_CONSTANT_7");
    public static final TestCrossRefConstant CROSS_REF_CONSTANT_8 = get("CROSS_REF_CONSTANT_8");

    private TestCrossRefConstant(String id) {
        super(id);
    }

    public static TestCrossRefConstant get(String id) {
        return PseudoConstant.get(TestCrossRefConstant.class, id);
    }
}
