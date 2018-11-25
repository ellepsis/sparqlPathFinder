package com.ellepsis.comunicaPathFinder.comunicaPathFinder.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author ellepsis created on 17-Nov-18.
 */
@RestController
@RequestMapping("/api/request")
class RequestController {

    @GetMapping("/hello")
    fun hello() = "Hello, World!"
}