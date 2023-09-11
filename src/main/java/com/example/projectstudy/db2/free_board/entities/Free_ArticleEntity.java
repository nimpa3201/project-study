package com.example.projectstudy.db2.free_board.entities;

import com.example.projectstudy.db.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "free_article")
public class Free_ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String tag;

    private LocalDateTime created_at;

    @OneToMany(mappedBy = "freeArticle")
    private List<Free_Article_CommentEntity> freeArticleComments = new ArrayList<>();


    @OneToMany(mappedBy = "freeArticle")
    private List<Free_Article_imgEntity> freeArticleImgs = new ArrayList<>();


}
