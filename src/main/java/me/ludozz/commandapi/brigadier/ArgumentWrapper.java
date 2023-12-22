package me.ludozz.commandapi.brigadier;

import org.jetbrains.annotations.NotNull;

public final class ArgumentWrapper {

    private final String argument;
    private final Object result;

    public ArgumentWrapper(@NotNull String argument, @NotNull Object result) {
        this.argument = argument;
        this.result = result;
    }

    @NotNull
    public String getArgument() {
        return argument;
    }

    @NotNull
    public Object get() {
        return result;
    }

    @NotNull
    public <T> T get(@NotNull Class<T> classType) {
        return classType.cast(get());
    }

    @Override
    public String toString() {
        return "ArgumentWrapper{" +
                "argument='" + argument + '\'' +
                ", result=" + result +
                '}';
    }

}
