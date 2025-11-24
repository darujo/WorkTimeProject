package ru.darujo.assistant.helper;

public class CompareHelper {
    public static String compareField(String name, String oldStr, String newStr) {
        if (oldStr == null) {
            if (newStr == null) {
                return "";
            } else {
                return name + " стал: " + newStr + "\n";
            }
        } else if (newStr == null) {
            return name + ": " + oldStr + " -> " + "\"\"\n";
        } else {
            return newStr.equals(oldStr) ? "" : (name + ": " + oldStr + " -> " + newStr + "\n");
        }
    }
}
