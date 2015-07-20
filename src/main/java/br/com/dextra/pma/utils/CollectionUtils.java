package br.com.dextra.pma.utils;

import java.util.Iterator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionUtils {

	public <T> T remove(Iterable<T> iterable, int n) {
		Iterator<T> it = iterate(iterable, n);
		T toRemove = it.next();
		it.remove();
		return toRemove;
	}

	public <T> T get(Iterable<T> iterable, int n) {
		return iterate(iterable, n).next();
	}

	private <T> Iterator<T> iterate(Iterable<T> iterable, int n) {
		Iterator<T> it = iterable.iterator();
		if (n < 0) {
			throw new ArrayIndexOutOfBoundsException(n);
		}
		for (int i = 0; i < n; i++) {
			if (!it.hasNext()) {
				throw new ArrayIndexOutOfBoundsException(n);
			}
			it.next();
		}
		if (!it.hasNext()) {
			throw new ArrayIndexOutOfBoundsException(n);
		}
		return it;
	}
}
