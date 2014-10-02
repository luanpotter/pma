package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.ActionRef;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import xyz.luan.console.parser.callable.Pattern;
import br.com.dextra.pma.main.Options;
import br.com.dextra.pma.main.PmaContext;

public class OptionsController extends Controller<PmaContext> {

    @Action("list")
    public CallResult list() {
        context.o().list((o, v) -> {
            console.result(o + " : " + v);
        });
        return CallResult.SUCCESS;
    }

    @Action("get")
    public CallResult get(String option) {
        try {
            console.result(context.o().get(option));
            return CallResult.SUCCESS;
        } catch (Options.InvalidOptionExcpetion e) {
            console.error("Invalid option " + option);
            return CallResult.ERROR;
        }
    }

    @Action("set")
    public CallResult set(String option, String value) {
        try {
            context.o().set(option, value);
            context.o().save();
            console.result("Option updated with success.");
            return CallResult.SUCCESS;
        } catch (Options.InvalidOptionExcpetion e) {
            console.error("Invalid option " + option);
            return CallResult.ERROR;
        }
    }
    
    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":list", ":options", "List all options with their values"));
        callables.add(new ActionCall(name + ":get", ":options :get option", "Return the current value of option"));
        callables.add(new ActionCall(new ActionRef(name, "set"), new Pattern(":options :set option value", true), "Set the value of option to value"));
    }
}