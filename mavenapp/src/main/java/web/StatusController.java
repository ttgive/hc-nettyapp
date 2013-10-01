package web;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import db.JdbcRequestDao;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

public class StatusController extends Controller {

	@Override
	public FullHttpResponse getResponse() throws InterruptedException {
		JdbcRequestDao dao = new JdbcRequestDao();
		StatusCommand sc = dao.getStatusCommand();
		sc.setConnectsCount(this.getConnections());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, 
				StatusPage.getContent(sc));
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		return response;
	}

}
