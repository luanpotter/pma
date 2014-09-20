package br.com.dextra.pma.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Pattern implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1722841935156285867L;
    private String[] sections;
    private boolean continuous;

    public Pattern(String pattern) {
        this(pattern, false);
    }

    public Pattern(String pattern, boolean continuous) {
        this.continuous = continuous;
        this.sections = pattern.split(" ");
        this.assertValid();
    }

    private void assertValid() {
        if (this.sections.length == 0) {
            throw new IllegalArgumentException("Empty pattern");
        }
        for (String part : sections) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Empty argument in pattern");
            }
        }

        boolean lastArgIsKeyword = this.sections[this.sections.length - 1].startsWith(":");
        if (continuous && lastArgIsKeyword) {
            throw new IllegalArgumentException("If continuous is set to true, the last argument must be a variable");
        }
    }

    public Map<String, String> parse(String[] args, Map<String, String> aliases) {
        if (continuous) {
            if (sections.length > args.length) {
                return null;
            }
        } else {
            if (sections.length != args.length) {
                return null;
            }
        }

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < sections.length; i++) {
            String section = sections[i];
            if (section.charAt(0) == ':') {
                String connector = aliases.get(args[i]);
                if (!section.equals(connector)) {
                    return null; // no match
                }
            } else {
                String arg = args[i];
                if (continuous && i == sections.length - 1 && args.length > sections.length) {
                    for (int j = i + 1; j < args.length; j++) {
                        arg += " " + args[j];
                    }
                }
                map.put(section, arg);
            }
        }

        return map;
    }

    private transient String _toStringCache;

    private String buildToString() {
        StringBuilder pattern = new StringBuilder();
        pattern.append(sections[0]);
        for (int i = 1; i < sections.length; i++) {
            pattern.append(" " + sections[i]);
        }
        if (continuous) {
            pattern.append("...");
        }
        return pattern.toString();
    }

    @Override
    public String toString() {
        if (_toStringCache != null) {
            return _toStringCache;
        } else {
            return _toStringCache = buildToString();
        }
    }
}