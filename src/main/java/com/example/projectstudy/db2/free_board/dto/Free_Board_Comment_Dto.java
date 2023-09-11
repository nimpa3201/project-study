package com.example.projectstudy.db2.free_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Free_Board_Comment_Dto {
        @NotBlank
        private String comment;
    }


