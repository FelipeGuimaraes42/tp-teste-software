package com.ufmg.testedesoftware.animelistofmine.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestBody {
  @NotEmpty(message = "The name of the anime cannot be empty")
  private String name;

  @Min(value = 1, message = "The score must not be lower than 1")
  @Max(value = 10, message = "The score must not be higher than 10")
  private Double score;
}
