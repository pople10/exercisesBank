package beans;

import beans.generic.GenericBean;

public class Exercise extends GenericBean {
	private static final long serialVersionUID = -3170034546833593149L;

	private Long id;
	
	private String question;
	
	private String answer;
	
	private String category;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", question=" + question + ", answer=" + answer + ", category=" + category + "]";
	}
	
	
}
