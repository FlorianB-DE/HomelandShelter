package main.entities.items;

final class Attribute<T> implements Comparable<Attribute<?>> {

    private final String keyWord;
    private final T value;

    public Attribute(String keyWord, T value) {
        this.keyWord = keyWord;
        this.value = value;
    }

    public Attribute(String keyWord) {
        this.keyWord = keyWord;
        this.value = null;
    }

    @Override
    public int compareTo(Attribute<?> o) {
        return o.getKeyWord().compareTo(getKeyWord());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute)
            return keyWord.compareTo(((Attribute<?>) obj).getKeyWord()) == 0;
        return false;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public T getValue() {
        return value;
    }

}
