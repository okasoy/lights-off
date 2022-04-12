package tests;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {
    private final CommentService commentService = new CommentServiceJDBC();

    @Test
    public void testReset() {
        commentService.reset();

        assertEquals(0, commentService.getComments("lights off").size());
    }

    @Test
    public void testAddComment() {
        commentService.reset();
        Date date = new Date();

        commentService.addComment(new Comment("lights off", "Nick", "cool", date));

        var comments = commentService.getComments("lights off");
        assertEquals(1, comments.size());
        assertEquals("lights off", comments.get(0).getGame());
        assertEquals("Nick", comments.get(0).getPlayer());
        assertEquals("cool", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testGetComments() {
        commentService.reset();
        Date date = new Date();
        commentService.addComment(new Comment("lights off", "Jake", "not bad", date));
        commentService.addComment(new Comment("lights off", "Kate", "nice game", date));
        commentService.addComment(new Comment("lights off", "Nick", "ok", date));

        var comments = commentService.getComments("lights off");

        assertEquals(3, comments.size());

        assertEquals("lights off", comments.get(0).getGame());
        assertEquals("Jake", comments.get(0).getPlayer());
        assertEquals("not bad", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());

        assertEquals("lights off", comments.get(1).getGame());
        assertEquals("Kate", comments.get(1).getPlayer());
        assertEquals("nice game", comments.get(1).getComment());
        assertEquals(date, comments.get(1).getCommentedOn());

        assertEquals("lights off", comments.get(2).getGame());
        assertEquals("Nick", comments.get(2).getPlayer());
        assertEquals("ok", comments.get(2).getComment());
        assertEquals(date, comments.get(2).getCommentedOn());
    }

}
