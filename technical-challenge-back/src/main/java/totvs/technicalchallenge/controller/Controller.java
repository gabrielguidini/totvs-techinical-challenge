package totvs.technicalchallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import totvs.technicalchallenge.dto.LoanResponseDto;
import totvs.technicalchallenge.dto.SimluationDto;
import totvs.technicalchallenge.service.LoanService;

@RestController()
@Validated
@RequestMapping(Version.ACTUAL + "/loan")
@RequiredArgsConstructor
public class Controller {

  private final LoanService loanService;

  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Operation(description = "Simulate a loan based on the provided parameters.")
  public LoanResponseDto simulateLoan(
      @RequestBody final @NotNull @Valid SimluationDto simulationDto) {

    return loanService.simulateLoan(simulationDto);
  }
}
