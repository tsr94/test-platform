package com.test.testplatform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TestConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String testName;
    private int numberOfQuestions;
    private int durationInMinutes;
    private String accessPassword;
    private boolean published = false;

    
    @Column(unique = true)
    private String slug; // for link creation

    public TestConfig() {}
    public TestConfig(int numberOfQuestions, int durationInMinutes, String accessPassword) {
        this.numberOfQuestions = numberOfQuestions;
        this.durationInMinutes = durationInMinutes;
        this.accessPassword = accessPassword;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	public int getDurationInMinutes() {
		return durationInMinutes;
	}
	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	public String getAccessPassword() {
		return accessPassword;
	}
	public void setAccessPassword(String accessPassword) {
		this.accessPassword = accessPassword;
	}

	 public String getSlug() {
	        return slug;
	 }

	    public void setSlug(String slug) {
	        this.slug = slug;
	   }
		public String getTestName() {
			return testName;
		}
		public void setTestName(String testName) {
			this.testName = testName;
		}
		
		public boolean isPublished() {
		    return published;
		}

		public void setPublished(boolean published) {
		    this.published = published;
		}

	    
}

