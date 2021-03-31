package com.pikachu.framework.caching.datas.matchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LikeComparer implements IComparer {

    private Pattern pattern;

    public LikeComparer(String src) {
        String temp = "";
        if (src.startsWith("%")) {
            src = src.substring(1);
        } else {
            temp = temp + "^";
        }

        boolean isEndPercent = false;
        if (src.endsWith("%")) {
            src = src.substring(0, src.length() - 1);
        } else {
            isEndPercent = true;
        }

        src = src.replaceAll("%+", "%");
        src = src.replaceAll("\\[%\\]", "__AX__");
        Pattern pattern = Pattern.compile("([\\$\\(\\)\\[\\]\\{\\}\\*\\+\\-\\?\\\\\\^\\|\\.\\\\])", 8);
        Matcher matcher = pattern.matcher(src);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, "\\\\" + matcher.group());
        }

        matcher.appendTail(sb);
        temp = sb.toString().replaceAll("%", ".*");
        temp = temp.replaceAll("__AX__", "%");
        if (isEndPercent) {
            temp = temp + "$";
        }

        try {
            this.pattern = Pattern.compile(temp, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean compare(Object first, Object second) {
        if (this.pattern != null && first != null) {
            Matcher matcher = this.pattern.matcher(first.toString());
            return matcher.find();
        } else {
            return false;
        }
    }
}
