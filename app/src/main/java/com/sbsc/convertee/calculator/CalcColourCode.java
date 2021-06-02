package com.sbsc.convertee.calculator;

import android.graphics.Color;
import android.util.Log;

import com.sbsc.convertee.ui.converter.UnitConverterViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcColourCode {

    private UnitConverterViewModel viewModel;

    // SINGLETON
    private static CalcColourCode calcColourCode;

    private CalcColourCode(){ }

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static CalcColourCode getInstance(){
        if (calcColourCode == null){ //if there is no instance available... create new one
            calcColourCode = new CalcColourCode();
        }
        return calcColourCode;
    }

    // Delete singleton instance
    public static void deleteInstance(){
        calcColourCode = null;
    }

    /**
     * ViewModel is needed for changing the displayed colour in it
     * @param viewModel - UnitConverterViewModel
     */
    public void setViewModel( UnitConverterViewModel viewModel ) { this.viewModel = viewModel; }

    /**
     * Change colour in ViewModel
     * @param colour - either RGB or HEX expected
     */
    private void setColourInViewModel( CColour colour ){
        if( viewModel == null ) return;
        if( colour == null ){
            viewModel.setDisplayedColour( null );
        }else if( colour instanceof HEX )
            viewModel.setDisplayedColour( (HEX) colour );
        else
            viewModel.setDisplayedColour( new HEX((RGB) colour) );
    }

    /**
     * Get currency with value, values original size, and target size
     * @param valueString String size value
     * @param originUnit String original size
     * @param targetUnit String target size
     * @return value in target size
     */
    public String getResultFor( String valueString , String originUnit , String targetUnit){

        CColour value = getValueAsObject( valueString , originUnit );
        RGB rgbColour = getValueInRGB(value);
        value = getValueInTarget( rgbColour , targetUnit );

        // show in fragment
        if ((targetUnit.equals("colourhex"))) {
            setColourInViewModel(value);
        } else {
            setColourInViewModel(rgbColour);
        }

        if(value!=null)
            return value.toString();
        else
            return "N/A";
    }

    /**
     * Turn specific colour object into an RGB colour by converting
     * @param value CColour to be converted
     * @return converted RGB colour
     */
    private RGB getValueInRGB( CColour value ){

        if( value instanceof RGB ){
            return (RGB) value;

        }else if( value instanceof HSL ){
            HSL tempHsl = (HSL) value;
            int[] rgbArr = CalcColourHelper.hslToRgb( tempHsl.hue , tempHsl.saturation, tempHsl.lightness);
            return new RGB( rgbArr[0],rgbArr[1],rgbArr[2] );

        }else if( value instanceof HSV ){
            HSV tempHsv = (HSV) value;
            float[] hsvArr = { tempHsv.hue , tempHsv.saturation/100f , tempHsv.value/100f };
            int hsvColor = Color.HSVToColor(hsvArr);
            return new RGB( Color.red(hsvColor),Color.green(hsvColor),Color.blue(hsvColor) );

        }else if( value instanceof HEX ){
            int[] rgbArr = CalcColourHelper.hexToRgb( value.toString() );
            return new RGB( rgbArr[0],rgbArr[1],rgbArr[2] );

        }else if( value instanceof CMYK ){
            CMYK tempCmyk = (CMYK) value;
            int[] rgbArr = CalcColourHelper.cmykToRgb( tempCmyk.cyan , tempCmyk.magenta , tempCmyk.yellow , tempCmyk.black );
            return new RGB( rgbArr[0],rgbArr[1],rgbArr[2] );

        }else if( value instanceof ColourName ){
            ColourName tempName = (ColourName) value;
            if(tempName.equals(colourNames[0])) return null;
            for( ColourName colName : colourNames ){
                if(colName.name.equalsIgnoreCase(tempName.name)) return colName.colourCode;
            }
        }

        return null;
    }

    /**
     * Takes an RGB colour and converts it into a specific other colour
     * @param value RGB Colour
     * @param targetUnit Target Colour
     * @return CColour value of RGB converted into target colour
     */
    private CColour getValueInTarget( RGB value , String targetUnit ){

        if( value == null ) return null;

        switch( targetUnit ){

            case "colourrgb":
                return value;

            case "colourhex":
                return new HEX(value);

            case "colourhsl":
                float[] hslArr = CalcColourHelper.rgbToHsl( value.red , value.green , value.blue );
                return new HSL( hslArr[0] , hslArr[1] , hslArr[2] );

            case "colourhsv":
                float[] hsvArr = new float[3];
                Color.RGBToHSV( value.red , value.green , value.blue , hsvArr );
                return new HSV( hsvArr[0] , hsvArr[1]*100 , hsvArr[2]*100 );

            case "colourcmyk":
                int[] cmykArr = CalcColourHelper.rgbToCmyk( value.red , value.green , value.blue );
                return new CMYK( cmykArr[0] , cmykArr[1] , cmykArr[2] , cmykArr[3] );

            case "colourname":
                for( ColourName colName : colourNames ){
                    if( colName.colourCode.red == value.red &&
                        colName.colourCode.green == value.green &&
                        colName.colourCode.blue == value.blue
                    ) return colName;
                }
                return colourNames[0];
        }

        return null;
    }

    /**
     * Turns a value and a colour name into a colour object
     * @param value colour value
     * @param unitName colour name
     * @return new ColourObject of type CColour
     */
    private CColour getValueAsObject( String value , String unitName ){

        value = value.replaceAll("\\s","");

        switch( unitName ){

            case "colourrgb":

                if(value.matches("(?i)(rgb)?\\(?(\\b(?:1\\d{2}|2[0-4]\\d|[1-9]?\\d|25[0-5])\\b,){2}(\\b(?:1\\d{2}|2[0-4]\\d|[1-9]?\\d|25[0-5])\\b)\\)?")){
                    Matcher matcher = Pattern.compile("\\d{1,3},\\d{1,3},\\d{1,3}").matcher( value );
                    if(matcher.find()){
                        return new RGB( matcher.group() );
                    }
                }
                break;

            case "colourhex":

                if(value.matches("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")){
                    return new HEX( value );
                }
                break;

            case "colourhsl":

                if(value.matches("(?i)(hsl)?\\(?(\\b([1-2]?[0-9]?[0-9]|3[0-5][0-9]|360)\\b°?),([1-9]?\\d|100)%?,([1-9]?\\d|100)%?\\)?")){
                    value = value.replaceAll("%","");
                    value = value.replaceAll("°","");
                    Matcher matcher = Pattern.compile("\\d{1,3},\\d{1,3},\\d{1,3}").matcher( value );
                    if(matcher.find()){
                        return new HSL( matcher.group() );
                    }
                }
                break;

            case "colourhsv":

                if(value.matches("(?i)(hsl)?\\(?(\\b([1-2]?[0-9]?[0-9]|3[0-5][0-9]|360)\\b°?),([1-9]?\\d|100)%?,([1-9]?\\d|100)%?\\)?")){
                    value = value.replaceAll("%","");
                    value = value.replaceAll("°","");
                    Matcher matcher = Pattern.compile("\\d{1,3},\\d{1,3},\\d{1,3}").matcher( value );
                    if(matcher.find()){
                        return new HSV( matcher.group() );
                    }
                }
                break;

            case "colourcmyk":

                if(value.matches("(?i)(hsl)?\\(?(\\b([1-9]?\\d|100)\\b%?,){3}(\\b([1-9]?\\d|100)\\b%?)\\)?")){
                    value = value.replaceAll("%","");
                    Matcher matcher = Pattern.compile("(\\d{1,3},){3}\\d{1,3}").matcher( value );
                    if(matcher.find()){
                        return new CMYK( matcher.group() );
                    }
                }
                break;

            case "colourname":
                for( ColourName colName : colourNames ){
                    String name = colName.name.replaceAll("\\s","");
                    if( name.equalsIgnoreCase(value) ) return colName;
                }
                // First entry says "No name"
                return colourNames[0];
        }

        return null;
    }

    public boolean isCorrectInput( String value , String unitKey ){
        if( getValueAsObject( value , unitKey ) == null )
            return false;
        else
            return true;
    }

    /**
     * Colour parent class
     */
    private static class CColour{ }

    /**
     * RGB colour class
     */
    private static class RGB extends CColour{
        int red;
        int green;
        int blue;

        RGB( int red , int green , int blue ){
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        RGB( String rgbText ){
            String[] arr = rgbText.split(",");
            if(arr.length==3){
                red = Integer.parseInt(arr[0]);
                green = Integer.parseInt(arr[1]);
                blue = Integer.parseInt(arr[2]);
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "RGB( " + red + " , " + green + " , " + blue + " )";
        }
    }

    /**
     * HEX colour class
     */
    public static class HEX extends CColour{
        String red;
        String green;
        String blue;

        HEX( RGB rgb ){
            this( String.format("#%02X%02X%02X", rgb.red, rgb.green, rgb.blue) );
        }

        public HEX( String hexText ){
            hexText = hexText.replaceAll("#","");
            if(hexText.length()==6){
                red = hexText.substring(0,2);
                green = hexText.substring(2,4);
                blue = hexText.substring(4,6);
            }else if(hexText.length()==3){
                red = hexText.charAt(0)+""+hexText.charAt(0);
                green = hexText.charAt(1)+""+hexText.charAt(1);
                blue = hexText.charAt(2)+""+hexText.charAt(2);
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "#" + red + green + blue;
        }

        public double getRed() { return Integer.parseInt(red,16); }
        public double getGreen() { return Integer.parseInt(green,16); }
        public double getBlue() { return Integer.parseInt(blue,16); }
    }

    /**
     * HSL colour class
     */
    private static class HSL extends CColour{
        int hue;
        int saturation;
        int lightness;

        HSL( float hue , float saturation , float lightness ){
            this.hue = (int) Math.round(hue * 360);
            this.saturation = (int) Math.round(saturation * 100);
            this.lightness = (int) Math.round(lightness * 100);
        }

        HSL( String hslText ){
            String[] arr = hslText.split(",");
            if(arr.length==3){
                hue = Integer.parseInt(arr[0]);
                saturation = Integer.parseInt(arr[1]);
                lightness = Integer.parseInt(arr[2]);
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "( " + hue + " , " + saturation + "% , " + lightness + "% )";
        }
    }

    /**
     * HSV colour class
     */
    private static class HSV extends CColour{
        int hue;
        int saturation;
        int value;

        HSV( float hue, float saturation, float value ) {
            this.hue = (int) hue;
            this.saturation = (int) saturation;
            this.value = (int) value;
        }

        HSV(String hslText ){
            String[] arr = hslText.split(",");
            if(arr.length==3){
                hue = Integer.parseInt(arr[0]);
                saturation = Integer.parseInt(arr[1]);
                value = Integer.parseInt(arr[2]);
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "( " + hue + " , " + saturation + "% , " + value + "% )";
        }
    }

    /**
     * CMYK colour class
     */
    private static class CMYK extends CColour{
        int cyan;
        int magenta;
        int yellow;
        int black;

        CMYK( int cyan , int magenta , int yellow , int black ){
            this.cyan = cyan;
            this.magenta = magenta;
            this.yellow = yellow;
            this.black = black;
        }

        CMYK( String cmykText ){
            String[] arr = cmykText.split(",");
            if(arr.length==4){
                cyan = Integer.parseInt(arr[0]);
                magenta = Integer.parseInt(arr[1]);
                yellow = Integer.parseInt(arr[2]);
                black = Integer.parseInt(arr[3]);
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "(" + cyan + "%, " + magenta + "%, " + yellow + "%, " + black + "%)";
        }
    }

    /**
     * ColourName colour class
     */
    private static class ColourName extends CColour{
        String name;
        RGB colourCode;

        ColourName( String text , int red , int green , int blue ){
            this.name = text;
            this.colourCode = new RGB( red , green , blue );
        }

        @NotNull
        @Override
        public String toString() {
            return name;
        }
    }


    /**
     * Pre-defined colour names from set values
     * Source: https://flaviocopes.com/rgb-color-codes/
     */
    private final ColourName[] colourNames = {
            new ColourName( "No name" , -1 , -1 , -1 ),
            new ColourName( "Black" , 0 , 0 , 0 ),
            new ColourName( "White" , 255 , 255 , 255 ),
            new ColourName( "Maroon" , 128 , 0 , 0 ),
            new ColourName( "DarkRed" , 139 , 0 , 0 ),
            new ColourName( "Brown" , 162 , 42 , 42 ),
            new ColourName( "firebrick" , 178 , 34 , 34 ),
            new ColourName( "crimson" , 220 , 20 , 60 ),
            new ColourName( "red" , 255 , 0 , 0 ),
            new ColourName( "tomato" , 255 , 99 , 71 ),
            new ColourName( "coral" , 255 , 127 , 80 ),
            new ColourName( "indian Red" , 205 , 92 , 92 ),
            new ColourName( "light coral" , 240 , 128 , 128),
            new ColourName( "dark salmon" , 233,150,122 ),
            new ColourName( "salmon" , 250,128,114 ),
            new ColourName( "light salmon" , 255,160,122 ),
            new ColourName( "orange red" , 255,69,0 ),
            new ColourName( "dark orange" , 255,140,0 ),
            new ColourName( "orange" , 255,165,0 ),
            new ColourName( "gold" , 255,215,0 ),
            new ColourName( "dark golden rod" , 184,134,11 ),
            new ColourName( "golden rod" , 218,165,32 ),
            new ColourName( "pale golden rod" , 238,232,170 ),
            new ColourName( "dark khaki" , 189,183,107 ),
            new ColourName( "khaki" , 240,230,140 ),
            new ColourName( "olive" , 128,128,0 ),
            new ColourName( "yellow" , 255,255,0 ),
            new ColourName( "yellow green" , 154,205,50 ),
            new ColourName( "dark olive green" , 85,107,47 ),
            new ColourName( "olive drab" , 107,142,35 ),
            new ColourName( "lawn green" , 124,252,0 ),
            new ColourName( "chart reuse" , 127,255,0 ),
            new ColourName( "green yellow" , 173,255,47 ),
            new ColourName( "dark green" , 0,100,0 ),
            new ColourName( "green" , 0,128,0 ),
            new ColourName( "forest green" , 34,139,34 ),
            new ColourName( "lime" , 0,255,0 ),
            new ColourName( "lime green" , 50,205,50 ),
            new ColourName( "light green" , 144,238,144 ),
            new ColourName( "pale green" , 152,251,152 ),
            new ColourName( "dark sea green" , 143,188,143 ),
            new ColourName( "medium spring green" , 0,250,154 ),
            new ColourName( "spring green" , 0,255,127 ),
            new ColourName( "sea green" , 46,139,87 ),
            new ColourName( "medium aqua marine" , 102,205,170 ),
            new ColourName( "medium sea green" , 60,179,113 ),
            new ColourName( "light sea green" , 32,178,170 ),
            new ColourName( "dark slate gray" , 47,79,79 ),
            new ColourName( "teal" , 0,128,128 ),
            new ColourName( "dark cyan" , 0,139,139 ),
            new ColourName( "aqua" , 0,255,255 ),
            new ColourName( "cyan" , 0,255,255 ),
            new ColourName( "light cyan" , 224,255,255 ),
            new ColourName( "dark turquoise" , 0,206,209 ),
            new ColourName( "turquoise" , 64,224,208 ),
            new ColourName( "medium turquoise" , 72,209,204 ),
            new ColourName( "pale turquoise" , 175,238,238 ),
            new ColourName( "aqua marine" , 127,255,212 ),
            new ColourName( "powder blue" , 176,224,230 ),
            new ColourName( "cadet blue" , 95,158,160 ),
            new ColourName( "steel blue" , 70,130,180 ),
            new ColourName( "corn flower blue" , 100,149,237 ),
            new ColourName( "deep sky blue" , 0,191,255 ),
            new ColourName( "dodger blue" , 30,144,255 ),
            new ColourName( "light blue" , 173,216,230 ),
            new ColourName( "sky blue" , 135,206,235 ),
            new ColourName( "light sky blue" , 135,206,250 ),
            new ColourName( "midnight blue" , 25,25,112 ),
            new ColourName( "navy" , 0,0,128 ),
            new ColourName( "dark blue" , 0,0,139 ),
            new ColourName( "medium blue" , 0,0,205 ),
            new ColourName( "blue" , 0,0,255 ),
            new ColourName( "royal blue" , 65,105,225 ),
            new ColourName( "blue violet" , 138,43,226 ),
            new ColourName( "indigo" , 75,0,130 ),
            new ColourName( "dark slate blue" , 72,61,139 ),
            new ColourName( "slate blue" , 106,90,205 ),
            new ColourName( "medium slate blue" , 123,104,238 ),
            new ColourName( "medium purple" , 147,112,219 ),
            new ColourName( "dark magenta" , 139,0,139 ),
            new ColourName( "dark violet" , 148,0,211 ),
            new ColourName( "dark orchid" , 153,50,204 ),
            new ColourName( "medium orchid" , 186,85,211 ),
            new ColourName( "purple" , 128,0,128 ),
            new ColourName( "thistle" , 216,191,216 ),
            new ColourName( "plum" , 221,160,221 ),
            new ColourName( "violet" , 238,130,238 ),
            new ColourName( "magenta" , 255,0,255 ),
            new ColourName( "fuchsia" , 255,0,255 ),
            new ColourName( "orchid" , 218,112,214 ),
            new ColourName( "medium violet red" , 199,21,133 ),
            new ColourName( "pale violet red" , 219,112,147 ),
            new ColourName( "deep pink" , 255,20,147 ),
            new ColourName( "hot pink" , 255,105,180 ),
            new ColourName( "light pink" , 255,182,193 ),
            new ColourName( "pink" , 255,192,203 ),
            new ColourName( "antique white" , 250,235,215 ),
            new ColourName( "beige" , 245,245,220 ),
            new ColourName( "bisque" , 255,228,196 ),
            new ColourName( "blanched almond" , 255,235,205 ),
            new ColourName( "wheat" , 245,222,179 ),
            new ColourName( "corn silk" , 255,248,220 ),
            new ColourName( "lemon chiffon" , 255,250,205 ),
            new ColourName( "light golden rod yellow" , 250,250,210 ),
            new ColourName( "light yellow" , 255,255,224 ),
            new ColourName( "saddle brown" , 139,69,19 ),
            new ColourName( "sienna" , 160,82,45 ),
            new ColourName( "chocolate" , 210,105,30 ),
            new ColourName( "peru" , 205,133,63 ),
            new ColourName( "sandy brown" , 244,164,96 ),
            new ColourName( "burly wood" , 222,184,135 ),
            new ColourName( "tan" , 210,180,140 ),
            new ColourName( "rosy brown" , 188,143,143 ),
            new ColourName( "moccasin" , 255,228,181 ),
            new ColourName( "navajo white" , 255,222,173 ),
            new ColourName( "peach puff" , 255,218,185 ),
            new ColourName( "misty rose" , 255,228,225 ),
            new ColourName( "lavender blush" , 255,240,245 ),
            new ColourName( "linen" , 250,240,230 ),
            new ColourName( "old lace" , 253,245,230 ),
            new ColourName( "papaya whip" , 255,239,213 ),
            new ColourName( "sea shell" , 255,245,238 ),
            new ColourName( "mint cream" , 245,255,250 ),
            new ColourName( "slate gray" , 112,128,144 ),
            new ColourName( "light slate gray" , 119,136,153 ),
            new ColourName( "light steel blue" , 176,196,222 ),
            new ColourName( "lavender" , 230,230,250 ),
            new ColourName( "floral white" , 255,250,240 ),
            new ColourName( "alice blue" , 240,248,255 ),
            new ColourName( "ghost white" , 248,248,255 ),
            new ColourName( "honeydew" , 240,255,240 ),
            new ColourName( "ivory" , 255,255,240 ),
            new ColourName( "azure" , 240,255,255 ),
            new ColourName( "snow" , 255,250,250 ),
            new ColourName( "dark gray" , 105,105,105 ),
            new ColourName( "gray" , 128,128,128 ),
            new ColourName( "dim gray" , 169,169,169 ),
            new ColourName( "silver" , 128,128,128 ),
            new ColourName( "light gray" , 211,211,211 ),
            new ColourName( "gainsboro" , 220,220,220 ),
            new ColourName( "white smoke" , 245,245,245 ),
    };
}
