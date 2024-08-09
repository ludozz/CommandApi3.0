package me.ludozz.commandapi;

import me.ludozz.commandapi.brigadier.Argument;
import me.ludozz.commandapi.brigadier.CommandSyntaxException;
import me.ludozz.commandapi.brigadier.StringArgument;
import org.jetbrains.annotations.NotNull;

public class ArgumentReader implements Cloneable {

    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';
    private final String string;
    private int cursor;

    public ArgumentReader(@NotNull final ArgumentReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public ArgumentReader(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getRemainingLength() {
        return this.string.length() - this.cursor;
    }

    public int getTotalLength() {
        return this.string.length();
    }

    public int getCursor() {
        return this.cursor;
    }

    public String getRead() {
        return this.string.substring(0, this.cursor);
    }

    public String getRemaining() {
        return this.string.substring(this.cursor);
    }

    public boolean canRead(int length) {
        return this.cursor + length <= this.string.length();
    }

    public boolean canRead() {
        return this.canRead(1);
    }

    public char peek() {
        return this.string.charAt(this.cursor);
    }

    public char peek(int offset) {
        return this.string.charAt(this.cursor + offset);
    }

    public char read() {
        return this.string.charAt(this.cursor++);
    }

    public void skip() {
        ++this.cursor;
    }

    public static boolean isAllowedNumber(char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == '"' || c == '\'';
    }

    public void skipWhitespace() {
        while(this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
    }

    public String getNextStringArgument() {
        skipWhitespace();
        final StringBuilder argument = new StringBuilder();
        while (this.canRead()) {
            final char next = this.peek();
            if (Character.isWhitespace(next)) break;
            argument.append(next);
            cursor++;
        }
        return argument.toString();
    }

    public <T> T read(Argument<T> argument) throws CommandSyntaxException {
        if (argument instanceof StringArgument) {
            final StringArgument stringArgument = (StringArgument) argument;
            switch (stringArgument.getStringType()) {
                case SINGLE_WORD:
                    break;
                case GREEDY_PHRASE:
                //noinspection deprecation
                case QUOTABLE_PHRASE:

            }
        }
        return argument.checkValue(getNextStringArgument());
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        int start = this.cursor;
//todo fix
        while(this.canRead() && isAllowedInUnquotedString(this.peek())) {
            this.skip();
        }

        return this.string.substring(start, this.cursor);
    }

    public String readQuotedString() throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        if (!this.canRead()) {
            return "";
        } else {
            char next = this.peek();
            if (!isQuotedStringStart(next)) {
                throw com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
            } else {
                this.skip();
                return this.readStringUntil(next);
            }
        }
    }

    public String readStringUntil(char terminator) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        while(this.canRead()) {
            char c = this.read();
            if (escaped) {
                if (c != terminator && c != '\\') {
                    this.setCursor(this.getCursor() - 1);
                    throw com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
                }

                result.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                if (c == terminator) {
                    return result.toString();
                }

                result.append(c);
            }
        }

        throw com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ArgumentReader clone() {
        return new ArgumentReader(this);
    }
}
