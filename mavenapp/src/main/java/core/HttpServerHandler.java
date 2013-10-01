package core;

import java.util.*;

import web.Controller;
import web.ControllerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;

import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	public static final AttributeKey<Object> URI_KEY = new AttributeKey<Object>(
			"uri");
	public static final AttributeKey<Object> CONNECTIONS = new AttributeKey<Object>(
			"connections");
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	
        ctx.flush();
        
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;

			if (is100ContinueExpected(req)) {
				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			}

			String uri = req.getUri();
			Controller controller = ControllerFactory.getController(uri
					.toLowerCase());
			controller.setConnections((Integer) ctx
					.attr(HttpServerHandler.CONNECTIONS).get());
			FullHttpResponse response = controller.getResponse();

			Map<String, String> uriMap = new HashMap<String, String>();
			uriMap.put("uri", uri);
			uriMap.put("redirectUrl", controller.getRedirectUrl());
			ctx.channel().attr(URI_KEY).set(uriMap);

			ctx.write(response).addListener(ChannelFutureListener.CLOSE);

		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Integer count;
		if(ctx.attr(HttpServerHandler.CONNECTIONS).get() == null){
			count = 1;
		} else {
			count = (Integer) ctx.channel()
					.attr(HttpServerHandler.CONNECTIONS).get();
			count++;
		}
		ctx.attr(CONNECTIONS).set(count);
		super.channelActive(ctx);
    }

}
