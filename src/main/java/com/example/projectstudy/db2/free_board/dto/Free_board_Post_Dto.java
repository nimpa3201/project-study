package com.example.projectstudy.db2.free_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class Free_board_Post_Dto {

        private String tag;

        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }


