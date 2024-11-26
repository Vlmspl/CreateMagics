package net.vladitandlplayer.create_magics;

public interface IManaStorage {
    float getMana();
    float getMaxMana();
    void setMana(float amount);
    void addMana(float amount);
    void subMana(float amount);
    boolean canReceiveMana(int amount);
    boolean isConsumer();
}
