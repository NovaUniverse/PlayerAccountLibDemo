package net.novauniverse.playeraccountlibdemo;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.novauniverse.apilib.http.HTTPServer;
import net.novauniverse.apilib.http.middleware.middlewares.CorsAnywhereMiddleware;
import net.novauniverse.playeraccountlib.apilib.authprovider.PlayerAccountAuthProvider;
import net.novauniverse.playeraccountlib.apilib.endpoints.login.PlayerAccountLoginEndpoint;
import net.novauniverse.playeraccountlibdemo.endpoint.GetPlayerInfoEndpoint;

public class PlayerAccountLibDemo extends JavaPlugin {
	public HTTPServer server = null;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		try {
			int port = getConfig().getInt("Port");
			server = new HTTPServer(port);
			getLogger().info("Listening on port " + port + ". Server can be accessed on http://127.0.0.1:" + port);
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		server.addMiddleware(new CorsAnywhereMiddleware());
		
		// PlayerAccountLib related stuff
		server.addAuthenticationProvider(new PlayerAccountAuthProvider());
		server.addEndpoint("/api/login", new PlayerAccountLoginEndpoint());

		// Our own end points
		server.addEndpoint("/api/player_info", new GetPlayerInfoEndpoint());
	}

	@Override
	public void onDisable() {
		if (server != null) {
			server.stop();
		}
	}
}