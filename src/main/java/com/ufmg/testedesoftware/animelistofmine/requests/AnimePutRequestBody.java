package com.ufmg.testedesoftware.animelistofmine.requests;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AnimePutRequestBody {
  private Long id;

  @NotEmpty(message = "The name of the anime cannot be empty")
  private String name;
}
