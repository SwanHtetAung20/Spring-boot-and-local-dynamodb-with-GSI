package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.controllers;

import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Community;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories.CommunityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("community")
public class CommunityController {

    private final CommunityRepo communityRepo;


    @PostMapping("/save")
    public ResponseEntity<Community> insertHandler(@RequestBody Community community) {
        return new ResponseEntity<>(communityRepo.save(community), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Community>> getCommunitiesById(@PathVariable("id") String id) {
        return ResponseEntity.ok(communityRepo.findCommunitiesById(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Community>> getAllCommunities() {
        return ResponseEntity.status(HttpStatus.OK).body(communityRepo.findAllCommunities());
    }

    @DeleteMapping("/{id}/{date}")
    public ResponseEntity<String> deleteHandler(@PathVariable("id") String id, @PathVariable("date") String date) {
        return ResponseEntity.ok(communityRepo.deleteCommunity(id, date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateHandler(@PathVariable("id") String id, @RequestParam(required = false) String date, @RequestBody Community community) {
        return ResponseEntity.status(HttpStatus.OK).body(communityRepo.updateCommunity(id, date, community));
    }

    @GetMapping("/get-by-name")
    public ResponseEntity<List<Community>> getAllCommunitiesByName(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(communityRepo.findCommunitiesByName(name));
    }

    @GetMapping("/get-by-status")
    public ResponseEntity<List<Community>> getAllCommunitiesByStatus(@RequestParam String status) {
        try {
           // Community.Status communityStatus = Community.Status.valueOf(status.toUpperCase());
            List<Community> communities = communityRepo.findCommunitiesByStatus(status);
            return ResponseEntity.status(HttpStatus.OK).body(communities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }
    }
}
