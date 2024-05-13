package com.audition.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<String> comments; // Added a new field to hold comments


}
