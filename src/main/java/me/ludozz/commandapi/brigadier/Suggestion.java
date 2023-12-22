package me.ludozz.commandapi.brigadier;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public interface Suggestion {

    String getText();

    com.mojang.brigadier.suggestion.Suggestion toBrigadier(int start);

    static @NotNull List<com.mojang.brigadier.suggestion.Suggestion> toBrigadier(
            final int start, @NotNull List<Suggestion> suggestions) {
        final List<com.mojang.brigadier.suggestion.Suggestion> brigadier = new ArrayList<>(suggestions.size());
        for (Suggestion suggestion : suggestions) {
            brigadier.add(suggestion.toBrigadier(start));
        }
        return brigadier;
    }

}
