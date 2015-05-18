package br.com.dextra.pma.controllers;

import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.ExceptionHandler;
import xyz.luan.console.parser.actions.InvalidParameter;
import xyz.luan.console.parser.actions.parser.ArgumentParser;
import xyz.luan.console.parser.actions.parser.CustomParser;
import xyz.luan.console.parser.call.CallResult;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.exceptions.BaseRequirementException;
import br.com.dextra.pma.main.PmaContext;
import br.com.dextra.pma.parser.InvalidFormatException;

public class BaseController extends Controller<PmaContext> {

    static {
        ArgumentParser.parsers.put(Date.class, new CustomParser<Date>() {
            @Override
            public Date parse(String s) throws InvalidParameter {
                return new Date(s);
            }
        });
    }

    @ExceptionHandler({ BaseRequirementException.class, InvalidFormatException.class })
    public CallResult handle(Exception e) {
        console.error(e.getMessage());
        return CallResult.ERROR;
    }
}
