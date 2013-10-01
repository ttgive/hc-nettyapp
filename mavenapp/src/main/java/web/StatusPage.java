/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package web;

import java.util.Map;

import domain.Request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * Class of html-page, analogue of "view" in MVC
 * @author harnyk
 */
public final class StatusPage {

    private static final String NEWLINE = "\r\n";

    public static ByteBuf getContent(StatusCommand stc) {
    	String disp = "<html><head><title>Status page</title>" +
    			"<style>" +
    			"table, td, th {border: 1px solid #000000;} table{margin: 5px;} td, th {padding: 3px;}" +
    			"</style>"+
    			"</head>" + NEWLINE +
                "<body>" + NEWLINE;
    	disp += "Total requests: " + stc.getTotalCount() + "<br>";
    	disp += "Unic IP requests : " + stc.getIpCount() + "<br>";
    	disp += "Connections: " + stc.getConnectsCount() + "<br><br>";
    	disp += "<div style='float: left;'><table><thead><tr><th>IP</th><th>Count</th><th>Last request time</th></tr></thead>";
    	disp += "IP statistic:<br>";
    	for(Map<String, String> ip : stc.getIpRequests()){
    		disp += "<tr><td>" + ip.get("src_ip") + "</td><td>"+ ip.get("count") + "</td><td>" + ip.get("when") +"</td></tr>";
    	}
    	disp += "</table><br>";
    	disp += "Redirect statistic:<br>";
    	disp += "<table><thead><tr><th>Redirect url</th><th>Count</th></tr></thead>";
    	for(Map<String, String> url : stc.getRedirectUrls()){
    		disp += "<tr><td>" + url.get("redirect_url") + "</td><td>"+ url.get("count")+ "</td></tr>";
    	}
    	disp += "</table></div>";
    	disp += "<div style='float: left;'>Last request statistic:<br>";
    	disp += "<table><thead><tr><th>src_ip</th><th>URI</th><th>timestamp(ms)</th>" +
    			"<th>sent_bytes</th><th>received_bytes</th><th>speed (bytes/sec)</th></tr></thead>";
    	for(Request req : stc.getLastRequests()){
    		disp += "<tr><td>" + req.getIp() + "</td><td>"+ req.getUri() + "</td><td>"+ req.getTimestamp() + 
    				"</td><td>"+ req.getSent_bytes() + "</td><td>"+ req.getReceived_bytes() + "</td><td>"+ req.getSpeed() + "</td></tr>";
    	}
    	disp += "</table></div>";
    	
    	
    	
    	disp += "</body>" + NEWLINE +
                "</html>" + NEWLINE;
        return Unpooled.copiedBuffer(disp, CharsetUtil.UTF_8);
    }

    private StatusPage() {
        // Unused
    }
}
