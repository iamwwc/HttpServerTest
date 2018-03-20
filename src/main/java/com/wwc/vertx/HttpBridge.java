package com.wwc.vertx;
import javax.sound.midi.Receiver;

import org.apache.qpid.proton.engine.Session;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonReceiver;
import io.vertx.proton.ProtonSender;
import io.vertx.proton.ProtonServer;
import io.vertx.proton.ProtonSession;
public class HttpBridge extends AbstractVerticle{
	private ProtonServer server;
	private ProtonClient client;
	private static int count = 0;
	private Handler<HttpServerRequest> request;
	@Override
	public void start(Future<Void> startFuture) throws Exception{
		this.server = ProtonServer.create(vertx).connectHandler(this::ProcessConnection).listen(9999);
		
	}
	
	public void setMyRequestHandler(Handler<HttpServerRequest> hander){
		this.request = hander;
	}
	
	public void ProcessConnection(ProtonConnection connection) {
		connection
		.openHandler(this::ProcessOpenConnection)
		.closeHandler(this::ProcessCloseConnection)
		.disconnectHandler(this::ProcessDisconnection)
		.sessionOpenHandler(this::ProcessOpenSession)
		.senderOpenHandler(sender->{
			ProcessOpenSender(connection,sender);
		})
		.receiverOpenHandler(receiver->{
			ProcessOpenReceiver(connection,receiver);
		});
		
		System.out.println("ProcessConnection method invoked!");
	}
	public void ProcessDisconnection(ProtonConnection  connection) {
		System.out.println("ProcessDisconnection has been invoked");
	}
	
	public void ProcessOpenSession(ProtonSession connection) {
		System.out.println("ProcessOpenSession has been invoked!");
	}
	public void ProcessOpenReceiver(ProtonConnection connection, ProtonReceiver receiver) {
		System.out.println("Receiver!");
		receiver
		.setTarget(receiver.getRemoteTarget())
		.setAutoAccept(false)
		.closeHandler(ar->{
			ProcessCloseReceiver(ar.result());
		});
		
		receiver.handler((delivery,message)->{
			String address = message.getAddress();
			//String body = message.getBody();
			System.out.println("Message address is " + address);
		});
		receiver.open();
		
	}
	
	public void ProcessCloseReceiver(ProtonReceiver receiver) {
		receiver.close();
	}
	
	public void ProcessOpenSender(ProtonConnection connection, ProtonSender sender) {
		System.out.println("Sender!");
	}
	
	public void ProcessCloseConnection(AsyncResult<ProtonConnection> ar) {
		System.out.println("ProcessClossConnection has been invoked!");
	}
	
	public void ProcessOpenConnection(AsyncResult<ProtonConnection> ar) {
		System.out.println("Connection open by client");
		ProtonConnection connection = ar.result();
		connection.createReceiver("MyTopoc").handler((delivery,msg)->{
			System.out.println("Address is " + msg.getAddress());
		}).open();
	}
	
	public void myRequestHandler(HttpServerRequest request) {
		System.out.println("I am on Request hander! hander be invoked successfully");
	}
}