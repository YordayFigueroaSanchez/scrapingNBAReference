package scraping;

import java.util.ArrayList;

import javax.swing.text.AbstractDocument.LeafElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Game {

	public Game() {
		// TODO Auto-generated constructor stub
	}

	public Element extractGame(Document documento) {
		ArrayList<Element> teams = this.extractData(documento);
		Element teamAway = teams.get(0);
		Element teamHome = teams.get(1);

		teamAway.appendChild(this.extracLineScore(documento, teamAway.attr("url")));
		teamHome.appendChild(this.extracLineScore(documento, teamHome.attr("url")));

		teamAway.appendChild(this.extractBasicBoxScoreStats(documento, teamAway.attr("id")));
		teamHome.appendChild(this.extractBasicBoxScoreStats(documento, teamHome.attr("id")));

		Element game = documento.createElement("game").appendChild(teamAway);
		game.appendChild(teamHome);
		return game;
	}

	public Element extractBasicBoxScoreStats(Document documento, String id) {

		Element basicBoxScoreStats = documento.createElement("basicBoxScoreStats");
		Element basicBoxScoreStatsPlayer = documento.createElement("player");

		Elements basicScoreTrElements = documento.select("table#box-" + id + "-game-basic > tbody > tr");
		Elements advancedScoreTrElements = documento.select("table#box-" + id + "-game-advanced > tbody > tr");

		int iTr = 1;
		for (Element element : basicScoreTrElements) {
			if (iTr != 6) {

				basicBoxScoreStatsPlayer = documento.createElement("player");

				Elements basicScoreTh = element.select("th");
				basicBoxScoreStatsPlayer.attr("name", basicScoreTh.get(0).text());
				basicBoxScoreStatsPlayer.attr("id", basicScoreTh.get(0).attr("data-append-csv"));
				basicBoxScoreStatsPlayer.attr("url", basicScoreTh.get(0).select("a").get(0).attr("href"));
				Elements basicScoreTd = element.select("td");
				int iTd = 1;
				String attrName = "";
				for (Element element2 : basicScoreTd) {
					attrName = "";
					switch (iTd) {
					case 1:
						attrName = "MinutesPlayed";
						break;
					case 2:
						attrName = "FieldGoals";
						break;
					case 3:
						attrName = "FieldGoalAttempts";
						break;
					case 4:
						attrName = "FieldGoalPercentage";
						break;
					case 5:
						attrName = "3PointFieldGoals";
						break;
					case 6:
						attrName = "3PointFieldGoalAttempts";
						break;
					case 7:
						attrName = "3PointFieldGoalPercentage";
						break;
					case 8:
						attrName = "FreeThrows";
						break;
					case 9:
						attrName = "FreeThrowAttempts";
						break;
					case 10:
						attrName = "FreeThrowPercentage";
						break;
					case 11:
						attrName = "OffensiveRebounds";
						break;
					case 12:
						attrName = "DefensiveRebounds";
						break;
					case 13:
						attrName = "TotalRebounds";
						break;
					case 14:
						attrName = "Assists";
						break;
					case 15:
						attrName = "Steals";
						break;
					case 16:
						attrName = "Blocks";
						break;
					case 17:
						attrName = "Turnovers";
						break;
					case 18:
						attrName = "PersonalFouls";
						break;
					case 19:
						attrName = "Points";
						break;
					case 20:
						attrName = "PlusMinus";
						break;
					default:
						attrName = "Others";
						break;
					}
					basicBoxScoreStatsPlayer.attr(attrName, element2.text());
					iTd++;
				}
				// advancedBoxScore
				Element advancedBoxScoreElement = advancedScoreTrElements.get(iTr - 1);
				Elements advancedBoxScoreElementTd = advancedBoxScoreElement.select("td");
				int iTdAdvance = 1;
				for (Element element3 : advancedBoxScoreElementTd) {
					attrName = "";
					if (iTdAdvance > 1) {
						switch (iTdAdvance) {
						case 2:
							attrName = "TrueShootingPercentage";
							break;
						case 3:
							attrName = "EffectiveFieldGoalPercentage";
							break;
						case 4:
							attrName = "3PointAttemptRate";
							break;
						case 5:
							attrName = "FreeThrowAttemptRate";
							break;
						case 6:
							attrName = "OffensiveReboundPercentage";
							break;
						case 7:
							attrName = "DefensiveReboundPercentage";
							break;
						case 8:
							attrName = "TotalReboundPercentage";
							break;
						case 9:
							attrName = "AssistPercentage";
							break;
						case 10:
							attrName = "StealPercentage";
							break;
						case 11:
							attrName = "BlockPercentage";
							break;
						case 12:
							attrName = "TurnoverPercentage";
							break;
						case 13:
							attrName = "UsagePercentage";
							break;
						case 14:
							attrName = "OffensiveRating";
							break;
						case 15:
							attrName = "DefensiveRating";
							break;
						case 16:
							attrName = "BPM";
							break;
						default:
							attrName = "Others";
							break;
						}
						basicBoxScoreStatsPlayer.attr(attrName, element3.text());
						
					}
					iTdAdvance++;
				}
			}

			iTr++;
			basicBoxScoreStats.appendChild(basicBoxScoreStatsPlayer);
		}

		return basicBoxScoreStats;
	}

	public ArrayList<Element> extractData(Document documento) {
		Elements scoreboxElements = documento.select("div.scorebox > div > div > strong > a");

		Elements scoreElements = documento.select("div.scorebox > div > div.scores > div.score");

		Elements scoreElementsDiv = documento.select("div.scorebox > div > div");

		/* TEAM AWAY */
		Element teamAway = documento.createElement("team").attr("name", scoreboxElements.get(0).text());
		String urlTeamAway = scoreboxElements.get(0).attr("href");
		teamAway.attr("url", urlTeamAway);
		teamAway.attr("id", splitStringBySeparator(urlTeamAway, "/", 2));
		teamAway.attr("score", scoreElements.get(0).text());
		teamAway.attr("win_season", splitStringBySeparator(scoreElementsDiv.get(2).text(), "-", 0));
		teamAway.attr("loss_season", splitStringBySeparator(scoreElementsDiv.get(2).text(), "-", 1));

//		teamAway.appendChild(extracLineScore(documento, urlTeamAway));

		/* TEAM HOME */
		Element teamHome = documento.createElement("team").attr("name", scoreboxElements.get(1).text());
		String urlTeamHome = scoreboxElements.get(1).attr("href");
		teamHome.attr("url", urlTeamHome);
		teamHome.attr("id", splitStringBySeparator(urlTeamHome, "/", 2));
		teamHome.attr("score", scoreElements.get(1).text());
		teamHome.attr("win_season", splitStringBySeparator(scoreElementsDiv.get(6).text(), "-", 0));
		teamHome.attr("loss_season", splitStringBySeparator(scoreElementsDiv.get(6).text(), "-", 1));

//		teamHome.appendChild(extracLineScore(documento, urlTeamHome));

//		Element game =  documento.createElement("game").appendChild(teamAway);
//		game.appendChild(teamHome);

		ArrayList<Element> teams = new ArrayList<Element>();
		teams.add(teamAway);
		teams.add(teamHome);
		return teams;
	}

	private String splitStringBySeparator(String url, String stringSeparator, Integer pos) {
		String[] fragmentos = url.split(stringSeparator);
		return fragmentos[pos];
	}

	public Element extracLineScore(Document documento, String teamUrl) {
		Element lineScoreElement = null;
//		String cadena = documento.toString();
//		Elements prueba = documento.select("comment");
		Elements prueba = documento.select("div.content_grid > div > div.table_wrapper");
		String lineScore = prueba.get(0).toString().replaceAll("<!--", "").replaceAll("-->", "");
		Document doc = Jsoup.parse(lineScore);
		Elements trArray = doc.select("table > tbody > tr");

		for (Element element : trArray) {
			Elements urlElements = element.select("td");
			if (urlElements.size() > 0) {
				String url = urlElements.get(0).select("a").attr("href").toString();
				if (url.equals(teamUrl)) {

					lineScoreElement = documento.createElement("linescore");
					int contador = 0;
					for (Element element2 : urlElements) {
						if ((contador > 0) && (contador < urlElements.size() - 1)) {
							Element scoreElement = documento.createElement("score")
									.attr("time", String.valueOf(contador)).attr("points", element2.text());
							lineScoreElement.appendChild(scoreElement);
						}
						contador++;
//						System.out.println(element.toString());
					}
				}
			}
		}
//		Elements tdArray = trArray.get(2).select("td");
//		> div.overthrow.table_container > div.overthrow.table_container> table");
		return lineScoreElement;
	}

}
