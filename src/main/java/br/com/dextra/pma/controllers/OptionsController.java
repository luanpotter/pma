package br.com.dextra.pma.controllers;

import java.util.Map;

import br.com.dextra.pma.main.Options;
import br.com.dextra.pma.main.PMAContext;
import br.com.dextra.pma.parser.Controller;
import br.com.dextra.pma.parser.Output;

public class OptionsController extends Controller<PMAContext> {

    public Output list(Map<String, String> params) {
        Controller.empty(params);
        final Output out = new Output();
        context.o().list((o, v) -> {
            out.add(o + " : " + v);
        });
        return out;
    }

    public Output get(Map<String, String> params) {
        Controller.optional(Controller.required(params, "option"));
        String option = params.get("option");
        try {
            return new Output(context.o().get(option));
        } catch (Options.InvalidOptionExcpetion e) {
            return new Output("Invalid option " + option);
        }
    }

    public Output set(Map<String, String> params) {
        Controller.optional(Controller.required(params, "option", "value"));
        String option = params.get("option");
        String value = params.get("value");
        try {
            context.o().set(option, value);
            context.o().save();
            return new Output("Option updated with success.");
        } catch (Options.InvalidOptionExcpetion e) {
            return new Output("Invalid option " + option);
        }
    }
}