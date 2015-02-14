package br.com.dextra.pma.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {

    public String toString(int num, int digits) {
        String ns = String.valueOf(num);
        while (ns.length() < digits)
            ns = '0' + ns;
        return ns;
    }

    public String toString(int num) {
        return toString(num, 2);
    }
}