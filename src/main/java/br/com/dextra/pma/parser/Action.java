package br.com.dextra.pma.parser;

import java.util.HashMap;
import java.util.Map;

public class Action implements Callable {

    /**
     * 
     */
    private static final long serialVersionUID = 5347353630808864913L;
    private String description;
    private Pattern pattern;
    private Call predefCall;
    private Map<String, String> argsMapping;

    public Action(Keyword keyword, Pattern pattern, String description) {
        this(keyword, pattern, new HashMap<String, String>(), description);
    }

    public Action(Keyword keyword, Pattern pattern, Map<String, String> argsValues, String description) {
        this(keyword, pattern, argsValues, null, description);
    }

    public Action(Keyword keyword, Pattern pattern, Map<String, String> argsValues, Map<String, String> argsMapping, String description) {
        this.pattern = pattern;
        this.predefCall = new Call(keyword, argsValues);
        this.argsMapping = argsMapping;
        this.description = description;
    }

    public Call parseAction(String[] args, Map<String, String> map) {
        if (map == null) {
            return null; // no match
        }

        if (argsMapping != null) {
            Map<String, String> realMap = new HashMap<>();
            for (String actualArgument : argsMapping.keySet()) {
                realMap.put(actualArgument, map.get(argsMapping.get(actualArgument)));
            }
            map = realMap;
        }

        return predefCall.newCall(map);
    }

    @Override
    public Call[] parse(String[] args, Map<String, String> aliases) {
        Call call = parseAction(args, pattern.parse(args, aliases));
        if (call == null) {
            return null;
        } else {
            return new Call[] { call };
        }
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Pattern getPattern() {
        return this.pattern;
    }
}
