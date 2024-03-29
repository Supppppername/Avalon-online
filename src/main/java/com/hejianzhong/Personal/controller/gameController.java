package com.hejianzhong.Personal.controller;

import com.hejianzhong.Personal.model.*;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hejianzhong.Personal.service.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/Avalon/game")
public class gameController {

    private final gameService service;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/create")
    public ResponseEntity<Game> create() {
        log.info("Create game request");
        return ResponseEntity.ok(service.createGame());
    }

    @PostMapping("/join/{gameID}")
    public ResponseEntity<Game> join(@RequestBody Player player,
        @PathVariable(value = "gameID") String gameID) {
        log.info("Connect to game request : {}" + "/n gameID : " + gameID, player);
        Game game = service.joinGame(player, gameID);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameID, game);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameID}") // look up game state from the server
    public ResponseEntity<Game> getGame(@PathVariable(value = "gameID") String gameID) {
        log.info("Get game request : {}", gameID);
        return ResponseEntity.ok(service.getGame(gameID));
    }

    @PostMapping("/{gameID}/start")
    public ResponseEntity<Game> startGame(@RequestBody setting setting,
        @PathVariable(value = "gameID") String gameID) {
        log.info("Start game request : {}", gameID);
        Game game = service.startGame(gameID, setting);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameID, game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/proposeTeam")
    public ResponseEntity<Game> proposeTeam(@RequestBody ArrayList<String> proposal,
        @PathVariable(value = "gameID") String gameID) {
        log.info("Propose team request : {}", proposal);
        Game game = service.proposeTeam(proposal, gameID);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameID, game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/approveTeam")
    public ResponseEntity<Game> approveTeam(@PathVariable(value = "gameID") String gameID) {
        Game game = service.approveTeam(gameID);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/rejectTeam")
    public ResponseEntity<Game> rejectTeam(@PathVariable(value = "gameID") String gameID) {
        Game game = service.rejectTeam(gameID);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/approveTask")
    public ResponseEntity<Game> approveTask(@PathVariable(value = "gameID") String gameID) {
        Game game = service.approveTask(gameID);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/rejectTask")
    public ResponseEntity<Game> rejectTask(@PathVariable(value = "gameID") String gameID) {
        Game game = service.rejectTask(gameID);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameID}/assassin")
    public ResponseEntity<Game> assassin(@RequestBody String name, @PathVariable(value = "gameID") String gameID) {
        Game game = service.assassin(name, gameID);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameID, game);
        return ResponseEntity.ok(game);
    }
}
