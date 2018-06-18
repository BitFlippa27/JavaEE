package webSocket;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.IOException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import application.Player;
import application.Question;
import application.Quiz;
import error.QuizError;
import thread.TimerThread;
import webSocket.ConnectionManager;
import webSocket.BroadcastThread;
import webSocket.Echo;

@ServerEndpoint("/Echo")
public class Echo {

	private Quiz quiz = Quiz.getInstance();
	private QuizError error = new QuizError();
	private Thread broadcast = new BroadcastThread();

	@OnError
	public void error(Session session, Throwable t) {
		System.out.println("Fehler beim Oeffnen des Sockets");
	}

	@OnOpen
	// Ein Client meldet sich an und eröffnet eine neue Web-Socket-Verbindung
	public void open(Session session) {
		// speichern der aktuellen Socket-Session im ConnnectionManager

		ConnectionManager.addTmpSession(session);
		if (quiz.getCurrentCatalog() != null) {
			JSONObject catalogChange = new JSONObject();
			catalogChange.put("Type", "5");
			catalogChange.put("Length", quiz.getCurrentCatalog().getName().length());
			catalogChange.put("Message", quiz.getCurrentCatalog().getName());
			sendToAll(catalogChange);
		}
		if (ConnectionManager.getSessionCount() < 4) {
			if (!broadcast.isAlive()) {
				broadcast = new BroadcastThread();
				broadcast.start();
			}
		}
	}

	@OnClose
	// Client meldet sich wieder ab
	public void close(Session session, CloseReason reason) { // Client aus Liste
																// entfernen
		if (!ConnectionManager.removeTmpSession(session)) {
			Player tmp = ConnectionManager.getPlayer(session);
			if (tmp.getId() == 0) { // Superuser left
				quiz.removePlayer(tmp, error);
				ConnectionManager.removeSession(session);
				JSONObject error = new JSONObject();
				error.put("Type", "255");
				error.put("Length", 1 + 14);
				error.put("Subtype", "1");
				error.put("Message", "Spielleiter left");
				sendToAll(error);

			} else {
				quiz.removePlayer(tmp, error);
				ConnectionManager.removeSession(session);
			}
		}
		if (!broadcast.isAlive()) {
			broadcast = new BroadcastThread();
			broadcast.start();
		}
	}

	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last)
			throws ParserConfigurationException, Exception {
		System.out.println("Message im Server erhalten: " + msg);

		JSONObject jsonMsg = (JSONObject) new JSONParser().parse(msg);
		System.out.println(jsonMsg);
		int msgType = Integer.parseInt(jsonMsg.get("Type").toString());

		// LoginRequest
		if (msgType == 1) {
			String pName = jsonMsg.get("Name").toString();
			if (ConnectionManager.getSessionCount() >= 4) {
				System.out.println("Maximale Spielerzahl erreicht");
				JSONObject error = new JSONObject();
				error.put("Type", "255");
				error.put("Length", 1 + 29);
				error.put("Subtype", "1");
				error.put("Message", "Maximale Spielerzahl erreicht");
				sendJSON(session, error);
			} 
			else 
			{
				//Spielername pr�fen
				if ("" != pName && pName != null) {
					int nameVorhanden = 0;
					Collection<Player> tmpPlayer = ConnectionManager.getPlayers();
					for (Player p : tmpPlayer) {
						// Spielername schon vorhanden
						if(pName.equals(p.getName())){
							nameVorhanden = 1;
							JSONObject error = new JSONObject();
							error.put("Type", "255");
							error.put("Length", "30");
							error.put("Subtype", "1");
							error.put("Message", "Spielername bereits Vorhanden!");
							sendJSON(session, error);
						}
					}
					//Spielername in Ordnung
					if(nameVorhanden == 0){
						Player tmpP = quiz.createPlayer(pName, error);
						JSONObject newPlayer = new JSONObject();
						newPlayer.put("Type", "2");
						newPlayer.put("Length", "1");
						newPlayer.put("clientid", tmpP.getId());
						ConnectionManager.addSession(session, tmpP);
						ConnectionManager.removeTmpSession(session);
						sendJSON(session, newPlayer);
					}
					// Playerliste senden
					if (!broadcast.isAlive()) {
						broadcast = new BroadcastThread();
						broadcast.start();
					}
				}
				// Spielername fehlerhaft
				else {
					JSONObject error = new JSONObject();
					error.put("Type", "255");
					error.put("Length", "17");
					error.put("Subtype", "1");
					error.put("Message", "Ungueltiger Spielername");
					System.out.println(error);
					sendJSON(session, error);
				}
			}
		}
		// CatalagChange
		if (msgType == 5) {
			JSONObject catalogChange = new JSONObject();
			catalogChange.put("Type", "5");
			catalogChange.put("Length", jsonMsg.get("Length").toString());
			catalogChange.put("Message", jsonMsg.get("Message"));
			quiz.changeCatalog(ConnectionManager.getPlayer(session), jsonMsg.get("Message").toString(), error);
			sendToAll(catalogChange);
		}
		if (msgType == 7) {
			if (quiz.startGame(ConnectionManager.getPlayer(session), error)) {
				System.out.println("Spiel gestartet!");
				JSONObject gameStarted = new JSONObject();
				gameStarted.put("Type", "7");
				gameStarted.put("Length", jsonMsg.get("Length").toString());
				gameStarted.put("Message", jsonMsg.get("Message"));
				sendToAll(gameStarted);
			} else {
				System.out.println("Spiel konnte nicht gestartet werden");
			}
		}
		// QuestionRequest
		if (msgType == 8) {

			TimerTask timerTask = new TimerThread(session);
			JSONObject jsonQust = new JSONObject();
			Question aktQust = quiz.requestQuestion(ConnectionManager.getPlayer(session), timerTask, error);
			if (aktQust != null) {
				
				jsonQust.put("Type", "9");
				jsonQust.put("Length", "769");
				jsonQust.put("Frage", aktQust.getQuestion());
				JSONArray answer = new JSONArray();
				for (String tempAnswer : aktQust.getAnswerList()) {
					answer.add(tempAnswer);
				}
				jsonQust.put("Answer", answer);
				jsonQust.put("Zeitlimit", aktQust.getTimeout());
			} 
			else 
			{
				System.out.println("Question leer");
				//Hochz�hlen bis alle Spieler am ende sind
				ConnectionManager.countGameOver();
				quiz.setDone(ConnectionManager.getPlayer(session));
				jsonQust.put("Type", "9");
				jsonQust.put("Length", "0");
			}
			sendJSON(session, jsonQust);
		}
		// QuestionAnswerd
		if (msgType == 10) {
			System.out.println("Empfange Antwort");
			Long index = Long.parseLong((String) jsonMsg.get("Selection").toString());
			Long correctAnswer = quiz.answerQuestion(ConnectionManager.getPlayer(session), index, error);
			if (correctAnswer != -1) 
			{
				JSONObject questionResult = new JSONObject();
				questionResult.put("Type", "11");
				questionResult.put("Length", "2");
				questionResult.put("TimedOut", "0");
				questionResult.put("Correct", correctAnswer.toString());
				sendJSON(session, questionResult);
			}
		}
		if (!broadcast.isAlive()) {
			broadcast = new BroadcastThread();
			broadcast.start();
		}
		ConnectionManager.printall();
	}

	public static synchronized void sendJSON(Session session, JSONObject newPlayer) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Message from Server to Client " + newPlayer);
			session.getBasicRemote().sendText(newPlayer.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sendToAll(JSONObject error) {
		// Alle aktiven Sessions durchgehen
		Set<Session> tmpMap = ConnectionManager.getSessions();
		for (Iterator<Session> iter = tmpMap.iterator(); iter.hasNext();) {
			Session s = iter.next();
			sendJSON(s, error);
		}

		// Alle temp Sessions durchgehen, um die Spielerliste aktuell anzuzeigen
		List<Session> tmpSessions = ConnectionManager.getTmpSessions();
		if (tmpSessions.size() > 0) {
			for (Session tempS : tmpSessions) {
				sendJSON(tempS, error);
			}
		}
	}
}

class BroadcastThread extends Thread {
	private Echo playerEndpoint;

	BroadcastThread() {
	}

	@SuppressWarnings("unchecked")
	public void run() {
		System.out.println("BroadcastThread:");
		ConnectionManager.printall();
		// PlayerList vorbereiten und verschicken
		System.out.println("SessionCount: " + ConnectionManager.getSessionCount());
		if (ConnectionManager.getSessionCount() > 0) {
			JSONObject playerList = new JSONObject();
			playerList.put("Type", "6");
			playerList.put("Lenght", ConnectionManager.getSessionCount() * 37);
			Collection<Player> tmpPlayer = ConnectionManager.getPlayers();
			JSONArray players = new JSONArray();
			String spieler[][] = new String[tmpPlayer.size()][3];
			int countPlayer = 0;
			for (Player p : tmpPlayer) {
				spieler[countPlayer][0] = p.getName();
				spieler[countPlayer][1] = ""+p.getScore();
				spieler[countPlayer][2] = ""+p.getId();
				countPlayer++;
			}
			
			Arrays.sort(spieler, new Comparator<String[]>(){
				@Override
				public int compare(final String[] entry1, final String[] entry2){
					final String time1 = entry1[1];
					int t1 = Integer.parseInt(time1);
					final String time2 = entry2[1];
					int t2 = Integer.parseInt(time2);
					
					return Integer.compare(t1, t2);
				}
			});
		
			for(int i = 0; i< spieler.length; i++)
			{
				JSONObject tmp = new JSONObject();
				tmp.put("Spielername", spieler[i][0]);
				tmp.put("Punktestand", spieler[i][1]);
				tmp.put("clientid", spieler[i][2]);
				players.add(tmp);
			}

			playerList.put("Players", players);

			// GameOver Nachricht an alle Clients
			JSONObject jsonGameOver = new JSONObject();
			if(ConnectionManager.checkGameOver()==tmpPlayer.size())
			{
				jsonGameOver.put("Type", "12");
				jsonGameOver.put("Lenght", "0");
				
				// Alle aktuell angemeldeten SpielerSessions durchgehen
				Set<Session> tmpMap = ConnectionManager.getSessions();
				for (Iterator<Session> iter = tmpMap.iterator(); iter.hasNext();) {
					Session s = iter.next();
					// PlayerList message
					Echo.sendJSON(s, jsonGameOver);
				}
				
				ConnectionManager.reloadeGame();
			}
			// Alle aktuell angemeldeten SpielerSessions durchgehen
			Set<Session> tmpMap = ConnectionManager.getSessions();
			for (Iterator<Session> iter = tmpMap.iterator(); iter.hasNext();) {
				Session s = iter.next();
				// PlayerList message
				Echo.sendJSON(s, playerList);
			}

			// Alle temp Sessions durchgehen, um die Spielerliste aktuell
			// anzuzeigen
			List<Session> tmpSessions = ConnectionManager.getTmpSessions();
			if (tmpSessions.size() > 0) {
				for (Session tempS : tmpSessions) {
					Echo.sendJSON(tempS, playerList);
				}
			}
		}
	}
}
