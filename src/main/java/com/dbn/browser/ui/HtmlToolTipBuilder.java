package com.dbn.browser.ui;

public abstract class HtmlToolTipBuilder implements ToolTipProvider {
    private StringBuilder buffer;
    @Override
    public String getToolTip() {
        buffer = new StringBuilder();
        buffer.append("<html>");
        buffer.append("<table><tr><td><table cellpadding=0 cellspacing=0>\n");

        buildToolTip();

        closeOpenRow();
        buffer.append("</table></td></tr></table>");
        buffer.append("</html>");
        return buffer.toString();
    }

    public abstract void buildToolTip();

    public void append(boolean newRow, String text, boolean bold) {
        append(newRow, text, null, null, bold);
    }

    public void append(boolean newRow, String text, String size, String color, boolean bold) {
        if (newRow) createNewRow();
        if (bold) buffer.append("<b>");
        if (color != null || size != null) {
            buffer.append("<font");
            if (color != null) buffer.append(" color='").append(color).append("'");
            if (size != null) buffer.append(" size='").append(size).append("'");
            buffer.append(">");
        }
        buffer.append(text);
        if (color != null || size != null) buffer.append("</font>");
        if (bold) buffer.append("</b>");
    }


    public void createEmptyRow() {
        closeOpenRow();
        buffer.append("<tr><td>&nbsp;</td></tr>\n");
    }

    private void createNewRow() {
        closeOpenRow();
        buffer.append("<tr><td>");
    }

    private void closeOpenRow() {
        if (buffer.charAt(buffer.length()-1)!= '\n') {
            buffer.append("</td></tr>\n");
        }
    }
}
