package ru.gruzoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.gruzoff.dto.LikeDto;
import ru.gruzoff.dto.OrderDto;
import ru.gruzoff.dto.ResponseStatusOperationDto;
import ru.gruzoff.dto.UserDto;
import ru.gruzoff.entity.Likes;
import ru.gruzoff.entity.Order;
import ru.gruzoff.entity.User;
import ru.gruzoff.exception.UserNotFoundExeption;
import ru.gruzoff.payload.UserDtoPayload;
import ru.gruzoff.service.ClassToDtoService;
import ru.gruzoff.service.UserService;

@RestController
@RequestMapping(value = "/v1/api/user/private", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserPrivateController {

    @Autowired
    UserService userService;

    @Autowired
    ClassToDtoService classToDtoService;

    private User getAuthentificatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName).orElseThrow(
                () -> {throw new UserNotFoundExeption("");}
        );

        return currentUser;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        User user = getAuthentificatedUser();

        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : user.getOrders()) {
            orderDtoList.add(classToDtoService.convertOrderToOrderDto(order));
        }

        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/order")
    public ResponseEntity<OrderDto> geOrderById(@RequestParam long id) {
        User user = getAuthentificatedUser();

        return ResponseEntity.ok(
                classToDtoService.convertOrderToOrderDto(
                        userService.getUsersOrderByOrderId(user, id)
                )
        );
    }

    @PostMapping("/orders")
    public void getFilterredOrders() {

    }

    @GetMapping("/set_like")
    public void setLike(@RequestParam long id_to) {
        User user = getAuthentificatedUser();

        userService.setLike(user, id_to);
    }

    @GetMapping("/get_putted_likes")
    public ResponseEntity<List<LikeDto>> getUserLikes() {
        List<LikeDto> lstLikes = new ArrayList<>();
        for (Likes like : getAuthentificatedUser().getPuttedLikes())
            lstLikes.add(new LikeDto(
                    userService.convertUserToUserDto(like.getUser_from()),
                    userService.convertUserToUserDto(like.getUser_to())
            ));
        return ResponseEntity.ok(lstLikes);
    }

    @GetMapping("/get_recieved_likes")
    public ResponseEntity<List<LikeDto>> getUserRecievedLikes() {
        List<LikeDto> lstLikes = new ArrayList<>();
        for (Likes like : getAuthentificatedUser().getRecievedLikes())
            lstLikes.add(new LikeDto(
                    userService.convertUserToUserDto(like.getUser_from()),
                    userService.convertUserToUserDto(like.getUser_to())
            ));
        return ResponseEntity.ok(lstLikes);
    }

    @PostMapping("/set_comment")
    public void setComment() {

    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getAllUserInfo(@RequestParam long id) {
        User user = getAuthentificatedUser();

        return ResponseEntity.ok(userService.convertUserToUserDto(user));
    }

    @PutMapping("/user")
    public ResponseEntity<UserDto> changeUserInfo(@RequestBody UserDtoPayload userDtoPayload) throws NoSuchFieldException, IllegalAccessException {
        return ResponseEntity.ok(userService.changeInfo(getAuthentificatedUser(), userDtoPayload));
    }

    @DeleteMapping("/user")
    public ResponseEntity<ResponseStatusOperationDto> deleteUser() {
        if (userService.deleteUser(getAuthentificatedUser())) {
            return ResponseEntity.ok(new ResponseStatusOperationDto("OK"));
        }

        return ResponseEntity.ok(new ResponseStatusOperationDto("Not completed"));
    }
}
