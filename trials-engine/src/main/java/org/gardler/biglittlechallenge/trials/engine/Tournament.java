package org.gardler.biglittlechallenge.trials.engine;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.gardler.biglittlechallenge.core.model.AbstractGame;
import org.gardler.biglittlechallenge.core.model.AbstractRounds;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.Round;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Tournament is a single game of Trials. It consists of a number of events.
 * Players will enter each of the events in a tournament.
 * 
 */
public class Tournament extends AbstractGame {
	
	private static Logger logger = LoggerFactory.getLogger(Tournament.class);
	// Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/v0.1/";
			
	public Tournament() {
		super(new ArrayList<Player>());
		this.setRounds();
	}
	
	/**
	 * Set the Rounds to be played in this tournament.
	 */
	protected void setRounds() {
		try {
			rounds = AbstractRounds.load();
			return;
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		} catch (IOException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		}
		rounds = new Rounds();
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Speed", 1.0);
		formula.put("Reactions", 0.5);
		Event event = new Event("Track: 100m Sprint", formula);
    	rounds.add(event);
    	
		formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		event = new Event("Track: 8000m", formula);
    	rounds.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		formula.put("Dexterity", 1.0);
		event = new Event("Track: 8000m Steeple Chase", formula);
    	rounds.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
    	formula.put("Dexterity", 1.0);
		formula.put("Strength", 0.5);
		event = new Event("Field: Pole Vault", formula);
    	rounds.add(event);
	}
	
	public String toString() {
		String result = "This tournament consists of " + rounds.size() + " events.\n";
		Iterator<Round> itr = getRounds().iterator();
		while (itr.hasNext()) {
			Event event = (Event)itr.next();
			result = result + "\t" + event.getName() + "\n";
		}
		return result;
	}

	public HttpServer startServer() {
		final ResourceConfig rc = new ResourceConfig().register(TournamentAPI.class);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}
}
