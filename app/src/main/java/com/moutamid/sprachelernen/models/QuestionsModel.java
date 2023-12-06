package com.moutamid.sprachelernen.models;

import java.util.ArrayList;

public class QuestionsModel {
    String ID, question, correctAnswer;
    ArrayList<String> answers;
    boolean isMCQs;

    public QuestionsModel() {}

    public QuestionsModel(String ID, String question, String correctAnswer, ArrayList<String> answers, boolean isMCQs) {
        this.ID = ID;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.isMCQs = isMCQs;
    }

    public boolean isMCQs() {
        return isMCQs;
    }

    public void setMCQs(boolean MCQs) {
        isMCQs = MCQs;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
