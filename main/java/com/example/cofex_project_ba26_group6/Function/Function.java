package com.example.cofex_project_ba26_group6.Function;

public class Function {

    public boolean isAlphaNumeric(String text) {
        boolean haveDigit = false;
        boolean haveAlpha = false;
        boolean validAlphaOrDigit = false;
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (!(character >= 'A' && character <= 'Z') &&
                    !(character >= 'a' && character <= 'z') &&
                    !(character >= '0' && character <= '9')) {
                return false;
            } else if (i == text.length() - 1) {
                validAlphaOrDigit = true;
            }
        }
        if (validAlphaOrDigit) {
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (!haveAlpha || !haveDigit) {
                    if ((character >= 'A' && character <= 'Z') ||
                            (character >= 'a' && character <= 'z')) {
                        haveAlpha = true;
                    }
                    if (character >= '0' && character <= '9') {
                        haveDigit = true;

                    }
                } else if (haveAlpha && haveDigit) {
                    return true;
                }
            }
        }
        return false;
    }
}
