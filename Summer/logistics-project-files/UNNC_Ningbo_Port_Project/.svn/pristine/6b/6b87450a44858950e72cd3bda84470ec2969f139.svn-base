package server.engine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class implement the timeout mechanism of the server. The Connector will
 * pass a Future of a NetwoekController to construct this TimeoutController,
 * then the TimeoutController wait for 5s by calling Future.get. If the timeout
 * reaches before the NetworkController finish it's task, then the
 * TinmeoutrController will attempt to cancel the task.
 * 
 * Submitted as part of G52APR course work, 2012.
 * Re-used as part of Ningbo Port Project, 2013.
 * 
 * @author Jiaqi LI
 * 
 */
public class TimeoutController implements Runnable {

	private final int DEFALUT_TIME_OUT = 5;
	private final Future<Void> connection;
	
	@Override
	public void run() {
		try {
			connection.get(DEFALUT_TIME_OUT, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			connection.cancel(true);
		} catch (InterruptedException | ExecutionException e) {
			connection.cancel(true);
			System.out.println("Connection failed : "
					+ e.getCause().getMessage());
		}
	}

	public TimeoutController(Future<Void> connection) {
		this.connection = connection;
	}

}
