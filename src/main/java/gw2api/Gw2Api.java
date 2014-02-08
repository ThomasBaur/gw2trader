package gw2api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Gw2Api {

	public static final String STANDARD_URL = "https://api.guildwars2.com";
	public static String API_VERSION = "/v1/";

	private SSLSocketFactory sslSocketFactory;

	public Gw2Api() {
		try {
			setupSsl();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	private void setupSsl() throws NoSuchAlgorithmException,
			KeyManagementException {

		SSLContext sslCon = SSLContext.getInstance("SSL");
		sslCon.init(null, null, new SecureRandom());
		sslSocketFactory = sslCon.getSocketFactory();
	}

	private Object getJsonObject(URL url) {
		HttpsURLConnection httpConnection;
		try {
			httpConnection = (HttpsURLConnection) url.openConnection();
			httpConnection.setSSLSocketFactory(sslSocketFactory);
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					httpConnection.getInputStream(), "UTF-8"));
			String jsonString = "";
			String read;
			while ((read = buf.readLine()) != null) {
				jsonString += read;
			}
			httpConnection.disconnect();
			JSONParser parser = new JSONParser();
			try {
				return parser.parse(jsonString);
			} catch (Exception e) {
				throw new IllegalArgumentException("Cannot parse " + jsonString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getItems() {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "items.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getItemDetails(String itemId, String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "item_details.json?item_id="
					+ itemId + "&lang=" + lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getRecipes() {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "recipes.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getRecipeDetails(String recipeId, String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "recipe_details.json?recipe_id="
					+ recipeId + "&lang=" + lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getWvWMatches() {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "wvw/matches.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getWvWMatchDetails(String matchId) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "wvw/match_details?match_id="
					+ matchId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getWvWObjectiveNames(String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "wvw/objective_names.json?lang="
					+ lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getEvents(String worldId, String eventId, String mapId) {
		try {
			String args = "";
			if (worldId != null) {
				args += "?world_id=" + worldId;
			}
			if (worldId != null && eventId != null) {
				args += "&event_id=" + eventId;
			} else if (eventId != null) {
				args += "?event_id=" + eventId;
			}
			if (worldId != null && eventId != null && mapId != null
					|| worldId != null && eventId == null && mapId != null
					|| worldId == null && eventId != null && mapId != null) {
				args += "&map_id=" + mapId;
			} else if (worldId == null && eventId == null && mapId != null) {
				args += "?m_id=" + mapId;
			}
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "/events.json" + args));
		} catch (Exception e) {
			// Should not happen
			e.printStackTrace();
		}
		return null;
	}

	public Object getEventNames(String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "event_names.json?lang=" + lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getWorldNames(String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "world_names.json?lang=" + lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getMapNames(String lang) {
		try {
			return getJsonObject(new URL(Gw2Api.STANDARD_URL
					+ Gw2Api.API_VERSION + "map_names.json?lang=" + lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Gw2Api gw2 = new Gw2Api();
		JSONObject recipes = (JSONObject)gw2.getRecipes();
		Object object = recipes.get("recipes");
		JSONArray array = (JSONArray)object;

		for (Object entry : array) {
			System.out.println(entry);
		}
		System.out.println(array.size() + " entries");
	}
}
