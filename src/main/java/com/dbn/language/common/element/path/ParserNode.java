package com.dbn.language.common.element.path;

import com.dbn.language.common.element.ElementType;
import com.intellij.lang.PsiBuilder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParserNode extends LanguageNodeBase {
    private final int startOffset;
    private int currentOffset;
    private int cursorPosition;
    private PsiBuilder.Marker elementMarker;

    public ParserNode(ElementType elementType, ParserNode parent, int startOffset, int cursorPosition) {
        super(elementType, parent);
        this.startOffset = startOffset;
        this.currentOffset = startOffset;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public ParserNode getParent() {
        return (ParserNode) super.getParent();
    }

    @Override
    public boolean isRecursive() {
        ParserNode parseNode = this.getParent();
        while (parseNode != null) {
            if (parseNode.getElement() == this.getElement() &&
                parseNode.startOffset == startOffset) {
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    public boolean isRecursive(int currentOffset) {
        ParserNode parseNode = this.getParent();
        while (parseNode != null) {
            if (parseNode.getElement() == this.getElement() &&
                        parseNode.currentOffset == currentOffset) {
                    return true;
                }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    public int incrementIndex(int builderOffset) {
        cursorPosition++;
        this.currentOffset = builderOffset;
        return cursorPosition;
    }

    @Override
    public void detach() {
        super.detach();
        elementMarker = null;
    }
}

