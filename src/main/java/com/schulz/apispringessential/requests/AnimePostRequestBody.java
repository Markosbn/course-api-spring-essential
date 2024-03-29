package com.schulz.apispringessential.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestBody {

    @NotEmpty(message = "The anime name cannot be empty")
    @Schema(description = "This is Anime's name", example = "DBZ", required = true)
    private String name;
}
