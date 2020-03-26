package scraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Game {
	
	public Game() {
		// TODO Auto-generated constructor stub
	}
	
	public Element extractData(Document documento) {
		Elements scoreboxElements = documento.select("div.scorebox > div > div > strong > a");
		
		Elements scoreElements = documento.select("div.scorebox > div > div.scores > div.score");
		
		Elements scoreElementsDiv = documento.select("div.scorebox > div > div");
		
		Element teamAway =  documento.createElement("team").attr("name", scoreboxElements.get(0).text());
		String urlTeamAway = scoreboxElements.get(0).attr("href");
		teamAway.attr("url",urlTeamAway);
		teamAway.attr("id",splitStringBySeparator(urlTeamAway,"/",2));
		teamAway.attr("score",scoreElements.get(0).text());
		teamAway.attr("win_season",splitStringBySeparator(scoreElementsDiv.get(2).text(),"-",0));
		teamAway.attr("loss_season",splitStringBySeparator(scoreElementsDiv.get(2).text(),"-",1));
		
		Element teamHome =  documento.createElement("team").attr("name", scoreboxElements.get(1).text());
		String urlTeamHome = scoreboxElements.get(1).attr("href");
		teamHome.attr("url",urlTeamHome);
		teamHome.attr("id",splitStringBySeparator(urlTeamHome,"/",2));
		teamHome.attr("score",scoreElements.get(1).text());
		teamHome.attr("win_season",splitStringBySeparator(scoreElementsDiv.get(6).text(),"-",0));
		teamHome.attr("loss_season",splitStringBySeparator(scoreElementsDiv.get(6).text(),"-",1));
		
		Element game =  documento.createElement("game").appendChild(teamAway);
		game.appendChild(teamHome);
		return game;
	}
	
	private String splitStringBySeparator(String url, String stringSeparator, Integer pos) {
		String [] fragmentos = url.split(stringSeparator);
		return fragmentos[pos];
	}

}
