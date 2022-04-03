package utils;

import beans.Exercise;

public class Validator {
	public static boolean validateExercise(Exercise exo)
	{
		if(exo.getCategory()==null||exo.getCategory().trim().isEmpty())
			return false;
		if(exo.getAnswer()==null||exo.getAnswer().trim().isEmpty())
			return false;
		if(exo.getQuestion()==null||exo.getQuestion().trim().isEmpty())
			return false;
		return true;
	}
}
