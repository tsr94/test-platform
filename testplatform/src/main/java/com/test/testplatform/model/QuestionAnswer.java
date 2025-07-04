package com.test.testplatform.model;

public class QuestionAnswer {
    private Question question;
    private String userAnswer;

    

    public QuestionAnswer(Question question, String userAnswer) {
    	this.question = question;
        this.userAnswer = userAnswer;
	}

	public Question getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
    
}
