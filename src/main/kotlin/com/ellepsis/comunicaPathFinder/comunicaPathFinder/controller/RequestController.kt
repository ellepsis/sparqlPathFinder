package com.ellepsis.comunicaPathFinder.comunicaPathFinder.controller

import org.springframework.web.bind.annotation.*

/**
 * @author ellepsis created on 17-Nov-18.
 */
@RestController
@RequestMapping("/api/request")
class RequestController {

    @GetMapping("/hello")
    fun hello() = "Hello, World!"

    @PostMapping("/search")
    fun search(@RequestBody a: SearchDTO): String {
        println(a)
        return "fasdf"
    }
}