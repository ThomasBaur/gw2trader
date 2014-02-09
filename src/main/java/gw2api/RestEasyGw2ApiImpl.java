package gw2api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RestEasyGw2ApiImpl implements Gw2Api {

	private Object getJsonObject(String url) {
		Client c = ResteasyClientBuilder.newBuilder().build();
		WebTarget target = c.target(Gw2Api.STANDARD_URL + Gw2Api.API_VERSION
				+ url);
		Response response = target.request().get();
		String readEntity = response.readEntity(String.class);
		response.close();
		
		JSONParser parser = new JSONParser();
		try {
			return parser.parse(readEntity);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot parse " + readEntity);
		}
	}

	@Override
	public Object getItems() {
		return getJsonObject("items.json");
	}

	@Override
	public Object getItemDetails(String itemId, String lang) {
		return getJsonObject("item_details.json?item_id=" + itemId + "&lang="
				+ lang);
	}

	@Override
	public Object getRecipes() {
		return getJsonObject("recipes.json");
	}

	@Override
	public Object getRecipeDetails(String recipeId, String lang) {
		return getJsonObject("recipe_details.json?recipe_id=" + recipeId
				+ "&lang=" + lang);
	}

	@Override
	public Object getWvWMatches() {
		return getJsonObject("wvw/matches.json");
	}

	@Override
	public Object getWvWMatchDetails(String matchId) {
		return getJsonObject("wvw/match_details?match_id=" + matchId);
	}

	@Override
	public Object getWvWObjectiveNames(String lang) {
		return getJsonObject("wvw/objective_names.json?lang=" + lang);
	}

	@Override
	public Object getEvents(String worldId, String eventId, String mapId) {
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
		return getJsonObject("/events.json" + args);
	}

	@Override
	public Object getEventNames(String lang) {
		return getJsonObject("event_names.json?lang=" + lang);
	}

	@Override
	public Object getWorldNames(String lang) {
		return getJsonObject("world_names.json?lang=" + lang);
	}

	@Override
	public Object getMapNames(String lang) {
		return getJsonObject("map_names.json?lang=" + lang);
	}

	public static void main(String[] args) {
		Gw2Api gw2 = new RestEasyGw2ApiImpl();
		JSONObject recipes = (JSONObject) gw2.getRecipes();
		Object object = recipes.get("recipes");
		JSONArray array = (JSONArray) object;

		for (Object entry : array) {
			System.out.println(entry);
		}
		System.out.println(array.size() + " entries");
	}

}
