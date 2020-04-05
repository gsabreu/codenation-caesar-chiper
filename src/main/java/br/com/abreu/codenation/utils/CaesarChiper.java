package br.com.abreu.codenation.utils;

public class CaesarChiper {

	private static String STRING_REGEX = "[a-zA-Z]";

	public static String dechiper(String message, int offset) {
		return chiper(message, 26 - (offset % 26));
	}

	private static String chiper(String message, int offset) {
		StringBuilder result = new StringBuilder();
		for (char character : message.toCharArray()) {
			if (Character.toString(character).matches(STRING_REGEX)) {
				int originalAlphabetPosition = character - 'a';
				int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
				char newCharacter = (char) ('a' + newAlphabetPosition);
				result.append(newCharacter);
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}

}
