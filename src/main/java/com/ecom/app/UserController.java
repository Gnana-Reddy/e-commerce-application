package com.ecom.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")   //Base url path for all the request methods within this controller class
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @RequestMapping(value = "{/id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Long id){
//        User user = userService.fetchUser(id);
//        if(user == null) return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(user);

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user){
        boolean updatedUser = userService.updateUser(id,user);
        if(updatedUser)
            return ResponseEntity.ok("Successfully user updated");
        return ResponseEntity.notFound().build();
    }
}
