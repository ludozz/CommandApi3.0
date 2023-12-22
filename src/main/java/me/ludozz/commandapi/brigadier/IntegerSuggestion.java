package me.ludozz.commandapi.brigadier;

import com.mojang.brigadier.context.StringRange;

@SuppressWarnings("unused")
public final class IntegerSuggestion implements Suggestion {

    private final int number;

    public IntegerSuggestion(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String getText() {
        return Integer.toString(number);
    }

    @Override
    public com.mojang.brigadier.suggestion.IntegerSuggestion toBrigadier(int start) {
        final StringRange stringRange = StringRange.at(start);
        return new com.mojang.brigadier.suggestion.IntegerSuggestion(stringRange, number);
    }

    @Override
    public String toString() {
        return "IntegerSuggestion{" +
                "number=" + number +
                '}';
    }
}
