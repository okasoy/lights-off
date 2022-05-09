package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.TileState;
import sk.tuke.gamestudio.game.core.TypeOfLevel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
    private File scores = new File("./src/main/resources/saved/scores.txt");

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
        return "Moves: " + field.getMoveCount();
    }

    @RequestMapping("/lightsOff/load")
    public String load(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(scores));
            String str;
            boolean isSaved = true;
            char[] name = this.name.toCharArray();
            while ((str=bufferedReader.readLine()) != null) {
                char[] ch = new char[str.length()];
                for (int i = 0; i < str.length(); i++) {
                    ch[i] = str.charAt(i);
                }
                int k = 0;
                for (int i = 0; i < name.length; i++) {
                    if (ch[i] != name[i]) {
                        isSaved = false;
                        break;
                    }
                    else k = i;
                }
                k += 2;
                if (isSaved) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ch[k]);
                    if (ch[k+1] != ' ') sb.append(ch[k+1]);
                    k += 2;
                    level = Integer.parseInt(sb.toString());
                    sb.delete(0, sb.length());
                    for(int i = k; i < ch.length; i++) {
                        sb.append(ch[i]);
                    }
                    previousScore = Integer.parseInt(sb.toString());
                    isLoaded = true;
                    return prepared();
                }
            }
        }
        catch (Exception e) {
            return "homePage";
        }
        return "homePage";
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
