package raizes.nordeste.app.application;
import java.util.Map;

public interface TokenService {
    String encode(Map<String, String> data, long expiration);
    Map<String, Object> decode(String value);
}