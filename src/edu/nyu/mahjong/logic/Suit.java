package edu.nyu.mahjong.logic;

public enum Suit {
	WCHARACTERS, BAMBOOS, CIRCLES, EAST, WEST, SOUTH, NORTH, RED, GREEN, WHITE;

	private static final Suit[] VALUES = values();

	public static Suit fromFirstLetterLowerCase(String firstLetterLowerCase) {
		for (Suit suit : VALUES) {
			if (suit.getFirstLetterLowerCase().equals(firstLetterLowerCase)) {
				return suit;
			}
		}
		throw new IllegalArgumentException("Did not find firstLetterLowerCase="
				+ firstLetterLowerCase);
	}

	public String getFirstLetterLowerCase() {
		return name().substring(0, 1).toLowerCase();
	}

	public Suit getNext() {
		if (this == VALUES[VALUES.length - 1]) {
			return VALUES[0];
		}
		return values()[ordinal() + 1];
	}

	public Suit getPrev() {
		if (this == VALUES[0]) {
			return VALUES[VALUES.length - 1];
		}
		return values()[ordinal() - 1];
	}
}