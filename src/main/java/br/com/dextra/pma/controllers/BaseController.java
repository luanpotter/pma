package br.com.dextra.pma.controllers;

import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.ExceptionHandler;
import xyz.luan.console.parser.call.CallResult;
import br.com.dextra.pma.exceptions.BaseRequirementException;
import br.com.dextra.pma.main.FileParser.InvalidFormatException;
import br.com.dextra.pma.main.PmaContext;

public class BaseController extends Controller<PmaContext> {

    @ExceptionHandler({ BaseRequirementException.class, InvalidFormatException.class })
    public CallResult handle(Exception e) {
        console.error(e.getMessage());
        return CallResult.ERROR;
    }
}
