package core;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

import db.JdbcRequestDao;
import domain.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Handler for logging information about requests and saving it in database
 * @author harnyk
 *
 */
public class JbdcLoggingHandler extends LoggingHandler {

	protected Request request = new Request();
	protected long startTime;
	
	

	public JbdcLoggingHandler(LogLevel info) {
		super(info);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		UnpooledHeapByteBuf byteMsg = (UnpooledHeapByteBuf) msg;
		this.request.setReceived_bytes(this.request.getReceived_bytes()
				+ byteMsg.readableBytes());
		super.channelRead(ctx, msg);

	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		ByteBuf byteMsg = (ByteBuf) msg;
		this.request.setSent_bytes((this.request.getSent_bytes() + byteMsg
				.readableBytes()));
		super.write(ctx, msg, promise);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		this.startTime = System.currentTimeMillis();
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		this.request.setIp(((InetSocketAddress) ctx.channel().remoteAddress())
				.getAddress().getHostAddress());
		this.request.setWhen(new Date());
		this.request.setTimestamp(System.currentTimeMillis() - this.startTime);

		Map<String, String> uriMap = (Map<String, String>) ctx.channel()
				.attr(HttpServerHandler.URI_KEY).get();
		this.request.setUri(uriMap.get("uri"));
		this.request.setRedirectUrl(uriMap.get("redirectUrl"));
		JdbcRequestDao dao = new JdbcRequestDao();
		dao.saveRequest(this.request);
		super.channelUnregistered(ctx);
	}
	


}
