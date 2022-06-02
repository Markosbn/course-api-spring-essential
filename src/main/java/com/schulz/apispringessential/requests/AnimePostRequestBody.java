package com.schulz.apispringessential.requests;

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
    private String name;
}
