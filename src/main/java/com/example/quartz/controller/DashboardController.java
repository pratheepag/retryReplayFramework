package com.example.quartz.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.quartz.service.ReplayService;

@Controller
public class DashboardController {

  private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

  @Autowired
  @Lazy
  private ReplayService replayService;

  @GetMapping("/dashboard")
  public String showDashboard(Model model) {
    int retryCount = replayService.getRetryCount();
    int replayCount = replayService.getReplayCount();
    int successfulApiCallCount = replayService.getSuccessfulApiCallCount(); // Fetch successful API
                                                                            // call count
    var failedCalls = replayService.getErrors();

    logger.info("Retry Count: {}", retryCount);
    logger.info("Replay Count: {}", replayCount);
    logger.info("Successful API Calls: {}", successfulApiCallCount);
    logger.info("Failed Calls: {}", failedCalls);

    model.addAttribute("retryCount", retryCount != 0 ? retryCount : 0);
    model.addAttribute("replayCount", replayCount != 0 ? replayCount : 0);
    model.addAttribute("successfulApiCallCount",
        successfulApiCallCount != 0 ? successfulApiCallCount : 0);
    model.addAttribute("failedCalls", failedCalls != null ? failedCalls : List.of());
    return "dashboard";
  }
}