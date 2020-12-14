package gameClient;

import Gui.Frame;
import api.game_service;

import java.util.List;

public class GamePlay implements Runnable{

    private Arena arena;
    private Frame frame;
    private game_service game;
    private int length;

    public GamePlay(Arena a, Frame f){
        this.arena = a;
        this.frame = f;
        this.game = a.gatGame();
//        this.length = somthing;
    }

    @Override
    public void run() {
        while (game.isRunning()) {
            try {
//                move();
                for (int i = 0; i < 10; i++) {
                    game.move();
//                    if(i%200 == 0)
//                        this.frame.update();
                }
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            move();
        }
        game.stopGame();
        System.out.println(game);
    }

    public void move(){
//        this.frame.update();
//        System.out.println(game.getAgents());
        List<CL_Agent> l = jsonToObject.loadAgents(game.getAgents());
        this.arena.setAgents(l);
//        System.out.println(this.arena.getAgents().get(0).getDest());
        this.arena.movementStrategy();
//        System.out.println(this.arena.getAgents().get(0).getDest());
//        System.out.println(game);
        this.frame.update();

//        return  -1; // מה שחוזר מפונקצית החישוב
    }

    /*
     חישוב הצלע הכי ארוכה לסוכן חלקי המהירות שלו (להפוך למילי שניות) לעשות את החישוב במוב סטראתג'י
     */
}
