package main.entitiys.items;

@SuppressWarnings("rawtypes")
public class Attributes<T> implements Comparable<Attributes> {

	private final String keyWord;
	private final T value;

	public Attributes(String keyWord, T value) {
		this.keyWord = keyWord;
		this.value = value;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public T getValue() {
		return value;
	}

	@Override
	public int compareTo(Attributes o) {
		return o.getKeyWord().compareTo(getKeyWord());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Attributes)
			return keyWord.compareTo(((Attributes) obj).getKeyWord()) == 0;
		return false;
	}

}
