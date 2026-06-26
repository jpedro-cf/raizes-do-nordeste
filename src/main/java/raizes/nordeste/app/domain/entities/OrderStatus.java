package raizes.nordeste.app.domain.entities;

public enum OrderStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    PREPARING("PREPARING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    private final String value;
    OrderStatus(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}