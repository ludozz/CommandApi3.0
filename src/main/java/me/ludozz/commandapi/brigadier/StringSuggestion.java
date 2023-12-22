package me.ludozz.commandapi.brigadier;

import com.mojang.brigadier.context.StringRange;
import org.jetbrains.annotations.NotNull;

public final class StringSuggestion implements Suggestion {

    private final String text;

    public StringSuggestion(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public String getText() {
        return text;
    }

    @Override
    public com.mojang.brigadier.suggestion.Suggestion toBrigadier(int start) {
        final StringRange stringRange = StringRange.at(start);
        return new
                com.mojang.brigadier.suggestion.Suggestion(stringRange, text);
        //com.mojang.brigadier.suggestion.Suggestion(StringRange.at(args.lastIndexOf(32)+1), )
    }

    @Override
    public String toString() {
        return "StringSuggestion{" +
                "text='" + text + '\'' +
                '}';
    }


}
