package com.example.projectstudy.db2.free_board.repos;

import com.example.projectstudy.db2.free_board.entities.Free_ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Free_ArticleRepository extends JpaRepository<Free_ArticleEntity, Long> {


    Page<Free_ArticleEntity> findByUser_UsernameIgnoreCaseContaining(String username, Pageable pageable);

    // 태그로 게시물 목록 가져오기
    Page<Free_ArticleEntity> findByTagIgnoreCaseContaining(String tag, Pageable pageable);

    // 제목으로 게시물 목록 가져오기
    Page<Free_ArticleEntity> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

   Page<Free_ArticleEntity> findByTag(String tag, Pageable pageable);
    Page<Free_ArticleEntity> findByTagNot(String tag, Pageable pageable);



}


