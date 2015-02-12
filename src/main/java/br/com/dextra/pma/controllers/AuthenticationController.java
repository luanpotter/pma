package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.main.Wrapper;
import br.com.dextra.pma.main.Wrapper.InvalidLoginException;

@FnController
public class AuthenticationController extends BaseController {

    @Action("logout")
    public CallResult logout() {
        Wrapper.logout();
        return CallResult.SUCCESS;
    }

    @Action("login")
    public CallResult login() {
        try {
            if (Wrapper.isLoggedIn()) {
                console.error("You are already logged in! Run logout to choose a different user.");
                return CallResult.ERROR;
            }
            Wrapper.requestLogin(console);
            console.result("Successfully logged in.");
            return CallResult.SUCCESS;
        } catch (InvalidLoginException e) {
            console.error("Invalid login/password!");
            return CallResult.ERROR;
        }
    }

    @Action("update")
    public CallResult update() {
        console.result("Starting update...");
        context.p().update(console);
        console.result("Projects and tasks successfully updated.");
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":update", ":update", "Update the list of projects and tasks"));
        callables.add(new ActionCall(name + ":login", ":login", "Login to pma, creating a token that is valid for a few hours."));
        callables.add(new ActionCall(name + ":logout", ":logout", "Logout from pma, enabling you to choose a different user."));
    }
}
