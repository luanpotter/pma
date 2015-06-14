package br.com.dextra.pma.service;

import java.util.HashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;
import br.com.dextra.pma.date.Date;

@UtilityClass
public class FeriadosService {

	private final int defaultYear = 0;
	private final Set<Date> feriados = new HashSet<>();
	static {
		feriados.add(new Date(defaultYear, 1, 1));
		feriados.add(new Date(defaultYear, 4, 3));
		feriados.add(new Date(defaultYear, 4, 21));
		feriados.add(new Date(defaultYear, 5, 1));
		feriados.add(new Date(defaultYear, 6, 4));
		feriados.add(new Date(defaultYear, 7, 9));
		feriados.add(new Date(defaultYear, 9, 7));
		feriados.add(new Date(defaultYear, 10, 12));
		feriados.add(new Date(defaultYear, 11, 2));
		feriados.add(new Date(defaultYear, 11, 20));
		feriados.add(new Date(defaultYear, 12, 8));
		feriados.add(new Date(defaultYear, 12, 25));
		/* TODO validar */
	}

	public boolean isFeriado(Date date) {
		Date onDefaultYear = new Date(defaultYear, date.getMonth(), date.getDay());
		return feriados.contains(onDefaultYear);
	}
}
