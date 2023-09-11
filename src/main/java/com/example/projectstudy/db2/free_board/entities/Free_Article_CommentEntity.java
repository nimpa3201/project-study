package com.example.projectstudy.db2.free_board.entities;

import com.example.projectstudy.db.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "free_article_comment")
public class Free_Article_CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "free_article_id")
    private Free_ArticleEntity freeArticle;

    private String content;

    private LocalDateTime createdAt;


}
