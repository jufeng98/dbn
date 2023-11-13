package com.dbn.language.common.element.cache;

import com.dbn.language.common.element.impl.ElementTypeRef;
import com.dbn.language.common.element.impl.LeafElementType;
import com.dbn.language.common.element.impl.SequenceElementType;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.ElementType;

import java.util.Set;

public class SequenceElementTypeLookupCache<T extends SequenceElementType> extends ElementTypeLookupCacheIndexed<T> {

    public SequenceElementTypeLookupCache(T elementType) {
        super(elementType);
    }

    @Override
    boolean initAsFirstPossibleLeaf(LeafElementType leaf, ElementType source) {
        boolean notInitialized = !firstPossibleLeafs.contains(leaf);
        return notInitialized && (
                isWrapperBeginLeaf(leaf) ||
                    (couldStartWithElement(source) &&
                     source.getLookupCache().couldStartWithLeaf(leaf)));
    }

    @Override
    boolean initAsFirstRequiredLeaf(LeafElementType leaf, ElementType source) {
        boolean notInitialized = !firstRequiredLeafs.contains(leaf);
        return notInitialized &&
                shouldStartWithElement(source) &&
                source.getLookupCache().shouldStartWithLeaf(leaf);
    }

    private boolean couldStartWithElement(ElementType elementType) {
        ElementTypeRef child = this.elementType.getFirstChild();
        while (child != null) {
            if (child.isOptional()) {
                if (elementType == child.getElementType()) return true;
            } else {
                return child.getElementType() == elementType;
            }
            child = child.getNext();
        }
        return false;
    }

    private boolean shouldStartWithElement(ElementType elementType) {
        ElementTypeRef child = this.elementType.getFirstChild();
        while (child != null) {
            if (!child.isOptional()) {
                return child.getElementType() == elementType;
            }
            child = child.getNext();
        }
        return false;
    }

    @Override
    public boolean checkStartsWithIdentifier() {
        ElementTypeRef child = this.elementType.getFirstChild();
        while (child != null) {
            if (child.getLookupCache().startsWithIdentifier()) {
                return true;
            }

            if (!child.isOptional()) {
                return false;
            }
            child = child.getNext();
        }
        return false;
    }

    @Override
    public Set<LeafElementType> captureFirstPossibleLeafs(ElementLookupContext context, Set<LeafElementType> bucket) {
        bucket = super.captureFirstPossibleLeafs(context, bucket);
        bucket = initBucket(bucket);

        ElementTypeRef child = this.elementType.getFirstChild();
        while (child != null) {
            if (context.check(child)) {
                child.getLookupCache().captureFirstPossibleLeafs(context, bucket);
            }
            if (!child.isOptional()) break;
            child = child.getNext();
        }
        return bucket;
    }

    @Override
    public Set<TokenType> captureFirstPossibleTokens(ElementLookupContext context, Set<TokenType> bucket) {
        bucket = super.captureFirstPossibleTokens(context, bucket);
        bucket = initBucket(bucket);

        ElementTypeRef child = this.elementType.getFirstChild();
        while (child != null) {
            if (context.check(child)) {
                child.getLookupCache().captureFirstPossibleTokens(context, bucket);
            }
            if (!child.isOptional()) break;
            child = child.getNext();
        }
        return bucket;
    }
}

