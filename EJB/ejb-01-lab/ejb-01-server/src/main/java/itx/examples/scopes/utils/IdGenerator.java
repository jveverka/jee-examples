package itx.examples.scopes.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
	
	private static AtomicInteger counter;
	
	static {
		counter = new AtomicInteger(1000);
	}
	
	public static int getNextId() {
		return counter.getAndIncrement();
	}

}
