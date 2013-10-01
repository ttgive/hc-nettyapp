package domain;

import java.util.Date;
/**
 * Entity of http request
 * @author harnyk
 *
 */
public class Request {
	
	private int id;
	//client ip
	private String ip;
	
	//request uri
	private String uri = "";
	
	//datetime of request
	private Date when;
	
	//redirect url
	private String redirectUrl = "";
	
	//received bytes
	private int received_bytes = 0;
	
	//sent bytes
	private int sent_bytes = 0;
	
	//duration of request
	private long timestamp;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Date getWhen() {
		return when;
	}
	public void setWhen(Date date) {
		this.when = date;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public int getReceived_bytes() {
		return received_bytes;
	}
	public void setReceived_bytes(int received_bytes) {
		this.received_bytes = received_bytes;
	}
	public int getSent_bytes() {
		return sent_bytes;
	}
	public void setSent_bytes(int sent_bytes) {
		this.sent_bytes = sent_bytes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public float getSpeed(){
		return (this.timestamp > 0) ? (this.received_bytes+this.sent_bytes)*1000/this.timestamp : 0;
	}
}
