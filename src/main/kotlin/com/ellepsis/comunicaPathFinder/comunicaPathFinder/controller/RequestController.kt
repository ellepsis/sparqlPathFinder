package com.ellepsis.comunicaPathFinder.comunicaPathFinder.controller

import com.ellepsis.comunicaPathFinder.comunicaPathFinder.entity.TreeNode
import com.ellepsis.comunicaPathFinder.comunicaPathFinder.service.PathFinderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author ellepsis created on 17-Nov-18.
 */
@RestController
@RequestMapping("/api/request")
class RequestController(@Autowired
                        val pathFinderService: PathFinderService) {

    @GetMapping("/hello")
    fun hello() = "Hello, World!"

    @PostMapping("/search")
    fun search(@RequestBody params: SearchDTO): TreeNode? {
        return pathFinderService.findPath(params.source, params.target, params.maxDepth)
    }
}