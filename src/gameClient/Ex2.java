package gameClient;

import Gui.Frame;
import Server.Game_Server_Ex2;
import api.game_service;

import java.awt.*;

public class Ex2 {

    public static void main(String[] args){
//        System.out.println(g);
//        System.out.println(g.getAgents());
        Arena catchThemAll = new Arena(11);
//        System.out.println(catchThePokemon.getGraphAlgo().getGraph().toString());
        Frame f = new Frame(catchThemAll);
        catchThemAll.gatGame().startGame();
        catchThemAll.firstChooseNext();
        catchThemAll.gatGame().login(111222);
        GamePlay game = new GamePlay(catchThemAll, f);
        Thread gamePlay = new Thread(game);
        gamePlay.start();
    }
}
