package ite.examples.template.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ite.examples.data.entities.User;

public class FutureUser implements Future<User> {
	
	private User user;
	
	public FutureUser() {
		user = null;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return user != null;
	}

	@Override
	public User get() throws InterruptedException, ExecutionException {
		synchronized (this) {
			if (user != null) return user;
			this.wait();
			return user;
		}
	}

	@Override
	public User get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		synchronized (this) {
			if (user != null) return user;
			this.wait(unit.toMillis(timeout));
			return user;
		}
	}
	
	public void setUser(User user) {
		synchronized (this) {
			this.user = user;
			this.notifyAll();
		}
	}

}
