package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.TileState;
import sk.tuke.gamestudio.game.core.TypeOfLevel;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class LightsOffController {
    private Field field;
    private int level = 1;
    private int score;
    private int previousScore = 0;
    private String name = null;

    @RequestMapping("/lightsOff")
    public String lightsOff(@RequestParam(required = false) Integer row,
                            @RequestParam(required = false) Integer column){
        processCommand(row, column);
        return "lightsOff";
    }

    private void processCommand(Integer row, Integer column){
        if(row != null && column != null){
            field.turnOff(row, column);
            score = field.getScore() + previousScore;
            if(field.isSolved() && field.getTypeOfLevel() == TypeOfLevel.PREPARED && level != 10){
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

    @RequestMapping("/lightsOffChoice")
    public String choice(){
        return "choice";
    }

    @RequestMapping("/lightsOffHomePage")
    public String homePage(){
        return "homePage";
    }

    @RequestMapping("/lightsOff/prepared")
    public String prepared(){
        field = new Field(5, 5, level);
        field.setTypeOfLevel(TypeOfLevel.PREPARED);
        score = field.getScore();
        return "lightsOff";
    }

    @RequestMapping("/lightsOff/random")
    public String random(){
        field = new Field(5, 5);
        field.setTypeOfLevel(TypeOfLevel.RANDOM);
        score = field.getScore();
        return "lightsOff";
    }

    @RequestMapping("/lightsOff/new")
    public String newGame(){
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
        return "Level: " + level;
    }

    @RequestMapping(value = "/lightsOff/name", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getName(){
        if (name == null) return "Username: Noname";
        return "Username: " + name;
    }

    @RequestMapping(value = "/lightsOff/score", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getScore(){
        return "Score: " + score;
    }

    @RequestMapping(value = "/lightsOff/move", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getMoveCount(){
        return "Moves: " + field.getMoveCount();
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
                        stringBuilder.append("<img src='/images/on1.png'>\n");
                        stringBuilder.append("</a>\n");
                        break;
                    case OFF:
                        stringBuilder.append("<a href='/lightsOff?row=" + i + "&column=" + j + "'>\n");
                        stringBuilder.append("<img src='/images/off1.png'>\n");
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
