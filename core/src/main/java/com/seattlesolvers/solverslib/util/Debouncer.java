package com.seattlesolvers.solverslib.util;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Debouncer {
    /** Type of debouncing to perform. */
    public enum DebounceType {
        /** Rising edge. */
        Rising,
        /** Falling edge. */
        Falling,
        /** Both rising and falling edges. */
        Both
    }

    private double debounceRising;
    private double debounceFalling;

    private ElapsedTime timer;
    private boolean state;
    private boolean lastInput;

    /**
     * Creates a fully specified debouncer
     *
     * @param debounceRising Debounce time on the rising edge in seconds
     * @param debounceFalling Debounce time on the falling edge in seconds
     * @param baseline If it is initially rising (true) or falling (false)
     */
    public Debouncer(double debounceRising, double debounceFalling, boolean baseline) {
        this.debounceRising = debounceRising;
        this.debounceFalling = debounceFalling;
        this.timer = new ElapsedTime();
        this.state = baseline;
        this.lastInput = baseline;
    }

    /**
     * Creates a WPILib style debouncer.
     * For more versatility, use {@link #Debouncer(double, double, boolean)}<br>
     * {@link DebounceType#Rising} - rising edge: {@code debounce}, falling edge: {@code 0}, initially {@code false}<br>
     * {@link DebounceType#Falling} - rising edge: {@code 0}, falling edge: {@code debounce}, initially {@code true}<br>
     * {@link DebounceType#Both} - rising edge: {@code debounce}, falling edge: {@code debounce}, initially {@code false}
     *
     * @param debounce The debounce time in seconds
     * @param type Rising, falling, or both edges
     */
    public Debouncer(double debounce, DebounceType type) {
        this(
                type == DebounceType.Falling ? 0 : debounce,
                type == DebounceType.Rising ? 0 : debounce,
                type == DebounceType.Falling
        );
    }

    /**
     * Applies the debouncer to the input stream.
     *
     * @param input The measured value
     * @return The debounced value
     */
    public boolean calculate(boolean input) {
        if (input != lastInput) {
            lastInput = input;
            timer.reset();
        }

        double debounce = input ? debounceRising : debounceFalling;

        // This will still work before the first input change
        if (timer.seconds() >= debounce) {
            state = input;
        }

        return state;
    }
}
