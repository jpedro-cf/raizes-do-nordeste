package raizes.nordeste.app.application;

public interface Hasher {
    public String hash(String value);
    public boolean verify(String hash, String value);
}