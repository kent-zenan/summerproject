package server.engine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import server.utils.ServerVariables;


/**
 * This is the engine of the server which accept incoming connecting from
 * Android device.
 * 
 * @author Jiaqi LI
 *
 */
public class Engine {

	private ServerSocket serverSocket;
	private ExecutorService requestExecutor;
	private ExecutorService timeoutExecutor;
	
	public Engine(String host, int port){
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(host, port));
		} catch (IOException ioe) {
			System.out.println("Can not listen on port: " + port
					+ "\nServer will stop.");
			System.exit(-1);
		}
		
		requestExecutor = Executors.newCachedThreadPool();
		timeoutExecutor = Executors.newCachedThreadPool();

		waitingConnection();
	}
	
	public void waitingConnection() {
		while (!ServerVariables.shutdown) {
			try {
				Socket socket = serverSocket.accept();
				Future<Void> connection = requestExecutor
						.submit(new NetworkController(socket));
				timeoutExecutor.execute(new TimeoutController(connection));
			} catch (IOException ioe) {
				System.out.println("Accept connection failed on "
						+ ServerVariables.DEFAULT_PORT);
			}
		}
	}
	
	public boolean shutdown(int timeout) {

		requestExecutor.shutdown();
		timeoutExecutor.shutdown();
		try {
			return requestExecutor.awaitTermination(timeout, TimeUnit.SECONDS)
					&& timeoutExecutor.awaitTermination(timeout,
							TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public void shutdownNow() {

		requestExecutor.shutdownNow();
		timeoutExecutor.shutdownNow();
	}
}
