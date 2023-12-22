package me.ludozz.commandapi.brigadier;

import me.ludozz.commandapi.CommandManager;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class FloatArgument extends Argument<Float> {

    private final Object brigadier;
    private final float min, max;
    private final String invalidFloatMessage = "Invalid float '%arg%'",
            floatTooSmallMessage = "Float must not be less than %min%, found '%arg%'",
            floatTooBigMessage = "Float must not be more than %max%, found '%arg%'";

    public FloatArgument(@NotNull String name) {
        this(name, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatArgument(@NotNull String name, float min, float max) {
        super(name);
        if (!Float.isFinite(min)) throw new IllegalArgumentException("min cannot be Infinite or NaN");
        if (!Float.isFinite(max)) throw new IllegalArgumentException("max cannot be Infinite or NaN");
        this.min = min;
        this.max = max;
        if (CommandManager.getInstance().isExperimental()) {
            brigadier = com.mojang.brigadier.arguments.FloatArgumentType.floatArg(min, max);
        } else brigadier = null;
    }


    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public @NotNull Float checkValue(@NotNull String arg) throws CommandSyntaxException {
        final float result;
        try {
            result = Float.parseFloat(arg);
        } catch (NumberFormatException ex) {
            throw new CommandSyntaxException(invalidFloatMessage.replace("%arg%", arg));
        }
        if (!Double.isFinite(result)) {
            throw new CommandSyntaxException(invalidFloatMessage.replace("%arg%", arg));
        }
        if (result < min) {
            throw new CommandSyntaxException(floatTooSmallMessage
                    .replace("%min%", String.valueOf(min)).replace("%arg%", arg));
        }
        if (result > max) {
            throw new CommandSyntaxException(floatTooBigMessage
                    .replace("%max%", String.valueOf(max)).replace("%arg%", arg));
        }
        return result;
    }


    /*@NotNull
    public String getInvalidFloatMessage() {
        return invalidFloatMessage;
    }

    public void setInvalidFloatMessage(@NotNull String invalidFloatMessage) {
        if (isRegistered()) throw new IllegalStateException("argument is registered");
        this.invalidFloatMessage = invalidFloatMessage;
    }

    @NotNull
    public String getFloatTooSmallMessage() {
        return floatTooSmallMessage;
    }

    public void setFloatTooSmallMessage(@NotNull String floatTooSmallMessage) {
        if (isRegistered()) throw new IllegalStateException("argument is registered");
        this.floatTooSmallMessage = floatTooSmallMessage;
    }

    @NotNull
    public String getFloatTooBigMessage() {
        return floatTooBigMessage;
    }

    public void setFloatTooBigMessage(@NotNull String floatTooBigMessage) {
        if (isRegistered()) throw new IllegalStateException("argument is registered");
        this.floatTooBigMessage = floatTooBigMessage;
    }

    @Override
    public @NotNull Object toBrigadier() {
        return null;
    }*/
}
