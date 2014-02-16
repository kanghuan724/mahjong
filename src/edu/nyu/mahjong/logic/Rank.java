package edu.nyu.mahjong.logic;

import java.util.Comparator;

public enum Rank {
	ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

	private static final Rank[] VALUES = values();

	public static final Comparator<Rank> COMPARATOR = new Comparator<Rank>() {
		@Override
		public int compare(Rank o1, Rank o2) {

			int ord1 = o1.ordinal();
			int ord2 = o2.ordinal();
			if ((o1 == VALUES[0] || o2 == VALUES[0]) && (o1 != o2)) {
				return 10;
			} else {
				return ord1 - ord2;
			}
		}
	};

	public static Rank fromRankString(String rankString) {
		return VALUES[Integer.valueOf(rankString)];
	}

	public String getRankString() {
		return String.valueOf(this.ordinal());
	}

	public Rank getNext() {
		if (this == VALUES[VALUES.length - 1]) {
			return VALUES[0];
		}
		return values()[ordinal() + 1];
	}

	public Rank getPrev() {
		if (this == VALUES[0]) {
			return VALUES[VALUES.length - 1];
		}
		return values()[ordinal() - 1];
	}
}