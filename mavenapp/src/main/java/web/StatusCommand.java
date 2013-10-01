package web;

import java.util.List;
import java.util.Map;

import domain.Request;

/**
 * Command class for {@link StatusPage}
 * @author harnyk
 *
 */
public class StatusCommand {
	
	private int totalCount;
	private int ipCount;
	private int connectsCount;
	private List<Map<String, String>> ipRequests;
	private List<Map<String, String>> redirectUrls;
	private List<Request> lastRequests;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getIpCount() {
		return ipCount;
	}

	public void setIpCount(int ipCount) {
		this.ipCount = ipCount;
	}

	public List<Map<String, String>> getIpRequests() {
		return ipRequests;
	}

	public void setIpRequests(List<Map<String, String>> ipRequests) {
		this.ipRequests = ipRequests;
	}

	public List<Map<String, String>> getRedirectUrls() {
		return redirectUrls;
	}

	public void setRedirectUrls(List<Map<String, String>> redirectUrls) {
		this.redirectUrls = redirectUrls;
	}

	public List<Request> getLastRequests() {
		return lastRequests;
	}

	public void setLastRequests(List<Request> lastRequests) {
		this.lastRequests = lastRequests;
	}

	public int getConnectsCount() {
		return connectsCount;
	}

	public void setConnectsCount(int connectsCount) {
		this.connectsCount = connectsCount;
	}
	

}
