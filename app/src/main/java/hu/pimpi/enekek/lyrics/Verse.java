package hu.pimpi.enekek.lyrics;

public class Verse {

    public enum Type {
        BRIDGE,
        CHORUS,
        VERSE
    }

    private Type type;
    private String text;

    public Verse(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
