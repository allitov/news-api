package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.filter.CommentFilter;
import com.allitov.newsapi.web.mapper.CommentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CommentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    // Methods tests

    @Test
    @DisplayName("Test filterBy() status 200")
    public void givenCommentFilter_whenFilterBy_thenCommentListResponse() throws Exception {
        CommentFilter filter = new CommentFilter();
        filter.setNewsId(1L);
        List<Comment> foundComments = Collections.emptyList();
        CommentListResponse response = new CommentListResponse();

        Mockito.when(commentService.filterBy(filter))
                .thenReturn(foundComments);
        Mockito.when(commentMapper.commentListToCommentListResponse(foundComments))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/comment/filter?newsId={newsId}", filter.getNewsId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'comments': []}"));

        Mockito.verify(commentService, Mockito.times(1))
                .filterBy(filter);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentListToCommentListResponse(foundComments);
    }

    @Test
    @DisplayName("Test findById() status 200")
    public void givenId_whenFindById_thenCommentResponse() throws Exception {
        Long id = 1L;
        Comment comment = new Comment();
        CommentResponse response = createCommentResponse();

        Mockito.when(commentService.findById(id))
                .thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(comment))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/comment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'id': 1, " +
                                "'content': 'content', " +
                                "'authorId': 1, " +
                                "'newsId': 1, " +
                                "'creationDate': '1970-01-01T00:00:00Z', " +
                                "'lastUpdate': '1970-01-01T00:00:00Z'}")
                );

        Mockito.verify(commentService, Mockito.times(1))
                .findById(id);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentToResponse(comment);
    }

    @Test
    @DisplayName("Test findById() with nonexistent id")
    public void givenNonexistentId_whenFindById_thenErrorResponse() throws Exception {
        Long id = 1L;

        Mockito.when(commentService.findById(id))
                .thenThrow(new EntityNotFoundException(String.format("Comment with id = '%d' not found", id)));

        mockMvc.perform(get("/api/v2/comment/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"Comment with id = '%d' not found\"}", id)));

        Mockito.verify(commentService, Mockito.times(1))
                .findById(id);
    }

    @Test
    @DisplayName("Test create() status 201")
    public void givenCommentRequest_whenCreate_thenVoid() throws Exception {
        CommentRequest request = createCommentRequest();
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        Mockito.when(commentMapper.requestToComment(request))
                .thenReturn(comment);
        Mockito.when(commentService.save(comment))
                .thenReturn(comment);

        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/comment/" + comment.getId()));

        Mockito.verify(commentMapper, Mockito.times(1))
                .requestToComment(request);
        Mockito.verify(commentService, Mockito.times(1))
                .save(comment);
    }

    @Test
    @DisplayName("Test updateById() status 204")
    public void givenIdAndCommentRequest_whenUpdateById_thenVoid() throws Exception {
        Long id = 1L;
        CommentRequest request = createCommentRequest();
        Comment comment = new Comment();
        comment.setId(id);

        Mockito.when(commentMapper.requestToComment(id, request))
                .thenReturn(comment);
        Mockito.when(commentService.update(comment))
                .thenReturn(comment);

        mockMvc.perform(put("/api/v2/comment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(commentMapper, Mockito.times(1))
                .requestToComment(id, request);
        Mockito.verify(commentService, Mockito.times(1))
                .update(comment);
    }

    @Test
    @DisplayName("Test updateById() with nonexistent id")
    public void givenNonexistentIdAndCommentRequest_whenUpdateById_thenErrorResponse() throws Exception {
        Long id = 1L;
        CommentRequest request = createCommentRequest();
        Comment comment = new Comment();
        comment.setId(id);

        Mockito.when(commentMapper.requestToComment(id, request))
                .thenReturn(comment);
        Mockito.when(commentService.update(comment))
                .thenThrow(new EntityNotFoundException(String.format("Comment with id = '%d' not found", id)));

        mockMvc.perform(put("/api/v2/comment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"Comment with id = '%d' not found\"}", id)));

        Mockito.verify(commentMapper, Mockito.times(1))
                .requestToComment(id, request);
        Mockito.verify(commentService, Mockito.times(1))
                .update(comment);
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    public void givenId_whenDeleteById_thenVoid() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/comment/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(commentService, Mockito.times(1))
                .deleteById(id);
    }

    // Validation tests

    @Test
    @DisplayName("Test CommentFilter validation with null news id")
    public void givenNullCommentFilterNewsId_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'News id must be specified'}"));
    }

    @Test
    @DisplayName("Test CommentRequest validation with null news id")
    public void givenNullCommentRequestNewsId_whenCreate_thenErrorResponse() throws Exception {
        CommentRequest request = createCommentRequest();
        request.setNewsId(null);

        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'News id must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Test CommentRequest validation with blank content")
    public void givenBlankCommentRequestContent_whenCreate_thenErrorResponse(String content) throws Exception {
        CommentRequest request = createCommentRequest();
        request.setContent(content);

        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Content must be specified'}"));
    }

    private CommentResponse createCommentResponse() {
        return CommentResponse.builder()
                .id(1L)
                .content("content")
                .authorId(1L)
                .newsId(1L)
                .creationDate(Instant.parse("1970-01-01T00:00:00Z"))
                .lastUpdate(Instant.parse("1970-01-01T00:00:00Z"))
                .build();
    }

    private CommentRequest createCommentRequest() {
        return CommentRequest.builder()
                .newsId(1L)
                .content("content")
                .build();
    }

    private static Stream<Arguments> blankStrings() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("\n\n "),
                Arguments.of("    ")
        );
    }
}
