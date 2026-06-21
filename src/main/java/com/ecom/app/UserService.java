package com.ecom.app;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private long userId;

    List<User> usersList = new ArrayList<>();

    public List<User> fetchAllUsers(){
        return usersList;
    }

    public Optional<User> fetchUser(Long id){
        return usersList.stream().filter(u-> u.getId().equals(id)).findFirst();
    }

    public void addUser(User user){
        userId += 1;
        user.setId(userId);
        usersList.add(user);
    }

    public boolean updateUser(Long id,User updatedUser){

//        for(User user : usersList){
//            if(user.getId().equals(updatedUser.getId())){
//                user.setFirstName(updatedUser.getFirstName());
//                user.setLastName(updatedUser.getLastName());
//            }
//            return user;
//        }

        return usersList.stream()
                .filter(user->user.getId().equals(id))
                .findFirst()
                .map(existingUser-> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    return true;
                }).orElse(false);

    }

}
