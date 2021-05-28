package com.sbsc.convertee.calculator;

import android.util.Log;

public class CalcColourHelper {

    private static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r * 256));
        String gs = Integer.toHexString((int)(g * 256));
        String bs = Integer.toHexString((int)(b * 256));
        return rs + gs + bs;
    }

    public static int[] hexToRgb(String colorStr) {
        return new int[]{
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ) ,
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ) ,
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 )
        };
    }

    public static int[] rgbToCmyk(int r, int g, int b) {
        double percentageR = r / 255.0 * 100;
        double percentageG = g / 255.0 * 100;
        double percentageB = b / 255.0 * 100;

        double k = 100 - Math.max(Math.max(percentageR, percentageG), percentageB);

        if (k == 100) {
            return new int[]{ 0, 0, 0, 100 };
        }

        int c = (int)Math.round((100 - percentageR - k) / (100 - k) * 100);
        int m = (int)Math.round((100 - percentageG - k) / (100 - k) * 100);
        int y = (int)Math.round((100 - percentageB - k) / (100 - k) * 100);

        return new int[]{ c, m, y, (int)Math.round(k) };
    }

    public static int[] cmykToRgb(int c, int m, int y, int k) {
        int r = 255 * (1 - c/100) * (1 - k/100);
        int g = 255 * (1 - m/100) * (1 - k/100);
        int b = 255 * (1 - y/100) * (1 - k/100);

        return new int[]{ r, g, b };
    }


    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param h       The hue
     * @param s       The saturation
     * @param l       The lightness
     * @return int array, the RGB representation
     */
    public static int[] hslToRgb(float h, float s, float l){
        h = h / 360; s = s / 100; l = l / 100;
        Log.d("XD", "hslToRgb: "+h+" "+s+" "+l);
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        return new int[]{to255(r), to255(g), to255(b)};
    }
    private static int to255(float v) { return (int)Math.min(255,256*v); }

    /** Helper method that converts hue to rgb */
    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    /**
     * Converts an RGB color value to HSL. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes pR, pG, and bpBare contained in the set [0, 255] and
     * returns h, s, and l in the set [0, 1].
     *
     * @param pR       The red color value
     * @param pG       The green color value
     * @param pB       The blue color value
     * @return float array, the HSL representation
     */
    public static float[] rgbToHsl(int pR, int pG, int pB) {
        float r = pR / 255f;
        float g = pG / 255f;
        float b = pB / 255f;

        float max = (r > g && r > b) ? r : Math.max(g, b);
        float min = (r < g && r < b) ? r : Math.min(g, b);

        float h, s, l;
        l = (max + min) / 2.0f;

        if (max == min) {
            h = s = 0.0f;
        } else {
            float d = max - min;
            s = (l > 0.5f) ? d / (2.0f - max - min) : d / (max + min);

            if (r > g && r > b)
                h = (g - b) / d + (g < b ? 6.0f : 0.0f);

            else if (g > b)
                h = (b - r) / d + 2.0f;

            else
                h = (r - g) / d + 4.0f;

            h /= 6.0f;
        }

        return new float[]{h, s, l};
    }

}
