package br.com.dextra.pma.controller;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.model.Period;
import br.com.dextra.pma.model.TimedPeriod;

@FnController
public class PeriodsController extends BaseController {

	@Action("list")
	public CallResult list() {
		console.result("Default hours per day: " + context.t().getDefaultHoursPerDay());
		console.tabIn();
		int i = 0;
		for (TimedPeriod period : context.t().getPeriods()) {
			console.result("[" + i++ + "] " + period);
		}
		console.tabOut();
		return CallResult.SUCCESS;
	}

	@Action("add")
	public CallResult add(NullableDate start, NullableDate end, Integer hoursPerDay) {
		context.t().getPeriods().add(new TimedPeriod(new Period(start, end), hoursPerDay));
		context.t().save();
		return CallResult.SUCCESS;
	}

	@Action("remove")
	public CallResult remove(Integer index) {
		context.t().getPeriods().remove((int) index);
		context.t().save();
		return CallResult.SUCCESS;
	}

	@Action("getDefault")
	public CallResult getDefault() {
		console.result(context.t().getDefaultHoursPerDay());
		return CallResult.SUCCESS;
	}

	@Action("setDefault")
	public CallResult setDefault(Integer defaultHoursPerDay) {
		context.t().setDefaultHoursPerDay(defaultHoursPerDay);
		context.t().save();
		return CallResult.SUCCESS;
	}

	public static void defaultCallables(String name, List<Callable> callables) {
		callables.add(new ActionCall(name + ":list", ":periods :list", "List current periods"));
		callables.add(new ActionCall(name + ":add", ":periods :add start end hoursPerDay", "Add new period"));
		callables.add(new ActionCall(name + ":remove", ":periods :remove index", "Remove period at given index"));

		callables.add(new ActionCall(name + ":getDefault", ":periods :default :get", "Get default hours per day"));
		callables.add(new ActionCall(name + ":setDefault", ":periods :default :set defaultHoursPerDay", "Set default hours per day"));
	}
}