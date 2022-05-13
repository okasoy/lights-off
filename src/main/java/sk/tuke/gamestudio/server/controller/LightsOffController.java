package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.SavedGame;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.TileState;
import sk.tuke.gamestudio.game.core.TypeOfLevel;
import sk.tuke.gamestudio.service.SavedGameService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class LightsOffController {
    private Field field;
    private int level = 1;
    private int score;
    private int previousScore = 0;
    private boolean isSolved = false;
    private boolean isLoaded = false;
    private String name = null;
    @Autowired
    SavedGameService savedGame;

    @RequestMapping("/lightsOff")
    public String lightsOff(@RequestParam(required = false) Integer row,
                            @RequestParam(required = false) Integer column){
        processCommand(row, column);
        return "lightsOff";
    }

    private void processCommand(Integer row, Integer column){
        if(row != null && column != null){
            if(field.isSolved() && field.getTypeOfLevel() == TypeOfLevel.PREPARED && level == 10) return;
            field.turnOff(row, column);
            score = field.getScore() + previousScore;
            isSolved = field.isSolved();
            if(field.isSolved() && field.getTypeOfLevel() == TypeOfLevel.PREPARED && level != 10) {
                previousScore = score;
                level++;
                field = new Field(5, 5, level);
                field.setTypeOfLevel(TypeOfLevel.PREPARED);
            }
            if(field.isSolved() && field.getTypeOfLevel() == TypeOfLevel.RANDOM) {
                previousScore = score;
                field = new Field(5, 5);
                field.setTypeOfLevel(TypeOfLevel.RANDOM);
            }
        }
    }

    @RequestMapping("/lightsOff/Choice")
    public String choice(){
        return "choice";
    }

    @RequestMapping("/lightsOff/HomePage")
    public String homePage(){
        return "homePage";
    }

    @RequestMapping("/lightsOff/prepared")
    public String prepared(){
        if (!isLoaded) {
            level = 1;
            previousScore = 0;
        }
        field = new Field(5, 5, level);
        field.setTypeOfLevel(TypeOfLevel.PREPARED);
        if (!isLoaded) score = field.getScore();
        else score = field.getScore() + previousScore;
        isLoaded = false;
        return "lightsOff";
    }

    @RequestMapping("/lightsOff/random")
    public String random(){
        field = new Field(5, 5);
        field.setTypeOfLevel(TypeOfLevel.RANDOM);
        previousScore = 0;
        score = field.getScore();
        return "lightsOff";
    }

    @RequestMapping("/lightsOff/new")
    public String newGame(){
        previousScore = score;
        if(field.getTypeOfLevel() == TypeOfLevel.PREPARED) {
            field = new Field(5, 5, level);
            field.setTypeOfLevel(TypeOfLevel.PREPARED);
        }
        else {
            field = new Field(5, 5);
            field.setTypeOfLevel(TypeOfLevel.RANDOM);
        }
        return "lightsOff";
    }

    @RequestMapping(value = "/lightsOff/field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String returnField(@RequestParam(required = false) Integer row,
                              @RequestParam(required = false) Integer column) {
        processCommand(row, column);
        return getHtmlField();
    }

    @RequestMapping(value = "/lightsOff/level", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getLevel(){
        if(field.getTypeOfLevel() == TypeOfLevel.RANDOM) return "";
        return Integer.toString(level);
    }

    @RequestMapping(value = "/lightsOff/typeOfLevel", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getTypeOfLevel(){
        if(field.getTypeOfLevel() == TypeOfLevel.PREPARED) return "prepared";
        return "random";
    }

    @RequestMapping(value = "/lightsOff/setName")
    public void setName(@RequestParam String username){
        if(username == null) return;
        if(username.equals("")) name = null;
        else name = username;
    }

    @RequestMapping(value = "/lightsOff/name", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getName(){
        return name;
    }

    @RequestMapping(value = "/lightsOff/isSolved", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String isSolved(){
        if(isSolved) return "true";
        return "false";
    }

    @RequestMapping(value = "/lightsOff/score", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getScore(){
        return Integer.toString(score);
    }

    @RequestMapping(value = "/lightsOff/move", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getMoveCount(){
        return Integer.toString(field.getMoveCount());
    }

    @RequestMapping(value = "/lightsOff/load", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String load(){
        if(this.name != null){
            SavedGame newGame = savedGame.getGame(this.name);
            if(newGame != null){
                previousScore = newGame.getScore();
                this.level = newGame.getLevel();
                isLoaded = true;
                return "true";
            }
            return "false";
        }
        return "false";
    }

    @RequestMapping(value = "/lightsOff/save", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String save() {
        if(this.name != null) {
            savedGame.addGame(new SavedGame(this.name, this.score, this.level));
            return "true";
        }
        return "false";
    }

    public String getHtmlField(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table>\n");
        for(int i = 0; i < field.getRowCount(); i++){
            stringBuilder.append("<tr>\n");
            for(int j = 0; j < field.getColumnCount(); j++){
                stringBuilder.append("<td>\n");
                TileState tileState = field.getTile(i,j).getTileState();
                switch (tileState){
                    case ON:
                        stringBuilder.append("<a href='/lightsOff?row=" + i + "&column=" + j + "'>\n");
                        stringBuilder.append("<img src='/images/on.png'>\n");
                        stringBuilder.append("</a>\n");
                        break;
                    case OFF:
                        stringBuilder.append("<a href='/lightsOff?row=" + i + "&column=" + j + "'>\n");
                        stringBuilder.append("<img src='/images/off.png'>\n");
                        stringBuilder.append("</a>\n");
                        break;
                }
                stringBuilder.append("</td>\n");
            }
            stringBuilder.append("</tr>\n");
        }
        stringBuilder.append("</table>\n");
        return stringBuilder.toString();
    }

}
