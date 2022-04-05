package com.schulz.apispringessential.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AnimePutRequestBody {
    private Long id;

    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
}
