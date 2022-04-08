package beans;

import beans.generic.GenericBean;

public class Exercise extends GenericBean {
	private static final long serialVersionUID = -3170034546833593149L;

	private Long id;
	
	private String question;
	
	private String answer;
	
	private String matiere;
	
	private String prof;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}

	public String getProf() {
		return prof;
	}

	public void setProf(String prof) {
		this.prof = prof;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", question=" + question + ", answer=" + answer + ", matiere=" + matiere
				+ ", prof=" + prof + "]";
	}
	
	
}
