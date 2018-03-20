package com.wwc.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServerRequest;

public class AsyncHandle {
	public void onRequest(AsyncResult<HttpServerRequest> result) {
		System.out.println("I am on Request hander! hander be invoked successfully");
	}
	
	/*public void onRequest(HttpServerRequest request) {
		System.out.println("I am on Request hander! hander be invoked successfully");
	}*/
}
