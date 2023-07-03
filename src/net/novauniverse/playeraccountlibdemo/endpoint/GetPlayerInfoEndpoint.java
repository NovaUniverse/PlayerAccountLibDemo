package net.novauniverse.playeraccountlibdemo.endpoint;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import net.novauniverse.apilib.http.auth.Authentication;
import net.novauniverse.apilib.http.endpoint.HTTPEndpoint;
import net.novauniverse.apilib.http.enums.HTTPMethod;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;
import net.novauniverse.apilib.http.request.Request;
import net.novauniverse.apilib.http.response.AbstractHTTPResponse;
import net.novauniverse.apilib.http.response.JSONResponse;
import net.novauniverse.playeraccountlib.apilib.authprovider.PlayerAccountAuth;

public class GetPlayerInfoEndpoint extends HTTPEndpoint {
	public GetPlayerInfoEndpoint() {
		setAllowedMethods(HTTPMethod.GET);
		setRequireAuthentication(true);
	}

	@Override
	public AbstractHTTPResponse handleRequest(Request request, Authentication authentication) throws Exception {
		PlayerAccountAuth auth = (PlayerAccountAuth) authentication;

		Player player = Bukkit.getPlayer(auth.getAccount().getPlayerUuid());
		if (player == null) {
			JSONObject data = new JSONObject();
			data.put("error", "player not online");
			return new JSONResponse(data, HTTPResponseCode.CONFLICT);
		}

		JSONObject data = new JSONObject();
		JSONObject location = new JSONObject();

		location.put("x", player.getLocation().getX());
		location.put("y", player.getLocation().getY());
		location.put("z", player.getLocation().getZ());
		location.put("world", player.getLocation().getWorld().getName());

		data.put("gamemode", player.getGameMode().name());
		data.put("location", location);
		data.put("health", player.getHealth());
		data.put("food", player.getFoodLevel());
		data.put("level", player.getLevel());
		data.put("xp", player.getExp());

		return new JSONResponse(data);
	}

}