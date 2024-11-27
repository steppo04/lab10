package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 */
public final class DrawNumberImpl implements DrawNumber {

    private int choice;
    private final int min;
    private final int max;
    private final int attempts;
    private int remainingAttempts;
    private final Random random = new Random();
    private final File f = new File("src/main/resources/config.yml");
    /**
     * @throws IllegalStateException if the configuration is not consistent
     * @param min numero
     * @param max numero
     * @param attempts tentativi
     */
    public DrawNumberImpl(final int min, final int max, final int attempts) {
        String v;
        int fmin = min;
        int fmax = max;
        int fattempts = attempts;
        String line;
        try (BufferedReader bfile = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            line = bfile.readLine();
            while (line != null) {
                final StringTokenizer st = new StringTokenizer(line, ": ");
                v = st.nextToken();
                if (v.contains("minimum")) {
                    fmin = Integer.parseInt(st.nextToken());
                } else if (v.contains("maximum")) {
                    fmax = Integer.parseInt(st.nextToken());
                } else {
                    fattempts = Integer.parseInt(st.nextToken());
                }
                line = bfile.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); //NOPMD
        }
        this.min = fmin;
        this.max = fmax;
        this.attempts = fattempts;
        this.reset();
    }

    @Override
    public void reset() {
        this.remainingAttempts = this.attempts;
        this.choice = this.min + random.nextInt(this.max - this.min + 1);
    }

    @Override
    public DrawResult attempt(final int n) {
        if (this.remainingAttempts <= 0) {
            return DrawResult.YOU_LOST;
        }
        if (n < this.min || n > this.max) {
            throw new IllegalArgumentException("The number is outside boundaries");
        }
        remainingAttempts--;
        if (n > this.choice) {
            return DrawResult.YOURS_HIGH;
        }
        if (n < this.choice) {
            return DrawResult.YOURS_LOW;
        }
        return DrawResult.YOU_WON;
    }
}

