package gw2api;

public interface Gw2Api {

	public static final String STANDARD_URL = "https://api.guildwars2.com";
	public static final String API_VERSION = "/v1/";

	public abstract Object getItems();

	public abstract Object getItemDetails(String itemId, String lang);

	public abstract Object getRecipes();

	public abstract Object getRecipeDetails(String recipeId, String lang);

	public abstract Object getWvWMatches();

	public abstract Object getWvWMatchDetails(String matchId);

	public abstract Object getWvWObjectiveNames(String lang);

	public abstract Object getEvents(String worldId, String eventId,
			String mapId);

	public abstract Object getEventNames(String lang);

	public abstract Object getWorldNames(String lang);

	public abstract Object getMapNames(String lang);

}