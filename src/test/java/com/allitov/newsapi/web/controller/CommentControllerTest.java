package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.utils.TestUtils;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.filter.CommentFilter;
import com.allitov.newsapi.web.mapper.CommentMapper;
import jakarta.persistence.EntityNotFoundException;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CommentControllerTest extends AbstractControllerTest {

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    @Test
    public void whenFindById_thenReturnCommentById() throws Exception {
        CommentResponse response = createCommentResponse(1L, "content", 1L, 1L);
        Comment comment = createComment(1L, "content");

        Mockito.when(commentService.findById(1L)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(comment)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comment/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(commentService, Mockito.times(1)).findById(1L);
        Mockito.verify(commentMapper, Mockito.times(1)).commentToResponse(comment);

        String expectedResponse = TestUtils.readStringFromResource("response/comment/method/find_comment_by_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterBy_thenReturnComments() throws Exception {
        List<Comment> comments = new ArrayList<>();
        comments.add(createComment(1L, "content1"));
        comments.add(createComment(2L, "content2"));
        List<CommentResponse> responses = new ArrayList<>();
        responses.add(createCommentResponse(1L, "content1", 1L, 1L));
        responses.add((createCommentResponse(2L, "content2", 2L, 1L)));
        CommentListResponse response = new CommentListResponse();
        response.setComments(responses);
        CommentFilter filter = new CommentFilter();
        filter.setNewsId(1L);

        Mockito.when(commentService.filterBy(filter)).thenReturn(comments);
        Mockito.when(commentMapper.commentListToCommentListResponse(comments)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comment/filter?newsId=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(commentService, Mockito.times(1)).filterBy(filter);
        Mockito.verify(commentMapper, Mockito.times(1)).commentListToCommentListResponse(comments);

        String expectedResponse = TestUtils.readStringFromResource("response/comment/method/filter_by_comment_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreate_thenReturnCreatedComment() throws Exception {
        Comment comment = createComment(1L, "created content");
        CommentResponse response = createCommentResponse(1L, "created content", 1L, 2L);
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setNewsId(2L);
        request.setContent("created content");

        Mockito.when(commentMapper.requestToComment(request)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(comment)).thenReturn(response);
        Mockito.when(commentService.save(comment)).thenReturn(comment);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(commentMapper, Mockito.times(1)).requestToComment(request);
        Mockito.verify(commentMapper, Mockito.times(1)).commentToResponse(comment);
        Mockito.verify(commentService, Mockito.times(1)).save(comment);

        String expectedResponse = TestUtils.readStringFromResource("response/comment/method/create_comment_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdate_thenReturnUpdatedComment() throws Exception {
        Comment comment = createComment(1L, "updated comment");
        CommentResponse response = createCommentResponse(1L, "updated content", 1L, 1L);
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setNewsId(1L);
        request.setContent("updated content");

        Mockito.when(commentMapper.requestToComment(1L, request)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(comment)).thenReturn(response);
        Mockito.when(commentService.update(comment)).thenReturn(comment);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(commentMapper, Mockito.times(1)).requestToComment(1L, request);
        Mockito.verify(commentMapper, Mockito.times(1)).commentToResponse(comment);
        Mockito.verify(commentService, Mockito.times(1)).update(comment);

        String expectedResponse = TestUtils.readStringFromResource("response/comment/method/update_comment_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/comment/500"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(commentService, Mockito.times(1)).deleteById(500L);
    }

    @Test
    public void whenFindByIdNotExistedComment_thenReturnError() throws Exception {
        Mockito.when(commentService.findById(500L)).thenThrow(new EntityNotFoundException("Comment with id 500 not found"));

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comment/500"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(commentService, Mockito.times(1)).findById(500L);

        String expectedResponse = TestUtils.readStringFromResource("response/comment/method/comment_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterByNullNewsId_thenReturnError() throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comment/filter"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/filter/null_comment_filter_news_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidNewsId")
    public void whenFilterByInvalidNewsId_thenReturnError(Long newsId) throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/comment/filter?newsId=%d", newsId)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/filter/invalid_comment_filter_news_id.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateCommentWithNullAuthorId_thenReturnError() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(null);
        request.setNewsId(1L);
        request.setContent("content");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/request/null_author_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateCommentWithNullNewsId_thenReturnError() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setNewsId(null);
        request.setContent("content");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/request/null_news_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("blankContent")
    public void whenCreateCommentWithBlankContent_thenReturnError(String content) throws Exception {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setNewsId(1L);
        request.setContent(content);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/request/blank_content_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidAuthorId")
    public void whenCreateCommentWithInvalidAuthorId_thenReturnError(Long authorId) throws Exception {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(authorId);
        request.setNewsId(1L);
        request.setContent("content");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/request/invalid_author_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidNewsId")
    public void whenCreateCommentWithInvalidNewsId_thenReturnError(Long newsId) throws Exception {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setNewsId(newsId);
        request.setContent("content");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/comment/request/invalid_news_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    private static Stream<Arguments> invalidNewsId() {
        return Stream.of(
                Arguments.of(-1L),
                Arguments.of(0L)
        );
    }

    private static Stream<Arguments> blankContent() {
        return Stream.of(
                null,
                Arguments.of(""),
                Arguments.of(" ")
        );
    }

    private static Stream<Arguments> invalidAuthorId() {
        return Stream.of(
                Arguments.of(-100L),
                Arguments.of(0L)
        );
    }
}
