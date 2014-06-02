public class Action {

  public enum Keyword {
    HERE("here"),
    EXIT("exit"),
    SAVE("save"),
    LOG("log"),
    TODAY("today"),
    LIST("list");

    public String controller;

    private Keyword(String controller) {
      this.controller = controller;
    }
  }

  private String pattern;
  private Call predefCall;

  public Action(Keyword keyword) {
    this("0", keyword);
  }

  public Action(Keyword keyword, String pattern) {
    this(keyword, pattern, new HashMap<String, String>());
  }

  public Action(Keyword keyword, String pattern, HashMap<String, String> argumentValues) {
    this.keyword = keyword;
    this.predefCall = new Call(keyword, argumentValues);
  }

  public Map<String, String> parse(String[] args, Words words) {
    String[] sections = this.pattern.split(" ");
    if (words.aliases.get(args[0]) != predefCall.getKeyword()) {
      return null; // no match
    }

    return parsePattern(sections, args, words, predefCall.getArgs());
  }

  private static Map<String, String> parsePattern(String[] sections, String[] args, Words words, Map<String, String> defaults) {
    if (sections.length != args.length - 1) {
      return null; //no match
    }

    Map<String, String> map = new HashMap<>(defaults);
    for (int i = 0; i < sections.length; i++) {
      String section = sections[i];
      if (section.chartAt(0) == ':') {
        if (words.connectors.get(args[i + 1]) != section) {
          return null; //no match
        }
      } else {
        map.put(sections[i], args[i + 1]);
      }
    }
  }

  public static class Task {

    private String pattern;
    private List<Action> actions;

    public Task(String pattern, List<Action> actions) {
      this.pattern = pattern;
      this.actions = actions;
    }

    public HashMap<String, String> parse(String[] args, Words words) {
      String[] sections = this.pattern.split(" ");
      return Action.parsePattern(sections, args, words, new HashMap<String, String>());
    }

  }
}