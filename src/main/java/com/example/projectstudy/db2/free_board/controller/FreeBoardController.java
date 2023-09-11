package com.example.projectstudy.db2.free_board.controller;


import com.example.projectstudy.db.UserEntity;
import com.example.projectstudy.db.UserRepository;
import com.example.projectstudy.db.diet_inform.dto.Diet_Inform_Comment_Dto;
import com.example.projectstudy.db.diet_inform.entities.Diet_Inform_Article;
import com.example.projectstudy.db.diet_inform.entities.Diet_Inform_Article_Img;
import com.example.projectstudy.db.diet_inform.entities.Diet_Inform_Comment;
import com.example.projectstudy.db2.free_board.dto.Free_Board_Comment_Dto;
import com.example.projectstudy.db2.free_board.dto.Free_board_Post_Dto;
import com.example.projectstudy.db2.free_board.entities.Free_ArticleEntity;
import com.example.projectstudy.db2.free_board.entities.Free_Article_CommentEntity;
import com.example.projectstudy.db2.free_board.entities.Free_Article_imgEntity;
import com.example.projectstudy.db2.free_board.repos.Free_ArticleRepository;
import com.example.projectstudy.db2.free_board.repos.Free_Article_imgRepository;
import com.example.projectstudy.db2.free_board.repos.Free_Article_CommentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class FreeBoardController {


    private final Free_ArticleRepository freeArticleRepository;
    private final UserRepository userRepository;
    private final Free_Article_imgRepository freeArticleImgRepository;
    private final Free_Article_CommentRepository freeArticleCommentRepository;

    /*@Autowired
    public FreeBoardController(
     Free_ArticleRepository freeArticleRepository,
     UserRepository userRepository,
     Free_Article_imgRepository freeArticleImgRepository,
     Free_Article_CommentRepository freeArticleCommentRepository)
 {
        this.freeArticleRepository = freeArticleRepository;
        this.userRepository = userRepository;
        this.freeArticleImgRepository = freeArticleImgRepository;
        this.freeArticleCommentRepository = freeArticleCommentRepository;


    }
*/

    @GetMapping
    public String community() {

        return "community";
    }

    //오운완 안만듦


    @GetMapping("/FreeBoard")
    public String freeBoardAll(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        // 페이지네이션 정보 생성
        Pageable pageable = PageRequest.of(page, 10); // 한 페이지에 10개의 게시글 표시

        // "공지사항" 태그 게시물 가져오기
        Page<Free_ArticleEntity> noticeArticles = freeArticleRepository.findByTag("공지사항", pageable);

        // 나머지 게시물 가져오기 (공지사항 제외)
        Page<Free_ArticleEntity> otherArticles = freeArticleRepository.findByTagNot("공지사항", pageable);

        List<Free_ArticleEntity> allArticles = new ArrayList<>();
        allArticles.addAll(noticeArticles.getContent());
        allArticles.addAll(otherArticles.getContent());

        // 전체 게시물 리스트를 페이지네이션으로 다시 생성
        Page<Free_ArticleEntity> articlePage = new PageImpl<>(allArticles, pageable, noticeArticles.getTotalElements() + otherArticles.getTotalElements());

        model.addAttribute("articles", articlePage);

        return "FreeBoard";
    }


    @Transactional
    @PostMapping("/FreeBoard/post")
    public ResponseEntity<String> freeBoardPostStart(@RequestBody @Valid Free_board_Post_Dto dto, LocalDateTime localDateTime) {

        // 현재 사용자 정보 가져오기
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        log.info(username);
        log.info("자유 게시판 글 작성을 시작합니다");

        Optional<UserEntity> user = userRepository.findByUsername(username);


        Free_ArticleEntity freeArticleEntity = new Free_ArticleEntity();
        freeArticleEntity.setTitle(dto.getTitle());
        freeArticleEntity.setContent(dto.getContent());
        freeArticleEntity.setTag(dto.getTag());
        freeArticleEntity.setUser(user.get());
        freeArticleEntity.setCreated_at(localDateTime.now());


        try {
            freeArticleRepository.save(freeArticleEntity);
            log.info("작성완료");

            Long generatedId = freeArticleEntity.getId();

            String Id = generatedId.toString();

            return ResponseEntity.ok(Id);
        } catch (Exception e) {
            log.error("글 작성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성에 실패하였습니다.");
        }


    }

    @GetMapping("/FreeBoard/post")
    public String freeBoardPost() {

        return "FreeBoardPost";
    }


    @GetMapping("/FreeBoard/search/byAuthor")
    public String freeBoardByAuthor(
            @RequestParam(name = "author") String author,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        // 페이지네이션 정보 생성
        Pageable pageable = PageRequest.of(page, 10); // 한 페이지에 10개의 게시글 표시

        // 작성자로 게시물 목록 페이지별로 가져오기
        Page<Free_ArticleEntity> articlePage = freeArticleRepository.findByUser_UsernameIgnoreCaseContaining(author, pageable);

        model.addAttribute("articles", articlePage);

        return "FreeBoard";
    }


    @GetMapping("/FreeBoard/search/byTag")
    public String freeBoardByTag(
            @RequestParam(name = "tag") String tag,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        // 페이지네이션 정보 생성
        Pageable pageable = PageRequest.of(page, 10); // 한 페이지에 10개의 게시글 표시

        // 태그로 게시물 목록 페이지별로 가져오기
        Page<Free_ArticleEntity> articlePage = freeArticleRepository.findByTagIgnoreCaseContaining(tag, pageable);

        model.addAttribute("articles", articlePage);

        return "FreeBoard";
    }


    @GetMapping("/FreeBoard/search/byTitle")
    public String freeBoardByTitle(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        // 페이지네이션 정보 생성
        Pageable pageable = PageRequest.of(page, 10); // 한 페이지에 10개의 게시글 표시

        // 태그로 게시물 목록 페이지별로 가져오기
        Page<Free_ArticleEntity> articlePage = freeArticleRepository.findByTitleIgnoreCaseContaining(title, pageable);

        model.addAttribute("articles", articlePage);

        return "FreeBoard";
    }


    // 다이어트 게시판 단일 조회
    @GetMapping("/FreeBoard/{id}")
    public String dietInformArticle(@PathVariable("id") Long id, Model model) {

        Optional<Free_ArticleEntity> freeArticle = freeArticleRepository.findById(id);
        if (freeArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // 이미지
        List<Free_Article_imgEntity> freeArticleImgs = freeArticleImgRepository.findAll();

        List<Free_Article_imgEntity> freeArticleImgList = new ArrayList<>();

        for (Free_Article_imgEntity Img : freeArticleImgs) {
            if (Img.getFreeArticle().getId() == id) {
                freeArticleImgList.add(Img);
            }
        }

        List<Free_Article_CommentEntity> free_comments = freeArticleCommentRepository.findAll();
        List<Free_Article_CommentEntity> article_comments = new ArrayList<>();

        for (Free_Article_CommentEntity comment : free_comments) {
            if (comment.getFreeArticle().getId() == id) {
                article_comments.add(comment);
            }
        }
        model.addAttribute("article", freeArticle.get());
        model.addAttribute("articleImgs", freeArticleImgList);
        model.addAttribute("articleComments", free_comments);
        return "FreeBoardArticle";
    }


    @PostMapping("/FreeBoard/comment/{articleId}")
    public ResponseEntity<String> postComment(@PathVariable("articleId") Long articleId,
                                              @Valid @RequestBody Free_Board_Comment_Dto requestDto,
                                              LocalDateTime time) {

        Optional<Free_ArticleEntity> freeBoardArticle = freeArticleRepository.findById(articleId);

        if (freeBoardArticle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Free_ArticleEntity free_articleentity = freeBoardArticle.get();

        // 현재 사용자 정보 가져오기
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Bad request");
        }

        // 댓글 작성 로직 (requestDto의 comment 사용)
        Free_Article_CommentEntity free_article_comment = new Free_Article_CommentEntity();
        free_article_comment.setFreeArticle(free_articleentity); // 게시글
        free_article_comment.setUser(user.get()); // 작성자
        free_article_comment.setContent(requestDto.getComment()); // 댓글내용

        LocalDateTime now = time.now();
        free_article_comment.setCreatedAt(now); // 댓글 작성 시간

        freeArticleCommentRepository.save(free_article_comment);

        return ResponseEntity.ok("Comment posted successfully");


    }
}

