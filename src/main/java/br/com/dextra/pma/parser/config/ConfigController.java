package br.com.dextra.pma.parser.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.dextra.pma.parser.Action;
import br.com.dextra.pma.parser.Callable;
import br.com.dextra.pma.parser.Context;
import br.com.dextra.pma.parser.Controller;
import br.com.dextra.pma.parser.Keyword;
import br.com.dextra.pma.parser.Output;
import br.com.dextra.pma.parser.Parser;
import br.com.dextra.pma.parser.Pattern;

public class ConfigController extends Controller<Context> {

    public static final String[] MESSAGES = { "Alias added successfully.", "Alias already set to something else." };

    public Output addAlias(Map<String, String> args) {
        Controller.required(args, "alias", "keyword");

        boolean results = context.getParser().addAlias(args.get("alias"), args.get("keyword"));

        return new Output(MESSAGES[results ? 0 : 1]);
    }

    public Output listAliases(Map<String, String> args) {
        Controller.optional(args, "keyword");
        return context.getParser().listAliasesFor(args.get("keyword") == null ? null : ':' + args.get("keyword"));
    }

    public Output listKeywords(Map<String, String> args) {
        Controller.optional(args, "group", "keyword");
        String groupName = args.get("group");
        if (groupName == null) {
            return Parser.listKeywords();
        }
        Class<? extends Keyword> group = Parser.getGroupByName(groupName);
        if (group == null) {
            return new Output("Invalid group name. No keyword group found with that name.");
        }
        String keywordName = args.get("keyword");
        if (keywordName == null) {
            return Parser.listKeywordsForGroup(group);
        }
        Keyword keyword = Parser.getKeywordByNameAndGroup(group, keywordName);
        if (keyword == null) {
            return new Output("Invalid keyword name. No keyword found with that name.");
        }
        return new Output("Keyword is " + keyword);
    }

    public static List<Callable> getDefaultActions() {
        List<Callable> callables = new ArrayList<>(1);

        callables.add(new Action(ConfigKeyword.LIST_KEYWORDS, new Pattern(":config :keywords"), "List all keywords"));
        callables.add(new Action(ConfigKeyword.LIST_KEYWORDS, new Pattern(":config :keywords group"), "List all keywords in the group"));
        callables.add(new Action(ConfigKeyword.LIST_KEYWORDS, new Pattern(":config :keywords group keyword"),
                "Show information about a keyword"));
        callables
                .add(new Action(ConfigKeyword.ADD_ALIAS, new Pattern(":config :aliases :add alias :to keyword"), "Add an alias to keyword"));
        callables.add(new Action(ConfigKeyword.LIST_ALIASES, new Pattern(":config :aliases :list"), "List all aliases"));
        callables.add(new Action(ConfigKeyword.LIST_ALIASES, new Pattern(":config :aliases :list keyword"),
                "List all aliases for this keyword"));

        return callables;
    }
}