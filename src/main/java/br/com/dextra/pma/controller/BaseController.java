package br.com.dextra.pma.controller;

import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.ExceptionHandler;
import xyz.luan.console.parser.actions.InvalidParameter;
import xyz.luan.console.parser.actions.parser.ArgumentParser;
import xyz.luan.console.parser.actions.parser.CustomParser;
import xyz.luan.console.parser.call.CallResult;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.exception.BaseRequirementException;
import br.com.dextra.pma.main.PmaContext;
import br.com.dextra.pma.parser.InvalidFormatException;

public class BaseController extends Controller<PmaContext> {

	public static class NullableDate extends Date {

		private static final long serialVersionUID = 8528210705977016997L;

		public NullableDate(String date) {
			super(date);
		}
	}

	static {
		ArgumentParser.parsers.put(Date.class, new CustomParser<Date>() {
			@Override
			public Date parse(String s) throws InvalidParameter {
				return new Date(s);
			}
		});
		ArgumentParser.parsers.put(NullableDate.class, new CustomParser<NullableDate>() {
			@Override
			public NullableDate parse(String s) throws InvalidParameter {
				if (s.equals("null")) {
					return null;
				}
				return new NullableDate(s);
			}
		});

	}

	@ExceptionHandler({ BaseRequirementException.class, InvalidFormatException.class })
	public CallResult handle(Exception e) {
		console.error(e.getMessage());
		return CallResult.ERROR;
	}
}
