package com.example.quartz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quartz.service.ReplayService;

@Controller
public class ReplayController {

  @Autowired
  private ReplayService replayService;

  @GetMapping("/replay")
  public String showReplayPage(@RequestParam(value = "message", required = false) String message,
      Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    model.addAttribute("isAdmin", isAdmin);
    model.addAttribute("errors", replayService.getErrors());
    if (message != null) {
      model.addAttribute("message", message);
    }
    return "replay";
  }

  @PostMapping("/replay")
  public String handleReplay(@RequestParam("errorId") String errorId,
      RedirectAttributes redirectAttributes) {
    try {
      replayService.replayError(errorId);
      redirectAttributes.addFlashAttribute("message", "Replay successful for error ID: " + errorId);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message",
          "Replay failed for error ID: " + errorId + ". Reason: " + e.getMessage());
    }
    return "redirect:/replay";
  }
}