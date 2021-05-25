package com.example.myapplication;

import java.util.Date;

public class CommunicationMessage {

    public enum CommunicationMessageStateType {
        inProgress,
        completed
    }

    private String subject;
    private String content;
    private Date sendTime;
    private Sender sender;
    private Receiver receiver;
    private CommunicationMessageStateType state;

    public CommunicationMessage() {
        state = CommunicationMessageStateType.inProgress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public CommunicationMessageStateType getState() { return state; }

    public void setState(CommunicationMessageStateType state) { this.state = state; }
}
