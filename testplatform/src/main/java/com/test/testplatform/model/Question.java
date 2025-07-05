package com.test.testplatform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(columnDefinition = "TEXT")
private String optionA;

@Column(columnDefinition = "TEXT")
private String optionB;

@Column(columnDefinition = "TEXT")
private String optionC;

@Column(columnDefinition = "TEXT")
private String optionD;

@Column(columnDefinition = "TEXT")
private String text;

@Column(columnDefinition = "TEXT")
private String correctAnswer;
    
    @Transient
    private List<String> options;

    public List<String> getOptions() {
        if (options == null) {
            options = List.of(optionA, optionB, optionC, optionD);
        }
        return options;
    }
    public Question() {
        // default constructor required by JPA
    }
	public Question(Long id, String text, String optionA, String optionB, String optionC, String optionD,
			String correctAnswer) {
		super();
		this.id = id;
		this.text = text;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.correctAnswer = correctAnswer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
