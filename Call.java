public class Call {
  private Keyword keyword;
  private HashMap<String, String> args;

  public Call(Keyword keyword, HashMap<String, String> args) {
    this.keyword = keyword;
    this.args = args;
  }

  public Keyword getKeyword() {
    return keyword;
  }

  public HashMap<String, String> getArgs() {
    return args;
  }
}
