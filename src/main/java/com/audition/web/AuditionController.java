package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    @Autowired
    AuditionService auditionService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) String userId) {

        List<AuditionPost> posts;
        if (userId != null) {
            posts = auditionService.getPostsByUserId(Integer.parseInt(userId));
        } else {
            posts = auditionService.getPosts();
        }

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
    public @ResponseBody List<Comment> getCommentsForPost(@PathVariable("postId") final String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("Post ID cannot be empty or null");
        }

        return auditionService.getCommentsForPost(postId);
    }

}
