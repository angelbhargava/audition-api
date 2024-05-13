package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";


    @Autowired
    private RestTemplate restTemplate;

    public List<AuditionPost> getPosts() {

        ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
            POSTS_URL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            }
        );

        return response.getBody();


    }

    public AuditionPost getPostById(final String id) {
        try {
            ResponseEntity<AuditionPost> response = restTemplate
                .getForEntity(POSTS_URL + "/{id}", AuditionPost.class, id);
            return response.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, e);
            } else {
                throw new SystemException("Error occurred while fetching post: " + e.getMessage(), e);
            }
        }
    }

    public List<String> getCommentsForPost(final String postId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(COMMENTS_URL)
            .queryParam("postId", postId);

        ResponseEntity<List<String>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<String>>() {
            }
        );
        return response.getBody();
    }

    public AuditionPost getPostWithCommentsById(final String id) {
        AuditionPost post = getPostById(id);
        List<String> comments = getCommentsForPost(id);
        post.setComments(comments);
        return post;
    }
}
