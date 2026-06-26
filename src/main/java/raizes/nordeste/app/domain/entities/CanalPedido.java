package raizes.nordeste.app.domain.entities;

import lombok.Getter;

@Getter
public enum CanalPedido {
    APP("APP"),
    TOTEM("TOTEM"),
    BALCAO("BALCAO"),
    PICKUP("PICKUP");

    private final String value;
    CanalPedido(String value){
        this.value = value;
    }
}