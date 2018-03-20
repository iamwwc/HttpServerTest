package com.wwc.vertx;

import java.io.File;

import io.vertx.core.Vertx;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Vertx.vertx().deployVerticle(new HttpBridge());
    	
        System.out.println( "I am server" );
    }
}
