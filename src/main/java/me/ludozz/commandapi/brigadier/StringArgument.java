package me.ludozz.commandapi.brigadier;

import me.ludozz.commandapi.CommandManager;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public final class StringArgument extends Argument<String> {

    private final StringType stringType;
    private final String expectedStartOfQuoteMessage = "Expected quote to start a string",
            expectedEndOfQuoteMessage = "Unclosed quoted string";

    public StringArgument(final @NotNull String name) {
        this(name, StringType.SINGLE_WORD);
    }

    public StringArgument(final @NotNull String name, final @NotNull StringType stringType) {
        super(name);
        this.stringType = stringType;
        //todo support this..
        if (stringType == StringType.QUOTABLE_PHRASE) throw new UnsupportedOperationException("not supported yet...");
    }

    @Override
    public @NotNull String checkValue(@NotNull String arg) throws CommandSyntaxException {
        if (stringType == StringType.GREEDY_PHRASE) {
            return arg.replaceAll("[^0-9a-zA-Z_\\-.+]", "");
        }
        return arg;
    }

    private static boolean isAllowedInUnquotedString(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    @NotNull
    public StringType getStringType() {
        return stringType;
    }


    public enum StringType {
        SINGLE_WORD,
        @Deprecated
        QUOTABLE_PHRASE,
        GREEDY_PHRASE;
        StringType() {}

    }

}
