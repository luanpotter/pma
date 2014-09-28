package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.parser.ActionCall;
import xyz.luan.console.parser.ActionRef;
import xyz.luan.console.parser.Callable;
import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.Output;
import xyz.luan.console.parser.Pattern;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Required;
import br.com.dextra.pma.main.Options;
import br.com.dextra.pma.main.PMAContext;

public class OptionsController extends Controller<PMAContext> {

    @Action("list")
    public Output list() {
        final Output out = new Output();
        context.o().list((o, v) -> {
            out.add(o + " : " + v);
        });
        return out;
    }

    @Action("get")
    public Output get(@Required String option) {
        try {
            return new Output(context.o().get(option));
        } catch (Options.InvalidOptionExcpetion e) {
            return new Output("Invalid option " + option);
        }
    }

    @Action("set")
    public Output set(@Required String option, @Required String value) {
        try {
            context.o().set(option, value);
            context.o().save();
            return new Output("Option updated with success.");
        } catch (Options.InvalidOptionExcpetion e) {
            return new Output("Invalid option " + option);
        }
    }
    
    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":list", ":options", "List all options with their values"));
        callables.add(new ActionCall(name + ":get", ":options :get option", "Return the current value of option"));
        callables.add(new ActionCall(new ActionRef(name, "set"), new Pattern(":options :set option value", true), "Set the value of option to value"));
    }
}