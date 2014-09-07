package main;

public class NotLoggedIn extends RuntimeException {
  
  public NotLoggedIn() {
    super("Not logged in! Run pma_token to log in.");
  }
}