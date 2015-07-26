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

	public <T> T first(Iterable<T> iterable) {
		return get(iterable, 0);
	}

	public <T> Iterable<T> tail(Iterable<T> iterable) {
		return new Iterable<T>() {

			private Iterator<T> it = iterate(iterable, 1);

			@Override
			public Iterator<T> iterator() {
				return it;
			}
		};
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
