package scraping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapeador {

	public Scrapeador() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public void toXML(String pathRecurso) {
		String file = "jornada21.html";
		File input = new File("data/" + file);

		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Element jornadaElement = doc.createElement("matchday");
		doc.appendChild(jornadaElement);
		jornadaElement.attr("nro", "23");

		if (getStatusConnectionCode(pathRecurso) == 200) {
			Document documento = getHtmlDocument(pathRecurso);
			
			Game game = new Game();
			jornadaElement.appendChild(game.extractGame(documento));
//			game.extracLineScore(documento);
			//jornadaElement.attr("nro_scorebox", String.valueOf(scoreboxElements.size()));
		}

		// nombre del fichero
		Date fecha = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		System.out.println("Hora y fecha: " + hourdateFormat.format(fecha));
		String nombreFichero = hourdateFormat.format(fecha);
		String ruta = "dataXML\\";

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta + nombreFichero + ".xml"),
					StandardCharsets.UTF_8));
			writer.write(jornadaElement.outerHtml());

		} catch (IOException e) {
			System.out.println("error");
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("File saved!");
	}

	/**
	 * Con esta método compruebo el Status code de la respuesta que recibo al hacer
	 * la petición EJM: 200 OK 300 Multiple Choices 301 Moved Permanently 305 Use
	 * Proxy 400 Bad Request 403 Forbidden 404 Not Found 500 Internal Server Error
	 * 502 Bad Gateway 503 Service Unavailable
	 * 
	 * @param url
	 * @return Status Code
	 */
	public  int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	/**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia
	 * JSoup
	 * 
	 * @param url
	 * @return Documento con el HTML
	 */
	public  Document getHtmlDocument(String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}

}
