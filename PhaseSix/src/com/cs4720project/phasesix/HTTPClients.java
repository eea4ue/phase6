package com.cs4720project.phasesix;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HttpContext;

public class HTTPClients {

    private static DefaultHttpClient _defaultClient;
    private static String session_id;
    private static HTTPClients _me;
    private static ThreadSafeClientConnManager connMan;
    private HTTPClients() {

    }
    public static DefaultHttpClient getDefaultHttpClient(){
        if ( _defaultClient == null ) {
            _defaultClient = new DefaultHttpClient();
            _me = new HTTPClients();
           // _defaultClient.setKeepAliveStrategy((ConnectionKeepAliveStrategy) new DefaultConnectionReuseStrategy());
             //connMan=(ThreadSafeClientConnManager) _defaultClient.createClientConnectionManager();
            _defaultClient.addResponseInterceptor(_me.new SessionKeeper());
            _defaultClient.addRequestInterceptor(_me.new SessionAdder());
        }
        return _defaultClient;
    }
    public static void clearClient(){
    	    	//session_id=null;
    	_defaultClient=null;
    }
    private class SessionAdder implements HttpRequestInterceptor {

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {
            if ( session_id != null ) {
                request.setHeader("Cookie", session_id);
            }
        }

    }

    private class SessionKeeper implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse response, HttpContext context)
                throws HttpException, IOException {
            Header[] headers = response.getHeaders("Set-Cookie");
            if ( headers != null && headers.length == 1 ){
                session_id = headers[0].getValue();
            }
        }

    }
}