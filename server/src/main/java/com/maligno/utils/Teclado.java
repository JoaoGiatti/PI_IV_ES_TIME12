package com.maligno.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Teclado
{
    private static BufferedReader teclado =
                   new BufferedReader (
                   new InputStreamReader (
                   System.in));

    public static String getUmString () {
        String ret=null;

        try {
            ret = teclado.readLine ();
        }
        catch (IOException erro) {} // sei que não vai dar erro

        return ret;
    }
}
