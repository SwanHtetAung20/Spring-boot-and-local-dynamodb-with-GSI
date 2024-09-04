package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.controllers;

import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Person;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories.PersonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("person")
public class PersonController {

    private final PersonRepo personRepo;


    @PostMapping("/save")
    public ResponseEntity<Person> saveHandler(@RequestBody Person person) {
        return new ResponseEntity<>(personRepo.save(person), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(personRepo.findById(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Person>> getAll() {
        return ResponseEntity.ok(personRepo.findAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateHandler(@PathVariable("id") String id, @RequestBody Person person) {
        return ResponseEntity.status(HttpStatus.OK).body(personRepo.update(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHandler(@PathVariable("id") String id) {
        return ResponseEntity.ok(personRepo.delete(id));
    }
}
