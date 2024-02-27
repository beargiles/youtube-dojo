/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.controller;

// this page also discusses REST controller behavior https://www.digitalocean.com/community/tutorials/spring-mvc-exception-handling-controlleradvice-exceptionhandler-handlerexceptionresolver

import com.coyotesong.dojo.youtube.service.YouTubeClientException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@ControllerAdvice
public class AuthenticationFailureHandler {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(YouTubeClientException.class)
    public ModelAndView handleYouTubeUnauthorizedException(HttpServletRequest request, Exception ex) {
        final ModelAndView mv = new ModelAndView("views/youtube/403");
        // HttpStatusCode status = HttpStatusCode.valueOf((Integer) request.getAttribute("javax.servlet.error.status_code"));
        // if we do this we'll get the usual error page
        // mv.setStatus(status);

        mv.getModelMap().addAttribute("timestamp", new Date(Instant.now().toEpochMilli()));
        mv.getModelMap().addAttribute("path", request.getPathInfo());
        mv.getModelMap().addAttribute("status", "403");
        mv.getModelMap().addAttribute("error", "Unauthorized");
        mv.getModelMap().addAttribute("message", ex.getMessage());
        if (ex.getCause() instanceof GoogleJsonResponseException) {
            GoogleJsonResponseException gje = (GoogleJsonResponseException) ex.getCause();
            GoogleJsonError details = gje.getDetails();
            if (details != null) {
                mv.getModelMap().addAttribute("details", details.getDetails());
                mv.getModelMap().addAttribute("errors", details.getErrors());
            } else {
                mv.getModelMap().addAttribute("details", Collections.emptyList());
                mv.getModelMap().addAttribute("errors", Collections.emptyList());
            }
        } else {
            mv.getModelMap().addAttribute("details", Collections.emptyList());
            mv.getModelMap().addAttribute("errors", Collections.emptyList());
        }
        return mv;
    }
}