package ir.syphix.palladiumshop.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {


    public static String toFormattedName(String name) {
        return Arrays.stream(name.split("_"))
                .map(t -> t.charAt(0) + t.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
