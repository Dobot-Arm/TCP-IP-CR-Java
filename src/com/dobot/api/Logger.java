package com.dobot.api;

import javax.swing.*;

public enum Logger {

    instance;

    private JTextArea logTextArea= new JTextArea();

    private JTextArea errorTextArea = new JTextArea();

    private StringBuilder logStringBuilder = new StringBuilder();

    private StringBuilder errorStringBuilder = new StringBuilder();
    public void addLogListener(JTextArea logTextArea) {
        synchronized(logTextArea) {
            this.logTextArea = logTextArea;
        }
    }


    public void addErrorListener(JTextArea errorTextArea) {
        synchronized(errorTextArea) {
            this.errorTextArea = errorTextArea;
        }
    }

    public void log(String log) {
        synchronized(logTextArea) {
            if (logTextArea.getLineCount()>5000){
                logTextArea.setText("");
            }
            logStringBuilder.append(log+"\n");
            logTextArea.setText(logStringBuilder.toString());
        }
    }

    public void error(String error) {
        synchronized(errorTextArea) {
            if (errorTextArea.getLineCount()>5000){
                errorTextArea.setText("");
            }
            errorStringBuilder.append(error+"\n");
            errorTextArea.setText(errorStringBuilder.toString());
        }
    }

    interface Listener {
        void log(String log);
        void error(String error);
    }

}
