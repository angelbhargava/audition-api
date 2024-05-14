package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuditionController {

    @Autowired
    AuditionService auditionService;



    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPostsByFilter(@RequestParam(name = "filter", required = false) String filter) {
        List<AuditionPost> posts = auditionService.getPosts();

        posts = posts.stream()
                .filter(post -> !post.getTitle().isEmpty())
                .collect(Collectors.toList());

        return posts;
    }



    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPosts(@PathVariable("id") final String postId) {
        final AuditionPost auditionPosts = auditionService.getPostById(postId);

        if (StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("Post ID cannot be empty or null");
        }

        final AuditionPost auditionPost = auditionService.getPostById(postId);
        return auditionPost;
    }

    @RequestMapping(value = "/posts/{postId}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCommentsForPost(@PathVariable("postId") String postId) {
        if (StringUtils.isEmpty(postId)) {
            return ResponseEntity.badRequest().build();
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> comments = Arrays.asList(response.getBody());
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
