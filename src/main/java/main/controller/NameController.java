package main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NameController {
  @GetMapping("/name")
  public String createName(Model model) {
    return "/name";
  }

  @GetMapping("/timer")
  public String timer(Model model) {
    return "/timer";
  }
}
