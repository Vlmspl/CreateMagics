package net.vladitandlplayer.create_magics;

public interface ManaStorage {
    float getMana();
    float getMaxMana();
    void setMana(float amount);
    void addMana(float amount);
    void subMana(float amount);
}
