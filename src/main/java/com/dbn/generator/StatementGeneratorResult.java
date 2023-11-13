package com.dbn.generator;


import com.dbn.common.message.MessageBundle;

public class StatementGeneratorResult {
    private String statement;
    private MessageBundle messages = new MessageBundle();


    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public MessageBundle getMessages() {
        return messages;
    }
}
